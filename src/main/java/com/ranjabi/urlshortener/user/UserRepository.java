package com.ranjabi.urlshortener.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ranjabi.urlshortener.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
