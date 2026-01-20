package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eco.oauth2_login.databaza.PutniNalog;

@RestController
@CrossOrigin(origins = { "${FRONTEND_URL:http://localhost:5173}", "https://ekonomisti-frontend.onrender.com",
        "http://localhost:5173" }, allowCredentials = "true")
@RequestMapping("/api")
public class AddNalogController {

    @Autowired
    private AddNalogService addNalogService;

    @PostMapping("/addnalog")
    public ResponseEntity<String> addKorisnik(@RequestBody PutniNalog nalog) {
        try {
            addNalogService.addNalog(nalog);
            return ResponseEntity.ok("Nalog added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}