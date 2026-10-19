package com.example.admin.Controllers;



import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.admin.DTO.registerAdmin;
import com.example.admin.DTO.registerAdmin.InnerregisterAdmin;
import com.example.admin.Seqrity.JWT_UTILS;
import com.example.admin.Servises.ServiseRegister;

@RestController
@RequestMapping("/admin")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWT_UTILS jwtUtils;
    private  final ServiseRegister serviseRegister;


    public AuthController(AuthenticationManager authenticationManager, JWT_UTILS jwtUtils,ServiseRegister serviseRegister) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.serviseRegister=serviseRegister;
        
    }
      public record LoginRequest(String username, String password) {
    }
   

    public record TokenResponse(String accessToken, String tokenType, long expiresInSeconds) {
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        if (request == null || isBlank(request.username()) || isBlank(request.password())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username and password are required");
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (AuthenticationException exception) {
            System.out.println("ДОСТУЧАЛСЯ НО UNAUTHORIZED");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return new TokenResponse(jwtUtils.createToken(authentication), "Bearer", jwtUtils.getExpirationSeconds());
    }

    @PostMapping("/register")
    public String postMethodName(@RequestBody String password,String name) {
        try{
        serviseRegister.addNewAdminDB(password,name);
        return "OK";
        }catch(Exception e){
            return "error";
        }

 
        
    }
    

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }



   
    
}
