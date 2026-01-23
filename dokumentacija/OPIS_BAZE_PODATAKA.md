# Opis baze podataka

## • Opis tablica

### **ULOGE**

> Sadrži sve moguće uloge korisnika u sustavu (Administrator, Računovođa, Klijent, Zaposlenik).

| ATRIBUT      | TIP PODATAKA | OPIS VARIJABLE                                                  |
| ------------ | ------------ | --------------------------------------------------------------- |
| idUloge (PK) | SERIAL       | jedinstveni identifikacijski ključ za ulogu                     |
| imeUloge     | VARCHAR(50)  | jedna od uloga: Admin, Računovođa, Klijent i Radnik            |

***

### **KORISNICI**

> Pohranjuje podatke o svim korisnicima sustava i povezuje ih s njihovom ulogom.

| ATRIBUT           | TIP PODATAKA | OPIS VARIJABLE                                     |
| ------------------ | ------------ | -------------------------------------------------- |
| idKorisnika (PK)   | SERIAL       | jedinstveni identifikator korisnika                |
| email              | VARCHAR(100) | jedinstvena e-mail adresa korisnika                |
| imeKorisnik        | VARCHAR(50)  | ime korisnika                                      |
| prezimeKorisnik    | VARCHAR(50)  | prezime korisnika                                  |
| provider           | VARCHAR(255) | naziv servisa koji je korisnik koristio za prijavu |
| providerUserId     | VARCHAR(255) | ID koji taj provider koristi za korisnika          |
| datumRegistracije  | DATE         | kada je korisnik kreiran prvi put (default: CURRENT_DATE) |
| idUloge (FK)       | INT          | jedinstveni identifikator uloge (referenca na Uloge) |
| oib                | VARCHAR(11)  | OIB korisnika (jedinstven, točno 11 znakova)       |

***

### **RACUNOVODJA**

> Specijalizirana tablica koja opisuje korisnike s ulogom računovođe.

| ATRIBUT              | TIP PODATAKA | OPIS VARIJABLE                      |
| -------------------- | ------------ | ----------------------------------- |
| idKorisnika (PK, FK) | INT          | jedinstveni identifikator korisnika |

***

### **KLIJENT**

> Specijalizirana tablica koja opisuje korisnike s ulogom klijenta.

| ATRIBUT              | TIP PODATAKA | OPIS VARIJABLE                      |
| -------------------- | ------------ | ----------------------------------- |
| idKorisnika (PK, FK) | INT          | jedinstveni identifikator korisnika |

***

### **ZAPOSLENIK**

> Specijalizirana tablica koja opisuje korisnike s ulogom zaposlenika i njihovu osnovnu plaću.

| ATRIBUT              | TIP PODATAKA  | OPIS VARIJABLE                      |
| -------------------- | ------------- | ----------------------------------- |
| idKorisnika (PK, FK) | INT           | jedinstveni identifikator korisnika |
| placa                | DECIMAL(10,2) | osnovna plaća zaposlenika           |

***

### **RacunovodjaKlijent**

> Veza koja prikazuje odnos između računovođe i njegovih klijenata te trošak usluge za klijenta.

| ATRIBUT                | TIP PODATAKA  | OPIS VARIJABLE                              |
| ---------------------- | ------------- | ------------------------------------------- |
| idRacunovodja (PK, FK) | INT           | jedinstveni identifikator računovođe        |
| idKlijent (PK, FK)     | INT           | jedinstveni identifikator klijenta          |
| mjesecniTrosakUsluge   | DECIMAL(10,2) | personalizirani trošak usluge računovodstva (>= 0) |

***

### **FIRMA**

> Sadrži podatke o firmama, njihovim vlasnicima (klijentima) i financijskim informacijama.

| ATRIBUT            | TIP PODATAKA  | OPIS VARIJABLE                                 |
| ------------------ | ------------- | ---------------------------------------------- |
| idFirma (PK)       | SERIAL        | jedinstveni identifikator firme                |
| idKlijent (PK, FK) | INT           | jedinstveni identifikator vlasnika firme       |
| nazivFirme         | VARCHAR(100)  | naziv firme                                    |
| stanjeRacuna       | DECIMAL(15,2) | stanje računa firme (default: 0.0)              |
| emailIzvjestaj     | VARCHAR(100)  | jedinstvena e-mail adresa za slanje izvještaja |

***

### **jeZaposlen**

> Veza koja opisuje zaposlenost između zaposlenika i firmi, uključujući datum zapošljavanja i status aktivnosti.

| ATRIBUT               | TIP PODATAKA | OPIS VARIJABLE                           |
| --------------------- | ------------ | ---------------------------------------- |
| idZaposlenik (PK, FK) | INT          | jedinstveni identifikator zaposlenika    |
| idFirma (PK, FK)      | INT          | jedinstveni identifikator firme          |
| idKlijent (PK, FK)    | INT          | jedinstveni identifikator vlasnika firme |
| datumZaposljavanja    | DATE         | datum zapošljavanja (default: CURRENT_DATE) |
| statusAktivnosti      | BOOLEAN      | je li trenutno zaposlen (default: TRUE)  |

***

### **PLACA**

> Pohranjuje plaće zaposlenika po firmama. Omogućava da isti zaposlenik ima različite plaće u različitim firmama.

| ATRIBUT           | TIP PODATAKA  | OPIS VARIJABLE                      |
| ----------------- | ------------- | ----------------------------------- |
| idZaposlenik (PK, FK) | INT       | jedinstveni identifikator zaposlenika |
| idFirma (PK, FK)  | INT           | jedinstveni identifikator firme      |
| iznosPlace        | DECIMAL(10,2) | plaća zaposlenika u toj firmi (>= 0) |

***

### **FAKTURA**

> Pohranjuje podatke o svim fakturama firmi, uključujući datum, iznos, tip i opis.

| ATRIBUT        | TIP PODATAKA  | OPIS VARIJABLE                                         |
| -------------- | ------------- | ------------------------------------------------------ |
| idFaktura (PK) | SERIAL        | jedinstveni identifikator fakture                      |
| datum          | DATE          | datum fakture                                          |
| iznos          | DECIMAL(10,2) | iznos fakture                                          |
| opis           | TEXT          | kratak opis fakture                                    |
| dobavljac      | VARCHAR(250)  | naziv dobavljača fakture                               |
| tipFakture     | VARCHAR(50)   | opisuje je li fakturu svrstavamo u prihode ili rashode ('prihod' ili 'rashod') |
| odradena       | BOOLEAN       | je li faktura obrađena (default: FALSE)                |
| idFirma (FK)   | INT           | jedinstveni identifikator firme                        |
| idKlijent (FK) | INT           | jedinstveni identifikator vlasnika firme               |

***

### **PUTNI NALOG**

> Pohranjuje podatke o službenim putovanjima zaposlenika, trošku i datumu putovanja.

| ATRIBUT           | TIP PODATAKA  | OPIS VARIJABLE                           |
| ----------------- | ------------- | ---------------------------------------- |
| idPutniNalog (PK) | SERIAL        | jedinstveni identifikator putnog naloga  |
| polaziste         | VARCHAR(100)  | polazište putovanja                      |
| odrediste         | VARCHAR(100)  | odredište putovanja                       |
| datumPolaska      | DATE          | datum odlaska na poslovni put            |
| datumPovratka     | DATE          | datum povratka s poslovnog puta           |
| svrhaPutovanja    | TEXT          | svrha poslovnog putovanja                |
| prijevoznoSredstvo| VARCHAR(50)   | sredstvo prijevoza                        |
| trosak            | DECIMAL(10,2) | trošak poslovnog puta                     |
| datumIzdavanja    | DATE          | datum izdavanja putnog naloga             |
| idZaposlenik (FK) | INT           | jedinstveni identifikator zaposlenika    |
| idFirma (FK)      | INT           | jedinstveni identifikator firme          |
| idKlijent (FK)    | INT           | jedinstveni identifikator vlasnika firme |

***

### **MjesecniRacun**

> Pohranjuje podatke o mjesečnim računima za usluge računovodstva koje računovođa naplaćuje klijentima.

| ATRIBUT                | TIP PODATAKA  | OPIS VARIJABLE                                                      |
| ---------------------- | ------------- | ------------------------------------------------------------------- |
| idRacun (PK)           | SERIAL        | jedinstveni identifikator računa                                    |
| idRacunovodja (FK)     | INT           | jedinstveni identifikator računovođe                                |
| idKlijent (FK)         | INT           | jedinstveni identifikator klijenta                                 |
| idFirma (FK)           | INT           | jedinstveni identifikator firme                                     |
| mjesec                 | INT           | mjesec za koji se račun izdaje (1-12)                               |
| godina                 | INT           | godina za koju se račun izdaje                                      |
| iznos                  | DECIMAL(10,2) | iznos računa (>= 0)                                                 |
| datumGeneriranja       | DATE          | datum generiranja računa (default: CURRENT_DATE)                    |
| datumRoka              | DATE          | datum roka plaćanja                                                  |
| statusPlacanja         | VARCHAR(20)   | status plaćanja ('neplaceno', 'placeno', 'otkazano', 'refundirano', default: 'neplaceno') |
| stripePaymentIntentId  | VARCHAR(255)  | Stripe Payment Intent ID                                            |
| stripeCustomerId       | VARCHAR(255)  | Stripe Customer ID                                                   |
| datumPlacanja          | DATE          | datum kada je račun plaćen                                          |

***

### **OdradjeneFakture**

> Veza koja prati koje su fakture obrađene (knjižene) i kada su obrađene.

| ATRIBUT            | TIP PODATAKA | OPIS VARIJABLE                           |
| ------------------ | ------------ | ---------------------------------------- |
| idFaktura (PK, FK) | INT          | jedinstveni identifikator fakture        |
| idFirma (PK, FK)   | INT          | jedinstveni identifikator firme          |
| idKlijent (PK, FK) | INT          | jedinstveni identifikator vlasnika firme |
| datumOdradjivanja  | TIMESTAMP    | datum i vrijeme kada je faktura obrađena (default: now()) |

***

### **OdradjeniPutniNalozi**

> Veza koja prati koji su putni nalozi obrađeni (knjiženi) i kada su obrađeni.

| ATRIBUT            | TIP PODATAKA | OPIS VARIJABLE                           |
| ------------------ | ------------ | ---------------------------------------- |
| idPutniNalog (PK, FK) | INT       | jedinstveni identifikator putnog naloga  |
| idFirma (PK, FK)   | INT          | jedinstveni identifikator firme          |
| idKlijent (PK, FK) | INT          | jedinstveni identifikator vlasnika firme |
| datumOdradjivanja  | TIMESTAMP    | datum i vrijeme kada je putni nalog obrađen (default: now()) |

***

## Relacije između tablica

- **Korisnici** → **Uloge** (many-to-one preko idUloge)
- **Korisnici** → **Klijent** (one-to-one, automatski trigger)
- **Korisnici** → **Racunovodja** (one-to-one, automatski trigger)
- **Korisnici** → **Zaposlenik** (one-to-one, automatski trigger)
- **Klijent** → **Firma** (one-to-many)
- **Racunovodja** ↔ **Klijent** (many-to-many preko RacunovodjaKlijent)
- **Zaposlenik** ↔ **Firma** (many-to-many preko jeZaposlen)
- **Zaposlenik** ↔ **Firma** (many-to-many preko Placa)
- **Firma** → **Faktura** (one-to-many)
- **Firma** → **PutniNalog** (one-to-many)
- **Zaposlenik** → **PutniNalog** (one-to-many)
- **Racunovodja** → **MjesecniRacun** (one-to-many)
- **Klijent** → **MjesecniRacun** (one-to-many)
- **Faktura** → **OdradjeneFakture** (one-to-one)
- **PutniNalog** → **OdradjeniPutniNalozi** (one-to-one)

## Automatski triggeri

1. **sync_klijent_trigger** - Automatski dodaje/uklanja korisnike iz tablice Klijent kada se idUloge postavi na 3
2. **sync_racunovodja_trigger** - Automatski dodaje/uklanja korisnike iz tablice Racunovodja kada se idUloge postavi na 2
3. **trigger_sync_zaposlenik** - Automatski dodaje/uklanja korisnike iz tablice Zaposlenik kada se idUloge postavi na 3 ili 4
