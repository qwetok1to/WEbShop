package com.example.admin.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.admin.Service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/admin-dashboard")
public class RegisterController{
    private final AdminService adminService;

    public RegisterController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody String token) {
        if (adminService.check_admin(token)) {
            return ResponseEntity.ok("admin");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("not admin");
    }
}
