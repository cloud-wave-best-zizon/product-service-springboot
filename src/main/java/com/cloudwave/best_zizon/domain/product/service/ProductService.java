package com.cloudwave.best_zizon.domain.product.service;

import com.cloudwave.best_zizon.domain.product.entity.Product;
import com.cloudwave.best_zizon.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProduct(String productId) {
        Product p = productRepository.get(productId);
        if (p == null) {
            // 전역 예외 처리기에서 404로 매핑
            throw new IllegalArgumentException("상품 없음: " + productId);
        }
        return p;
    }

    public void upsertProduct(Product product) {
        if (product.getProductId() == null || product.getProductId().isBlank()) {
            // 전역 예외 처리기에서 400으로 매핑
            throw new IllegalArgumentException("productId 필수");
        }
        productRepository.put(product);
    }

    public boolean decrementStock(String productId, long qty) {
        if (qty <= 0) throw new IllegalArgumentException("차감 수량은 1 이상이어야 함");
        // 재고 부족(조건 실패)은 false로 반환 → 컨트롤러에서 409로 응답
        return productRepository.decrementStock(productId, qty);
    }
}
