-- Add user luka.mestarm@gmail.com to the database
-- This script is idempotent - safe to run multiple times

-- Insert user with ADMIN role (idUloge = 1)
INSERT INTO Korisnici (imeKorisnik, prezimeKorisnik, email, provider, providerUserId, idUloge, datumRegistracije)
VALUES ('Luka', 'Mestrovic', 'luka.mestarm@gmail.com', 'google', 'luka.mestarm@gmail.com', 1, CURRENT_DATE)
ON CONFLICT (email) 
DO UPDATE SET 
    imeKorisnik = EXCLUDED.imeKorisnik,
    prezimeKorisnik = EXCLUDED.prezimeKorisnik,
    provider = EXCLUDED.provider,
    providerUserId = EXCLUDED.providerUserId,
    idUloge = EXCLUDED.idUloge;

-- Verify the user was added
SELECT idKorisnika, imeKorisnik, prezimeKorisnik, email, idUloge 
FROM Korisnici 
WHERE email = 'luka.mestarm@gmail.com';

