package com.devsuperior.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.services.UserService;

@RestController
@RequestMapping()
public class UserResource {

	@Autowired
	private UserService service;
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
		Page<UserDTO> list = service.findAllPaged( pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> finById(@PathVariable Long id) {
		UserDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}

 /*	@PostMapping
	public ResponseEntity<UserDTO> insert(@RequestBody UserDTO dto) {
		dto = service.inset(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
*/
	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
