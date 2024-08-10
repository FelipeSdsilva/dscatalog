package com.felipe.souls.dscatalog.repositories;

import com.felipe.souls.dscatalog.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN FETCH p.categories")
    Page<Product> findAllProductsWithCategories(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE CONCAT(UPPER(:name),'%')")
    Page<Product> paginationAllProductsAndSearchPerName(String name, Pageable pageable);
}
