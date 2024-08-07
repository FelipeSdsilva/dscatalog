package com.felipe.souls.dscatalog.dto;

import com.felipe.souls.dscatalog.entities.User;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private String phone;

    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(Long id, String name, String email, LocalDate birthDate, String phone, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.phone = phone;
    }

    public UserDTO(User user) {
        BeanUtils.copyProperties(user, this);
        user.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }
}
