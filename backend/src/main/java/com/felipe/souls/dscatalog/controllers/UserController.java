package com.felipe.souls.dscatalog.controllers;

import com.felipe.souls.dscatalog.dto.UserDTO;
import com.felipe.souls.dscatalog.dto.UserInsertDTO;
import com.felipe.souls.dscatalog.dto.UserUpdateDTO;
import com.felipe.souls.dscatalog.services.UserService;
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
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUserPaginated(Pageable pageable) {
        return ResponseEntity.ok().body(userService.listUserPaginated(pageable));
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENT')")
    public ResponseEntity<UserDTO> getUserLogged() {
        return ResponseEntity.ok(userService.retrieverUserLogged());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUserPerId(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.retrieveUserPerId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> postNewUser(@Valid @RequestBody UserInsertDTO dto) {
        UserDTO insertDTO = userService.insertNewUser(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(insertDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(insertDTO);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> putUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        return ResponseEntity.ok().body(userService.updateUserPerId(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUserPerId(@PathVariable Long id) {
        userService.deleteUserPerId(id);
        return ResponseEntity.noContent().build();
    }
}
