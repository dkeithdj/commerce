package com.proj.commerce.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

@Entity
@Table(name = "product")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String description;

  private double price;

  @ManyToOne
  @JoinColumn(name = "client_id")
  private Client client;

  private String image;

  private LocalDateTime date;

  private int stocks;

  // @ManyToMany(mappedBy = "products", fetch = FetchType.EAGER)
  // @Nullable
  // private List<Order> orders = new ArrayList<>();

  public Product() {
  }

  public Product(String title, String description, double price, String image, int stocks) {
    this.title = title;
    this.description = description;
    this.price = price;
    this.image = image;
    this.date = LocalDateTime.now();
    this.stocks = stocks;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client user) {
    this.client = user;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public int getStocks() {
    return stocks;
  }

  public void setStocks(int stocks) {
    this.stocks = stocks;
  }

  // public List<Order> getOrders() {
  // return orders;
  // }

  // public void setOrders(List<Order> orders) {
  // this.orders = orders;
  // }

  @Override
  public String toString() {
    return String.format("ID: %d\nTitle: %s\nSeller: %s", id, title, client.getUsername());
  }

  public Object stream() {
    return null;
  }

}
