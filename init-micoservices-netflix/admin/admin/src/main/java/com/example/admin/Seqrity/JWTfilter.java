package com.example.admin.Seqrity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.example.admin.Servises.CustomUserDetrails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JWTfilter extends OncePerRequestFilter {

    private final CustomUserDetrails userDetailsService;
    

    public JWTfilter(CustomUserDetrails userDetailsService) {

        this.userDetailsService = userDetailsService;
    }
    @Autowired 
    JWT_UTILS jwt;
      
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            System.out.println("ДОСТУЧАЛСЯ НО UNAUTHORIZED ИЗ ЗА ОТСУТСВИЯ ГОЛОВЫ ЗАПРОСА ИЛИ НЕ ПРАВИЛЬНОГО ПРЕФИКСА");
            logger.info("прошло");
            return;
        }

        String token = authHeader.substring(7);
        String username=jwt.getUsernameFromToken(token);

        if (!jwt.validateToken(token)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            
            System.out.println("ДОСТУЧАЛСЯ НО UNAUTHORIZED YНЕ ИЗ ЗА ФИЛЬТРА");
            logger.info("токен валиден");
            return;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                
        SecurityContextHolder.getContext().setAuthentication(authentication);

        req.setAttribute("username", username);

        chain.doFilter(req, res);
    }
}
