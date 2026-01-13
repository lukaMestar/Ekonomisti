package com.eco.oauth2_login.dto;

public class KlijentDTO {
    private Long id;
    private String ime;
    private String prezime;

    public KlijentDTO(Long id, String ime, String prezime) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
    }

    // getters & setters
    public Long getId() { return id; }
    public String getIme() { return ime; }
    public String getPrezime() { return prezime; }

    public void setId(Long id) { this.id = id; }
    public void setIme(String ime) { this.ime = ime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }
}