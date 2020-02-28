/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.enhanced.dynamodb.converter.attribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import software.amazon.awssdk.annotations.Immutable;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.annotations.ThreadSafe;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.TypeToken;
import software.amazon.awssdk.enhanced.dynamodb.converter.PrimitiveConverter;
import software.amazon.awssdk.enhanced.dynamodb.converter.string.StringConverter;
import software.amazon.awssdk.enhanced.dynamodb.converter.string.StringConverterProvider;
import software.amazon.awssdk.utils.Logger;
import software.amazon.awssdk.utils.Validate;

/**
 * <p>
 * Given an input, this will identify a converter that can convert the specific Java type and invoke it. If a converter cannot
 * be found, it will invoke a "parent" converter, which would be expected to be able to convert the value (or throw an exception).
 */
@SdkPublicApi
@ThreadSafe
@Immutable
public final class DefaultAttributeConverterProvider implements AttributeConverterProvider {
    private static final Logger log = Logger.loggerFor(DefaultAttributeConverterProvider.class);

    private final ConcurrentHashMap<TypeToken<?>, AttributeConverter<?>> converterCache =
        new ConcurrentHashMap<>();

    private DefaultAttributeConverterProvider(Builder builder) {
        // Converters are used in the REVERSE order of how they were added to the builder.
        for (int i = builder.converters.size() - 1; i >= 0; i--) {
            AttributeConverter<?> converter = builder.converters.get(i);
            converterCache.put(converter.type(), converter);

            if (converter instanceof PrimitiveConverter) {
                PrimitiveConverter primitiveConverter = (PrimitiveConverter) converter;
                converterCache.put(primitiveConverter.primitiveType(), converter);
            }
        }
    }

    /**
     * Equivalent to {@code builder(TypeToken.of(Object.class))}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Find a converter that matches the provided type. If one cannot be found, throw an exception.
     */
    public <T> AttributeConverter<T> converterFor(TypeToken<T> type) {
        return findConverter(type, null).orElseThrow(() -> new IllegalStateException("Converter not found for " + type));
    }

    /**
     * Find a converter that matches the provided type. If one cannot be found, throw an exception.
     */
    public <T> AttributeConverter<T> converterFor(TypeToken<T> type,
                                                  TableSchema<T> tableSchema) {
        return findConverter(type, tableSchema).orElseThrow(() -> new IllegalStateException("Converter not found for " + type));
    }

    /**
     * Find a converter that matches the provided type. If one cannot be found, return empty.
     */
    private <T> Optional<AttributeConverter<T>> findConverter(TypeToken<T> type,
                                                              TableSchema<T> tableSchema) {
        log.debug(() -> "Loading converter for " + type + ".");

        @SuppressWarnings("unchecked") // We initialized correctly, so this is safe.
            AttributeConverter<T> converter = (AttributeConverter<T>) converterCache.get(type);
        if (converter != null) {
            return Optional.of(converter);
        }

        if (type.rawClass().isAssignableFrom(Map.class)) {
            converter = createMapConverter(type, tableSchema);
        } else if (type.rawClass().isAssignableFrom(Set.class)) {
            converter = createSetConverter(type, tableSchema);
        } else if (type.rawClass().isAssignableFrom(List.class)) {
            TypeToken<T> innerType = (TypeToken<T>) type.rawClassParameters().get(0);
            AttributeConverter<?> innerConverter = findConverter(innerType, tableSchema)
                .orElseThrow(() -> new IllegalStateException("Converter not found for " + type));
            return Optional.of((AttributeConverter<T>) ListAttributeConverter.create(innerConverter));
        }

        if (tableSchema != null) {
            converter = DocumentAttributeConverter.create(tableSchema, type);
        }

        if (shouldCache(type.rawClass())) {
            this.converterCache.put(type, converter);
        }

        return Optional.ofNullable(converter);
    }

    private boolean shouldCache(Class<?> type) {
        // Do not cache anonymous classes, to prevent memory leaks.
        return !type.isAnonymousClass();
    }

    private <T> AttributeConverter<T> createMapConverter(TypeToken<T> type,
                                                                   TableSchema<T> tableSchema) {
        TypeToken<?> keyType = type.rawClassParameters().get(0);
        TypeToken<T> valueType = (TypeToken<T>) type.rawClassParameters().get(1);

        StringConverter<?> keyConverter = StringConverterProvider.defaultProvider().converterFor(keyType);
        AttributeConverter<?> valueConverter = findConverter(valueType, tableSchema)
            .orElseThrow(() -> new IllegalStateException("Converter not found for " + type));

        return (AttributeConverter<T>) MapAttributeConverter.mapConverter(keyConverter, valueConverter);
    }

    private <T> AttributeConverter<T> createSetConverter(TypeToken<T> type,
                                                         TableSchema<T> tableSchema) {
        TypeToken<T> innerType = (TypeToken<T>) type.rawClassParameters().get(0);
        AttributeConverter<?> innerConverter = findConverter(innerType, tableSchema)
            .orElseThrow(() -> new IllegalStateException("Converter not found for " + type));

        return (AttributeConverter<T>) SetAttributeConverter.setConverter(innerConverter);
    }

    public static DefaultAttributeConverterProvider create() {
        return DefaultAttributeConverterProvider.builder()
                                                .addConverter(AtomicBooleanAttributeConverter.create())
                                                .addConverter(AtomicIntegerAttributeConverter.create())
                                                .addConverter(AtomicLongAttributeConverter.create())
                                                .addConverter(BigDecimalAttributeConverter.create())
                                                .addConverter(BigIntegerAttributeConverter.create())
                                                .addConverter(BooleanAttributeConverter.create())
                                                .addConverter(ByteArrayAttributeConverter.create())
                                                .addConverter(ByteAttributeConverter.create())
                                                .addConverter(CharacterArrayAttributeConverter.create())
                                                .addConverter(CharacterAttributeConverter.create())
                                                .addConverter(CharSequenceAttributeConverter.create())
                                                .addConverter(DoubleAttributeConverter.create())
                                                .addConverter(DurationAttributeConverter.create())
                                                .addConverter(FloatAttributeConverter.create())
                                                .addConverter(InstantAsIntegerAttributeConverter.create())
                                                .addConverter(IntegerAttributeConverter.create())
                                                .addConverter(LocalDateAttributeConverter.create())
                                                .addConverter(LocalDateTimeAttributeConverter.create())
                                                .addConverter(LocalTimeAttributeConverter.create())
                                                .addConverter(LongAttributeConverter.create())
                                                .addConverter(MonthDayAttributeConverter.create())
                                                .addConverter(OffsetDateTimeAsStringAttributeConverter.create())
                                                .addConverter(OptionalDoubleAttributeConverter.create())
                                                .addConverter(OptionalIntAttributeConverter.create())
                                                .addConverter(OptionalLongAttributeConverter.create())
                                                .addConverter(PeriodAttributeConverter.create())
                                                .addConverter(SdkBytesAttributeConverter.create())
                                                .addConverter(ShortAttributeConverter.create())
                                                .addConverter(StringAttributeConverter.create())
                                                .addConverter(StringBufferAttributeConverter.create())
                                                .addConverter(StringBuilderAttributeConverter.create())
                                                .addConverter(UriAttributeConverter.create())
                                                .addConverter(UrlAttributeConverter.create())
                                                .addConverter(UuidAttributeConverter.create())
                                                .addConverter(ZonedDateTimeAsStringAttributeConverter.create())
                                                .addConverter(ZoneIdAttributeConverter.create())
                                                .addConverter(ZoneOffsetAttributeConverter.create())
                                                .build();
    }

    /**
     * A builder for configuring and creating {@link DefaultAttributeConverterProvider}s.
     */
    public static class Builder {
        private List<AttributeConverter<?>> converters = new ArrayList<>();

        private Builder() {
        }

        public Builder addConverters(Collection<? extends AttributeConverter<?>> converters) {
            Validate.paramNotNull(converters, "converters");
            Validate.noNullElements(converters, "Converters must not contain null members.");
            this.converters.addAll(converters);
            return this;
        }

        public Builder addConverter(AttributeConverter<?> converter) {
            Validate.paramNotNull(converter, "converter");
            this.converters.add(converter);
            return this;
        }

        public Builder clearConverters() {
            this.converters.clear();
            return this;
        }

        public DefaultAttributeConverterProvider build() {
            return new DefaultAttributeConverterProvider(this);
        }
    }
}
