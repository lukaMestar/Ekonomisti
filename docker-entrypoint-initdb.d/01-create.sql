DROP TABLE IF EXISTS RacunovodjaKlijent ;
DROP TABLE IF EXISTS PutniNalog ;
DROP TABLE IF EXISTS Faktura ;
DROP TABLE IF EXISTS jeZaposlen ;
DROP TABLE IF EXISTS Firma ;
DROP TABLE IF EXISTS Racunovodja ;
DROP TABLE IF EXISTS Zaposlenik ;
DROP TABLE IF EXISTS Klijent ;
DROP TABLE IF EXISTS Korisnici ;
DROP TABLE IF EXISTS Uloge ;
CREATE TABLE Uloge (
    idUloge SERIAL PRIMARY KEY,
    imeUloge VARCHAR(50) NOT NULL
);

CREATE TABLE Korisnici (
    idKorisnika SERIAL PRIMARY KEY,
    imeKorisnik VARCHAR(50) NOT NULL,
    prezimeKorisnik VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    provider VARCHAR(255),
    providerUserId VARCHAR(255) NOT NULL,
    datumRegistracije DATE NOT NULL DEFAULT CURRENT_DATE,
    idUloge INT REFERENCES Uloge(idUloge) ON DELETE SET NULL
);


CREATE TABLE Klijent (
    idKorisnika INT PRIMARY KEY REFERENCES Korisnici(idKorisnika) ON DELETE CASCADE
);

CREATE TABLE Zaposlenik (
    idKorisnika INT PRIMARY KEY REFERENCES Korisnici(idKorisnika) ON DELETE CASCADE,
    placa DECIMAL(10,2)   
);

CREATE TABLE Racunovodja (
    idKorisnika INT PRIMARY KEY REFERENCES Korisnici(idKorisnika) ON DELETE CASCADE    
);


CREATE TABLE Firma (
    idFirma SERIAL,
    idKlijent INT NOT NULL REFERENCES Klijent(idKorisnika) ON DELETE CASCADE,
    nazivFirme VARCHAR(100),
    stanjeRacuna DECIMAL(15,2) DEFAULT 0.0,
    emailIzvjestaj VARCHAR(100),
    PRIMARY KEY (idFirma, idKlijent)
);


CREATE TABLE jeZaposlen (
    idZaposlenik INT NOT NULL REFERENCES Zaposlenik(idKorisnika) ON DELETE CASCADE,
    idFirma INT NOT NULL,
    idKlijent INT NOT NULL,
    datumZaposljavanja DATE NOT NULL DEFAULT CURRENT_DATE,
    statusAktivnosti BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (idZaposlenik, idFirma, idKlijent),
    FOREIGN KEY (idFirma, idKlijent)
        REFERENCES Firma(idFirma, idKlijent)
        ON DELETE CASCADE
);


CREATE TABLE Faktura (
    idFaktura SERIAL PRIMARY KEY,
    datum DATE NOT NULL,
    iznos DECIMAL(10,2) NOT NULL,
    opis TEXT,
    tipFakture VARCHAR(50) CHECK (tipFakture IN ('prihod', 'rashod')) NOT NULL,
    idFirma INT,
    idKlijent INT,
    FOREIGN KEY (idFirma, idKlijent)
        REFERENCES Firma(idFirma, idKlijent)
        ON DELETE CASCADE
);



CREATE TABLE PutniNalog (
    idPutniNalog SERIAL PRIMARY KEY,
    relacija VARCHAR(100),
    datumIzdavanja DATE,
    trosak DECIMAL(10,2),
    idZaposlenik INT REFERENCES Zaposlenik(idKorisnika) ON DELETE CASCADE,
    idFirma INT,
    idKlijent INT,
    FOREIGN KEY (idFirma, idKlijent)
        REFERENCES Firma(idFirma, idKlijent)
        ON DELETE CASCADE
);


CREATE TABLE RacunovodjaKlijent (
    idRacunovodja INT REFERENCES Racunovodja(idKorisnika) ON DELETE CASCADE,
    idKlijent INT REFERENCES Klijent(idKorisnika) ON DELETE CASCADE,
    mjesecniTrosakUsluge DECIMAL(10,2) CHECK (mjesecniTrosakUsluge >= 0),
    PRIMARY KEY (idRacunovodja, idKlijent)
);

INSERT INTO Uloge (imeUloge) VALUES
('Admin'),
('Računovođa'),
('Klijent'),
('Radnik');

-- Triggeri da se automatski popuni "Klijent" tablica
CREATE OR REPLACE FUNCTION sync_klijent_role()
RETURNS TRIGGER AS $$
BEGIN
    -- If user is Klijent and not already in Klijent table
    IF NEW.idUloge IS NOT NULL AND NEW.idUloge = 3 THEN
        INSERT INTO Klijent(idKorisnika)
        VALUES (NEW.idKorisnika)
        ON CONFLICT DO NOTHING;
    ELSE
        -- If user is no longer Klijent, remove from Klijent table
        DELETE FROM Klijent
        WHERE idKorisnika = NEW.idKorisnika;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER sync_klijent_trigger
AFTER INSERT OR UPDATE ON Korisnici
FOR EACH ROW
EXECUTE FUNCTION sync_klijent_role();

-- Insert admin user
INSERT INTO Korisnici (imeKorisnik, prezimeKorisnik, email, provider, providerUserId, idUloge, datumRegistracije) VALUES
('Luka', 'Mestrovic', 'luka.mestarm@gmail.com', 'google', 'luka.mestarm@gmail.com', 1, CURRENT_DATE)
ON CONFLICT (email) 
DO UPDATE SET 
    imeKorisnik = EXCLUDED.imeKorisnik,
    prezimeKorisnik = EXCLUDED.prezimeKorisnik,
    provider = EXCLUDED.provider,
    providerUserId = EXCLUDED.providerUserId,
    idUloge = EXCLUDED.idUloge;
