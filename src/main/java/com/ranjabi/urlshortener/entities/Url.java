package com.ranjabi.urlshortener.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "urls")
@Entity
@Getter @Setter @NoArgsConstructor
public class Url {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String path;

    public Url(String path) {
        this.path = path;
    }
}
