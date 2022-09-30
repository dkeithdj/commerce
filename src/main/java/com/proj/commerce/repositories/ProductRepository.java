package com.proj.commerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.proj.commerce.models.Product;
import com.proj.commerce.models.Client;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByClientId(Long id);

  List<Product> findByStocksGreaterThan(Integer stocks);

  List<Product> findByTitleContainsAndStocksGreaterThan(String search, Integer stocks);

}
