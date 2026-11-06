package com.example.admin.Service;

import org.springframework.stereotype.Service;

import com.example.admin.config.JWT_UTILS;
import com.example.admin.entity.UserEntity;
import com.example.admin.repository.UserRepository;

@Service
public class AdminService {

    private static final String NOT_ADMIN = "not admin";

    private final JWT_UTILS jwt;
    private final UserRepository userRepository;

    public AdminService(JWT_UTILS jwt, UserRepository userRepository) {
        this.jwt = jwt;
        this.userRepository = userRepository;
    }

    public String check_admin(String token) {
        String login = jwt.getUsernameFromToken(token);
        if (login == null || login.isBlank()) {
            return NOT_ADMIN;
        }

        UserEntity user = userRepository.findByLogin(login);
        if (user == null || user.getRole() == null) {
            return NOT_ADMIN;
        }

        return "admin".equalsIgnoreCase(user.getRole()) ? "admin" : NOT_ADMIN;
    }
}
