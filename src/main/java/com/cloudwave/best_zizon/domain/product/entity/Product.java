package com.cloudwave.best_zizon.domain.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private String productId;  // product_id 컬럼 매핑
    private Long stock;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("product_id")  // AWS 테이블 속성명 매핑
    public String getProductId() {
        return productId;
    }
}
