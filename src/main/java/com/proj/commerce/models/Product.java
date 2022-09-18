package com.proj.commerce.models;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
  @Id
  // @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence",
  // allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String description;

  private double price;

  // @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey =
  // @ForeignKey(name = "user_product_fk"))
  @ManyToOne
  @JoinColumn(name = "user_id")
  private Client client;

  private String image;

  private LocalDateTime date;

  private int stocks;

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

}
