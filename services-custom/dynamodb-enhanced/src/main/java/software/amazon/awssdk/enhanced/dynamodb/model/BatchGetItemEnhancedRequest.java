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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import software.amazon.awssdk.annotations.SdkPublicApi;

/**
 * Defines an immutable list of {@link ReadBatch}.
 */
@SdkPublicApi
public final class BatchGetItemEnhancedRequest {

    private final List<ReadBatch> readBatches;

    private BatchGetItemEnhancedRequest(Builder builder) {
        this.readBatches = getListIfExist(builder.readBatches);
    }

    /**
     * All requests must be constructed using a Builder.
     * @return a builder of this type
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * @return a builder with all existing values set
     */
    public Builder toBuilder() {
        return new Builder().readBatches(readBatches);
    }

    /**
     * @return a collection of {@link ReadBatch}
     */
    public Collection<ReadBatch> readBatches() {
        return readBatches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BatchGetItemEnhancedRequest that = (BatchGetItemEnhancedRequest) o;

        return readBatches != null ? readBatches.equals(that.readBatches) : that.readBatches == null;
    }

    @Override
    public int hashCode() {
        return readBatches != null ? readBatches.hashCode() : 0;
    }

    private static List<ReadBatch> getListIfExist(List<ReadBatch> readBatches) {
        return readBatches != null ? Collections.unmodifiableList(readBatches) : null;
    }

    /**
     * Use this builder to create a request with the
     */
    public static final class Builder {
        private List<ReadBatch> readBatches;

        private Builder() {
        }

        /**
         *
         * @param readBatches
         * @return
         */
        public Builder readBatches(Collection<ReadBatch> readBatches) {
            this.readBatches = readBatches != null ? new ArrayList<>(readBatches) : null;
            return this;
        }

        /**
         *
         * @param readBatches
         * @return
         */
        public Builder readBatches(ReadBatch... readBatches) {
            this.readBatches = Arrays.asList(readBatches);
            return this;
        }

        /**
         *
         * @param readBatch
         * @return
         */
        public Builder addReadBatch(ReadBatch readBatch) {
            if (readBatches == null) {
                readBatches = new ArrayList<>();
            }
            readBatches.add(readBatch);
            return this;
        }

        public BatchGetItemEnhancedRequest build() {
            return new BatchGetItemEnhancedRequest(this);
        }
    }

}
