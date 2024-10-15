package com.apiScraping.api;

import com.apiScraping.api.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>  {
    // Custom query method to find products by ID
    List<Product> findByProductId(String productId);

    // Custom query method to find products by name
    List<Product> findByName(String name);

    // Custom query method to find products by name (partial match)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Method to find products by name containing initial and color
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:initial% AND p.name LIKE %:color%")
    List<Product> findByInitialAndColor(@Param("initial") String initial, @Param("color") String color);
}
