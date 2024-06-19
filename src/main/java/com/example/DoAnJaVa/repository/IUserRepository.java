package com.example.DoAnJaVa.repository;

import com.example.DoAnJaVa.Role;
import com.example.DoAnJaVa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    void deleteById(Long id);

    Optional<User> findById(Long id);

    List<User> findUsersByRoles(Role role);
}