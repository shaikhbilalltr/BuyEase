package com.buyease.repository;

import com.buyease.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find products by category
     */
    List<Product> findByCategory(String category);

    /**
     * Find active products by category
     */
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.isActive = true")
    List<Product> findActiveByCategoryIgnoreCase(@Param("category") String category);

    /**
     * Find all active products
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true")
    List<Product> findAllActive();

    /**
     * Find product by name
     */
    Optional<Product> findByNameIgnoreCase(String name);

    /**
     * Check if product exists by name
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Find products with low quantity
     */
    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold AND p.isActive = true")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
}
