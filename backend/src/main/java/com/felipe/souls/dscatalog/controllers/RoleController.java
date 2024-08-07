package com.felipe.souls.dscatalog.controllers;

import com.felipe.souls.dscatalog.dto.RoleDTO;
import com.felipe.souls.dscatalog.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<RoleDTO>> getAllRolePaginated(Pageable pageable) {
        return ResponseEntity.ok().body(roleService.listRolePaginated(pageable));
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoleDTO> getRolePerId(@PathVariable Long id) {
        return ResponseEntity.ok().body(roleService.retrieveRolePerId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<RoleDTO> postNewRole(@Valid @RequestBody RoleDTO dto) {
        RoleDTO insertDTO = roleService.insertNewRole(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(insertDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(insertDTO);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<RoleDTO> putRole(@PathVariable Long id, @Valid @RequestBody RoleDTO dto) {
        return ResponseEntity.ok().body(roleService.updateRolePerId(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRolePerId(@PathVariable Long id) {
        roleService.deleteRolePerId(id);
        return ResponseEntity.noContent().build();
    }
}
