package com.proj.commerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.proj.commerce.models.Product;
import com.proj.commerce.models.UserInfo;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

}
