package com.proj.commerce.service;

import java.util.List;

import com.proj.commerce.models.Product;

public interface ProductService {
  Product saveProduct(Product product);

  List<Product> fetchProductList();

  List<Product> fetchProductListByClient(Long clientId);

  List<Product> fetchProductListByStocks();

  Product updateProduct(Product product, Long productId);

  void deleteProductById(Long productId);
}
