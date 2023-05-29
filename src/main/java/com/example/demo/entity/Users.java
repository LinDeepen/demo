package com.example.demo.entity;

import com.example.demo.component.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Users extends BaseEntity {
    private String account;//用户登陆账号
    private String password;//密码
    @Column(name = "real_name")
    private String realName;//真实姓名
    private String gender;//性别
    private String mobile;//手机号
    private Boolean enabled;//
}