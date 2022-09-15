package com.proj.commerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proj.commerce.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
