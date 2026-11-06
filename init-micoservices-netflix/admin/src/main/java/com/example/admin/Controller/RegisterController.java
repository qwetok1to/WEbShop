package com.example.admin.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.admin.Service.AdminService;

@RestController
@RequestMapping("/register")
public class RegisterController{
    private final AdminService adminService;

    public RegisterController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/admin")
    public String checkAdmin(@RequestBody String token) {
        return adminService.check_admin(token);
    }

}
