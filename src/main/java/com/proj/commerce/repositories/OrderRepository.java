package com.proj.commerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.proj.commerce.models.Order;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByClientId(Long client_id);

  List<Order> findByClientIdAndProductsId(Long client_id, Long product_id);

}
