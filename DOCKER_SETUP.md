# Docker Setup - Ekonomisti Aplikacija

Ovaj dokument opisuje kako pokrenuti aplikaciju lokalno i na produkcijskom serveru koristeći Docker.

## 📋 Preduvjeti

- Docker i Docker Compose instalirani
- Za produkciju: pristup serveru (SSH)
- Google OAuth credentials (za produkciju)

## 🏠 Lokalno Pokretanje

### Opcija 1: Bez Docker-a (razvoj)

```bash
# Terminal 1: Backend
cd react/backend
mvn spring-boot:run

# Terminal 2: Frontend
cd react/frontend
npm install
npm run dev
```

Aplikacija će biti dostupna na:

- Frontend: http://localhost:5173
- Backend: http://localhost:9090

### Opcija 2: Docker Compose (backend + baza)

```bash
# Pokrenite backend i bazu u Docker-u
docker-compose -f docker-compose.local.yml up -d

# Frontend pokrenite lokalno
cd react/frontend
npm run dev
```

### Opcija 3: Cijeli stack u Docker-u

```bash
# Pokrenite sve servise
docker-compose -f docker-compose.local.yml up -d --build

# Pregled logova
docker-compose -f docker-compose.local.yml logs -f
```

## 🚀 Produkcijsko Pokretanje

### Korak 1: Priprema na serveru

```bash
# SSH na server
ssh user@your-server

# Klonirajte ili prenesite kod
cd /opt
git clone <your-repo> ekonomisti
cd ekonomisti
```

### Korak 2: Konfiguracija environment varijabli

```bash
# Kopirajte .env.example u .env
cp .env.example .env

# Uredite .env datoteku
nano .env
```

**Sadržaj .env:**

```env
DB_PASSWORD=your-secure-password-here
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
FRONTEND_URL=https://ekonomisti.primakon.com
SHOW_SQL=false
```

### Korak 3: Ažurirajte Google OAuth Redirect URI

U Google Cloud Console, dodajte:

```
https://ekonomisti.primakon.com/backend/login/oauth2/code/google
```

### Korak 4: Pokrenite Docker Compose

```bash
# Build i pokreni sve servise
docker-compose up -d --build

# Provjerite status
docker-compose ps

# Pratite logove
docker-compose logs -f
```

### Korak 5: Provjera

Aplikacija je dostupna na:

- Frontend: `https://ekonomisti.primakon.com`
- Backend API: `https://ekonomisti.primakon.com/backend`

## 🔧 Upravljanje na Produkciji

### Korisne komande:

```bash
# Zaustavi sve servise
docker-compose down

# Zaustavi i obriši volumene (briše bazu!)
docker-compose down -v

# Restart servisa
docker-compose restart

# Restart samo backend-a
docker-compose restart backend

# Pregled logova
docker-compose logs -f              # Svi servisi
docker-compose logs -f backend      # Samo backend
docker-compose logs -f frontend     # Samo frontend
docker-compose logs -f nginx        # Samo nginx

# Update aplikacije (nakon git pull)
docker-compose up -d --build

# Provjera statusa
docker-compose ps
```

## 🌐 Konfiguracija pfSense HAProxy

Na pfSense-u konfigurirajte HAProxy da prosljeđuje promet:

1. **Backend Server:**

   - Name: `ekonomisti-backend`
   - IP: `IP-adresa-vašeg-servera`
   - Port: `4443`

2. **Frontend Rule:**
   - Domain: `ekonomisti.primakon.com`
   - Backend: `ekonomisti-backend`
   - SSL: Ako koristite SSL na HAProxy-u

## 🔍 Troubleshooting

### Lokalno:

```bash
# Backend ne može spojiti bazu
# Provjerite da li je PostgreSQL pokrenut i na portu 5433

# Frontend ne može pristupiti backend-u
# Provjerite da li backend radi na http://localhost:9090
# Provjerite CORS postavke u backend-u
```

### Produkcija:

```bash
# Provjerite logove
docker-compose logs backend
docker-compose logs nginx

# Provjerite da li su svi servisi pokrenuti
docker-compose ps

# Provjerite network konekcije
docker network inspect ekonomisti_ekonomisti-network

# Testirajte backend direktno (s servera)
curl http://localhost:9090/api/health
```

## 📁 Struktura Datoteka

```
ekonomisti/
├── docker-compose.yml              # Produkcijska konfiguracija
├── docker-compose.local.yml        # Lokalna konfiguracija
├── .env.example                     # Template za environment varijable
├── nginx/
│   ├── nginx.conf                  # Nginx konfiguracija
│   └── ssl/                        # SSL certifikati (opcionalno)
└── react/
    ├── backend/
    │   ├── Dockerfile
    │   └── .dockerignore
    └── frontend/
        ├── Dockerfile
        └── .dockerignore
```

## 🔐 SSL Certifikati (Opcionalno)

Ako koristite SSL certifikate:

1. Postavite certifikate u `nginx/ssl/`:

   - `cert.pem` - SSL certifikat
   - `key.pem` - Privatni ključ

2. Odkomentirajte SSL sekciju u `nginx/nginx.conf`

## 📝 Napomene

- **Port 4443**: Nginx sluša na portu 4443 (konfigurirano za HAProxy)
- **Frontend na rootu**: Frontend je dostupan na `/`
- **Backend na /backend**: Backend API je dostupan na `/backend`
- **Lokalno razvijanje**: Aplikacija automatski detektira `localhost` i koristi direktnu konekciju na backend
- **Produkcija**: Aplikacija koristi relativne putanje kroz nginx (VITE_API_URL=/backend)

## 🆘 Podrška

Za probleme ili pitanja, provjerite:

1. Docker logove: `docker-compose logs -f`
2. Nginx logove: `docker-compose logs nginx`
3. Backend logove: `docker-compose logs backend`
