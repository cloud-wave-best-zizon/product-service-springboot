package com.cloudwave.best_zizon.domain.product.repository;

import com.cloudwave.best_zizon.domain.product.entity.Product;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final DynamoDbEnhancedClient enhanced;
    private final DynamoDbClient dynamo;


    private DynamoDbTable<Product> table() {
        return enhanced.table("products-table", TableSchema.fromBean(Product.class));
    }

    public Product get(String id) {
        return table().getItem(Key.builder().partitionValue(id).build());
    }

    public void put(Product p) {
        table().putItem(p);
    }

    /** 재고 차감: stock >= qty 조건 만족 시에만 차감 */
    public boolean decrementStock(String productId, long qty) {
        UpdateItemRequest req = UpdateItemRequest.builder()
                .tableName("products-table")
                .key(Map.of("product_id", AttributeValue.builder().s(productId).build()))
                .updateExpression("SET stock = if_not_exists(stock, :zero) - :q")
                .conditionExpression("stock >= :q")
                .expressionAttributeValues(Map.of(
                        ":q", AttributeValue.builder().n(Long.toString(qty)).build(),
                        ":zero", AttributeValue.builder().n("0").build()
                ))
                .build();

        try {
            dynamo.updateItem(req);
            return true;
        } catch (ConditionalCheckFailedException e) {
            // 재고 부족
            return false;
        }
    }
}

