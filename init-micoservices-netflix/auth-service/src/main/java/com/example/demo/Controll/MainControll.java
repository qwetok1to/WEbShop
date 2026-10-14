package com.example.demo.Controll;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.AuthDTOS;
import com.example.demo.Servise.RedisSerivce;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping ("/auth-public")
public class MainControll {
    private final RedisSerivce servise;

    public MainControll(RedisSerivce servise) {
        this.servise = servise;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthDTOS.UserDTO dto) {
        servise.register(dto);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");
    }
    // TODO: Make a jwt
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTOS.LoginRequest request) {
        return ResponseEntity.ok(servise.login(request));
    }
    @GetMapping("/test")
    public String getMethodName() {
       return "Received parameter: ";
    }
    


    
}
