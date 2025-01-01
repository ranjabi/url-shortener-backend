package com.ranjabi.urlshortener.url;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ranjabi.urlshortener.entities.Url;

@Repository
public interface UrlRepository extends CrudRepository<Url, Integer> {
    Optional<Url> findByShortCode(String shortCode);
}
