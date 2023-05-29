package com.example.demo.controller;

import com.example.demo.entity.Users;
import com.example.demo.entity.global.IResult;
import com.example.demo.entity.global.JsonResult;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getOne")
    public IResult getOne(@RequestParam(value = "id", defaultValue = "") String id) {
        Users user = userService.getOne(id);
        return JsonResult.success("查询成功", user);
    }

}
