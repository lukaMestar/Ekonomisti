-- Dodaj si usera sa ADMIN ulogom (iduloge = 1)
INSERT INTO Korisnici (imeKorisnik, prezimeKorisnik, email, provider, providerUserId, idUloge, datumRegistracije)
VALUES ('Luka', 'Mestrovic', 'luka.mestarm@gmail.com', 'google', 'luka.mestarm@gmail.com', 1, CURRENT_DATE)
ON CONFLICT (email) 
DO NOTHING;

INSERT INTO Korisnici (imeKorisnik, prezimeKorisnik, email, provider, providerUserId, idUloge, datumRegistracije)
VALUES ('Luka', 'Mestrovic2', 'vecar222@gmail.com', 'google', 'vecar222@gmail.com', 1, CURRENT_DATE)
ON CONFLICT (email) 
DO NOTHING;

