<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.elis.homeservices.model.Utente"%>
<%@page import="org.elis.homeservices.model.enums.Ruolo"%>
<%
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
    boolean isPro = utenteLoggato != null && utenteLoggato.getRuolo() != null
                    && utenteLoggato.getRuolo().name().equals("PRO");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>FAQ – HomeServices</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/shared.css">
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
    <style>
        body {
            background-color: #f4f6fb;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        main { flex: 1 0 auto; padding-top: 80px; padding-bottom: 60px; }

        .hero {
            background: linear-gradient(135deg, #313D5A 0%, #416FDD 100%);
            color: white;
            border-radius: 16px;
            padding: 3rem 2.5rem;
            margin-bottom: 2.5rem;
            box-shadow: 0 6px 30px rgba(49,61,90,0.28);
            position: relative;
            overflow: hidden;
        }
        .hero::before {
            content: "";
            position: absolute;
            top: -50px; right: -50px;
            width: 240px; height: 240px;
            border-radius: 50%;
            background: rgba(255,255,255,0.06);
        }
        .hero h1 { font-weight: 800; font-size: 2.2rem; margin-bottom: 0.6rem; }
        .hero p  { font-size: 1.05rem; opacity: 0.88; max-width: 580px; margin: 0; }

        .cat-pill {
            display: inline-flex; align-items: center; gap: 0.4rem;
            background: white;
            border: 2px solid #dee2e6;
            border-radius: 50px;
            padding: 0.45rem 1.1rem;
            font-size: 0.88rem;
            font-weight: 600;
            color: #495057;
            cursor: pointer;
            transition: all 0.18s;
            text-decoration: none;
        }
        .cat-pill:hover, .cat-pill.active {
            background: #416FDD;
            border-color: #416FDD;
            color: white;
        }

        .faq-section-title {
            font-weight: 800;
            color: #313D5A;
            font-size: 1.15rem;
            margin-bottom: 1rem;
            margin-top: 2rem;
            display: flex; align-items: center; gap: 0.5rem;
        }
        .faq-section-title i {
            background: linear-gradient(135deg, #416FDD, #313D5A);
            color: white;
            border-radius: 8px;
            padding: 5px 7px;
            font-size: 1rem;
        }

        .accordion-item {
            border: none;
            border-radius: 12px !important;
            margin-bottom: 0.6rem;
            box-shadow: 0 2px 10px rgba(49,61,90,0.08);
            overflow: hidden;
        }
        .accordion-button {
            font-weight: 600;
            color: #313D5A;
            background: white;
            border-radius: 12px !important;
        }
        .accordion-button:not(.collapsed) {
            color: #416FDD;
            background: #f0f4ff;
            box-shadow: none;
        }
        .accordion-button::after {
            filter: invert(30%) sepia(90%) saturate(400%) hue-rotate(200deg);
        }
        .accordion-button:not(.collapsed)::after {
            filter: invert(30%) sepia(90%) saturate(700%) hue-rotate(200deg);
        }
        .accordion-body {
            color: #495057;
            font-size: 0.94rem;
            background: white;
            line-height: 1.65;
        }

        .contact-banner {
            background: linear-gradient(135deg, #313D5A 0%, #416FDD 100%);
            border-radius: 14px;
            color: white;
            padding: 2.5rem 2rem;
            text-align: center;
            box-shadow: 0 4px 20px rgba(49,61,90,0.22);
            margin-top: 3rem;
        }
        .contact-banner h3 { font-weight: 700; margin-bottom: 0.6rem; }
        .contact-banner p  { opacity: 0.88; margin-bottom: 1.25rem; }

        .faq-search {
            border-radius: 50px;
            border: 2px solid #dee2e6;
            padding: 0.65rem 1.25rem;
            font-size: 0.95rem;
            width: 100%;
            outline: none;
            transition: border-color 0.18s;
        }
        .faq-search:focus { border-color: #416FDD; }
        .search-wrap { position: relative; }
        .search-wrap i {
            position: absolute; top: 50%; right: 1.1rem;
            transform: translateY(-50%);
            color: #adb5bd;
        }

        #no-result { display: none; }
    </style>
</head>
<body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top sfondo">
        <div class="container-fluid">
            <a href="<%= request.getContextPath() %>/" class="navbar-brand">
                <img class="logo" src="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png" alt="HomeServices Logo">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navMenu" aria-controls="navMenu" aria-expanded="false" aria-label="Menu">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navMenu">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/list-pro">Professionisti</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/chi-siamo">Chi Siamo</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="<%= request.getContextPath() %>/faq">FAQ</a>
                    </li>
                </ul>
                <div class="d-flex align-items-center gap-2 flex-wrap">
                    <% if (utenteLoggato != null) { %>
                        <% if (!isPro) { %>
                            <a href="<%= request.getContextPath() %>/user-to-pro" class="btn btn-outline-light btn-sm">Diventa Pro</a>
                        <% } else { %>
                            <a href="<%= request.getContextPath() %>/pro-home" class="btn btn-sm"
                               style="background:#416FDD; color:white; border:none;">Pro Home</a>
                        <% } %>
                        <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline-danger btn-sm">Logout</a>
                    <% } else { %>
                        <a href="<%= request.getContextPath() %>/login" class="btn btn-outline-light btn-sm">Accedi</a>
                        <a href="<%= request.getContextPath() %>/register" class="btn btn-primary btn-sm">Registrati</a>
                    <% } %>
                </div>
            </div>
        </div>
    </nav>

    <main class="container">

        <div class="hero">
            <h1><i class="bi bi-question-circle-fill me-2"></i>Domande frequenti</h1>
            <p>Hai dubbi su HomeServices? Qui trovi le risposte alle domande più comuni su account, professionisti, richieste e molto altro.</p>
        </div>

        <div class="search-wrap mb-4">
            <input type="text" class="faq-search" id="faqSearch" placeholder="Cerca una domanda…" oninput="filterFaq()">
            <i class="bi bi-search"></i>
        </div>

        <div class="d-flex flex-wrap gap-2 mb-2">
            <a class="cat-pill active" onclick="filterCat('all', this)"><i class="bi bi-grid"></i> Tutte</a>
            <a class="cat-pill" onclick="filterCat('account', this)"><i class="bi bi-person-circle"></i> Account</a>
            <a class="cat-pill" onclick="filterCat('professionisti', this)"><i class="bi bi-briefcase"></i> Professionisti</a>
            <a class="cat-pill" onclick="filterCat('richieste', this)"><i class="bi bi-send"></i> Richieste</a>
            <a class="cat-pill" onclick="filterCat('pagamenti', this)"><i class="bi bi-credit-card"></i> Pagamenti</a>
            <a class="cat-pill" onclick="filterCat('sicurezza', this)"><i class="bi bi-shield"></i> Sicurezza</a>
        </div>

        <div id="no-result" class="text-center py-5 text-muted">
            <i class="bi bi-search" style="font-size:2.5rem; opacity:0.3"></i>
            <p class="mt-2">Nessun risultato trovato. Prova con un'altra parola chiave.</p>
        </div>

        <!-- ACCOUNT  -->
        <div class="faq-section-title faq-cat" data-cat="account">
            <i class="bi bi-person-circle"></i> Account
        </div>
        <div class="accordion faq-cat" data-cat="account" id="acc-account">

            <div class="accordion-item faq-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#a1">
                        Come posso registrarmi su HomeServices?
                    </button>
                </h2>
                <div id="a1" class="accordion-collapse collapse" data-bs-parent="#acc-account">
                    <div class="accordion-body">
                        Clicca su <strong>Registrati</strong> in alto a destra, compila il modulo con nome, cognome, email e password
                        e scegli la tua città. La registrazione è gratuita e immediata.
                    </div>
                </div>
            </div>

        </div>

        <!-- PROFESSIONISTI -->
        <div class="faq-section-title faq-cat" data-cat="professionisti">
            <i class="bi bi-briefcase"></i> Professionisti
        </div>
        <div class="accordion faq-cat" data-cat="professionisti" id="acc-pro">

            <div class="accordion-item faq-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#p1">
                        Come posso diventare un professionista su HomeServices?
                    </button>
                </h2>
                <div id="p1" class="accordion-collapse collapse" data-bs-parent="#acc-pro">
                    <div class="accordion-body">
                        Accedi al tuo account e clicca su <strong>"Diventa Pro"</strong>. Compila il modulo con il tuo
                        codice fiscale, la professione che offri, la tariffa oraria e la città. La tua richiesta sarà
                        valutata dal nostro team entro 48 ore lavorative.
                    </div>
                </div>
            </div>

            <div class="accordion-item faq-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#p2">
                        Come vengono verificati i professionisti?
                    </button>
                </h2>
                <div id="p2" class="accordion-collapse collapse" data-bs-parent="#acc-pro">
                    <div class="accordion-body">
                        Il nostro team amministrativo esamina ogni richiesta manualmente. Verifichiamo la coerenza
                        dei dati inseriti e, se necessario, potremmo richiederti documentazione aggiuntiva. Solo i
                        professionisti approvati appaiono nella lista pubblica.
                    </div>
                </div>
            </div>

            <div class="accordion-item faq-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#p3">
                        Posso offrire più professioni?
                    </button>
                </h2>
                <div id="p3" class="accordion-collapse collapse" data-bs-parent="#acc-pro">
                    <div class="accordion-body">
                        Sì. Una volta approvato come professionista, puoi aggiungere ulteriori professioni al tuo
                        profilo dalla sezione <strong>Pro Home → Gestisci professioni</strong>.
                    </div>
                </div>
            </div>

            <div class="accordion-item faq-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#p4">
                        Come imposto le mie disponibilità?
                    </button>
                </h2>
                <div id="p4" class="accordion-collapse collapse" data-bs-parent="#acc-pro">
                    <div class="accordion-body">
                        Dalla tua <strong>Pro Home</strong> trovi la sezione <strong>Disponibilità</strong>. Puoi impostare
                        i giorni della settimana e le fasce orarie in cui sei disponibile. Gli utenti potranno inviarti
                        richieste solo negli slot che hai indicato.
                    </div>
                </div>
            </div>
        </div>

        <!-- RICHIESTE -->
        <div class="faq-section-title faq-cat" data-cat="richieste">
            <i class="bi bi-send"></i> Richieste
        </div>
        <div class="accordion faq-cat" data-cat="richieste" id="acc-richieste">

            <div class="accordion-item faq-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#r1">
                        Come invio una richiesta a un professionista?
                    </button>
                </h2>
                <div id="r1" class="accordion-collapse collapse" data-bs-parent="#acc-richieste">
                    <div class="accordion-body">
                        Vai alla pagina del professionista dalla lista, seleziona una data e un orario disponibile
                        e clicca <strong>"Invia richiesta"</strong>. Il professionista riceverà una notifica e potrà
                        accettare o rifiutare.
                    </div>
                </div>
            </div>

            <div class="accordion-item faq-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#r4">
                        Come lascio una recensione dopo il servizio?
                    </button>
                </h2>
                <div id="r4" class="accordion-collapse collapse" data-bs-parent="#acc-richieste">
                    <div class="accordion-body">
                        Una volta che la richiesta risulta <strong>Completata</strong>, puoi lasciare una recensione
                        dal pannello <strong>Le mie richieste</strong>. Valuta il professionista con un punteggio da 1 a 5
                        e aggiungi un commento facoltativo.
                    </div>
                </div>
            </div>
        </div>

        <!-- PAGAMENTI -->
        <div class="faq-section-title faq-cat" data-cat="pagamenti">
            <i class="bi bi-credit-card"></i> Pagamenti
        </div>
        <div class="accordion faq-cat" data-cat="pagamenti" id="acc-pag">

            <div class="accordion-item faq-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#pa1">
                        HomeServices è gratuito per gli utenti?
                    </button>
                </h2>
                <div id="pa1" class="accordion-collapse collapse" data-bs-parent="#acc-pag">
                    <div class="accordion-body">
                        Sì, registrarsi e cercare professionisti è completamente gratuito. Pagherai direttamente
                        il professionista per il servizio ricevuto, senza commissioni nascoste da parte nostra.
                    </div>
                </div>
            </div>

            <div class="accordion-item faq-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#pa2">
                        Come avviene il pagamento al professionista?
                    </button>
                </h2>
                <div id="pa2" class="accordion-collapse collapse" data-bs-parent="#acc-pag">
                    <div class="accordion-body">
                        Il pagamento avviene direttamente tra te e il professionista, secondo le modalità concordate
                        (contanti, bonifico, ecc.). HomeServices non gestisce transazioni economiche.
                    </div>
                </div>
            </div>
        </div>

        <!-- SICUREZZA -->
        <div class="faq-section-title faq-cat" data-cat="sicurezza">
            <i class="bi bi-shield"></i> Sicurezza e segnalazioni
        </div>
        <div class="accordion faq-cat" data-cat="sicurezza" id="acc-sic">

            <div class="accordion-item faq-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#s1">
                        Come posso segnalare un professionista scorretto?
                    </button>
                </h2>
                <div id="s1" class="accordion-collapse collapse" data-bs-parent="#acc-sic">
                    <div class="accordion-body">
                        Dal profilo del professionista trovi il pulsante <strong>"Segnala"</strong>. Indica il motivo
                        della segnalazione: il nostro team valuterà il caso entro 72 ore e potrà sospendere il profilo
                        se necessario.
                    </div>
                </div>
            </div>

            <div class="accordion-item faq-item">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#s2">
                        I miei dati personali sono al sicuro?
                    </button>
                </h2>
                <div id="s2" class="accordion-collapse collapse" data-bs-parent="#acc-sic">
                    <div class="accordion-body">
                        Sì. Le password sono conservate in forma criptata e non vengono mai condivise con terze parti.
                        Trattiamo i tuoi dati in conformità con il GDPR e la normativa italiana sulla privacy.
                    </div>
                </div>
            </div>
        </div>

        <!-- Banner contatto -->
        <div class="contact-banner">
            <h3><i class="bi bi-envelope-fill me-2"></i>Non hai trovato la risposta?</h3>
            <p>Il nostro team di supporto è sempre disponibile ad aiutarti.</p>
            <a href="mailto:supporto@homeservices.it" class="btn btn-light fw-semibold px-4">
                <i class="bi bi-envelope me-1"></i> Scrivici
            </a>
        </div>

    </main>

    <footer class="text-center py-4 mt-5" style="background:#313D5A; color:rgba(255,255,255,0.6); font-size:0.88rem;">
        &copy; 2024 HomeServices &mdash;
        <a href="<%= request.getContextPath() %>/chi-siamo" class="text-white-50 text-decoration-none">Chi Siamo</a> &middot;
        <a href="<%= request.getContextPath() %>/faq" class="text-white-50 text-decoration-none">FAQ</a>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function filterCat(cat, el) {
            document.querySelectorAll('.cat-pill').forEach(p => p.classList.remove('active'));
            el.classList.add('active');

            document.querySelectorAll('.faq-cat').forEach(section => {
                if (cat === 'all' || section.dataset.cat === cat) {
                    section.style.display = '';
                } else {
                    section.style.display = 'none';
                }
            });

            document.getElementById('faqSearch').value = '';
            document.getElementById('no-result').style.display = 'none';
            document.querySelectorAll('.faq-item').forEach(i => i.style.display = '');
        }

        function filterFaq() {
            const q = document.getElementById('faqSearch').value.toLowerCase().trim();
            let visibleCount = 0;

            document.querySelectorAll('.faq-cat').forEach(s => s.style.display = '');

            document.querySelectorAll('.faq-item').forEach(item => {
                const text = item.textContent.toLowerCase();
                if (!q || text.includes(q)) {
                    item.style.display = '';
                    visibleCount++;
                } else {
                    item.style.display = 'none';
                }
            });

            document.getElementById('no-result').style.display = (q && visibleCount === 0) ? 'block' : 'none';
        }
    </script>
</body>
</html>
