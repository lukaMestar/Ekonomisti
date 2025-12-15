
INSERT INTO Korisnici (imeKorisnik, prezimeKorisnik, email, provider, providerUserId, idUloge, datumRegistracije)
VALUES ('Luka', 'Mestrovic', 'luka.mestarm@gmail.com', 'google', 'luka.mestarm@gmail.com', 1, CURRENT_DATE)
ON CONFLICT (email) 
DO UPDATE SET 
    imeKorisnik = EXCLUDED.imeKorisnik,
    prezimeKorisnik = EXCLUDED.prezimeKorisnik,
    provider = EXCLUDED.provider,
    providerUserId = EXCLUDED.providerUserId,
    idUloge = EXCLUDED.idUloge;

