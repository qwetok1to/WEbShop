package com.example.demo.DTO;

public class AuthDTOS {

    public static record UserDTO(String id, String gmail, String password, String name) {}
    public static record LoginRequest(String id, String password) {}
    public static record UserResponse(String id, String gmail, String name) {}

    
}
