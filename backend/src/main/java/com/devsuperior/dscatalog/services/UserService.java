package com.devsuperior.dscatalog.services;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(User.class);
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User obj = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not Found"));
        return new UserDTO(obj);
    }

    @Transactional
    public UserDTO inset(UserInsertDTO dto) {
        User entity = new User();
        entity.converterDtoInEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        try {
            @SuppressWarnings("deprecation")
            User entity = repository.getOne(id);
            entity.converterDtoInEntity(dto, entity);
            entity = repository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity Violantion");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);
        if (user == null) {
            logger.error("User not found " + username);
            throw new UsernameNotFoundException("Email not found ");
        }
        logger.info("User found " + username);
        return user;
    }
}