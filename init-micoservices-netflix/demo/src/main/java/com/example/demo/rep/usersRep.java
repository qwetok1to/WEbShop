package com.example.demo.rep;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.UsersEntity;

public interface usersRep extends JpaRepository<UsersEntity, Long> {
    
}
