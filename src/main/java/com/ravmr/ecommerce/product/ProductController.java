package com.ravmr.ecommerce.product;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ravmr.ecommerce.product.ProductResponseDtos.ProductResponseDto;

@RestController
@RequestMapping("/api/products")
class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponseDto> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        return productService.getAllProducts(category, minPrice, maxPrice);
    }

    @GetMapping("/{category}")
    public List<ProductResponseDto> getProductsByCategory(
        @PathVariable String category,
        @RequestParam Map<String, String> filters
    ) {
        System.out.println("Received filters: " + filters);
        return productService.getProductsByCategory(category, filters);
    }
    
}