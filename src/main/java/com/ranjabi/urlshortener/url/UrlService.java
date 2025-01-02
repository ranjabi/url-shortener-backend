package com.ranjabi.urlshortener.url;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ranjabi.urlshortener.entities.Url;
import com.ranjabi.urlshortener.entities.User;

@Service
public class UrlService {
    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public List<Url> getAllUrlsByUserId(Integer userId) {
        return urlRepository.findByUserId(userId);
    }

    public String getOriginalUrl(String shortCode) throws NoSuchElementException {
        Optional<Url> url = urlRepository.findByShortCode(shortCode);
        return url.get().getOriginalUrl();
    }   

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public Url save(Url url) {
        url.setShortCode(generateShortCode());

        return urlRepository.save(url);
    }

    public Url save(Url url, User user) {
        url.setShortCode(generateShortCode());
        url.setUser(user);

        return urlRepository.save(url);
    }
}
