package com.example.demo.dao;

import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<Users, String> {
}
