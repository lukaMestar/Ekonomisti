package com.eco.oauth2_login;

import com.eco.oauth2_login.databaza.Korisnik;
import com.eco.oauth2_login.databaza.UserRepository;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

import jakarta.transaction.Transactional;

@Service
public class InvalidMail extends OidcUserService {

    private final UserRepository userRepository;

    public InvalidMail(UserRepository userRepository) {
        this.userRepository = userRepository;
        
    }

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        

        // dohvaćamo korisničke podatke preko super.loadUser
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail();
        
        

        if (email == null) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_email", "Email nije dostupan", null),
                    "Email atribut nije pronađen u Google user info objektu"
            );
        }

        // provjera u bazi
        Optional<Korisnik> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_token", "Email nije dozvoljen", null),
                    "Pristup odbijen: korisnik nije u bazi podataka"
            );
        }
        Korisnik user1 = user.get();
        String ime = oidcUser.getGivenName();
        String prezime = oidcUser.getFamilyName();
        if (ime == null) {
            ime = "-";
        }
        
        if (prezime == null) {

            prezime = "-";  
        }
        if(!user1.getImeKorisnik().equals(ime) || !user1.getPrezimeKorisnik().equals(prezime)) {
            user1.setImeKorisnik(ime);
            user1.setPrezimeKorisnik(prezime);
            userRepository.save(user1);
        }
        return oidcUser;
    }
}
