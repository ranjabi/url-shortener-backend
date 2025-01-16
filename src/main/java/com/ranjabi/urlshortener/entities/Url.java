package com.ranjabi.urlshortener.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false, unique = true)
    private String shortCode;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User user;

    @Transient
    private String shortUrl;

    public Url(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Url(String originalUrl, String shortCode) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
    }

    public void setShortUrl(String serverUrl) {
        this.shortUrl = serverUrl + '/' + this.shortCode;
    }
}
