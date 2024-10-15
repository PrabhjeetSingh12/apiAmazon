package com.apiScraping.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FlipkartApiController {

    @Autowired
    private FlipkartApiService flipkartApiService;

    // Endpoint to trigger Flipkart product details fetch
    @GetMapping("/api/flipkart/product-details")
    public String getFlipkartProductDetails(@RequestParam String pid) {
        return flipkartApiService.getProductDetails(pid);
    }

    @GetMapping("/api/products")
    public List<Product> getAllProducts(){
        return flipkartApiService.getAllProducts();
    }

    // Endpoint to fetch products by product ID
    @GetMapping("/api/products/productId/{productId}")
    public ResponseEntity<List<Product>> getProductsByProductId(@PathVariable String productId) {
        List<Product> products = flipkartApiService.getProductsByProductId(productId);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no products found
        }
        return ResponseEntity.ok(products); // Return 200 with product list
    }

    // Endpoint to fetch products by name
    @GetMapping("/api/products/name/{name}")
    public ResponseEntity<List<Product>> getProductsByName(@PathVariable String name) {
        List<Product> products = flipkartApiService.getProductsByName(name);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no products found
        }
        return ResponseEntity.ok(products); // Return 200 with product list
    }

    // Endpoint to fetch products by partial name
    @GetMapping("/api/products/partial-name/{partialName}")
    public ResponseEntity<List<Product>> getProductsByPartialName(@PathVariable String partialName) {
        List<Product> products = flipkartApiService.getProductsByPartialName(partialName);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no products found
        }
        return ResponseEntity.ok(products); // Return 200 with product list
    }

    // Endpoint to fetch Amazon product details by ASIN
    @GetMapping("/api/amazon/product-details/{asin}")
    public ResponseEntity<String> getAmazonProductDetails(@PathVariable String asin) {
        String result = flipkartApiService.getAmazonProductDetails(asin);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/products/search")
    public List<Product> searchProducts(@RequestParam String initial, @RequestParam String color) {
        return flipkartApiService.getProductsByInitialAndColor(initial, color);
    }

}
