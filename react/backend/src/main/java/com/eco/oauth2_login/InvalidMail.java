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

import org.springframework.transaction.annotation.Transactional;

@Service
public class InvalidMail extends OidcUserService {

    private final UserRepository userRepository;

    public InvalidMail(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OidcUser oidcUser = super.loadUser(userRequest);

            String email = oidcUser.getEmail();

            if (email == null) {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_email", "Email nije dostupan", null),
                        "Email atribut nije pronađen u Google user info objektu"
                );
            }

            Optional<Korisnik> user;
            try {
                user = userRepository.findByEmail(email);
            } catch (Exception dbException) {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("server_error", "Greška pristupa bazi podataka", null),
                        "Database error: " + dbException.getMessage()
                );
            }

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

            String currentIme = user1.getImeKorisnik();
            String currentPrezime = user1.getPrezimeKorisnik();
            
            if (currentIme == null || currentPrezime == null || 
                !currentIme.equals(ime) || !currentPrezime.equals(prezime)) {
                user1.setImeKorisnik(ime);
                user1.setPrezimeKorisnik(prezime);
                try {
                    userRepository.save(user1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return oidcUser;
        } catch (OAuth2AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("server_error", "Greška na serveru", null),
                    "Neočekivana greška pri učitavanju korisnika: " + e.getMessage()
            );
        }
    }
}
