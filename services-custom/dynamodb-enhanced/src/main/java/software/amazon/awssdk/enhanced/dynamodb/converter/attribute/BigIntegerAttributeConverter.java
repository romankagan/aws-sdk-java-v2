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

import java.math.BigInteger;
import software.amazon.awssdk.annotations.Immutable;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.annotations.ThreadSafe;
import software.amazon.awssdk.enhanced.dynamodb.TypeToken;
import software.amazon.awssdk.enhanced.dynamodb.converter.TypeConvertingVisitor;
import software.amazon.awssdk.enhanced.dynamodb.converter.string.BigIntegerStringConverter;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * A converter between {@link BigInteger} and {@link AttributeValue}.
 *
 * <p>
 * This stores values in DynamoDB as a number.
 *
 * <p>
 * This supports reading the full range of integers supported by DynamoDB. For smaller numbers, consider using
 * {@link ShortAttributeConverter}, {@link IntegerAttributeConverter} or {@link LongAttributeConverter}.
 *
 * <p>
 * This does not support reading decimal numbers. For decimal numbers, consider using {@link FloatAttributeConverter},
 * {@link DoubleAttributeConverter} or {@link BigDecimalAttributeConverter}. Decimal numbers will cause a
 * {@link NumberFormatException} on conversion.
 *
 * <p>
 * This can be created via {@link #create()}.
 */
@SdkPublicApi
@ThreadSafe
@Immutable
public final class BigIntegerAttributeConverter implements AttributeConverter<BigInteger> {
    private static final Visitor VISITOR = new Visitor();
    private static final BigIntegerStringConverter STRING_CONVERTER = BigIntegerStringConverter.create();

    private BigIntegerAttributeConverter() {
    }

    public static BigIntegerAttributeConverter create() {
        return new BigIntegerAttributeConverter();
    }

    @Override
    public TypeToken<BigInteger> type() {
        return TypeToken.of(BigInteger.class);
    }

    @Override
    public AttributeValue transformFrom(BigInteger input) {
        return EnhancedAttributeValue.fromNumber(STRING_CONVERTER.toString(input)).toAttributeValue();
    }

    @Override
    public BigInteger transformTo(AttributeValue input) {
        if (input.n() != null) {
            return EnhancedAttributeValue.fromNumber(input.n()).convert(VISITOR);
        }
        return EnhancedAttributeValue.fromAttributeValue(input).convert(VISITOR);
    }

    private static final class Visitor extends TypeConvertingVisitor<BigInteger> {
        private Visitor() {
            super(BigInteger.class, BigIntegerAttributeConverter.class);
        }

        @Override
        public BigInteger convertString(String value) {
            return STRING_CONVERTER.fromString(value);
        }

        @Override
        public BigInteger convertNumber(String value) {
            return STRING_CONVERTER.fromString(value);
        }
    }
}
