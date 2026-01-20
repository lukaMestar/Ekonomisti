package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eco.oauth2_login.databaza.Faktura;
import com.eco.oauth2_login.databaza.FakturaRequest;

@RestController
@CrossOrigin(origins = { "${FRONTEND_URL:http://localhost:5173}", "https://ekonomisti-frontend.onrender.com",
        "http://localhost:5173" }, allowCredentials = "true")

@RequestMapping("/api")
public class AddFakturaController {

    @Autowired
    private AddFakturaService addFakturaService;

    @PostMapping("/addfaktura")
    public ResponseEntity<String> addKorisnik(@RequestBody FakturaRequest faktura) {
        try {
            addFakturaService.addFaktura(faktura);
            return ResponseEntity.ok("Faktura added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding faktura");
        }
    }
}
