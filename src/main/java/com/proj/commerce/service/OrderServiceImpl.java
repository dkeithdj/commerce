package com.proj.commerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proj.commerce.models.Order;
import com.proj.commerce.models.Product;
import com.proj.commerce.repositories.OrderRepository;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {
  @Autowired
  OrderRepository orderRepository;

  @Override
  public Order saveOrder(Order order) {
    return orderRepository.save(order);
  }

  @Override
  public List<Order> fetchOrdersByClientId(Long client_id) {
    return orderRepository.findByClientId(client_id);
  }

  @Override
  public Order fetchOrderByClientIdAndProductId(Long client_id, Long product_id) {
    // clientOrders.forEach(order -> {
    // order.getProducts().forEach(product -> productOrders.add(product));
    // });
    return orderRepository.findByClientIdAndProductsId(client_id, product_id);
  }

  @Override
  public List<Product> fetchProductsByClientId(Long client_id) {
    return orderRepository.findByClientId(client_id).stream().flatMap(orders -> orders.getProducts().stream())
        .collect(Collectors.toList());
  }

}
