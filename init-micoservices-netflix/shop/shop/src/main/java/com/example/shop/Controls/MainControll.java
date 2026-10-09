package com.example.shop.Controls;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class MainControll {


    @PostMapping ("/shop")
    public String shop(@RequestBody String request) {
        return "shop";
    }
    
}
