package com.felipe.souls.dscatalog.controllers;

import com.felipe.souls.dscatalog.dto.CategoryDTO;
import com.felipe.souls.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CategoryDTO>> getCategoryPerPaged() {
        return ResponseEntity.ok(categoryService.retrieverAllCategories());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> getCategoryPerId(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findCategoryPerId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryDTO> postNewCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO categoryDTO1 = categoryService.insertNewCategory(categoryDTO);
        var uri = fromCurrentRequest().path("/{id}").buildAndExpand(categoryDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryDTO1);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryDTO> putCategoryPerId(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategoryPerId(id, categoryDTO));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCategoryPerId(@PathVariable Long id) {
        categoryService.deleteCategoryPerId(id);
        return ResponseEntity.noContent().build();
    }
}
