package com.felipe.souls.dscatalog.controllers;


import com.felipe.souls.dscatalog.dto.ProductDTO;
import com.felipe.souls.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProductsPaginated(Pageable pageable) {
        return ResponseEntity.ok(productService.allProductsPaginated(pageable));
    }

//    @GetMapping(value = "/mine")
//    public ResponseEntity<Page<ProductMinRecord>> getProductsResumedPerNameAndPagination(@RequestParam(name = "name", defaultValue = "") String name, Pageable pageable) {
//        return ResponseEntity.ok(productService.retrieverAllProductResumedPaginated(name, pageable));
//    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> getProductPerId(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductPerId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDTO> postProduct(@RequestBody ProductDTO productDTO) {
        productDTO = productService.insertNewProduct(productDTO);
        var uri = fromCurrentRequest().path("/{id}").buildAndExpand(productDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(productDTO);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDTO> putProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProductPerId(id, productDTO));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProductPerId(@PathVariable Long id) {
        productService.deleteProductPerId(id);
        return ResponseEntity.noContent().build();
    }
}
