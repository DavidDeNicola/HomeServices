<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.elis.homeservices.model.Disponibilita"%>
<%@page import="org.elis.homeservices.model.Utente"%>
<%@page import="org.elis.homeservices.model.Immagine"%>
<%@page import="org.elis.homeservices.model.Professione"%>
<%@page import="org.elis.homeservices.model.enums.Ruolo"%>
<%@page import="org.elis.homeservices.model.enums.TipoImmagine"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>HomeServices – Richiesta Servizio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/shared.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/index.css">
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
    <style>
        body {
            background-image: url("<%= request.getContextPath() %>/css/immagini/sfondoIndex.webp");
            background-size: cover; background-position: center top;
            background-attachment: fixed; background-repeat: no-repeat;
            min-height: 100vh; display: flex; flex-direction: column;
        }
        main { flex: 1 0 auto; padding-top: 90px; padding-bottom: 40px; }

        /* Banner pro */
        .pro-banner {
            background: linear-gradient(135deg, #313D5A 0%, #416FDD 100%);
            color: white; border-radius: 14px;
            padding: 1.5rem 2rem; margin-bottom: 2rem;
            box-shadow: 0 4px 20px rgba(49,61,90,0.35);
        }
        .pro-banner .pro-nome { font-weight: 700; font-size: 1.5rem; margin: 0; }
        .pro-banner .pro-sub  { margin: 0.2rem 0 0; opacity: 0.85; font-size: 0.9rem; }
        .pro-avatar {
            width: 72px; height: 72px; object-fit: cover;
            border-radius: 50%; border: 3px solid rgba(255,255,255,0.5); flex-shrink: 0;
        }

        /* Badge info */
        .info-pill {
            background: rgba(255,255,255,0.15);
            border: 1px solid rgba(255,255,255,0.25);
            border-radius: 8px; padding: 0.4rem 0.85rem;
            font-size: 0.85rem; display: inline-flex; align-items: center; gap: 0.4rem;
        }

        /* Form card */
        .form-card {
            background: rgba(237,242,244,0.95);
            backdrop-filter: blur(6px); border: none;
            border-radius: 14px; box-shadow: 0 2px 12px rgba(0,0,0,0.10); padding: 2rem;
        }

        /* Section title */
        .section-title {
            font-weight: 700; color: #1e293b; font-size: 1.1rem;
            margin-bottom: 1.25rem; display: flex; align-items: center; gap: 0.5rem;
        }
        .section-title::after {
            content: ''; flex: 1; height: 2px;
            background: linear-gradient(90deg, #416FDD44, transparent); border-radius: 2px;
        }

        /* Form */
        .form-label { font-size: 0.78rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.05em; color: #64748b; margin-bottom: 0.4rem; }
        .form-control, .form-select { border: 1px solid #e2e8f0; border-radius: 8px; font-size: 0.92rem; background: white; }
        .form-control:focus, .form-select:focus { border-color: #416FDD; box-shadow: 0 0 0 3px rgba(65,111,221,0.12); }

        /* Submit */
        .btn-invia {
            background: linear-gradient(135deg, #313D5A 0%, #416FDD 100%);
            color: white; border: none; border-radius: 10px;
            font-weight: 700; font-size: 1rem; padding: 0.75rem;
            transition: opacity 0.2s, transform 0.15s;
        }
        .btn-invia:hover { opacity: 0.9; transform: translateY(-1px); color: white; }

        /* Carosello lavori */
        .lavori-card {
            background: rgba(237,242,244,0.95);
            border-radius: 14px; padding: 1.5rem;
            box-shadow: 0 2px 12px rgba(0,0,0,0.10); margin-bottom: 2rem;
        }
        .carousel-item img {
            height: 280px; object-fit: cover; border-radius: 10px; width: 100%;
        }
        .carousel-control-prev-icon,
        .carousel-control-next-icon {
            filter: invert(1) drop-shadow(0 0 3px rgba(0,0,0,0.5));
        }
        .carousel-indicators [data-bs-target] { background-color: #416FDD; }

        .alert-form { border-radius: 10px; font-size: 0.9rem; }
    </style>
</head>
<body>

    <%
        Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
        boolean isPro = utenteLoggato != null && utenteLoggato.getRuolo() != null
                        && utenteLoggato.getRuolo().name().equals("PRO");

        Utente proScelto = (Utente) request.getAttribute("proScelto");
        @SuppressWarnings("unchecked")
        List<Disponibilita> dispPro = (List<Disponibilita>) request.getAttribute("dispPro");
        @SuppressWarnings("unchecked")
        List<Professione> profPro = (List<Professione>) request.getAttribute("profPro");
        String errore = (String) request.getAttribute("errore");

        // Prepara immagini del professionista (se proScelto non è null)
        String proFotoProfilo = null;
        List<Immagine> fotoLavori = new ArrayList<>();

        if (proScelto != null && proScelto.getImmagini() != null) {
            for (Immagine img : proScelto.getImmagini()) {
                if (Boolean.TRUE.equals(img.getIsFotoProfilo()) && proFotoProfilo == null) {
                    proFotoProfilo = img.getPercorso();
                }
                if (img.getTipo() != null && img.getTipo() == TipoImmagine.LAVORO) {
                    fotoLavori.add(img);
                }
            }
        }

        String proAvatarSrc = (proFotoProfilo != null)
            ? request.getContextPath() + "/" + proFotoProfilo
            : (proScelto != null
                ? "https://ui-avatars.com/api/?name=" + proScelto.getNome() + "+" + proScelto.getCognome() + "&background=416FDD&color=fff&size=128"
                : "");
    %>

    <!-- NAVBAR -->
    <nav class="navbar navbar-expand-lg sfondo navbar-dark bg-dark fixed-top">
        <div class="container-fluid">
            <a href="/HomeServices" class="navbar-brand">
                <img class="logo" src="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png" alt="HomeServices Logo">
            </a>
            <a href="/HomeServices" class="navbar-brand">
                <img class="logo1" src="<%= request.getContextPath() %>/css/immagini/loghetto-finale2.png" alt="HomeServices Logo">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item"><a class="nav-link" href="user-home">Dashboard</a></li>
                    <li class="nav-item"><a class="nav-link" href="list-pro">Professionisti</a></li>
                </ul>
                <div class="d-flex align-items-center gap-2 flex-wrap">
                    <% if (!isPro) { %>
                    <a href="user-to-pro" class="btn btn-outline-light btn-sm">Diventa Pro</a>
                    <% } else { %>
                    <a href="pro-home" class="btn btn-sm" style="background:#416FDD; color:white; border:none;">Pro Home</a>
                    <% } %>
                    <a href="/HomeServices/logout" class="btn btn-outline-danger btn-sm">Logout</a>
                </div>
            </div>
        </div>
    </nav>

    <!-- MAIN -->
    <main class="container" style="max-width: 720px;">

        <% if (proScelto == null) { %>
            <div class="alert alert-warning alert-form">
                <i class="bi bi-exclamation-triangle me-2"></i>Professionista non trovato.
                <a href="list-pro" class="alert-link ms-1">Torna alla lista</a>.
            </div>
        <% } else { %>

        <!-- Banner professionista con foto -->
        <div class="pro-banner d-flex align-items-center gap-3">
            <img src="<%= proAvatarSrc %>" alt="Foto professionista" class="pro-avatar">
            <div class="flex-grow-1">
                <h2 class="pro-nome"><%= proScelto.getNome() %> <%= proScelto.getCognome() %></h2>
                <p class="pro-sub">
                    <i class="bi bi-geo-alt-fill me-1"></i>
                    <%= (proScelto.getCitta() != null) ? proScelto.getCitta().getNome() : "Città non specificata" %>
                </p>
                <div class="d-flex flex-wrap gap-2 mt-2">
                    <span class="info-pill">
                        <i class="bi bi-cash-stack"></i>
                        <strong><%= proScelto.getTariffa() %>€/h</strong>
                    </span>
                    <span class="info-pill">
                        <i class="bi bi-star-fill"></i>
                        <%= proScelto.getRating() %>/10
                        <span style="opacity:0.75; font-size:0.8rem;">
                            (<%= request.getAttribute("nRecensioni") != null ? request.getAttribute("nRecensioni") : 0 %> rec.)
                        </span>
                    </span>
                </div>
            </div>
        </div>

        <!-- CAROSELLO FOTO LAVORI -->
        <% if (!fotoLavori.isEmpty()) { %>
        <div class="lavori-card">
            <div class="section-title">
                <i class="bi bi-images" style="color:#416FDD;"></i>
                I lavori di <%= proScelto.getNome() %>
            </div>

            <div id="carouselLavori" class="carousel slide" data-bs-ride="carousel">
            
                <% if (fotoLavori.size() > 1) { %>
                <div class="carousel-indicators">
                    <% for (int i = 0; i < fotoLavori.size(); i++) { %>
                    <button type="button" data-bs-target="#carouselLavori"
                            data-bs-slide-to="<%= i %>"
                            class="<%= i == 0 ? "active" : "" %>"
                            aria-label="Foto <%= i + 1 %>"></button>
                    <% } %>
                </div>
                <% } %>

                <div class="carousel-inner">
                    <% for (int i = 0; i < fotoLavori.size(); i++) {
                           Immagine img = fotoLavori.get(i); %>
                    <div class="carousel-item <%= i == 0 ? "active" : "" %>">
                        <img src="<%= request.getContextPath() %>/<%= img.getPercorso() %>"
                             alt="Foto lavoro <%= i + 1 %>">
                    </div>
                    <% } %>
                </div>

                <!-- Controlli frecce -->
                <% if (fotoLavori.size() > 1) { %>
                <button class="carousel-control-prev" type="button" data-bs-target="#carouselLavori" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon"></span>
                    <span class="visually-hidden">Precedente</span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#carouselLavori" data-bs-slide="next">
                    <span class="carousel-control-next-icon"></span>
                    <span class="visually-hidden">Successiva</span>
                </button>
                <% } %>
            </div>

            <p class="text-muted text-center mt-2 mb-0" style="font-size:0.82rem;">
                <i class="bi bi-images me-1"></i><%= fotoLavori.size() %> foto <%= fotoLavori.size() == 1 ? "caricata" : "caricate" %>
            </p>
        </div>
        <% } %>

        <!-- Alert errore -->
        <% if (errore != null) { %>
        <div class="alert alert-danger alert-form mb-3">
            <i class="bi bi-x-circle me-2"></i><%= errore %>
        </div>
        <% } %>

        <!-- Form richiesta -->
        <div class="form-card">
            <div class="section-title">
                <i class="bi bi-send" style="color:#416FDD;"></i>
                Invia una richiesta
            </div>

            <form action="request-pro" method="POST" class="needs-validation" novalidate>
                <input type="hidden" name="idProfessionista" value="<%= proScelto.getId() %>">
                <input type="hidden" name="idpro"            value="<%= proScelto.getId() %>">

                <!-- Descrizione -->
                <div class="mb-4">
                    <label class="form-label">Descrizione del problema</label>
                    <textarea name="descrizione" class="form-control" rows="4"
                              placeholder="Descrivi il problema o il lavoro da svolgere…" required></textarea>
                    <div class="invalid-feedback">Inserisci una descrizione.</div>
                </div>

                <!-- Indirizzo -->
                <div class="mb-4">
                    <label class="form-label">Indirizzo dell'intervento</label>
                    <div class="input-group">
                        <span class="input-group-text bg-white border-end-0">
                            <i class="bi bi-geo-alt text-danger"></i>
                        </span>
                        <input type="text" name="indirizzo" class="form-control border-start-0"
                               placeholder="Via, numero civico, città" required>
                    </div>
                    <div class="invalid-feedback">Inserisci l'indirizzo dell'intervento.</div>
                </div>

                <!-- Professione -->
                <div class="mb-4">
                    <label class="form-label">Tipo di servizio richiesto</label>
                    <% if (profPro != null && profPro.size() > 1) { %>
                    <select name="idProfessione" class="form-select" required>
                        <option value="" disabled selected>Seleziona il servizio…</option>
                        <% for (Professione p : profPro) { %>
                        <option value="<%= p.getId() %>"><%= p.getNome() %></option>
                        <% } %>
                    </select>
                    <div class="invalid-feedback">Seleziona il tipo di servizio.</div>
                    <% } else if (profPro != null && profPro.size() == 1) { %>
                    <input type="hidden" name="idProfessione" value="<%= profPro.get(0).getId() %>">
                    <div class="info-pill d-inline-flex" style="background:#f0f7ff; border-color:#bfdbfe; color:#1d4ed8;">
                        <i class="bi bi-wrench-adjustable"></i>
                        <strong><%= profPro.get(0).getNome() %></strong>
                    </div>
                    <% } %>
                </div>

                <!-- Disponibilità -->
                <div class="mb-4">
                    <label class="form-label">Disponibilità del professionista</label>
                    <% if (dispPro == null || dispPro.isEmpty()) { %>
                    <div class="alert alert-warning alert-form py-2">
                        <i class="bi bi-calendar-x me-2"></i>
                        Nessuna disponibilità presente al momento. Riprova più tardi.
                    </div>
                    <% } else { %>
                    <select name="fasciaOraria" class="form-select" required>
                        <option value="" disabled selected>Seleziona una fascia oraria…</option>
                        <% for (Disponibilita d : dispPro) { %>
                        <option value="<%= d.getData() %>-<%= d.getDa() %>-<%= d.getA() %>">
                            <%= d.getData() %> &nbsp;·&nbsp; dalle <%= d.getDa() %> alle <%= d.getA() %>
                        </option>
                        <% } %>
                    </select>
                    <div class="invalid-feedback">Seleziona una fascia oraria.</div>
                    <% } %>
                </div>

                <% if (dispPro != null && !dispPro.isEmpty()) { %>
                <button type="submit" class="btn btn-invia w-100">
                    <i class="bi bi-send-fill me-2"></i>Invia richiesta
                </button>
                <% } %>
            </form>
        </div>

        <% } %>
    </main>

    <%@ include file="/WEB-INF/includes/footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        (() => {
            'use strict';
            document.querySelectorAll('.needs-validation').forEach(form => {
                form.addEventListener('submit', e => {
                    if (!form.checkValidity()) { e.preventDefault(); e.stopPropagation(); }
                    form.classList.add('was-validated');
                }, false);
            });
        })();
    </script>
</body>
</html>
