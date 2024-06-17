package com.example.DoAnJaVa.repository;
import com.example.DoAnJaVa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}