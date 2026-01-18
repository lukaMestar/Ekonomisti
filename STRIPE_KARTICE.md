# Stripe

forma za unos kartice, koristite ove test kartice za testiranje plaćanja.

## Uspješne Test Kartice

- **CVV:** bilo koji 3-cifreni broj (npr. `123`)
- **Datum isteka:** bilo koji budući datum (npr. `12/34`)
- **ZIP:** bilo koji 5-cifreni broj (npr. `12345`)

### Visa

- **Broj kartice:** `4242 4242 4242 4242`

### Mastercard

- **Broj kartice:** `5555 5555 5555 4444`

### American Express

- **Broj kartice:** `3782 822463 10005`

## Test Kartice za Neuspješne Transakcije

### Odbijena kartica

- **Broj kartice:** `4000 0000 0000 0002`

### Nedovoljno sredstava

- **Broj kartice:** `4000 0000 0000 9995`

### 3D Secure potreban

- **Broj kartice:** `4000 0025 0000 3155`
- **Razlog:** Potrebna dodatna autentifikacija

## Važne Napomene

1. **Test Mode:** Ove kartice rade samo u Stripe Test Mode-u
2. **Nema stvarnog naplaćivanja:** Test kartice ne naplaćuju stvarno
3. **CVV i Datum:** Mogu biti bilo koji budući datum i bilo koji CVV
4. **Ime:** Može biti bilo koje ime

## Proces sa stripeom

1. Računovođa postavlja cijenu → Sprema se u bazu
   ↓
2. Automatski generiranje (svaki dan 2:00 AM) → Kreira se račun sa statusom "neplaceno" sa provjerom da li je račun izdat taj mjesec i da li je proslo mjesec dana
   ↓
3. Klijent vidi račun → Frontend prikazuje listu računa
   ↓
4. Klijent klikne "Plati račun" → Frontend poziva backend za Payment Intent
   ↓
5. Backend kreira Stripe Payment Intent → Vraća clientSecret i publishableKey
   ↓
6. Frontend učitava Stripe Elements → Prikazuje formu za unos kartice
   ↓
7. Klijent unosi podatke kartice → Stripe procesira plaćanje
   ↓
8. Stripe vraća status "succeeded" → Frontend poziva backend za potvrdu
   ↓
9. Backend označava račun kao "placeno" → Ažurira financijsko stanje firme
   ↓
10. Klijent vidi uspješno plaćanje → Račun se prikazuje kao "placeno"
