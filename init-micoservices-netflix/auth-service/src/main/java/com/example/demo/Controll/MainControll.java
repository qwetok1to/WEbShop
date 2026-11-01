
package com.example.demo.Controll;

import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.DTO.AuthDTOS;
import com.example.demo.Servise.JWT_UTILS;
import com.example.demo.Servise.RedisSerivce;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
 @Tag (name="auth",description="привет")
@RestController
@RequestMapping("/auth-public")
public class MainControll {

    private final JWT_UTILS jwt;
    private final RedisSerivce redisS;

    public MainControll(JWT_UTILS jwt, RedisSerivce redisS) {
        this.redisS = redisS;
        this.jwt = jwt;
    }
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "регистер пост мапинг") 
    @Operation (description ="register")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthDTOS dto,   @Parameter(name = "name",description = "String-lololo") String name) {
      
        String req = redisS.register(dto);

        switch (req) {
            case "BAD_REQUEST":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(req);

            case "GONE":
                return ResponseEntity.status(HttpStatus.GONE).body(req);

            default:
                return ResponseEntity.status(HttpStatus.CREATED).body(req);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDTOS dto) {
        String req = redisS.login(dto);
        switch (req) {
            case "Forbidden":
                   return ResponseEntity.status(HttpStatus.FORBIDDEN).body(req);

            default:
                return ResponseEntity.status(HttpStatus.OK).body(req);
            
        }

   
    }

}
