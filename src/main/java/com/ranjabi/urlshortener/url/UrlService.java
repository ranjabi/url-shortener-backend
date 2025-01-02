package com.ranjabi.urlshortener.url;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ranjabi.urlshortener.entities.Url;

@Service
public class UrlService {
    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Iterable<Url> getAllUrls() {
        return urlRepository.findAll();
    }

    public String getOriginalUrl(String shortCode) throws NoSuchElementException {
        Optional<Url> url = urlRepository.findByShortCode(shortCode);
        return url.get().getOriginalUrl();
    }   

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public Url save(String originalUrl) {
        Url newUrl = this.urlRepository.save(new Url(originalUrl, this.generateShortCode()));
        
        return newUrl;
    }
}
