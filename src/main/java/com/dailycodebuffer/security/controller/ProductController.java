package com.dailycodebuffer.security.controller;

import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private record Product(Integer productId, String productName, double price) {}

    List<Product> products = new ArrayList<>(
            List.of(
                    new Product(1, "Laptop", 85000),
                    new Product(2, "Smartphone", 50000),
                    new Product(3, "Tablet", 30000)
            )
    );

    @GetMapping
    public List<Product> getProducts() {
        return products;
    }

    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        products.add(product);
        return product;
    }

}
