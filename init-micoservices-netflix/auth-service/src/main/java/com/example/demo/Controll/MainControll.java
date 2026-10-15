package com.example.demo.Controll;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.DTO.AuthDTOS;
import com.example.demo.Servise.AuthTokenService;
import com.example.demo.Servise.RedisSerivce;

@RestController
@RequestMapping("/auth-public")
public class MainControll {

    private final RedisSerivce servise;
    private final AuthTokenService tokenService;

    public MainControll(RedisSerivce servise, AuthTokenService tokenService) {
        this.servise = servise;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDTOS.UserResponse> register(@RequestBody AuthDTOS.UserDTO dto) {
        AuthDTOS.UserResponse user;
        try {
            user = servise.register(dto);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        } catch (IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage(), exception);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTOS.LoginResponse> login(@RequestBody AuthDTOS.LoginRequest request) {
        if (request == null || request.id() == null || request.id().isBlank()
                || request.password() == null || request.password().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id and password are required");
        }
        try {
            AuthDTOS.UserResponse user = servise.login(request);
            return ResponseEntity.ok(new AuthDTOS.LoginResponse(
                    user.id(),
                    user.gmail(),
                    user.name(),
                    tokenService.createToken(user),
                    "Bearer",
                    tokenService.getExpirationSeconds()));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid id or password", exception);
        }
    }

    @GetMapping("/test")
    public String getMethodName() {
        return "Received parameter: ";
    }
}
