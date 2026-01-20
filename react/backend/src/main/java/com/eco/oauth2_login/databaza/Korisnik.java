package com.eco.oauth2_login.databaza;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "Korisnici")
public class Korisnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idKorisnika")
    private Long idKorisnika;

    @Column(name = "imeKorisnik")
    @JsonProperty("imeKorisnik")
    private String imeKorisnik;

    @Column(name = "prezimeKorisnik")
    @JsonProperty("prezimeKorisnik")
    private String prezimeKorisnik;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "provider", length = 255)
    private String provider;

    @Column(name = "providerUserId", nullable = false, length = 255)
    private String providerUserId;

    @Column(name = "datumRegistracije", nullable = false)
    private LocalDate datumRegistracije;

    @Column(name = "idUloge")
    private Integer idUloge; // Možeš ovo zamijeniti s @ManyToOne ako imaš entitet Uloga

    public Korisnik() {
        this.datumRegistracije = LocalDate.now(); // default vrijednost
    }

    public Korisnik(String imeKorisnik, String prezimeKorisnik, String email, String provider,
            String providerUserId, Integer idUloge) {
        this.imeKorisnik = imeKorisnik;
        this.prezimeKorisnik = prezimeKorisnik;
        this.email = email;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.datumRegistracije = LocalDate.now();
        this.idUloge = idUloge;
    }

    // Getteri i setteri
    public Long getIdKorisnika() {
        return idKorisnika;
    }

    public String getImeKorisnik() {
        return imeKorisnik;
    }

    public void setImeKorisnik(String imeKorisnik) {
        this.imeKorisnik = imeKorisnik;
    }

    public String getPrezimeKorisnik() {
        return prezimeKorisnik;
    }

    public void setPrezimeKorisnik(String prezimeKorisnik) {
        this.prezimeKorisnik = prezimeKorisnik;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public LocalDate getDatumRegistracije() {
        return datumRegistracije;
    }

    public void setDatumRegistracije(LocalDate datumRegistracije) {
        this.datumRegistracije = datumRegistracije;
    }

    public Integer getIdUloge() {
        return idUloge;
    }

    public void setIdUloge(Integer idUloge) {
        this.idUloge = idUloge;
    }
}
