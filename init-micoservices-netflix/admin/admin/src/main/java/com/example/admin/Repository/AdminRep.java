package com.example.admin.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.admin.Entity.AdminEntity;


public interface AdminRep extends  JpaRepository<AdminEntity, Long> {
    Optional<AdminEntity> findByname(String name);

    
}