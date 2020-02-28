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

package software.amazon.awssdk.enhanced.dynamodb;

import java.util.List;
import java.util.function.Consumer;

import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.internal.client.DefaultDynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetResultPage;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactGetItemsEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactGetResultPage;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Synchronous interface for running commands against a DynamoDb database.
 */
@SdkPublicApi
public interface DynamoDbEnhancedClient {

    /**
     * Returns a mapped table that can be used to execute commands that work with mapped items against that table.
     *
     * @param tableName The name of the physical table persisted by DynamoDb.
     * @param tableSchema A {@link TableSchema} that maps the table to a modelled object.
     * @return A {@link DynamoDbTable} object that can be used to execute table operations against.
     * @param <T> The modelled object type being mapped to this table.
     */
    <T> DynamoDbTable<T> table(String tableName, TableSchema<T> tableSchema);

    /**
     * Retrieves items from one or more tables by their primary keys, see {@link Key}.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link BatchGetItemEnhancedRequest}.
     * <p/>
     * <b>Partial results</b>. A single call has restraints on how much
     * data can be retrieved. If those limits are exceeded, the call will yield a partial result. This may also be the case if
     * provisional throughput is exceeded or there is an internal DynamoDb processing failure. The enhanced client does not
     * currently support retrieving information on partial results.
     * <p/>
     * This operation calls the low-level DynamoDB API BatchGetItem operation. Consult the BatchGetItem documentation for
     * further details and constraints as well as current limits of data retrieval.
     * <p/>
     * Example:
     * <pre>
     * {@code
     * batchResults = enhancedClient.batchGetItem(
     *            BatchGetItemEnhancedRequest.builder()
     *                                       .readBatches(ReadBatch.builder(FirstItem.class)
     *                                                             .mappedTableResource(firstItemTable)
     *                                                             .addGetItem(GetItemEnhancedRequest.builder().key(key1).build())
     *                                                             .addGetItem(GetItemEnhancedRequest.builder().key(key2).build())
     *                                                             .build(),
     *                                                    ReadBatch.builder(SecondItem.class)
     *                                                             .mappedTableResource(secondItemTable)
     *                                                             .addGetItem(GetItemEnhancedRequest.builder().key(key3).build())
     *                                                             .build())
     *                                       .build());
     * }
     * </pre>
     *
     * @param request A {@link BatchGetItemEnhancedRequest} containing keys grouped by tables.
     * @return an iterator of type {@link SdkIterable} with paginated results of type {@link BatchGetResultPage}.
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default SdkIterable<BatchGetResultPage> batchGetItem(BatchGetItemEnhancedRequest request) {
        throw new UnsupportedOperationException();
    }

    default SdkIterable<BatchGetResultPage> batchGetItem(Consumer<BatchGetItemEnhancedRequest.Builder> requestConsumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Puts or deletes multiple items in one or more tables.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link BatchWriteItemEnhancedRequest}.
     * <p/>
     * <b>Note: </b> BatchWriteItem cannot update items. Instead, use the individual updateItem operation
     * {@link DynamoDbTable#updateItem(UpdateItemEnhancedRequest)}.
     * <p/>
     * <b>Partial updates</b><br>Each delete or put call is atomic, but the operation as a whole is not.
     * If individual operations fail due to exceeded provisional throughput internal DynamoDb processing failures,
     * the failed requests can be retrieved through the result, see {@link BatchWriteResult}.
     * <p/>
     * There are some conditions that cause the whole batch operation to fail. These include non-existing tables, erroneously
     * defined primary key attributes, attempting to put and delete the same item as well as referring more than once to the same
     * hash and range (sort) key.
     * <p/>
     * This operation calls the low-level DynamoDB API BatchWriteItem operation. Consult the BatchWriteItem documentation for
     * further details and constraints, current limits of data to write and/or delete, how to handle partial updates and retries
     * and under which conditions the operation will fail.
     * <p/>
     * Example:
     * <pre>
     * {@code
     * batchResult = enhancedClient.batchWriteItem(
     *            BatchWriteItemEnhancedRequest.builder()
     *                                         .writeBatches(WriteBatch.builder(FirstItem.class)
     *                                                                 .mappedTableResource(firstItemTable)
     *                                                                 .addPutItem(PutItemEnhancedRequest.builder().item(item1).build())
     *                                                                 .addDeleteItem(DeleteItemEnhancedRequest.builder().key(key2).build())
     *                                                                 .build(),
     *                                                       WriteBatch.builder(SecondItem.class)
     *                                                                 .mappedTableResource(secondItemTable)
     *                                                                 .addPutItem(PutItemEnhancedRequest.builder().item(item3).build())
     *                                                                 .build())
     *                                         .build());
     * }
     * </pre>
     *
     * @param request A {@link BatchWriteItemEnhancedRequest} containing keys grouped by tables.
     * @return a {@link BatchWriteResult} containing any unprocessed requests.
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default BatchWriteResult batchWriteItem(BatchWriteItemEnhancedRequest request) {
        throw new UnsupportedOperationException();
    }

    default BatchWriteResult batchWriteItem(Consumer<BatchWriteItemEnhancedRequest.Builder> requestConsumer) {
        throw new UnsupportedOperationException();
    }

    default List<TransactGetResultPage> transactGetItems(TransactGetItemsEnhancedRequest request) {
        throw new UnsupportedOperationException();
    }

    default List<TransactGetResultPage> transactGetItems(Consumer<TransactGetItemsEnhancedRequest.Builder> requestConsumer) {
        throw new UnsupportedOperationException();
    }

    default Void transactWriteItems(TransactWriteItemsEnhancedRequest request) {
        throw new UnsupportedOperationException();
    }

    default Void transactWriteItems(Consumer<TransactWriteItemsEnhancedRequest.Builder> requestConsumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a default builder for {@link DynamoDbEnhancedClient}.
     */
    static Builder builder() {
        return DefaultDynamoDbEnhancedClient.builder();
    }

    /**
     * The builder definition for a {@link DynamoDbEnhancedClient}.
     */
    interface Builder {
        Builder dynamoDbClient(DynamoDbClient dynamoDbClient);

        Builder extendWith(DynamoDbEnhancedClientExtension dynamoDbEnhancedClientExtension);

        DynamoDbEnhancedClient build();
    }
}
