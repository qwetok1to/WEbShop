package com.example.demo.Servise;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.UsersEntity;
import com.example.demo.rep.usersRep;

@Service
public class SaveDB {

    private final usersRep usersRep;

    public SaveDB(usersRep usersRep) {
        this.usersRep = usersRep;
    }


public void saveUser(UsersEntity usersEntity, String username, String email, String password) {
    usersEntity.setUsername(username);
    usersEntity.setEmail(email);
    usersEntity.setPassword(password);
    usersRep.save(usersEntity);
}
    
}
