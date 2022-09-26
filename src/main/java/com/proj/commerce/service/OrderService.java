package com.proj.commerce.service;

import java.util.List;

import com.proj.commerce.models.Order;
import com.proj.commerce.models.Product;

public interface OrderService {
  Order saveOrder(Order order);

  List<Order> fetchOrdersByClientId(Long client_id);

  List<Product> fetchProductsByClientId(Long client_id);

  Order fetchOrderByClientIdAndProductId(Long client_id, Long product_id);
}
