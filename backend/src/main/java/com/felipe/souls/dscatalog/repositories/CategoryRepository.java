package com.felipe.souls.dscatalog.repositories;

import com.felipe.souls.dscatalog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
