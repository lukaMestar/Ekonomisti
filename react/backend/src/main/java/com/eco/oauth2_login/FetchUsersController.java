package com.eco.oauth2_login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.UserRepository;

@RestController
@CrossOrigin(origins = {"${FRONTEND_URL:http://localhost:5173}", "http://localhost:5173"}, allowCredentials = "true")

@RequestMapping("/api")
public class FetchUsersController {
    private final UserRepository userRepository;

    @Autowired
    public FetchUsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    @GetMapping("/fetchusers")
    public List<Korisnik> getAllUsers() {
        return userRepository.findAll();  
    }
    
}