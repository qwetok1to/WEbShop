package com.example.admin.Servises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;

import com.example.admin.Entity.AdminEntity;
import com.example.admin.Repository.AdminRep;

public class CustomUserDetrails implements UserDetailsService {
    @Autowired
    private AdminRep userRepository;

    @Override
    public UserDetails loadUserByUsername (String name) throws UsernameNotFoundException {
         AdminEntity user = userRepository.findByname(name).orElseThrow(()->new UsernameNotFoundException("Такого пользователя нет: " + name));
        
        if (user == null) {
            
            throw new UsernameNotFoundException("такого пользователя нет" + name);
            
        }
        @Bulder
        return user.builder()
                .name(user.getName())
                .password(user.getPassword())
                .roles("USER") 
                .build();
    }

}
