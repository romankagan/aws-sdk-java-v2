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

package software.amazon.awssdk.enhanced.dynamodb.mapper;

import java.util.function.BiConsumer;
import java.util.function.Function;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.TypeToken;
import software.amazon.awssdk.enhanced.dynamodb.converter.attribute.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.converter.attribute.AttributeConverterProvider;
import software.amazon.awssdk.enhanced.dynamodb.internal.mapper.StaticAttributeType;

@SdkPublicApi
@SuppressWarnings("WeakerAccess")
public final class Attributes {

    private static final AttributeConverterProvider DEFAULT_CONVERTER_PROVIDER = AttributeConverterProvider.defaultProvider();

    private Attributes() {
    }

    public static <T, R> Attribute.AttributeSupplier<T> attribute(String attributeName,
                                                                  TypeToken<R> attributeType,
                                                                  Function<T, R> getAttributeMethod,
                                                                  BiConsumer<T, R> updateItemMethod) {
        return attribute(attributeName, attributeType, getAttributeMethod, updateItemMethod, DEFAULT_CONVERTER_PROVIDER);
    }

    public static <T, R> Attribute.AttributeSupplier<T> attribute(String attributeName,
                                                                  TypeToken<R> attributeType,
                                                                  Function<T, R> getAttributeMethod,
                                                                  BiConsumer<T, R> updateItemMethod,
                                                                  AttributeConverterProvider converterProvider) {

        AttributeConverter<R> attributeConverter = converterProvider.converterFor(attributeType);
        AttributeType<R> attribute = StaticAttributeType.create(attributeConverter);
        return Attribute.create(attributeName, getAttributeMethod, updateItemMethod, attribute);
    }

    public static <T, R> Attribute.AttributeSupplier<T> attribute(String attributeName,
                                                                  TypeToken<R> attributeType,
                                                                  Function<T, R> getAttributeMethod,
                                                                  BiConsumer<T, R> updateItemMethod,
                                                                  TableSchema tableSchema) {
        return attribute(attributeName,
                         attributeType,
                         getAttributeMethod,
                         updateItemMethod,
                         tableSchema,
                         DEFAULT_CONVERTER_PROVIDER);
    }

    public static <T, R> Attribute.AttributeSupplier<T> attribute(String attributeName,
                                                                  TypeToken<R> attributeType,
                                                                  Function<T, R> getAttributeMethod,
                                                                  BiConsumer<T, R> updateItemMethod,
                                                                  TableSchema<R> tableSchema,
                                                                  AttributeConverterProvider converterProvider) {

        AttributeConverter<R> attributeConverter = converterProvider.converterFor(attributeType, tableSchema);
        AttributeType<R> attribute = StaticAttributeType.create(attributeConverter);
        return Attribute.create(attributeName, getAttributeMethod, updateItemMethod, attribute);
    }
}