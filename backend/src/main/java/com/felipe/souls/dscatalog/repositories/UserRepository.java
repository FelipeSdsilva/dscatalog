package com.felipe.souls.dscatalog.repositories;

import com.felipe.souls.dscatalog.entities.User;
import com.felipe.souls.dscatalog.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(name = "searchUserAndRolesByEmail")
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);

}
