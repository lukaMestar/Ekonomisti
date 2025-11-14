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
        System.out.println("[InvalidMail] Constructor - START");
        this.userRepository = userRepository;
        System.out.println("[InvalidMail] Constructor - END");
    }

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("[InvalidMail] loadUser() - START");
        try {
            System.out.println("[InvalidMail] loadUser() - Loading user from OAuth2 provider");
            OidcUser oidcUser = super.loadUser(userRequest);

            String email = oidcUser.getEmail();
            System.out.println("[InvalidMail] loadUser() - Email from OAuth2: " + email);

            if (email == null) {
                System.out.println("[InvalidMail] loadUser() - ERROR: Email is null!");
                System.err.println("InvalidMail: Email is null!");
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_email", "Email nije dostupan", null),
                        "Email atribut nije pronađen u Google user info objektu"
                );
            }

            System.out.println("[InvalidMail] loadUser() - Checking database for user with email: " + email);
            Optional<Korisnik> user;
            try {
                user = userRepository.findByEmail(email);
            } catch (Exception dbException) {
                System.err.println("[InvalidMail] loadUser() - Database error: " + dbException.getMessage());
                dbException.printStackTrace();
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("server_error", "Greška pristupa bazi podataka", null),
                        "Database error: " + dbException.getMessage()
                );
            }

            if (user.isEmpty()) {
                System.out.println("[InvalidMail] loadUser() - ERROR: User not found in database");
                System.err.println("InvalidMail: User not found in database for email: " + email);
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("invalid_token", "Email nije dozvoljen", null),
                        "Pristup odbijen: korisnik nije u bazi podataka"
                );
            }
            
            System.out.println("[InvalidMail] loadUser() - User found in database");
            Korisnik user1 = user.get();
            String ime = oidcUser.getGivenName();
            String prezime = oidcUser.getFamilyName();
            if (ime == null) {
                ime = "-";
            }
            
            if (prezime == null) {
                prezime = "-";  
            }
            
            System.out.println("[InvalidMail] loadUser() - OAuth2 name: " + ime + " " + prezime);

            String currentIme = user1.getImeKorisnik();
            String currentPrezime = user1.getPrezimeKorisnik();
            System.out.println("[InvalidMail] loadUser() - Database name: " + currentIme + " " + currentPrezime);
            
            if (currentIme == null || currentPrezime == null || 
                !currentIme.equals(ime) || !currentPrezime.equals(prezime)) {
                System.out.println("[InvalidMail] loadUser() - Updating user name in database");
                user1.setImeKorisnik(ime);
                user1.setPrezimeKorisnik(prezime);
                try {
                    userRepository.save(user1);
                    System.out.println("[InvalidMail] loadUser() - User name updated successfully");
                } catch (Exception e) {
                    System.out.println("[InvalidMail] loadUser() - ERROR saving user: " + e.getMessage());
                    System.err.println("GREŠKA PRI SPREMANJU KORISNIKA: " + e.getMessage());
                    e.printStackTrace();
                    
                }
            } else {
                System.out.println("[InvalidMail] loadUser() - User name unchanged, no update needed");
            }

            System.out.println("[InvalidMail] loadUser() - END, returning OidcUser");
            return oidcUser;
        } catch (OAuth2AuthenticationException e) {
            System.out.println("[InvalidMail] loadUser() - OAuth2AuthenticationException: " + e.getMessage());
            System.err.println("InvalidMail: OAuth2AuthenticationException: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("[InvalidMail] loadUser() - Unexpected exception: " + e.getMessage());
            System.err.println("InvalidMail: Unexpected exception: " + e.getMessage());
            e.printStackTrace();
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("server_error", "Greška na serveru", null),
                    "Neočekivana greška pri učitavanju korisnika: " + e.getMessage()
            );
        }
    }
}
