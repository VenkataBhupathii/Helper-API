package com.help.help_backend.repository;

import com.help.help_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByUsername(String username);

    List<User> findByRolesContaining(User.Role role);

}
