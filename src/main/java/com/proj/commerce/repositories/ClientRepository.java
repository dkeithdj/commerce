package com.proj.commerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.proj.commerce.models.Product;
import com.proj.commerce.models.Client;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Long> {

  Client findByUsernameAndPassword(String username, String password);
  // UserInfo findByUsernameToPassword(String username, String password);
  // @Query("SELECT a FROM USER_INFO a WHERE a.username=:u AND a.password=:p")
  // List<UserInfo> findUser(@Param("u") String username, @Param("p") String
  // password);

  // @Query("SELECT * FROM USER_INFO")
  // List<UserInfo> getAll();

}
