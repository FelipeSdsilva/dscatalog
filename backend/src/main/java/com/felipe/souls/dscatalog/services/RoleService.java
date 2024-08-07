package com.felipe.souls.dscatalog.services;


import com.felipe.souls.dscatalog.dto.RoleDTO;
import com.felipe.souls.dscatalog.entities.Role;
import com.felipe.souls.dscatalog.repositories.RoleRepository;
import com.felipe.souls.dscatalog.services.exceptions.DatabaseException;
import com.felipe.souls.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<RoleDTO> listRolePaginated(Pageable pageable) {
        return roleRepository.findAll(pageable).map(RoleDTO::new);
    }

    @Transactional(readOnly = true)
    public RoleDTO retrieveRolePerId(Long id) {
        return new RoleDTO(roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id not found!")));
    }

    @Transactional
    public RoleDTO insertNewRole(RoleDTO dto) {
        Role role = new Role();
        role.setAuthority(dto.getAuthority());
        role = roleRepository.save(role);
        return new RoleDTO(role);
    }

    @Transactional
    public RoleDTO updateRolePerId(Long id, RoleDTO dto) {
        try {
            Role role = roleRepository.getReferenceById(id);
            role.setAuthority(dto.getAuthority());
            role = roleRepository.save(role);
            return new RoleDTO(role);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found!");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteRolePerId(Long id) {
        if (!roleRepository.existsById(id))
            throw new ResourceNotFoundException("Id not found!");
        try {
            roleRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation!");
        }
    }
}
