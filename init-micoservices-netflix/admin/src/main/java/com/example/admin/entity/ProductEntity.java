package com.example.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int sell;

    @Column(name = "photo_id", nullable = false)
    private String photoId;

    @Column(length = 5000)
    private String description;

    protected ProductEntity() {
    }

    public ProductEntity(String name, int sell, String photoId, String description) {
        this.name = name;
        this.sell = sell;
        this.photoId = photoId;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getSell() { return sell; }
    public String getPhotoId() { return photoId; }
    public String getDescription() { return description; }
}
