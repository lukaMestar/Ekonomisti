DROP TABLE IF EXISTS MjesecniRacun ;
DROP TABLE IF EXISTS RacunovodjaKlijent ;
DROP TABLE IF EXISTS PutniNalog ;
DROP TABLE IF EXISTS Faktura ;
DROP TABLE IF EXISTS jeZaposlen ;
DROP TABLE IF EXISTS Placa ;
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

CREATE TABLE Placa (
    idZaposlenik INT NOT NULL,
    idFirma INT NOT NULL,
    iznosPlace DECIMAL(10,2) NOT NULL CHECK (iznosPlace >= 0),

    PRIMARY KEY (idFirma, idZaposlenik)

);

CREATE TABLE Faktura (
    idFaktura SERIAL PRIMARY KEY,
    datum DATE NOT NULL,
    iznos DECIMAL(10,2) NOT NULL,
    opis TEXT,
    tipFakture VARCHAR(50) CHECK (tipFakture IN ('prihod', 'rashod')) NOT NULL,
    idFirma INT,
    idKlijent INT,
    odradena BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (idFirma, idKlijent)
        REFERENCES Firma(idFirma, idKlijent)
        ON DELETE CASCADE
);



CREATE TABLE PutniNalog (
    idPutniNalog SERIAL PRIMARY KEY,
    polaziste VARCHAR(100) NOT NULL,
    odrediste VARCHAR(100) NOT NULL,
    datumPolaska DATE,
    datumPovratka DATE,
    svrhaPutovanja TEXT,
    prijevoznoSredstvo VARCHAR(50),
    trosak DECIMAL(10,2),
    datumIzdavanja DATE,
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

CREATE TABLE MjesecniRacun (
    idRacun SERIAL PRIMARY KEY,
    idRacunovodja INT NOT NULL REFERENCES Racunovodja(idKorisnika) ON DELETE CASCADE,
    idKlijent INT NOT NULL REFERENCES Klijent(idKorisnika) ON DELETE CASCADE,
    idFirma INT NOT NULL,
    mjesec INT NOT NULL CHECK (mjesec >= 1 AND mjesec <= 12),
    godina INT NOT NULL,
    iznos DECIMAL(10,2) NOT NULL CHECK (iznos >= 0),
    datumGeneriranja DATE NOT NULL DEFAULT CURRENT_DATE,
    datumRoka DATE NOT NULL,
    statusPlacanja VARCHAR(20) DEFAULT 'neplaceno' CHECK (statusPlacanja IN ('neplaceno', 'placeno', 'otkazano', 'refundirano')),
    mockPaymentId VARCHAR(255),
    stripePaymentIntentId VARCHAR(255),
    stripeCustomerId VARCHAR(255),
    datumPlacanja DATE,
    FOREIGN KEY (idFirma, idKlijent) REFERENCES Firma(idFirma, idKlijent) ON DELETE CASCADE,
    UNIQUE(idRacunovodja, idKlijent, idFirma, mjesec, godina)
);
CREATE TABLE OdradjeneFakture (
    idFaktura INT NOT NULL,
    idFirma INT NOT NULL,
    idKlijent INT NOT NULL,
    datumOdradjivanja TIMESTAMP DEFAULT now(),
    PRIMARY KEY (idFaktura, idFirma, idKlijent),
    FOREIGN KEY (idFaktura) REFERENCES Faktura(idFaktura) ON DELETE CASCADE,
    FOREIGN KEY (idFirma, idKlijent) REFERENCES Firma(idFirma, idKlijent) ON DELETE CASCADE
);

CREATE TABLE OdradjeniPutniNalozi (
    idPutniNalog INT NOT NULL,
    idFirma INT NOT NULL,
    idKlijent INT NOT NULL,
    datumOdradjivanja TIMESTAMP DEFAULT now(),
    PRIMARY KEY (idPutniNalog, idFirma, idKlijent),
    FOREIGN KEY (idPutniNalog) REFERENCES PutniNalog(idPutniNalog) ON DELETE CASCADE,
    FOREIGN KEY (idFirma, idKlijent) REFERENCES Firma(idFirma, idKlijent) ON DELETE CASCADE
);


CREATE INDEX idx_mjesecni_racun_status ON MjesecniRacun(statusPlacanja);
CREATE INDEX idx_mjesecni_racun_datum_roka ON MjesecniRacun(datumRoka);

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

--Trigger za računovođe
CREATE OR REPLACE FUNCTION sync_racunovodja_role()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.idUloge = 2 THEN -- Računovođa
        INSERT INTO Racunovodja(idKorisnika)
        VALUES (NEW.idKorisnika)
        ON CONFLICT DO NOTHING;
    ELSE
        DELETE FROM Racunovodja
        WHERE idKorisnika = NEW.idKorisnika;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER sync_racunovodja_trigger
AFTER INSERT OR UPDATE ON Korisnici
FOR EACH ROW
EXECUTE FUNCTION sync_racunovodja_role();

CREATE OR REPLACE FUNCTION sync_to_zaposlenik()
RETURNS TRIGGER AS $$
BEGIN
    -- Ako je korisnik Klijent (3) ILI Radnik (4), mora biti u tablici Zaposlenik
    IF NEW.idUloge IN (3, 4) THEN
        INSERT INTO Zaposlenik(idKorisnika, placa)
        VALUES (NEW.idKorisnika, 0.00)
        ON CONFLICT (idKorisnika) DO NOTHING;
    ELSE
        -- Ako mu se uloga promijeni u nešto drugo, brišemo ga iz zaposlenika
        DELETE FROM Zaposlenik WHERE idKorisnika = NEW.idKorisnika;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_sync_zaposlenik
AFTER INSERT OR UPDATE ON Korisnici
FOR EACH ROW
EXECUTE FUNCTION sync_to_zaposlenik();

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
