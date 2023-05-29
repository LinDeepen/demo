package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<Users> getList() {
        return userDao.findAll();
    }

    public Users getOne(String id) {
        return userDao.getOne(id);
    }


}
