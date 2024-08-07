package com.felipe.souls.dscatalog.services;


import com.felipe.souls.dscatalog.dto.CategoryDTO;
import com.felipe.souls.dscatalog.entities.Category;
import com.felipe.souls.dscatalog.repositories.CategoryRepository;
import com.felipe.souls.dscatalog.services.exceptions.DatabaseException;
import com.felipe.souls.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> retrieverAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findCategoryPerId(Long id) {
        return new CategoryDTO(categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id + " Id not found")));
    }

    @Transactional
    public CategoryDTO insertNewCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category = categoryRepository.save(category);
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO updateCategoryPerId(Long id, CategoryDTO categoryDTO) {
        try {
            Category category = categoryRepository.getReferenceById(id);
            category.setName(categoryDTO.getName());
            category = categoryRepository.save(category);
            return new CategoryDTO(category);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id + " Id not found");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteCategoryPerId(Long id) {
        if (!categoryRepository.existsById(id))
            throw new ResourceNotFoundException("Id: " + id + " not found!");
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrate Violation!");
        }
    }
}
