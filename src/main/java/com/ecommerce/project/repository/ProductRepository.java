package com.ecommerce.project.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);

    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageDetails);

    Product findByProductNameIgnoreCase(String productName);
}
