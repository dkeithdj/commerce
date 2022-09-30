package com.proj.commerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proj.commerce.models.Product;
import com.proj.commerce.repositories.ProductRepository;

@Transactional
@Service
public class ProudctServiceImpl implements ProductService {
  @Autowired
  private ProductRepository productRepository;

  @Override
  public Product saveProduct(Product product) {
    return productRepository.save(product);
  }

  @Override
  public List<Product> fetchProductList() {
    return (List<Product>) productRepository.findAll();
  }

  @Override
  public Product updateProduct(Product product, Long productId) {
    Product productDB = productRepository.findById(productId).get();

    productDB.setTitle(product.getTitle());
    productDB.setDescription(product.getDescription());
    productDB.setImage(product.getImage());
    productDB.setPrice(product.getPrice());
    productDB.setStocks(product.getStocks());
    productDB.setClient(product.getClient());

    return productRepository.save(productDB);
  }

  @Override
  public void deleteProductById(Long productId) {
    productRepository.deleteById(productId);

  }

  @Override
  public List<Product> fetchProductListByClient(Long userId) {
    return productRepository.findByClientId(userId);
  }

  @Override
  public List<Product> fetchProductListByStocks() {
    return productRepository.findByStocksGreaterThan(0);
  }

  @Override
  public List<Product> fetchProductListBySearch(String search) {
    return productRepository.findByTitleContainsAndStocksGreaterThan(search, 0);
  }

}
