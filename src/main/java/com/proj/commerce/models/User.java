package com.proj.commerce.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  // @Column(name = "username", nullable = false, columnDefinition = "TEXT")
  private String username;

  // @Column(name = "password", nullable = false, columnDefinition = "TEXT")
  private String password;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public User() {
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return String.format("id: %d\nusername: %s\npassword: %s", id, username, password);
  }
}
