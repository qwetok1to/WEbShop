package com.example.admin.Servises;

import org.springframework.stereotype.Service;
import com.example.admin.Entity.AdminEntity;
import com.example.admin.Repository.AdminRep;

@Service
public class ServiseRegister {

    private final AdminRep adminRep;

    public ServiseRegister(AdminRep adminRep) {
        this.adminRep = adminRep;
    }
    public String addNewAdminDB(String password, String name) {
        AdminEntity adminEntity = new AdminEntity();
        try {
            adminEntity.setPassword(password);
            adminEntity.setName(name);
            adminRep.save(adminEntity);
            return "Save";
        } catch (Exception e) {
            return "error save";
        }
    }

}