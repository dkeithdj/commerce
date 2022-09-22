package com.proj.commerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.proj.commerce.models.Order;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {

}
