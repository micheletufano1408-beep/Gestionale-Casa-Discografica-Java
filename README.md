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



erDiagram
    MANAGER {
        varchar id_dipendente PK
        varchar nome
        varchar cognome
        date data_assunzione
        numeric bonus_percentuale
    }
    ARTISTA {
        varchar id_artista PK
        varchar nome_arte
        varchar genere_musicale
        date data_inizio_contratto
        date data_fine_contratto
        varchar id_manager FK
    }
    RELEASE {
        varchar codice_catalogo PK
        varchar titolo
        varchar tipo_formato
        date data_pubblicazione
        varchar stato
        varchar id_artista FK
    }
    CAMPAGNA_MARKETING {
        varchar id_campagna PK
        varchar piattaforma
        numeric costo_stimato
        varchar id_dipartimento FK
        varchar codice_release FK
    }
    DIPARTIMENTO {
        varchar id_dipartimento PK
        varchar nome_dipartimento
        numeric budget_annuale
    }
    ROYALTY_REPORT {
        varchar id_report PK
        varchar periodo_riferimento
        numeric ricavi_totali
        varchar codice_release FK
    }
    TECNICO {
        varchar id_dipendente PK
        varchar nome
        varchar cognome
        date data_assunzione
        varchar ruolo_specializzato
        varchar codice_release FK
    }
    TECNICO_RELEASE {
        varchar id_tecnico PK, FK
        varchar codice_release PK, FK
    }
    UTENTE {
        varchar username PK
        varchar password
    }

    MANAGER ||--o{ ARTISTA : "gestisce"
    ARTISTA ||--o{ RELEASE : "pubblica"
    RELEASE ||--o{ CAMPAGNA_MARKETING : "promossa da"
    DIPARTIMENTO ||--o{ CAMPAGNA_MARKETING : "finanzia"
    RELEASE ||--o{ ROYALTY_REPORT : "genera"
    RELEASE ||--o{ TECNICO_RELEASE : "coinvolge"
    TECNICO ||--o{ TECNICO_RELEASE : "lavora a"
    RELEASE ||--o{ TECNICO : "ha tecnico principale"
