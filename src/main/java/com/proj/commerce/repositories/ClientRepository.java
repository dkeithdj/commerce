package com.proj.commerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.proj.commerce.models.Client;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Long> {

  Client findByUsernameAndPassword(String username, String password);
}
