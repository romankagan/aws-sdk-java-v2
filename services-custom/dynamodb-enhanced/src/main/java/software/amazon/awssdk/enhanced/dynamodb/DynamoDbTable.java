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

import java.util.function.Consumer;

import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;

/**
 * Synchronous interface for running commands against an object that is linked to a specific DynamoDb table resource
 * and therefore knows how to map records from that table into a modelled object.
 * <p/>
 * By default, all command methods throw an {@link UnsupportedOperationException} to prevent interface extensions from breaking
 * implementing classes.
 * <p/>
 * @param <T> The type of the modelled object.
 */
@SdkPublicApi
public interface DynamoDbTable<T> extends MappedTableResource<T> {
    /**
     * Returns a mapped index that can be used to execute commands against a secondary index belonging to the table
     * being mapped by this object. Note that only a subset of the commands that work against a table will work
     * against a secondary index.
     *
     * @param indexName The name of the secondary index to build the command interface for.
     * @return A {@link DynamoDbIndex} object that can be used to execute database commands against.
     */
    DynamoDbIndex<T> index(String indexName);

    /**
     * Creates a new table in DynamoDb with the name and schema already defined for this DynamoDbTable
     * together with additional parameters specified in the supplied request object, {@link CreateTableEnhancedRequest}.
     * <p/>
     * Use {@link DynamoDbEnhancedClient#table(String, TableSchema)} to define the mapped table resource.
     * <p/>
     * This operation calls the low-level DynamoDB API CreateTable operation. Note that this is an asynchronous operation and that
     * the table may not immediately be available for writes and reads. Currently, there is no mechanism supported within this
     * library to wait for/check the status of a created table. You must provide this functionality yourself.
     * Consult the CreateTable documentation for further details and constraints.
     *
     * @param request A {@link CreateTableEnhancedRequest} containing optional parameters for table creation.
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default Void createTable(CreateTableEnhancedRequest request) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new table in DynamoDb with the name and schema already defined for this DynamoDbTable
     * together with additional parameters specified in the supplied request object, {@link CreateTableEnhancedRequest}.
     * <p/>
     * Use {@link DynamoDbEnhancedClient#table(String, TableSchema)} to define the mapped table resource.
     * <p/>
     * This operation calls the low-level DynamoDB API CreateTable operation. Note that this is an asynchronous operation and that
     * the table may not immediately be available for writes and reads. Currently, there is no mechanism supported within this
     * library to wait for/check the status of a created table. You must provide this functionality yourself.
     * Consult the CreateTable documentation for further details and constraints.
     *
     * @param requestConsumer A {@link Consumer} of {@link CreateTableEnhancedRequest.Builder} containing optional parameters
     * for table creation.
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default Void createTable(Consumer<CreateTableEnhancedRequest.Builder> requestConsumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a new table in DynamoDb with the name and schema already defined for this DynamoDbTable.
     * <p/>
     * Use {@link DynamoDbEnhancedClient#table(String, TableSchema)} to define the mapped table resource.
     * <p/>
     * This operation calls the low-level DynamoDB API CreateTable operation. Note that this is an asynchronous operation and that
     * the table may not immediately be available for writes and reads. Currently, there is no mechanism supported within this
     * library to wait for/check the status of a created table. You must provide this functionality yourself.
     * Consult the CreateTable documentation for further details and constraints.
     *
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default Void createTable() {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes a single item from the mapped table using a supplied primary {@link Key}.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link DeleteItemEnhancedRequest}.
     * <p/>
     * This operation calls the low-level DynamoDB API DeleteItem operation. Consult the DeleteItem documentation for
     * further details and constraints.
     * <p/>
     * Example:
     * <pre>
     * {@code
     * mappedTable.delete(DeleteItemEnhancedRequest.builder().key(key).build());
     * }
     * </pre>
     *
     * @param request A {@link DeleteItemEnhancedRequest} with key and optional directives for deleting an item from the table.
     * @return The deleted item
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default T deleteItem(DeleteItemEnhancedRequest request) {
        throw new UnsupportedOperationException();
    }

    /**
     * Deletes a single item from the mapped table using a supplied primary {@link Key}.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link DeleteItemEnhancedRequest}.
     * <p/>
     * This operation calls the low-level DynamoDB API DeleteItem operation. Consult the DeleteItem documentation for
     * further details and constraints.
     * <p/>
     * Example:
     * <pre>
     * {@code
     * mappedTable.delete(r -> r.key(key));
     * }
     * </pre>
     *
     * @param requestConsumer A {@link Consumer} of {@link DeleteItemEnhancedRequest} with key and
     * optional directives for deleting an item from the table.
     * @return The deleted item
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default T deleteItem(Consumer<DeleteItemEnhancedRequest.Builder> requestConsumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieves a single item from the mapped table using a supplied primary {@link Key}. Because the enhanced library operates
     * on whole objects, getItem retrieves all available attribute values for the item.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link GetItemEnhancedRequest}.
     * <p/>
     * This operation calls the low-level DynamoDB API GetItem operation. Consult the GetItem documentation for
     * further details and constraints.
     * <p/>
     * Example:
     * <pre>
     * {@code
     * MyItem item = mappedTable.getItem(GetItemEnhancedRequest.builder().key(key).build());
     * }
     * </pre>
     *
     * @param request A {@link GetItemEnhancedRequest} with key and optional directives for retrieving an item from the table.
     * @return The retrieved item
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default T getItem(GetItemEnhancedRequest request) {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieves a single item from the mapped table using a supplied primary {@link Key}. Because the enhanced library operates
     *      * on whole objects, getItem retrieves all available attribute values for the item.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link GetItemEnhancedRequest}.
     * <p/>
     * This operation calls the low-level DynamoDB API GetItem operation. Consult the GetItem documentation for
     * further details and constraints.
     * <p/>
     * Example:
     * <pre>
     * {@code
     * MyItem item = mappedTable.getItem(r -> r.key(key));
     * }
     * </pre>
     *
     * @param requestConsumer A {@link Consumer} of {@link GetItemEnhancedRequest.Builder} with key and optional directives
     * for retrieving an item from the table.
     * @return The retrieved item
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default T getItem(Consumer<GetItemEnhancedRequest.Builder> requestConsumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Executes a query against the table using {@link QueryConditional} expressions to retrieve a list of items matching the
     * given conditions.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link QueryEnhancedRequest} and include:
     * <ul>
     *     <li>{@link QueryEnhancedRequest#filterExpression()} - Filters the returned results from the database.</li>
     *     <li>{@link QueryEnhancedRequest#limit()} - Sets the maximal number of results per page</li>
     * </ul>
     * This operation calls the low-level DynamoDB API Query operation. Consult the Query documentation for
     * further details and constraints.
     * <p/>
     * Example:
     * <pre>
     * {@code
     *
     * QueryConditional queryConditional = equalTo(Key.create(stringValue("id-value")));
     * mappedTable.query(QueryEnhancedRequest.builder().queryConditional(queryConditional).build());
     * }
     * </pre>
     *
     * @param request A {@link QueryEnhancedRequest} defining the query conditions and how
     * to handle the results.
     * @return an iterator of type {@link SdkIterable} with paginated results (see {@link Page}). If no matches are found,
     * the iterator will contain an empty page. Results are sorted by sort key value in ascending order.
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default SdkIterable<Page<T>> query(QueryEnhancedRequest request) {
        throw new UnsupportedOperationException();
    }

    /**
     * Executes a query against the table using {@link QueryConditional} expressions to retrieve a list of items matching the
     * given conditions.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link QueryEnhancedRequest}.
     *
     * <p/>
     * Example:
     * <pre>
     * {@code
     *
     * QueryConditional queryConditional = equalTo(Key.create(stringValue("id-value")));
     * mappedTable.query(r -> r.queryConditional(queryConditional));
     * }
     * </pre>
     *
     * @param requestConsumer A {@link Consumer} of {@link QueryEnhancedRequest} defining the query conditions and how to
     * handle the results.
     * @return an iterator of type {@link SdkIterable} with paginated results (see {@link Page}). If no matches are found,
     * the iterator will contain an empty page. Results are sorted by sort key value in ascending order.
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default SdkIterable<Page<T>> query(Consumer<QueryEnhancedRequest.Builder> requestConsumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Puts a single item in the mapped table. If the table contains an item with the same primary key, it will be replaced with
     * this item.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link PutItemEnhancedRequest}.
     * <p/>
     * This operation calls the low-level DynamoDB API PutItem operation. Consult the PutItem documentation for
     * further details and constraints.
     * <p/>
     * Example:
     * <pre>
     * {@code
     * mappedTable.putItem(UpdateItemEnhancedRequest.builder(MyItem.class).item(item).build());
     * }
     * </pre>
     *
     * @param request A {@link PutItemEnhancedRequest} that includes the item to enter into
     * the table, its class and optional directives.
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default Void putItem(PutItemEnhancedRequest<T> request) {
        throw new UnsupportedOperationException();
    }

    /**
     * Puts a single item in the mapped table. If the table contains an item with the same primary key, it will be replaced with
     * this item.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link PutItemEnhancedRequest}.
     * <p/>
     * This operation calls the low-level DynamoDB API PutItem operation. Consult the PutItem documentation for
     * further details and constraints.
     * <p/>
     * Example:
     * <pre>
     * {@code
     * mappedTable.putItem(MyItem.class, r -> r.item(item));
     * }
     * </pre>
     *
     * @param requestConsumer A {@link Consumer} of {@link PutItemEnhancedRequest.Builder} that includes the item
     * to enter into the table, its class and optional directives.
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default Void putItem(Class<? extends T> itemClass, Consumer<PutItemEnhancedRequest.Builder<T>> requestConsumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Scans the table and retrieves all items.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link ScanEnhancedRequest}.
     * <p/>
     * Example:
     * <pre>
     * {@code
     *
     * mappedTable.scan(ScanEnhancedRequest.builder().build());
     * }
     * </pre>
     *
     * @param request A {@link ScanEnhancedRequest} defining how to handle the results.
     * @return an iterator of type {@link SdkIterable} with paginated results (see {@link Page}). If no matches are found,
     * the iterator will contain an empty page.
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default SdkIterable<Page<T>> scan(ScanEnhancedRequest request) {
        throw new UnsupportedOperationException();
    }

    /**
     * Scans the table and retrieves all items.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link ScanEnhancedRequest}.
     * <p/>
     * Example:
     * <pre>
     * {@code
     *
     * mappedTable.scan(r -> r.limit(5));
     * }
     * </pre>
     *
     * @param requestConsumer A {@link Consumer} of {@link ScanEnhancedRequest} defining the query conditions and how to
     * handle the results.
     * @return an iterator of type {@link SdkIterable} with paginated results (see {@link Page}). If no matches are found,
     * the iterator will contain an empty page.
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default SdkIterable<Page<T>> scan(Consumer<ScanEnhancedRequest.Builder> requestConsumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Scans the table and retrieves all items using default settings.
     * <p/>
     * Example:
     * <pre>
     * {@code
     *
     * mappedTable.scan();
     * }
     * </pre>
     *
     * @return an iterator of type {@link SdkIterable} with paginated results (see {@link Page}). If no matches are found,
     * the iterator will contain an empty page.
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default SdkIterable<Page<T>> scan() {
        throw new UnsupportedOperationException();
    }

    /**
     * Updates an item in the mapped table.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link UpdateItemEnhancedRequest}.
     * <p/>
     * This operation calls the low-level DynamoDB API UpdateItem operation. Consult the UpdateItem documentation for
     * further details and constraints.
     * <p/>
     * Example:
     * <pre>
     * {@code
     * mappedTable.updateItem(UpdateItemEnhancedRequest.builder(MyItem.class).item(item).build());
     * }
     * </pre>
     *
     * @param request A {@link UpdateItemEnhancedRequest} that includes the item to be updated,
     * its class and optional directives.
     * @return The updated item
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default T updateItem(UpdateItemEnhancedRequest<T> request) {
        throw new UnsupportedOperationException();
    }

    /**
     * Updates an item in the mapped table.
     * <p/>
     * The additional configuration parameters that the enhanced client supports are defined
     * in the {@link UpdateItemEnhancedRequest}.
     * <p/>
     * This operation calls the low-level DynamoDB API UpdateItem operation. Consult the UpdateItem documentation for
     * further details and constraints.
     * <p/>
     * Example:
     * <pre>
     * {@code
     * mappedTable.updateItem(MyItem.class, r -> r.item(item));
     * }
     * </pre>
     *
     * @param requestConsumer A {@link Consumer} of {@link UpdateItemEnhancedRequest.Builder} that includes the item
     * to be updated, its class and optional directives.
     * @return The updated item
     * @throws UnsupportedOperationException if there exists no overriding implementation of this method
     */
    default T updateItem(Class<? extends T> itemClass, Consumer<UpdateItemEnhancedRequest.Builder<T>> requestConsumer) {
        throw new UnsupportedOperationException();
    }
}
