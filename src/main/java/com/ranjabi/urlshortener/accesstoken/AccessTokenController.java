package com.ranjabi.urlshortener.accesstoken;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessTokenController {
    
    @PostMapping("/token")
    public String token() {
        return "Not yet implemented";
    }

    @PostMapping("/action")
    public String action() {
        return "Requested action has been performed!";
    }
}
