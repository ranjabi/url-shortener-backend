package com.ranjabi.urlshortener.user;

import java.sql.SQLException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ranjabi.urlshortener.entities.User;
import com.ranjabi.urlshortener.exception.DuplicateUsernameException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void saveUser(String username, String password) {
        try {
            User newUser = new User(username, passwordEncoder.encode(password));
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") && ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23505")) {
                throw new DuplicateUsernameException("Username already exists");
            }

            throw new RuntimeException("DATABASE ERROR: " + e.getMessage(), e);
        }
    }
}
