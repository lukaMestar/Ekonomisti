package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eco.oauth2_login.databaza.PutniNalog;
import com.eco.oauth2_login.databaza.PutniNalogRequest;

@RestController
@CrossOrigin(origins = {"${FRONTEND_URL:http://localhost:5173}", "https://ekonomisti-frontend.onrender.com", "http://localhost:5173"}, allowCredentials = "true")

@RequestMapping("/api")
public class AddNalogController {
    
     @Autowired
    private AddNalogService addNalogService;

    @PostMapping("/addnalog")
    public ResponseEntity<String> addKorisnik(@RequestBody PutniNalogRequest nalog) {
        try {
            addNalogService.addNalog(nalog);
            return ResponseEntity.ok("Nalog added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding nalog");
        }
    }
}
