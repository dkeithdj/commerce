package com.proj.commerce.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Product {
  private Long id;
  private String title;
  private String description;
  private User user;
  private String image;

}
