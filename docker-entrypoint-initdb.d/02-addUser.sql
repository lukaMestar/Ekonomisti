-- Add user luka.mestarm@gmail.com to the database
-- This script is idempotent - safe to run multiple times

-- Insert user with ADMIN role (iduloge = 1)
INSERT INTO korisnici (imekorisnik, prezimekorisnik, email, provider, provideruserid, iduloge, datumregistracije)
VALUES ('Luka', 'Mestrovic', 'luka.mestarm@gmail.com', 'google', 'luka.mestarm@gmail.com', 1, CURRENT_DATE)
ON CONFLICT (email) 
DO NOTHING;
INSERT INTO Korisnici (imeKorisnik, prezimeKorisnik, email, provider, providerUserId, idUloge, datumRegistracije)
VALUES ('Vedran', 'Ivankovic', 'vecar222@gmail.com', 'google', 'vecar222@gmail.com', 1, CURRENT_DATE)
ON CONFLICT (email) 
DO NOTHING;
INSERT INTO Korisnici (imeKorisnik, prezimeKorisnik, email, provider, providerUserId, idUloge, datumRegistracije)
VALUES ('Ivan', 'Lovrinović', 'ivan.lovrinovic.zg@gmail.com', 'google', 'ivan.lovrinovic.zg@gmail.com', 2, CURRENT_DATE)
ON CONFLICT (email) 
DO NOTHING;
INSERT INTO Korisnici (imeKorisnik, prezimeKorisnik, email, provider, providerUserId, idUloge, datumRegistracije)
VALUES ('primjer', 'primjer', 'primjer', 'google', 'primjer', 3, CURRENT_DATE)
ON CONFLICT (email) 
DO NOTHING;
INSERT INTO Korisnici (imeKorisnik, prezimeKorisnik, email, provider, providerUserId, idUloge, datumRegistracije)
VALUES ('Luka', 'Salopek', 'luka.salopek47@gmail.com', 'google', 'luka.salopek47@gmail.com', 1, CURRENT_DATE)
ON CONFLICT (email) 
DO NOTHING;
INSERT INTO Korisnici (imeKorisnik, prezimeKorisnik, email, provider, providerUserId, idUloge, datumRegistracije)
VALUES ('Simun', 'Covic', 'simun0942@gmail.com', 'google', 'simun0942@gmail.com', 4, CURRENT_DATE)
ON CONFLICT (email) 
DO NOTHING;

