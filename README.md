#  EventApplication Backend

Detta är backend-delen av **EventApplication**, en applikation för bokning av eventbiljetter med betalning via **Stripe**.

Projektet är byggt i **Spring Boot**, använder **MySQL** som databas och har stöd för **Stripe Webhooks** för betalningshantering.

---

## Funktionalitet

- Skapa, hämta och hantera event
- Reservera biljetter temporärt (1 minut)
- Automatisk rensning av utgångna reservationer
- Stripe Checkout-integration
- Hantering av betalningsbekräftelser via Webhook
- Realtidsuppdatering till frontend via WebSocket

---

## Teknologier

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA (Hibernate)**
- **MySQL**
- **Stripe API**
- **WebSocket (SimpMessagingTemplate)**
- **Maven**

---

## Installation och körning

### 1️Klona projektet

```bash
git clone https://github.com/S7120f/EventApplication.git
cd EventApplication

```
2️ Konfigurera miljövariabler
Skapa en fil med namnet .env (eller sätt miljövariabler i din IDE / server).
STRIPE_SECRET_KEY=sk_test_xxxxxxxxxxxxxxxxxxxxxxx
STRIPE_WEBHOOK_SECRET=whsec_xxxxxxxxxxxxxxxxxxxxxxx

SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/eventdb
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=yourpassword

SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true

Om du kör i DigitalOcean, sätt dessa i "Environment Variables" i din appinställning.

mvn spring-boot:run

http://localhost:8080


Endpoint	Method	Beskrivning
/api/events	GET	Hämtar alla event
/api/events/{id}	GET	Hämtar specifikt event
/api/reservations	POST	Skapar en biljettreservation
/api/stripe/create-checkout-session	POST	Skapar en Stripe checkout session
/api/stripe/webhook	POST	Stripe webhook endpoint


Databas (MySQL) Setup 

När applikationen startas första gången kommer tabellerna att skapas automatiskt av Spring Boot JPA (tack vare spring.jpa.hibernate.ddl-auto=update).
Om du vill för-populera databasen med exempeldata för lokal utveckling, kör följande SQL:


INSERT INTO event (title, description, price, ticket_available)
VALUES
('Summer Festival', 'A huge outdoor music event with DJs and live bands', 499, 345),
('Tech Expo 2025', 'Experience the latest innovations in AI and robotics', 199, 201),
('Art & Wine Evening', 'An exclusive night of art exhibits and wine tasting', 349, 106),
('Comedy Night', 'Stand-up performances by Sweden’s top comedians', 299, 129),
('Winter Wonderland', 'Family-friendly holiday market with food and activities', 249, 308);

Du kan köra detta i MySQL Workbench, phpMyAdmin, eller med mysql CLI.
