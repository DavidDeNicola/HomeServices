🏠 HomeServices

HomeServices è una piattaforma web sviluppata in Java Web progettata per facilitare l'incontro tra utenti alla ricerca di professionisti qualificati per la casa (come idraulici, elettricisti, imbianchini e tecnici) e i prestatori di servizi stessi.

🛠️ Tecnologie Utilizzate
⚬	Linguaggio: Java (Java Web / Servlets, JSP)
⚬	Architettura: MVC (Model-View-Controller)
⚬	Database: MySQL
⚬	Server di sviluppo / Servlet Container: Apache Tomcat
⚬	Frontend: HTML5, CSS3, JavaScript, Bootstrap

📂 Struttura del Progetto
Il progetto segue la struttura classica delle applicazioni Java Web:
⚬	src/ - Contiene il codice sorgente Java (Servlet, classi di gestione 			database, bean).
⚬	webapp/ - Contiene le pagine JSP, i file CSS, le immagini e gli script 			JavaScript dell'interfaccia utente.

🚀 Come avviare il progetto in locale

Prerequisiti
Assicurati di avere installato sul tuo Mac:
⚬	Java JDK 
⚬	Apache Tomcat (compatibile con la versione del progetto)
⚬	Un IDE per Java EE / Web (es. Eclipse for Enterprise Java and Web Developers)
⚬	Un database SQL configurato (es. MySQL)

Passaggi per l'avvio
	1.	Apri il tuo IDE.
	2.	Importa il progetto come Existing Maven Projects.
	3.	Configura il server Apache Tomcat all'interno dell'IDE e collega il 		progetto al server.
	4.	Crea un database in mysql chiamato “home_services_db”. Il file 			import.sql presente all’interno del progetto si occuperà di popolare 		il database all’avvio dell’app.
	5.	Avvia il server Tomcat dall'IDE e apri il browser all'indirizzo:
		http://localhost:8080/HomeServices/