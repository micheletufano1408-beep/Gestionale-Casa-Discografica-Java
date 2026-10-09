## Gestionale Casa Discografica

Applicazione gestionale sviluppata in **Java** e **PostgreSQL** per l'amministrazione centralizzata di una casa discografica. 

## Architettura Software
Il progetto è stato rigorosamente strutturato seguendo il pattern architetturale **BCE (Boundary-Control-Entity)** per separare la logica di presentazione, la logica di business e il modello dei dati. L'interazione con il database è gestita interamente tramite il pattern **DAO (Data Access Object)**, garantendo un codice modulare, manutenibile e disaccoppiato.

## Stack Tecnologico
* **Backend:** Java (Pattern BCE + DAO)
* **Database:** PostgreSQL
* **Librerie:** JDBC Driver

## Inizializzazione del Database
Nella cartella `/database` è presente il dump completo (`schema.sql`).
Per testare il progetto:
1. Crea un database vuoto su PostgreSQL.
2. Esegui lo script `schema.sql` per generare automaticamente le tabelle e popolare i dati fittizi di prova.
3. Configura le credenziali (username e password) nel file di connessione al database del progetto.
