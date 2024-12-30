package com.ranjabi.urlshortener.accesstoken;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ranjabi.urlshortener.entities.AccessToken;

@Repository
public interface AccessTokenRepository extends CrudRepository<AccessToken, Integer> {
    Optional<AccessToken> findByToken(String token);
}
