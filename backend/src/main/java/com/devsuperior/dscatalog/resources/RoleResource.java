package com.devsuperior.dscatalog.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.services.RoleService;

@RestController
public class RoleResource {

	@SuppressWarnings("unused")
	@Autowired
	private RoleService service;
}
