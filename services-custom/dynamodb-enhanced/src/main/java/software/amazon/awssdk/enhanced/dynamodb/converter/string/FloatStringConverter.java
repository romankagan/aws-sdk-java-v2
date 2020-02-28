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
 * A converter between {@link Float} and {@link String}.
 */
@SdkPublicApi
@ThreadSafe
@Immutable
public class FloatStringConverter implements StringConverter<Float>, PrimitiveConverter<Float> {
    private FloatStringConverter() { }

    public static FloatStringConverter create() {
        return new FloatStringConverter();
    }

    @Override
    public TypeToken<Float> type() {
        return TypeToken.of(Float.class);
    }

    @Override
    public TypeToken<Float> primitiveType() {
        return TypeToken.of(float.class);
    }

    @Override
    public Float fromString(String string) {
        return Float.valueOf(string);
    }
}
