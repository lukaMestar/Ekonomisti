package com.eco.oauth2_login;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController

public class Oauth2LoginApplication {
	/*@GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }*/
	public static void main(String[] args) {
		SpringApplication.run(Oauth2LoginApplication.class, args);
	}

}
