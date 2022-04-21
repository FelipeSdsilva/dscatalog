package com.devsuperior.dscatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsuperior.dscatalog.repositories.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository repository;
}
