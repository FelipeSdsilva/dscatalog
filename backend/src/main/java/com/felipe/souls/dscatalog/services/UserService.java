package com.felipe.souls.dscatalog.services;

import com.felipe.souls.dscatalog.dto.RoleDTO;
import com.felipe.souls.dscatalog.dto.UserDTO;
import com.felipe.souls.dscatalog.dto.UserInsertDTO;
import com.felipe.souls.dscatalog.dto.UserUpdateDTO;
import com.felipe.souls.dscatalog.entities.Role;
import com.felipe.souls.dscatalog.entities.User;
import com.felipe.souls.dscatalog.projections.UserDetailsProjection;
import com.felipe.souls.dscatalog.repositories.RoleRepository;
import com.felipe.souls.dscatalog.repositories.UserRepository;
import com.felipe.souls.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> listUserPaginated(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO retrieveUserPerId(Long id) {
        return new UserDTO(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found " + id)));
    }

    @Transactional(readOnly = true)
    public UserDTO retrieverUserLogged() {
        return new UserDTO(authenticate());
    }

    @Transactional
    public UserDTO insertNewUser(UserInsertDTO dto) {
        User user = new User();
        converterDtoForEntity(user, dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = userRepository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO updateUserPerId(Long id, UserUpdateDTO dto) {
        try {
            User user = userRepository.getReferenceById(id);
            converterDtoForEntity(user, dto);
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user = userRepository.save(user);
            return new UserDTO(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteUserPerId(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (EntityNotFoundException e) {

        }
    }

    private void converterDtoForEntity(User user, UserDTO dto) {
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        user.getRoles().clear();

        for (RoleDTO roleDTO : dto.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            user.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Email not found");
        }

        User user = new User();
        user.setEmail(result.get(0).getUsername());
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection projection : result) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }

    protected User authenticate() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            return userRepository.findByEmail(jwtPrincipal.getClaimAsString("username")).get();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Email not found");
        }
    }

}
