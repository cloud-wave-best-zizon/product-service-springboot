package com.cloudwave.best_zizon.domain.product.controller;

import com.cloudwave.best_zizon.domain.product.entity.Product;
import com.cloudwave.best_zizon.domain.product.service.ProductService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PostMapping
    public ResponseEntity<Void> upsert(@RequestBody Product product) {
        productService.upsertProduct(product);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/decrement")
    public ResponseEntity<?> decrement(@PathVariable String id, @RequestParam long qty) {
        boolean ok = productService.decrementStock(id, qty);
        if (ok) {
            return ResponseEntity.ok(Map.of("status", "OK"));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("code", "OUT_OF_STOCK", "message", "재고 부족"));
    }
}

