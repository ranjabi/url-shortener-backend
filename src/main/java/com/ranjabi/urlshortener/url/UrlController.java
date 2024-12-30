package com.ranjabi.urlshortener.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ranjabi.urlshortener.entity.Url;

@RestController
@RequestMapping(path="/urls")
public class UrlController {
    @Autowired
    private UrlRepository urlRepository;

    @GetMapping
    public Iterable<Url> getAllUrls() {
        return urlRepository.findAll();
    }

    @PostMapping
    public String addNewUrl(@RequestParam String path) {
        Url u = new Url(path);
        urlRepository.save(u);
        return "Saved";
    }
}