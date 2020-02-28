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

import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * Defines parameters used to when querying a DynamoDb table.
 * <p/>
 * A valid request object must contain a {@link QueryConditional} condition specifying how DynamoDb
 * should match items in the table.
 * <p/>
 * All other parameters are optional.
 */
@SdkPublicApi
public final class QueryEnhancedRequest {

    private final QueryConditional queryConditional;
    private final Map<String, AttributeValue> exclusiveStartKey;
    private final Boolean scanIndexForward;
    private final Integer limit;
    private final Boolean consistentRead;
    private final Expression filterExpression;

    private QueryEnhancedRequest(Builder builder) {
        this.queryConditional = builder.queryConditional;
        this.exclusiveStartKey = builder.exclusiveStartKey;
        this.scanIndexForward = builder.scanIndexForward;
        this.limit = builder.limit;
        this.consistentRead = builder.consistentRead;
        this.filterExpression = builder.filterExpression;
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
        return builder().queryConditional(queryConditional)
                        .exclusiveStartKey(exclusiveStartKey)
                        .scanIndexForward(scanIndexForward)
                        .limit(limit)
                        .consistentRead(consistentRead)
                        .filterExpression(filterExpression);
    }

    /**
     * @return The conditions of the query
     */
    public QueryConditional queryConditional() {
        return queryConditional;
    }

    /**
     * @return
     */
    public Map<String, AttributeValue> exclusiveStartKey() {
        return exclusiveStartKey;
    }

    /**
     * @return
     */
    public Boolean scanIndexForward() {
        return scanIndexForward;
    }

    /**
     * @return
     */
    public Integer limit() {
        return limit;
    }

    /**
     * @return whether or not this request will use consistent read
     */
    public Boolean consistentRead() {
        return consistentRead;
    }

    /**
     *
     * @return
     */
    public Expression filterExpression() {
        return filterExpression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QueryEnhancedRequest query = (QueryEnhancedRequest) o;

        if (queryConditional != null ? ! queryConditional.equals(query.queryConditional) :
            query.queryConditional != null) {
            return false;
        }
        if (exclusiveStartKey != null ? ! exclusiveStartKey.equals(query.exclusiveStartKey) :
            query.exclusiveStartKey != null) {
            return false;
        }
        if (scanIndexForward != null ? ! scanIndexForward.equals(query.scanIndexForward) :
            query.scanIndexForward != null) {
            return false;
        }
        if (limit != null ? ! limit.equals(query.limit) : query.limit != null) {
            return false;
        }
        if (consistentRead != null ? ! consistentRead.equals(query.consistentRead) : query.consistentRead != null) {
            return false;
        }
        return filterExpression != null ? filterExpression.equals(query.filterExpression) : query.filterExpression == null;
    }

    @Override
    public int hashCode() {
        int result = queryConditional != null ? queryConditional.hashCode() : 0;
        result = 31 * result + (exclusiveStartKey != null ? exclusiveStartKey.hashCode() : 0);
        result = 31 * result + (scanIndexForward != null ? scanIndexForward.hashCode() : 0);
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        result = 31 * result + (consistentRead != null ? consistentRead.hashCode() : 0);
        result = 31 * result + (filterExpression != null ? filterExpression.hashCode() : 0);
        return result;
    }

    /**
     * Use this builder to create a request with the desired parameters.
     */
    public static final class Builder {
        private QueryConditional queryConditional;
        private Map<String, AttributeValue> exclusiveStartKey;
        private Boolean scanIndexForward;
        private Integer limit;
        private Boolean consistentRead;
        private Expression filterExpression;

        private Builder() {
        }

        /**
         * Determines the matching conditions for this query request. See {@link QueryConditional} for examples
         * and constraints.
         *
         * @param queryConditional the query conditions
         * @return a builder of this type
         */
        public Builder queryConditional(QueryConditional queryConditional) {
            this.queryConditional = queryConditional;
            return this;
        }

        /**
         * Results are sorted by sort key in ascending order if {@link #scanIndexForward} is true. If its false, the
         * order is descending. The default value is true.
         *
         * @param scanIndexForward the sort order
         * @return a builder of this type
         */
        public Builder scanIndexForward(Boolean scanIndexForward) {
            this.scanIndexForward = scanIndexForward;
            return this;
        }

        /**
         * The primary key of the first item that this operation will evaluate. Use the value that was returned for
         * {@link Page#lastEvaluatedKey()} in the previous operation.
         *
         * @param exclusiveStartKey the primary key value to start evaluate
         * @return a builder of this type
         */
        public Builder exclusiveStartKey(Map<String, AttributeValue> exclusiveStartKey) {
            this.exclusiveStartKey = exclusiveStartKey != null ? new HashMap<>(exclusiveStartKey) : null;
            return this;
        }

        /**
         * Sets a limit on how many items to evaluate in the query. If not set, the operation uses
         * the maximum values allowed.
         * <p/>
         * <b>Note:</b>The limit does not refer to the number of items to return, but how many items
         * the database should evaluate while executing the query. Use limit together with {@link Page#lastEvaluatedKey()}
         * and {@link #exclusiveStartKey} in subsequent query calls to evaluate <em>limit</em> items per call.
         *
         * @param limit the maximum number of items to evalute
         * @return a builder of this type
         */
        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        /**
         Determines the read consistency model: If set to true, the operation uses strongly consistent reads; otherwise,
         the operation uses eventually consistent reads.
         *
         * @param consistentRead sets consistency model of the operation to use strong consistency
         * @return a builder of this type
         */
        public Builder consistentRead(Boolean consistentRead) {
            this.consistentRead = consistentRead;
            return this;
        }

        /**
         * Refines the query results by applying the filter expression on the results returned
         * from the query and discards items that do not match. See {@link Expression} for examples
         * and constraints.
         *
         * @param filterExpression an expression that filters results of evaluating the query
         * @return a builder of this type
         */
        public Builder filterExpression(Expression filterExpression) {
            this.filterExpression = filterExpression;
            return this;
        }

        public QueryEnhancedRequest build() {
            return new QueryEnhancedRequest(this);
        }
    }
}
