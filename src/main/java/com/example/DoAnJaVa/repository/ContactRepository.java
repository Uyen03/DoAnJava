package com.example.DoAnJaVa.repository;

import com.example.DoAnJaVa.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
