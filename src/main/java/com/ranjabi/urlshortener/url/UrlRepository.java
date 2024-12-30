package com.ranjabi.urlshortener.url;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ranjabi.urlshortener.entities.Url;

@Repository
public interface UrlRepository extends CrudRepository<Url, Integer> {

}
