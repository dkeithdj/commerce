package com.proj.commerce.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "client_id")
  private Client client;

  @ManyToOne(fetch = FetchType.EAGER)
  @Nullable
  private Product products;

  private int quantity;

  private LocalDateTime orderTime;

  public Order() {
    this.orderTime = LocalDateTime.now();
  }

  public Order(Client client) {
    this.client = client;
    this.orderTime = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Product getProducts() {
    return products;
  }

  public void setProducts(Product products) {
    this.products = products;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public LocalDateTime getOrderTime() {
    return orderTime;
  }

  public String toString() {
    return String.format("id: %d\ntime: %s\nClient ID: %d\nQuantity: %d\nProducts: %s", id, orderTime.toString(),
        client.getId(), quantity,
        products);
  }
}
