package com.proj.commerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.proj.commerce.models.Order;
import com.proj.commerce.models.Product;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByClientId(Long client_id);

  Order findByClientIdAndProductsId(Long client_id, Long product_id);

  // @Query(value = "SELECT * FROM ORDER_PRODUCT")
  // List<Order> getOrders();
  // Order findByProductsId(Long id);

}
