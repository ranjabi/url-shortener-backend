package com.ranjabi.urlshortener.url;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ranjabi.urlshortener.entities.Url;

@Repository
public interface UrlRepository extends CrudRepository<Url, Integer> {
    Optional<Url> findByShortCode(String shortCode);

    @Query("SELECT u FROM Url u WHERE u.user.id = :userId")
    List<Url> findByUserId(Integer userId);
}
