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

package software.amazon.awssdk.enhanced.dynamodb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.enhanced.dynamodb.MappedTableResource;
import software.amazon.awssdk.enhanced.dynamodb.TableMetadata;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes;

/**
 * Defines a collection of primary keys for items in a table, stored as {@link KeysAndAttributes}.
 * <p/>
 *
 * <b>Note:</b>The builder for the class is parameterized, however the class itself is not. The builder takes a parameterized
 * {@link MappedTableResource} in order for the constructor to transform {@link GetItemEnhancedRequest} objects into the keys and
 * attributes, but the object itself only stores a reference to the table name and does not need to remember the type.
 */
@SdkPublicApi
public final class ReadBatch {
    private final String tableName;
    private final KeysAndAttributes keysAndAttributes;

    private ReadBatch(BuilderImpl<?> builder) {
        this.tableName = builder.mappedTableResource != null ? builder.mappedTableResource.tableName() : null;
        this.keysAndAttributes = generateKeysAndAttributes(builder.requests, builder.mappedTableResource);
    }

    /**
     * All objects must be constructed using a Builder.
     * @param itemClass to determine the parameterized type T of the builder
     * @param <T> The type of the modelled object, corresponding to itemClass
     * @return a parameterized builder of this type
     */
    public static <T> Builder<T> builder(Class<? extends T> itemClass) {
        return new BuilderImpl<>();
    }

    /**
     * @return the table name
     */
    public String tableName() {
        return tableName;
    }

    /**
     * @return a collection of keys and attributes, see {@link KeysAndAttributes}
     */
    public KeysAndAttributes keysAndAttributes() {
        return keysAndAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReadBatch readBatch = (ReadBatch) o;

        if (tableName != null ? !tableName.equals(readBatch.tableName) :
            readBatch.tableName != null) {

            return false;
        }
        return keysAndAttributes != null ?
               keysAndAttributes.equals(readBatch.keysAndAttributes) :
               readBatch.keysAndAttributes == null;
    }

    @Override
    public int hashCode() {
        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + (keysAndAttributes != null ? keysAndAttributes.hashCode() : 0);
        return result;
    }

    /**
     * The builder takes a parameterized {@link MappedTableResource} and individual {@link GetItemEnhancedRequest}.
     * <p/>
     * <b>Note</b>: Must at a minimum define a {@link MappedTableResource} and add at least one {@link GetItemEnhancedRequest}.
     *
     * @param <T>
     */
    public interface Builder<T> {
        /**
         *
         * @param mappedTableResource
         * @return a parameterized Builder of this type
         */
        Builder<T> mappedTableResource(MappedTableResource<T> mappedTableResource);

        /**
         * Adds a {@link GetItemEnhancedRequest} to the builder.
         *
         * @param request A {@link GetItemEnhancedRequest}
         * @return a parameterized Builder of this type
         */
        Builder<T> addGetItem(GetItemEnhancedRequest request);

        /**
         * Adds a {@link GetItemEnhancedRequest} to the builder by accepting a consumer of
         * {@link GetItemEnhancedRequest.Builder}. Calls {@link #addGetItem(GetItemEnhancedRequest)} with the built request
         * object.
         *
         * @param requestConsumer a {@link Consumer} of {@link GetItemEnhancedRequest}
         * @return a parameterized Builder of this type
         */
        Builder<T> addGetItem(Consumer<GetItemEnhancedRequest.Builder> requestConsumer);

        /**
         * @return an object of this type initialized with the values of the Builder
         */
        ReadBatch build();
    }

    private static <T> KeysAndAttributes generateKeysAndAttributes(List<GetItemEnhancedRequest> readRequests,
                                                                   MappedTableResource<T> mappedTableResource) {
        if (readRequests == null || readRequests.isEmpty()) {
            return null;
        }

        Boolean firstRecordConsistentRead = validateAndGetConsistentRead(readRequests);

        List<Map<String, AttributeValue>> keys =
            readRequests.stream()
                        .map(GetItemEnhancedRequest::key)
                        .map(key -> key.keyMap(mappedTableResource.tableSchema(), TableMetadata.primaryIndexName()))
                        .collect(Collectors.toList());

        return KeysAndAttributes.builder()
                                .keys(keys)
                                .consistentRead(firstRecordConsistentRead)
                                .build();

    }

    private static Boolean validateAndGetConsistentRead(List<GetItemEnhancedRequest> readRequests) {
        Boolean firstRecordConsistentRead = null;
        boolean isFirstRecord = true;

        for (GetItemEnhancedRequest request : readRequests) {
            if (isFirstRecord) {
                isFirstRecord = false;
                firstRecordConsistentRead = request.consistentRead();
            } else {
                if (!compareNullableBooleans(firstRecordConsistentRead, request.consistentRead())) {
                    throw new IllegalArgumentException("All batchable read requests for the same "
                                                       + "table must have the same 'consistentRead' "
                                                       + "setting.");
                }
            }
        }
        return firstRecordConsistentRead;
    }

    private static boolean compareNullableBooleans(Boolean one, Boolean two) {
        if (one == null && two == null) {
            return true;
        }

        if (one != null) {
            return one.equals(two);
        } else {
            return false;
        }
    }

    private static final class BuilderImpl<T> implements Builder<T> {
        private MappedTableResource<T> mappedTableResource;
        private List<GetItemEnhancedRequest> requests = new ArrayList<>();

        private BuilderImpl() {
        }

        public Builder<T> mappedTableResource(MappedTableResource<T> mappedTableResource) {
            this.mappedTableResource = mappedTableResource;
            return this;
        }

        public Builder<T> addGetItem(GetItemEnhancedRequest request) {
            requests.add(request);
            return this;
        }

        public Builder<T> addGetItem(Consumer<GetItemEnhancedRequest.Builder> requestConsumer) {
            GetItemEnhancedRequest.Builder builder = GetItemEnhancedRequest.builder();
            requestConsumer.accept(builder);
            return addGetItem(builder.build());
        }

        public ReadBatch build() {
            return new ReadBatch(this);
        }

    }
}
