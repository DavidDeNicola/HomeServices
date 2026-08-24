INSERT INTO citta(nome) VALUES ('Milano'), ('Torino'), ('Bologna'), ('Palermo'), ('Roma'), ('Napoli'), ('Firenze');
INSERT INTO professione(nome) VALUES ('Tuttofare'), ('Giardiniere'), ('Climatizzazione'), ('Imbianchino'), ('Muratore'), ('Idraulico'), ('Elettricista');
INSERT INTO veicolo(nome) VALUES ('Fiat Ducato'), ('Ford Transit'), ('Moto Honda'), ('Bici Cargo'), ('Alfa Romeo'), ('Pandino');


-- Clienti (Ruolo 0)
INSERT INTO utente(nome, cognome, email, password, dataNascita, cf, ruolo, tariffa, citta_id, rating) VALUES ('admin', 'admin', 'admin@gmail.com', 'Password1!', '2000-01-01', 'ADMADM00A01H501U', 2, NULL, 5, 0.0);

INSERT INTO utente(nome, cognome, email, password, dataNascita, cf, ruolo, tariffa, citta_id, rating) VALUES ('Dario', 'Verdi', 'dario.verdi@gmail.com', 'Password1!', '2004-12-12', 'VRDDRA04T12H501U', 0, NULL, 5, 0.0);

INSERT INTO utente(nome, cognome, email, password, dataNascita, cf, ruolo, tariffa, citta_id, rating) VALUES ('Marco', 'Neri', 'marco.neri@gmail.com', 'Pass123!', '1990-05-15', 'NRIMRC90E15H501U', 0, NULL, 4, 0.0);

INSERT INTO utente(nome, cognome, email, password, dataNascita, cf, ruolo, tariffa, citta_id, rating) VALUES ('Laura', 'Bianchi', 'laura.b@live.it', 'Pass123!', '1995-08-20', 'BNCLRA95M60L219S', 0, NULL, 5, 0.0);

INSERT INTO utente(nome, cognome, email, password, dataNascita, cf, ruolo, tariffa, citta_id, rating) VALUES ('Sofia', 'Gialli', 'sofia.g@gmail.com', 'Pass123!', '1988-02-10', 'GLLSFN88B50F205Z', 0, NULL, 6, 0.0);


-- Professionisti (Ruolo 1)
INSERT INTO utente(nome, cognome, email, password, dataNascita, cf, ruolo, tariffa, citta_id, rating) VALUES ('Ciro', 'Muro', 'ciro.muro@yahoo.com', 'Password1!', '1967-09-11', 'MRUCRI67P11F839X', 1, 100.00, 6, 0.0); 

INSERT INTO utente(nome, cognome, email, password, dataNascita, cf, ruolo, tariffa, citta_id, rating) VALUES ('Roberto', 'Ferrante', 'roberto.ferrante@outlook.com', 'Password1!', '1952-10-27', 'BNGRRT52R27C312O', 1, 200.00, 7, 0.0);

INSERT INTO utente(nome, cognome, email, password, dataNascita, cf, ruolo, tariffa, citta_id, rating) VALUES ('Andrea', 'Sarti', 'sarti.service@gmail.com', 'Pass123!', '1980-01-01', 'SRTNDR80A01L219X', 1, 45.00, 4, 0.0);

INSERT INTO utente(nome, cognome, email, password, dataNascita, cf, ruolo, tariffa, citta_id, rating) VALUES ('Elena', 'Verdi', 'elena.clima@outlook.it', 'Pass123!', '1985-03-12', 'VRDLNE85C52L219Q', 1, 60.00, 4, 0.0);

INSERT INTO utente(nome, cognome, email, password, dataNascita, cf, ruolo, tariffa, citta_id, rating) VALUES ('Luca', 'Moro', 'luca.moro@fastweb.it', 'Pass123!', '1975-11-30', 'MROLCI75S30H501T', 1, 35.00, 5, 0.0);


-- Ciro (ID 3): 
INSERT INTO utente_professione(id_utente, id_professione) VALUES (6, 5), (6, 7); 
INSERT INTO utente_veicolo(id_utente, id_veicolo) VALUES (6, 6); 

-- Roberto (ID 4)
INSERT INTO utente_professione(id_utente, id_professione) VALUES (7, 6), (7, 7);
INSERT INTO utente_veicolo(id_utente, id_veicolo) VALUES (7, 5);

-- Andrea (ID 8): Tuttofare e Imbianchino. Mezzo: Ducato (ID 3)
INSERT INTO utente_professione(id_utente, id_professione) VALUES (8, 4), (8, 7);
INSERT INTO utente_veicolo(id_utente, id_veicolo) VALUES (8, 3);

-- Elena (ID 9): Climatizzazione. Mezzo: Transit (ID 4)
INSERT INTO utente_professione(id_utente, id_professione) VALUES (9, 6);
INSERT INTO utente_veicolo(id_utente, id_veicolo) VALUES (9, 4);

-- Luca (ID 10): Giardiniere e Tuttofare. Mezzo: Moto (ID 5)
INSERT INTO utente_professione(id_utente, id_professione) VALUES (10, 5), (10, 4);
INSERT INTO utente_veicolo(id_utente, id_veicolo) VALUES (10, 5);


-- DISPONIBILITA
INSERT INTO disponibilita(data, da, a, utente_id) VALUES ('2026-05-06', '09:00:00', '13:00:00', 6);
INSERT INTO disponibilita(data, da, a, utente_id) VALUES ('2026-05-06', '15:00:00', '19:00:00', 6);
INSERT INTO disponibilita(data, da, a, utente_id) VALUES ('2026-02-09', '14:00:00', '15:00:00', 7);
INSERT INTO disponibilita(data, da, a, utente_id) VALUES ('2026-05-10', '08:00:00', '12:00:00', 8);
INSERT INTO disponibilita(data, da, a, utente_id) VALUES ('2026-05-10', '14:00:00', '18:00:00', 8);
INSERT INTO disponibilita(data, da, a, utente_id) VALUES ('2026-05-11', '09:00:00', '17:00:00', 9);
INSERT INTO disponibilita(data, da, a, utente_id) VALUES ('2026-05-12', '08:30:00', '13:00:00', 10);


-- Richiesta completata con recensione
INSERT INTO richiesta(descrizione, data, da, a, indirizzo, stato, utenteRichiede_id, utenteRiceve_id, professione_id) VALUES ('Pittura camera da letto', '2026-04-15', '09:00:00', '13:00:00', 'Via Milano 1', 2, 5, 8, 7);

INSERT INTO recensione(voto, descrizione, data, utenteScrive_id, utenteRiceve_id) VALUES (10, 'Lavoro perfetto e pulito!', '2026-04-16', 5, 8);






-- Altre richieste in vari stati
INSERT INTO richiesta(descrizione, data, da, a, indirizzo, stato, utenteRichiede_id, utenteRiceve_id, professione_id) VALUES ('Controllo Condizionatore', '2026-05-11', '10:00:00', '11:00:00', 'Corso Vittorio 10', 1, 6, 9, 6), ('Potatura Siepe', '2026-05-12', '09:00:00', '11:00:00', 'Via dei Giardini 5', 0, 7, 10, 5);


-- Ultime cose
INSERT INTO richiesta(descrizione, data, da, a, indirizzo, stato, utenteRichiede_id, utenteRiceve_id, professione_id) VALUES ('Riparazione muro', '2026-07-06', '16:00:00', '19:00:00', 'Via Luigi Rossi 10', 0, 2, 6, 1), ('Pezzotto TV', '2026-07-06', '10:00:00', '12:00:00', 'Via Luigi Rossi 10', 2, 2, 7, 3), ('Riparazione lavandino', '2026-02-09', '14:00:00', '15:00:00', 'Via Brombeis 104', 2, 6, 7, 2);

INSERT INTO recensione(voto, descrizione, data, utenteScrive_id, utenteRiceve_id, segnalato) VALUES (8, 'Ottimo lavoro', '2026-02-09', 6, 7, 0);

INSERT INTO segnalazione(motivazione, sanzionato, utenteSegnalante_id, utenteSegnalato_id) VALUES ('Comportamento non consono', 0, 6, 7), ('Falsa testimonianza', 0, 7, 6);

-- 1. Immagine Profilo ciro
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id) VALUES ('Profilo Ciro', 'img/profilo/profilo-idraulico.webp', 1, 'PROFILO', 6);

-- 2. Foto dei Lavori ciro
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id) VALUES ('Lavoro Idraulico 1', 'img/idraulico.jpg', 0, 'LAVORO', 6), ('Lavoro Idraulico 2', 'img/idraulico2.webp', 0, 'LAVORO', 6),('Lavoro Idraulico 3', 'img/idraulico3.jpg', 0, 'LAVORO', 6),('Lavoro Idraulico 4', 'img/idraulico4.jpg', 0, 'LAVORO', 6),('Lavoro Idraulico 5', 'img/idraulico5.jpg', 0, 'LAVORO', 6);

-- 3. Foto del Veicolo (Associa all'Utente 6 e al Veicolo 6 "Pandino") ciro
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id, id_veicolo) VALUES ('Il mio Pandino', 'img/auto/pandino.webp', 0, 'VEICOLO', 6, 6);


-- 1. Immagine Profilo roberto
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id) VALUES ('Profilo Roberto', 'img/profilo/profilo-elettricista.webp', 1, 'PROFILO', 7);

-- 2. Foto dei Lavori roberto
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id) VALUES ('Lavoro Eletricista 1', 'img/elettricista.avif', 0, 'LAVORO', 7), ('Lavoro Elettricista 2', 'img/elettricista2.jpg', 0, 'LAVORO', 7),('Lavoro Elettricista 3', 'img/elettricista3.jpg', 0, 'LAVORO', 7),('Lavoro Elettricista 4', 'img/elettricista4.jpg', 0, 'LAVORO', 7),('Lavoro Elettricista 5', 'img/elettricista4.webp', 0, 'LAVORO', 7);

-- 3. Foto del Veicolo (Associa all'Utente 6 e al Veicolo 6 "Pandino") roberto
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id, id_veicolo) VALUES ('La mia alfaromeo', 'img/auto/alfaromeo.jpeg', 0, 'VEICOLO', 7, 5);


-- ANDREA SARTI (id 8) — Imbianchino + Elettricista | Fiat Ducato
 
-- 1. Foto Profilo
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id) VALUES ('Profilo Andrea', 'img/profilo/profilo-imbianchino.jpg', 1, 'PROFILO', 8);
 
-- 2. Foto Lavori (imbianchino)
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id) VALUES ('Lavoro Imbianchino 1', 'img/imbianchino.jpg',    0, 'LAVORO', 8), ('Lavoro Imbianchino 2', 'img/imbianchino2.webp',  0, 'LAVORO', 8), ('Lavoro Imbianchino 3', 'img/imbianchino3.jpeg',  0, 'LAVORO', 8), ('Lavoro Imbianchino 4', 'img/imbianchino4.jpg',   0, 'LAVORO', 8), ('Lavoro Imbianchino 5', 'img/imbianchino5.jpeg',  0, 'LAVORO', 8);
 
-- 3. Foto Veicolo (Fiat Ducato — id_veicolo 3 → img/auto/ducato.jpeg)
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id, id_veicolo) VALUES ('Il mio Ducato', 'img/auto/ducato.jpeg', 0, 'VEICOLO', 8, 3);
 
 

-- ELENA VERDI (id 9) — Idraulico | Ford Transit
 
-- 1. Foto Profilo
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id) VALUES ('Profilo Elena', 'img/profilo/profilo-idraulico.webp', 1, 'PROFILO', 9);
 
-- 2. Foto Lavori (climatizzazione — unica professione: Climatizzazione)
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id) VALUES ('Lavoro Climatizzazione 1', 'img/climatizzazione.jpeg',  0, 'LAVORO', 9), ('Lavoro Climatizzazione 2', 'img/climatizzazione2.png',  0, 'LAVORO', 9), ('Lavoro Climatizzazione 3', 'img/climatizzazione3.jpg',  0, 'LAVORO', 9), ('Lavoro Climatizzazione 4', 'img/climatizzazione4.jpg',  0, 'LAVORO', 9);
 
-- 3. Foto Veicolo (Ford Transit — id_veicolo 4 → img/auto/fordtransit.jpeg)
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id, id_veicolo) VALUES ('Il mio Transit', 'img/auto/fordtransit.jpeg', 0, 'VEICOLO', 9, 4);
 
 

-- LUCA MORO (id 10) — Muratore + Tuttofare | Moto Honda
 
-- 1. Foto Profilo
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id) VALUES ('Profilo Luca', 'img/profilo/profilo-muratore.jpg', 1, 'PROFILO', 10);
 
-- 2. Foto Lavori (muratore + giardiniere — usiamo muratore come primario, giardiniere come extra)
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id) VALUES ('Lavoro Muratore 1',    'img/muratore.jpg',    0, 'LAVORO', 10), ('Lavoro Muratore 2',    'img/muratore2.jpg',   0, 'LAVORO', 10), ('Lavoro Muratore 3',    'img/muratore3.jpg',   0, 'LAVORO', 10), ('Lavoro Muratore 4',    'img/muratore4.jpg',   0, 'LAVORO', 10), ('Lavoro Giardiniere 1', 'img/giadiniere.jpg',  0, 'LAVORO', 10), ('Lavoro Giardiniere 2', 'img/giardiniere2.jpg',0, 'LAVORO', 10), ('Lavoro Giardiniere 3', 'img/giardiniere3.jpeg',0,'LAVORO', 10), ('Lavoro Giardiniere 4', 'img/giardiniere4.jpeg',0,'LAVORO', 10);
 
-- 3. Foto Veicolo (Moto Honda — id_veicolo 5 → img/auto/honda.jpeg)
INSERT INTO immagine (nome, percorso, isFotoProfilo, tipo, utente_id, id_veicolo) VALUES ('La mia Honda', 'img/auto/honda.jpeg', 0, 'VEICOLO', 10, 5);


-- Fix media-voto utenti con recensioni inserite tramite import.sql
UPDATE utente u JOIN (SELECT utenteRiceve_id, AVG(voto) AS media FROM recensione GROUP BY utenteRiceve_id) r ON u.id = r.utenteRiceve_id SET u.rating = r.media;


-- Colleghiamo la recensione alla richiesta

UPDATE richiesta SET id_recensione = 1 WHERE id = 1;

UPDATE richiesta SET id_recensione = 2 WHERE id = 6;