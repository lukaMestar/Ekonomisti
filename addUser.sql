-- Add user luka.mestarm@gmail.com to the database
-- This script is idempotent - safe to run multiple times

-- Insert user with ADMIN role (iduloge = 1)
INSERT INTO korisnici (imekorisnik, prezimekorisnik, email, provider, provideruserid, iduloge, datumregistracije)
VALUES ('Luka', 'Mestrovic', 'luka.mestarm@gmail.com', 'google', 'luka.mestarm@gmail.com', 1, CURRENT_DATE)
ON CONFLICT (email) 
DO NOTHING;


