<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="org.elis.homeservices.model.Veicolo"%>
<%@ page import="org.elis.homeservices.model.Professione"%>
<%@ page import="org.elis.homeservices.model.Disponibilita"%>
<%@ page import="org.elis.homeservices.model.Immagine"%>
<%@ page import="org.elis.homeservices.model.Utente"%>
<%@ page import="org.elis.homeservices.model.enums.TipoImmagine"%>

<%
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

    // ---- Foto profilo attiva ----
    String fotoProfilo = null;
    if (utenteLoggato.getImmagini() != null) {
        for (Immagine img : utenteLoggato.getImmagini()) {
            if (Boolean.TRUE.equals(img.getIsFotoProfilo())) {
                fotoProfilo = img.getPercorso();
                break;
            }
        }
    }

    // ---- Mappa veicolo → prima foto caricata ----
    java.util.Map<Long, String> fotoVeicoloMap = new java.util.HashMap<>();
    if (utenteLoggato.getImmagini() != null) {
        for (Immagine img : utenteLoggato.getImmagini()) {
            if (img.getTipo() != null && img.getTipo() == TipoImmagine.VEICOLO
                    && img.getVeicolo() != null
                    && !fotoVeicoloMap.containsKey(img.getVeicolo().getId())) {
                fotoVeicoloMap.put(img.getVeicolo().getId(), img.getPercorso());
            }
        }
    }

    // ---- Fallback avatar con iniziali ----
    String avatarFallback = "https://ui-avatars.com/api/?name="
            + utenteLoggato.getNome() + "+" + utenteLoggato.getCognome()
            + "&background=313D5A&color=fff&size=128";
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>HomeServices – Pro Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/shared.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/index.css">
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
    <style>
        body {
            background-image: url("<%= request.getContextPath() %>/css/immagini/sfondoIndex.webp");
            background-size: cover;
            background-position: center top;
            background-attachment: fixed;
            background-repeat: no-repeat;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        main { flex: 1 0 auto; padding-top: 90px; padding-bottom: 40px; }

        /* ---- Welcome banner ---- */
        .welcome-banner {
            background: linear-gradient(135deg, #313D5A 0%, #416FDD 100%);
            color: white;
            border-radius: 14px;
            padding: 1.75rem 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 4px 20px rgba(49,61,90,0.35);
            display: flex;
            align-items: center;
            gap: 1.25rem;
        }
        .welcome-banner h2 { font-weight: 700; margin: 0; font-size: 1.6rem; }
        .welcome-banner p  { margin: 0.25rem 0 0; opacity: 0.85; font-size: 0.95rem; }

        /* Foto profilo nel banner */
        .banner-avatar {
            width: 72px;
            height: 72px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid rgba(255,255,255,0.5);
            flex-shrink: 0;
        }

        /* ---- Section title ---- */
        .section-title {
            font-weight: 700;
            color: #1e293b;
            font-size: 1.1rem;
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        .section-title::after {
            content: '';
            flex: 1;
            height: 2px;
            background: linear-gradient(90deg, #416FDD44, transparent);
            border-radius: 2px;
        }

        /* ---- Card comune ---- */
        .pro-card {
            background: rgba(237,242,244,0.92);
            backdrop-filter: blur(6px);
            border: none;
            border-radius: 14px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.10);
            padding: 1.5rem;
            margin-bottom: 1.5rem;
        }

        /* ---- Card attività ---- */
        .card-attivita {
            background: linear-gradient(135deg, rgba(49,61,90,0.9) 0%, rgba(65,111,221,0.85) 100%);
            color: white;
            border-radius: 14px;
            padding: 1.5rem 2rem;
            margin-bottom: 1.5rem;
            box-shadow: 0 4px 20px rgba(49,61,90,0.3);
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 1rem;
        }
        .card-attivita h5 { font-weight: 700; font-size: 1.1rem; margin: 0; }
        .card-attivita p  { margin: 0.25rem 0 0; opacity: 0.85; font-size: 0.88rem; }

        /* ---- Tariffa ---- */
        .tariffa-badge {
            display: inline-flex;
            align-items: center;
            gap: 0.4rem;
            background: linear-gradient(135deg, #313D5A, #416FDD);
            color: white;
            font-weight: 700;
            font-size: 1.1rem;
            border-radius: 10px;
            padding: 0.4rem 1rem;
            margin-bottom: 1rem;
        }

        /* ---- Form ---- */
        .form-control, .form-select {
            border-radius: 8px;
            border: 1.5px solid #d1d5db;
            background: white;
        }
        .form-control:focus, .form-select:focus {
            border-color: #416FDD;
            box-shadow: 0 0 0 3px rgba(65,111,221,0.15);
        }

        /* ---- Bottoni ---- */
        .btn-pro-primary {
            background: linear-gradient(135deg, #313D5A, #416FDD);
            color: white; border: none; border-radius: 8px;
            font-weight: 600; padding: 0.45rem 1.25rem;
            transition: opacity 0.2s, transform 0.15s;
        }
        .btn-pro-primary:hover { opacity: 0.9; transform: translateY(-1px); color: white; }

        .btn-pro-danger {
            background: #dc2626; color: white; border: none; border-radius: 8px;
            font-weight: 600; padding: 0.45rem 1.25rem;
            transition: opacity 0.2s, transform 0.15s;
        }
        .btn-pro-danger:hover { opacity: 0.85; transform: translateY(-1px); color: white; background: #b91c1c; }

        .btn-pro-success {
            background: #16a34a; color: white; border: none; border-radius: 8px;
            font-weight: 600; padding: 0.45rem 1.25rem;
            transition: opacity 0.2s, transform 0.15s;
        }
        .btn-pro-success:hover { opacity: 0.85; transform: translateY(-1px); color: white; background: #15803d; }

        /* ---- Card veicolo ---- */
        .veicolo-card {
            background: white;
            border-radius: 12px;
            border: 1px solid #e2e8f0;
            padding: 1rem;
            text-align: center;
            box-shadow: 0 2px 8px rgba(0,0,0,0.07);
            height: 100%;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .veicolo-card:hover { transform: translateY(-3px); box-shadow: 0 6px 18px rgba(0,0,0,0.12); }
        .veicolo-img {
            width: 100%; height: 130px;
            object-fit: cover;
            border-radius: 8px;
            margin-bottom: 0.75rem;
        }
        .veicolo-nome { font-weight: 700; color: #1e293b; font-size: 0.95rem; }

        /* ---- Professioni ---- */
        .professione-pill {
            display: inline-flex; align-items: center; gap: 0.4rem;
            background: #eff6ff; color: #1d4ed8;
            border: 1px solid #bfdbfe; border-radius: 20px;
            padding: 0.3rem 0.85rem; font-size: 0.85rem; font-weight: 600; margin: 0.2rem;
        }

        /* ---- Feedback ---- */
        .feedback-ok  { color: #16a34a; font-size: 0.85rem; margin-top: 0.5rem; }
        .feedback-err { color: #dc2626; font-size: 0.85rem; margin-top: 0.5rem; }

        /* ---- Weekly grid ---- */
        .weekly-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
            gap: 0.85rem;
        }
        .day-block {
            background: white;
            border: 1.5px solid #e2e8f0;
            border-radius: 10px;
            padding: 0.85rem;
            transition: border-color 0.2s, box-shadow 0.2s;
        }
        .day-block.active {
            border-color: #416FDD;
            box-shadow: 0 0 0 3px rgba(65,111,221,0.12);
        }
        .day-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 0.5rem;
        }
        .btn-add-slot {
            background: none;
            border: 1px solid #416FDD;
            border-radius: 6px;
            color: #416FDD;
            font-size: 0.75rem;
            font-weight: 600;
            padding: 0.2rem 0.55rem;
            cursor: pointer;
            transition: background 0.15s, color 0.15s;
            white-space: nowrap;
        }
        .btn-add-slot:hover:not(:disabled) { background: #416FDD; color: white; }
        .btn-add-slot:disabled { opacity: 0.35; cursor: default; border-color: #94a3b8; color: #94a3b8; }
        .slot-row {
            display: flex;
            align-items: center;
            gap: 0.35rem;
            margin-top: 0.45rem;
        }
        .slot-row input[type="time"] {
            flex: 1;
            font-size: 0.8rem;
            padding: 0.25rem 0.4rem;
            border: 1.5px solid #d1d5db;
            border-radius: 6px;
            background: white;
        }
        .slot-row input[type="time"]:focus {
            border-color: #416FDD;
            outline: none;
            box-shadow: 0 0 0 2px rgba(65,111,221,0.15);
        }
        .btn-remove-slot {
            background: none;
            border: none;
            color: #dc2626;
            font-size: 1rem;
            cursor: pointer;
            padding: 0 0.2rem;
            line-height: 1;
        }
        .slot-sep { font-size: 0.75rem; color: #64748b; flex-shrink: 0; }

        /* ---- Tabella disponibilità scorribile ---- */
        .disp-scroll {
            max-height: 320px;
            overflow-y: auto;
            border-radius: 10px;
            scrollbar-width: thin;
            scrollbar-color: #416FDD44 transparent;
        }
        .disp-scroll::-webkit-scrollbar { width: 6px; }
        .disp-scroll::-webkit-scrollbar-track { background: transparent; }
        .disp-scroll::-webkit-scrollbar-thumb { background: #416FDD66; border-radius: 3px; }
        .disp-scroll thead th { position: sticky; top: 0; z-index: 1; background: #f1f5f9; }
    </style>
</head>
<body>

<%
    @SuppressWarnings("unchecked")
    List<Veicolo> listaVeicoli         = (List<Veicolo>)    request.getAttribute("listaVeicoli");
    @SuppressWarnings("unchecked")
    List<Professione> listaProfessioni = (List<Professione>) request.getAttribute("listaProfessioni");
    @SuppressWarnings("unchecked")
    List<Veicolo> listaVeicoliUtente   = (List<Veicolo>)    request.getAttribute("listaVeicoliUtente");
    @SuppressWarnings("unchecked")
    List<Professione> listaProfessioniUtente = (List<Professione>) request.getAttribute("listaProfessioniUtente");
    String messaggio        = (String) request.getAttribute("messaggio");
    String messaggioVeicolo = (String) request.getAttribute("messaggioVeicolo");
    String messaggioPro     = (String) request.getAttribute("messaggioPro");
    String messaggioDisp    = (String) request.getAttribute("messaggioDisp");
    String scrollTo         = (String) request.getAttribute("scrollTo");
    @SuppressWarnings("unchecked")
    List<Disponibilita> listaDisponibilitaUtente = (List<Disponibilita>) request.getAttribute("listaDisponibilitaUtente");
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
                    data-bs-target="#navbarContent" aria-controls="navbarContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="pro-task-review">
                            <i class="bi bi-list-check me-1"></i>Le mie richieste
                        </a>
                    </li>
                </ul>
                <div class="d-flex align-items-center gap-2 flex-wrap">
                    <a href="user-home" class="btn btn-outline-light btn-sm">
                        <i class="bi bi-house me-1"></i>User Home
                    </a>
                    <a href="/HomeServices/logout" class="btn btn-outline-danger btn-sm">Logout</a>
                </div>
            </div>
        </div>
    </nav>

    <!-- MAIN -->
    <main class="container" style="max-width: 900px;">

        <!-- Welcome banner con foto profilo -->
        <div class="welcome-banner">
            <img src="<%= fotoProfilo != null ? request.getContextPath() + "/" + fotoProfilo : avatarFallback %>"
                 alt="Foto profilo"
                 class="banner-avatar">
            <div>
                <h2><i class="bi bi-briefcase-fill me-2"></i>Benvenuto, <%= utenteLoggato.getNome() %> <%= utenteLoggato.getCognome() %>!</h2>
                <p>Gestisci il tuo profilo professionale, i veicoli e le professioni.</p>
            </div>
        </div>

        <!-- CARD ATTIVITÀ -->
        <div class="card-attivita">
            <div>
                <h5><i class="bi bi-inbox-fill me-2"></i>Le tue attività</h5>
                <p>Visualizza le richieste ricevute e le recensioni dei clienti.</p>
            </div>
            <div class="d-flex gap-2 flex-wrap">
                <a href="pro-task-review" class="btn btn-pro-primary">
                    <i class="bi bi-arrow-right-circle me-1"></i>Vai alle richieste
                </a>
                <a href="upload-immagini" class="btn btn-pro-primary">
                    <i class="bi bi-images me-1"></i>Gestisci immagini
                </a>
            </div>
        </div>

        <!-- TARIFFA -->
        <div class="section-title">
            <i class="bi bi-currency-euro" style="color:#416FDD;"></i> Tariffa oraria
        </div>
        <div class="pro-card">
            <div class="tariffa-badge">
                <i class="bi bi-clock"></i>
                Tariffa attuale: <%= utenteLoggato.getTariffa() %> €/h
            </div>
            <form action="" method="post">
                <input type="hidden" name="action" value="modifyRate">
                <div class="row g-2 align-items-end">
                    <div class="col-md-6">
                        <label class="form-label small fw-semibold text-secondary">Nuova tariffa (€/h)</label>
                        <input type="number" step="0.50" min="0.50" max="9999.50"
                               name="nuovaTariffa" class="form-control"
                               placeholder="Es. 25.00" required>
                    </div>
                    <div class="col-md-3">
                        <button class="btn btn-pro-primary w-100" type="submit">
                            <i class="bi bi-check2 me-1"></i>Aggiorna
                        </button>
                    </div>
                </div>
                <% if (messaggio != null) {
                       boolean ok = messaggio.contains("successo"); %>
                    <div class="<%= ok ? "feedback-ok" : "feedback-err" %>">
                        <i class="bi bi-<%= ok ? "check-circle" : "exclamation-circle" %> me-1"></i><%= messaggio %>
                    </div>
                <% } %>
            </form>
        </div>

        <!-- VEICOLI -->
        <div class="section-title">
            <i class="bi bi-truck" style="color:#416FDD;"></i> I tuoi veicoli
        </div>
        <div class="pro-card">
            <div class="row g-3">

                <!-- Veicoli associati -->
                <% if (listaVeicoliUtente != null && !listaVeicoliUtente.isEmpty()) {
                       for (Veicolo v : listaVeicoliUtente) {
                           String fotoV = fotoVeicoloMap.get(v.getId());
                           String srcV  = (fotoV != null)
                               ? request.getContextPath() + "/" + fotoV
                               : "https://t4.ftcdn.net/jpg/05/97/47/95/360_F_597479556_7bbQ7t4Z8k3xbAloHFHVdZIizWK1PdOo.jpg";
                %>
                <div class="col-md-4 col-sm-6">
                    <div class="veicolo-card">
                        <img src="<%= srcV %>" class="veicolo-img" alt="<%= v.getNome() %>">
                        <div class="veicolo-nome mb-2"><%= v.getNome() %></div>
                        <form action="" method="post">
                            <input type="hidden" name="action" value="removeVeicolo">
                            <input type="hidden" name="idVeicolo" value="<%= v.getId() %>">
                            <button class="btn btn-pro-danger btn-sm">
                                <i class="bi bi-trash me-1"></i>Rimuovi
                            </button>
                        </form>
                    </div>
                </div>
                <% } } %>

                <!-- Card aggiungi veicolo -->
                <div class="col-md-4 col-sm-6">
                    <div class="veicolo-card d-flex flex-column justify-content-center">
                        <div class="text-center mb-3">
                            <i class="bi bi-plus-circle" style="font-size:2.5rem; color:#416FDD;"></i>
                            <div class="veicolo-nome mt-2">Aggiungi veicolo</div>
                        </div>
                        <form action="" method="post">
                            <input type="hidden" name="action" value="addVeicolo">
                            <select name="idAddVeicolo" class="form-select form-select-sm mb-2" required>
                                <% if (listaVeicoli != null) {
                                       for (Veicolo v : listaVeicoli) { %>
                                    <option value="<%= v.getId() %>"><%= v.getNome() %></option>
                                <% } } %>
                            </select>
                            <button class="btn btn-pro-success btn-sm w-100">
                                <i class="bi bi-plus me-1"></i>Aggiungi
                            </button>
                            <% if (messaggioVeicolo != null) { %>
                                <div class="feedback-err"><i class="bi bi-exclamation-circle me-1"></i><%= messaggioVeicolo %></div>
                            <% } %>
                        </form>
                    </div>
                </div>

            </div>
        </div>

        <!-- PROFESSIONI -->
        <div class="section-title">
            <i class="bi bi-person-badge" style="color:#416FDD;"></i> Professioni
        </div>
        <div class="pro-card">
            <div class="mb-3">
                <div class="small fw-semibold text-secondary mb-2">Le tue professioni:</div>
                <% if (listaProfessioniUtente != null && !listaProfessioniUtente.isEmpty()) {
                       for (Professione p : listaProfessioniUtente) { %>
                    <span class="professione-pill">
                        <i class="bi bi-check-circle-fill"></i><%= p.getNome() %>
                    </span>
                <% } } else { %>
                    <span class="text-muted small">Nessuna professione associata.</span>
                <% } %>
            </div>

            <div class="row g-3 mt-1">
                <div class="col-md-6">
                    <div class="small fw-semibold text-secondary mb-2">
                        <i class="bi bi-plus-circle me-1" style="color:#16a34a;"></i>Aggiungi professione
                    </div>
                    <form action="" method="post">
                        <input type="hidden" name="action" value="addProfessione">
                        <select name="idProfessione" class="form-select form-select-sm mb-2" required>
                            <% if (listaProfessioni != null) {
                                   for (Professione p : listaProfessioni) { %>
                                <option value="<%= p.getId() %>"><%= p.getNome() %></option>
                            <% } } %>
                        </select>
                        <button type="submit" class="btn btn-pro-success btn-sm">
                            <i class="bi bi-plus me-1"></i>Aggiungi
                        </button>
                        <% if (messaggioPro != null) {
                               boolean ok = messaggioPro.contains("successo"); %>
                            <div class="<%= ok ? "feedback-ok" : "feedback-err" %>">
                                <i class="bi bi-<%= ok ? "check-circle" : "exclamation-circle" %> me-1"></i><%= messaggioPro %>
                            </div>
                        <% } %>
                    </form>
                </div>

                <div class="col-md-6">
                    <div class="small fw-semibold text-secondary mb-2">
                        <i class="bi bi-dash-circle me-1" style="color:#dc2626;"></i>Rimuovi professione
                    </div>
                    <form action="" method="post">
                        <input type="hidden" name="action" value="removeProfessione">
                        <select name="idProfessioni" class="form-select form-select-sm mb-2" required>
                            <% if (listaProfessioniUtente != null) {
                                   for (Professione p : listaProfessioniUtente) { %>
                                <option value="<%= p.getId() %>"><%= p.getNome() %></option>
                            <% } } %>
                        </select>
                        <button type="submit" class="btn btn-pro-danger btn-sm">
                            <i class="bi bi-trash me-1"></i>Rimuovi
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <!-- DISPONIBILITÀ -->
        <div class="section-title" id="disponibilita">
            <i class="bi bi-calendar-week" style="color:#416FDD;"></i> Disponibilità
        </div>
        <div class="pro-card">
            <div class="mb-4">
                <div class="small fw-semibold text-secondary mb-2">Le tue disponibilità:</div>
                <% if (listaDisponibilitaUtente != null && !listaDisponibilitaUtente.isEmpty()) {
                       boolean scrollabile = listaDisponibilitaUtente.size() > 10; %>
                <div class="table-responsive<%= scrollabile ? " disp-scroll" : "" %>">
                    <table class="table table-sm align-middle mb-0" style="background:white; border-radius:10px; overflow:hidden;">
                        <thead style="background:#f1f5f9;">
                            <tr>
                                <th class="small text-secondary fw-semibold ps-3">Data</th>
                                <th class="small text-secondary fw-semibold">Dalle</th>
                                <th class="small text-secondary fw-semibold">Alle</th>
                                <th class="small text-secondary fw-semibold"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Disponibilita d : listaDisponibilitaUtente) { %>
                            <tr>
                                <td class="ps-3 fw-semibold"><i class="bi bi-calendar3 me-1 text-primary"></i><%= d.getData() %></td>
                                <td><i class="bi bi-clock me-1 text-muted"></i><%= d.getDa() %></td>
                                <td><%= d.getA() %></td>
                                <td class="text-end pe-3">
                                    <form action="" method="post" class="d-inline">
                                        <input type="hidden" name="action"          value="removeDisponibilita">
                                        <input type="hidden" name="idDisponibilita" value="<%= d.getId() %>">
                                        <button class="btn btn-pro-danger btn-sm py-0 px-2">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <span class="text-muted small">Nessuna disponibilità inserita.</span>
                <% } %>
            </div>

            <div class="small fw-semibold text-secondary mb-2">
                <i class="bi bi-plus-circle me-1" style="color:#16a34a;"></i>Aggiungi disponibilità
            </div>
            <form action="" method="post">
                <input type="hidden" name="action" value="addDisponibilita">
                <div class="row g-2 align-items-end">
                    <div class="col-md-4">
                        <label class="form-label small text-secondary fw-semibold">Data</label>
                        <input type="date" name="dataDisp" class="form-control form-control-sm" required>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small text-secondary fw-semibold">Dalle</label>
                        <input type="time" name="daDisp" class="form-control form-control-sm" min="08:00" max="20:00" required>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label small text-secondary fw-semibold">Alle</label>
                        <input type="time" name="aDisp" class="form-control form-control-sm" min="08:00" max="20:00" required>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-pro-success btn-sm w-100">
                            <i class="bi bi-plus me-1"></i>Aggiungi
                        </button>
                    </div>
                </div>
                <% if (messaggioDisp != null) {
                       boolean dispOk = messaggioDisp.contains("successo"); %>
                    <div class="<%= dispOk ? "feedback-ok" : "feedback-err" %> mt-2">
                        <i class="bi bi-<%= dispOk ? "check-circle" : "exclamation-circle" %> me-1"></i><%= messaggioDisp %>
                    </div>
                <% } %>
            </form>

            <!-- DISPONIBILITÀ SETTIMANALE IN BLOCCO -->
            <hr class="my-4" style="border-color:#d1d5db;">
            <div class="small fw-semibold text-secondary mb-3">
                <i class="bi bi-calendar4-week me-1" style="color:#416FDD;"></i>
                Aggiungi disponibilità settimanale in blocco
                <span class="text-muted fw-normal ms-1" style="font-size:0.8rem;">(verranno create le fasce selezionate per le prossime 4 settimane)</span>
            </div>
            <form action="" method="post" id="formSettimanale">
                <input type="hidden" name="action" value="addDisponibilitaSettimanale">

                <div class="weekly-grid mb-3">
                    <%
                        String[] nomiGiorni   = {"Lunedì","Martedì","Mercoledì","Giovedì","Venerdì","Sabato","Domenica"};
                        String[] valoriGiorni = {"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"};
                        String[] iconeGiorni  = {"bi-1-circle","bi-2-circle","bi-3-circle","bi-4-circle","bi-5-circle","bi-6-circle","bi-7-circle"};
                        for (int g = 0; g < 7; g++) {
                    %>
                    <div class="day-block" id="block-<%= valoriGiorni[g] %>">
                        <div class="day-header">
                            <div class="form-check mb-0">
                                <input class="form-check-input day-toggle" type="checkbox"
                                       id="chk-<%= valoriGiorni[g] %>"
                                       name="giorni"
                                       value="<%= valoriGiorni[g] %>"
                                       onchange="toggleDay('<%= valoriGiorni[g] %>')">
                                <label class="form-check-label fw-semibold" for="chk-<%= valoriGiorni[g] %>">
                                    <%= nomiGiorni[g] %>
                                </label>
                            </div>
                            <button type="button" class="btn-add-slot" onclick="addSlot('<%= valoriGiorni[g] %>')" disabled id="btnAdd-<%= valoriGiorni[g] %>">
                                <i class="bi bi-plus-circle"></i> Fascia
                            </button>
                        </div>
                        <div class="slots-container" id="slots-<%= valoriGiorni[g] %>">
                        </div>
                    </div>
                    <% } %>
                </div>

                <div class="d-flex align-items-center gap-3 flex-wrap">
                    <button type="submit" class="btn btn-pro-primary" id="btnSubmitSettimanale" disabled>
                        <i class="bi bi-calendar-check me-1"></i>Genera disponibilità per 4 settimane
                    </button>
                    <button type="button" class="btn btn-outline-secondary btn-sm" onclick="resetSettimanale()">
                        <i class="bi bi-arrow-counterclockwise me-1"></i>Reset
                    </button>
                </div>

                <%
                    String messaggioDispSett = (String) request.getAttribute("messaggioDispSett");
                    if (messaggioDispSett != null) {
                        boolean settOk = messaggioDispSett.contains("successo");
                %>
                <div class="<%= settOk ? "feedback-ok" : "feedback-err" %> mt-2">
                    <i class="bi bi-<%= settOk ? "check-circle" : "exclamation-circle" %> me-1"></i><%= messaggioDispSett %>
                </div>
                <% } %>
            </form>
        </div>

    </main>

    <!-- FOOTER -->
    <footer class="sfondo2">
        <div class="footer-container">
            <div class="footer-column">
                <div class="column-line">
                    <h4>HomeServices</h4>
                    <p class="small mb-0">Connettiamo professionisti qualificati a chi ha bisogno di aiuto.</p>
                </div>
            </div>
            <div class="footer-column">
                <h4>Scopri</h4>
                <ul><li><a href="/HomeServices/servizi">Tutti i servizi</a></li></ul>
            </div>
            <div class="footer-column">
                <h4>Contatti:</h4>
                <ul>
                    <li><a href="mailto:homeservices@gmail.com?subject=Richiesta informazioni">Email: homeservices@gmail.com</a></li>
                    <li><a href="tel:+393920626721">Telefono: 392 062 6721</a></li>
                </ul>
            </div>
        </div>
        <div class="footer-bottom"><p>© 2026 HomeServices</p></div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>

    let slotCounters = {};

    function toggleDay(giorno) {
        const chk    = document.getElementById('chk-' + giorno);
        const block  = document.getElementById('block-' + giorno);
        const btnAdd = document.getElementById('btnAdd-' + giorno);
        const slots  = document.getElementById('slots-' + giorno);

        if (chk.checked) {
            block.classList.add('active');
            btnAdd.disabled = false;
            if (!slotCounters[giorno] || slotCounters[giorno] === 0) {
                addSlot(giorno);
            }
        } else {
            block.classList.remove('active');
            btnAdd.disabled = true;
            slots.innerHTML = '';
            slotCounters[giorno] = 0;
        }
        updateSubmitButton();
    }

    function addSlot(giorno) {
        if (!slotCounters[giorno]) slotCounters[giorno] = 0;
        slotCounters[giorno]++;
        const idx = slotCounters[giorno];

        const container = document.getElementById('slots-' + giorno);
        const row = document.createElement('div');
        row.className = 'slot-row';
        row.id = 'slot-' + giorno + '-' + idx;
        row.innerHTML =
            '<input type="time" name="da_' + giorno + '_' + idx + '" min="08:00" max="20:00" value="08:00" required>' +
            '<span class="slot-sep">\u2192</span>' +
            '<input type="time" name="a_' + giorno + '_' + idx  + '" min="08:00" max="20:00" value="17:00" required>' +
            '<button type="button" class="btn-remove-slot" title="Rimuovi fascia" onclick="removeSlot(\'' + giorno + '\',' + idx + ')">' +
            '<i class="bi bi-x-circle-fill"></i>' +
            '</button>';
        container.appendChild(row);
        container.dataset.active = (parseInt(container.dataset.active || 0) + 1);
        updateSubmitButton();
    }

    function removeSlot(giorno, idx) {
        const row = document.getElementById('slot-' + giorno + '-' + idx);
        if (row) {
            const container = document.getElementById('slots-' + giorno);
            row.remove();
            container.dataset.active = Math.max(0, parseInt(container.dataset.active || 1) - 1);
            if (parseInt(container.dataset.active) === 0) {
                document.getElementById('chk-' + giorno).checked = false;
                toggleDay(giorno);
            }
        }
        updateSubmitButton();
    }

    function updateSubmitButton() {
        const anyChecked = document.querySelectorAll('.day-toggle:checked').length > 0;
        document.getElementById('btnSubmitSettimanale').disabled = !anyChecked;
    }

    function resetSettimanale() {
        ['MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY'].forEach(g => {
            const chk = document.getElementById('chk-' + g);
            if (chk && chk.checked) { chk.checked = false; toggleDay(g); }
        });
    }

    document.getElementById('formSettimanale').addEventListener('submit', function(e) {
        const giorni = ['MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY'];
        let errore = '';
        giorni.forEach(g => {
            const chk = document.getElementById('chk-' + g);
            if (!chk || !chk.checked) return;
            document.getElementById('slots-' + g).querySelectorAll('.slot-row').forEach(row => {
                const inputs = row.querySelectorAll('input[type="time"]');
                if (inputs[0].value >= inputs[1].value) {
                    errore = 'Per ogni fascia l\'orario di inizio deve essere precedente all\'orario di fine.';
                }
            });
        });
        if (errore) { e.preventDefault(); alert(errore); }
    });

    // AUTO-SCROLL
    (function() {
        var target = '<%= scrollTo != null ? scrollTo : "" %>';
        if (target) {
            var el = document.getElementById(target);
            if (el) {
                setTimeout(function() {
                    el.scrollIntoView({ behavior: 'smooth', block: 'start' });
                }, 80);
            }
        }
    })();
    </script>
</body>
</html>