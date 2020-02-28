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

package software.amazon.awssdk.enhanced.dynamodb.converter.string;

import software.amazon.awssdk.annotations.Immutable;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.annotations.ThreadSafe;
import software.amazon.awssdk.enhanced.dynamodb.TypeToken;
import software.amazon.awssdk.enhanced.dynamodb.converter.PrimitiveConverter;

/**
 * A converter between {@link Integer} and {@link String}.
 */
@SdkPublicApi
@ThreadSafe
@Immutable
public class IntegerStringConverter implements StringConverter<Integer>, PrimitiveConverter<Integer> {
    private IntegerStringConverter() { }

    public static IntegerStringConverter create() {
        return new IntegerStringConverter();
    }

    @Override
    public TypeToken<Integer> type() {
        return TypeToken.of(Integer.class);
    }

    @Override
    public TypeToken<Integer> primitiveType() {
        return TypeToken.of(int.class);
    }

    @Override
    public Integer fromString(String string) {
        return Integer.valueOf(string);
    }
}
