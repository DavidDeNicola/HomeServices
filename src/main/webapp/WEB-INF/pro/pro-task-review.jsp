<%@page import="org.elis.homeservices.model.enums.Stato"%>
<%@page import="org.elis.homeservices.model.Recensione"%>
<%@page import="org.elis.homeservices.model.Richiesta"%>
<%@page import="org.elis.homeservices.model.Utente"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>HomeServices – Le mie richieste</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/shared.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/index.css">
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
    <style>
        /* ---- Layout ---- */
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
        }
        .welcome-banner h2 { font-weight: 700; margin: 0; font-size: 1.6rem; }
        .welcome-banner p  { margin: 0.25rem 0 0; opacity: 0.85; font-size: 0.95rem; }

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

        /* ---- Card richiesta ---- */
        .card-richiesta {
            background: rgba(237,242,244,0.92);
            backdrop-filter: blur(6px);
            border: none;
            border-radius: 14px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.10);
            padding: 1.5rem;
            margin-bottom: 1.25rem;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .card-richiesta:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 24px rgba(0,0,0,0.14);
        }

        /* ---- Meta labels ---- */
        .meta-label {
            font-size: 0.7rem;
            color: #64748b;
            text-transform: uppercase;
            letter-spacing: 0.06em;
            font-weight: 600;
        }
        .meta-value {
            font-size: 0.9rem;
            font-weight: 500;
            color: #1e293b;
        }

        /* ---- Badge stato ---- */
        .badge-stato {
            font-size: 0.72rem;
            padding: 0.35em 0.85em;
            border-radius: 999px;
            font-weight: 600;
        }

        /* ---- Professione pill ---- */
        .professione-pill {
            display: inline-flex;
            align-items: center;
            gap: 0.35rem;
            background: #eff6ff;
            color: #1d4ed8;
            border: 1px solid #bfdbfe;
            border-radius: 20px;
            padding: 0.2rem 0.75rem;
            font-size: 0.8rem;
            font-weight: 600;
        }

        /* ---- Empty section ---- */
        .empty-section {
            color: #94a3b8;
            font-style: italic;
            font-size: 0.875rem;
        }

        /* ---- Card recensione ---- */
        .card-recensione {
            margin-top: 1rem;
            padding: 1rem 1.25rem;
            background: rgba(240,253,244,0.9);
            border: 1px solid #86efac;
            border-radius: 10px;
        }
        .stelle { color: #f59e0b; font-size: 1rem; letter-spacing: 2px; }

        /* ---- Form segnalazione ---- */
        .segnala-form {
            display: none;
            margin-top: 0.75rem;
            padding: 0.85rem 1rem;
            background: rgba(255,248,240,0.95);
            border: 1px solid #fcd34d;
            border-radius: 10px;
            animation: fadeIn 0.2s ease;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-6px); }
            to   { opacity: 1; transform: translateY(0); }
        }
        .segnala-form.visible { display: block; }

        /* ---- Bottoni ---- */
        .btn-pro-primary {
            background: linear-gradient(135deg, #313D5A, #416FDD);
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            padding: 0.4rem 1.1rem;
            transition: opacity 0.2s, transform 0.15s;
        }
        .btn-pro-primary:hover { opacity: 0.9; transform: translateY(-1px); color: white; }

        .btn-pro-success {
            background: #16a34a;
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            padding: 0.4rem 1.1rem;
            transition: opacity 0.2s, transform 0.15s;
        }
        .btn-pro-success:hover { opacity: 0.85; transform: translateY(-1px); color: white; background: #15803d; }

        .btn-pro-danger {
            background: #dc2626;
            color: white;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            padding: 0.4rem 1.1rem;
            transition: opacity 0.2s, transform 0.15s;
        }
        .btn-pro-danger:hover { opacity: 0.85; transform: translateY(-1px); color: white; background: #b91c1c; }

        /* ---- Form controls ---- */
        .form-control, .form-select {
            border-radius: 8px;
            border: 1.5px solid #d1d5db;
            background: white;
        }
        .form-control:focus, .form-select:focus {
            border-color: #416FDD;
            box-shadow: 0 0 0 3px rgba(65,111,221,0.15);
        }
    </style>
</head>

<body>

    <!-- ===== NAVBAR ===== -->
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
         
                </ul>
                <div class="d-flex align-items-center gap-2 flex-wrap">
                    <a href="pro-home" class="btn btn-outline-light btn-sm">
                        <i class="bi bi-briefcase me-1"></i>Pro Home
                    </a>
                    <a href="/HomeServices/logout" class="btn btn-outline-danger btn-sm">Logout</a>
                </div>
            </div>
        </div>
    </nav>

    <!-- ===== MAIN ===== -->
    <main class="container" style="max-width: 900px;">

        <!-- Welcome banner -->
        <div class="welcome-banner">
            <h2><i class="bi bi-list-check me-2"></i>Le mie richieste</h2>
            <p>Gestisci le richieste ricevute, accettale, completale e monitora le recensioni.</p>
        </div>

        <%
            @SuppressWarnings("unchecked")
            List<Richiesta> richieste = (List<Richiesta>) request.getAttribute("richieste");
            @SuppressWarnings("unchecked")
            Map<Long, Recensione> recensioniMap = (Map<Long, Recensione>) request.getAttribute("recensioniMap");
            @SuppressWarnings("unchecked")
            Map<Long, Utente> mittentiMap = (Map<Long, Utente>) request.getAttribute("mittentiMap");

            if (richieste == null || richieste.isEmpty()) {
        %>
            <div class="alert alert-info text-center">
                <i class="bi bi-inbox me-2"></i>Nessuna richiesta trovata.
            </div>
        <%
            } else {
                List<Richiesta> inAttesa   = new ArrayList<>();
                List<Richiesta> inCorso    = new ArrayList<>();
                List<Richiesta> completate = new ArrayList<>();

                for (Richiesta r : richieste) {
                    Stato s = r.getStato();
                    if      (s == Stato.IN_ATTESA)   inAttesa.add(r);
                    else if (s == Stato.IN_CORSO)    inCorso.add(r);
                    else if (s == Stato.COMPLETATO)  completate.add(r);
                }
        %>

        <%-- ========== IN ATTESA ========== --%>
        <div class="section-title">
            <i class="bi bi-hourglass-split" style="color:#d97706;"></i> In attesa
        </div>
        <% if (inAttesa.isEmpty()) { %>
            <p class="empty-section ms-1 mb-4">Nessuna richiesta in attesa.</p>
        <% } else { for (Richiesta r : inAttesa) {
               Utente mittente = mittentiMap != null ? mittentiMap.get(r.getUtenteRichiede().getId()) : null;
        %>
        <div class="card-richiesta">
            <div class="d-flex justify-content-between align-items-start flex-wrap gap-3">
                <div>
                    <div class="meta-label"><i class="bi bi-person me-1"></i>Mittente</div>
                    <div class="meta-value">
                        <% if (mittente != null) { %>
                            <%= mittente.getNome() %> <%= mittente.getCognome() %>
                            <span class="text-muted" style="font-size:0.8rem; font-weight:400;"> · <%= mittente.getEmail() %></span>
                        <% } else { %>
                            ID: <%= r.getUtenteRichiede().getId() %>
                        <% } %>
                    </div>
                    <div class="meta-label mt-2"><i class="bi bi-calendar3 me-1"></i>Data &amp; orario</div>
                    <div class="meta-value"><%= r.getData() %> &nbsp;·&nbsp; <%= r.getDa() %> – <%= r.getA() %></div>
                    <div class="meta-label mt-2"><i class="bi bi-geo-alt me-1"></i>Indirizzo</div>
                    <div class="meta-value"><%= r.getIndirizzo() %></div>
                    <% if (r.getProfessione() != null) { %>
                    <div class="meta-label mt-2"><i class="bi bi-briefcase me-1"></i>Tipo di servizio</div>
                    <div class="meta-value mt-1">
                        <span class="professione-pill">
                            <i class="bi bi-check-circle-fill"></i><%= r.getProfessione().getNome() %>
                        </span>
                    </div>
                    <% } %>
                    <div class="meta-label mt-2"><i class="bi bi-chat-left-text me-1"></i>Descrizione</div>
                    <div class="meta-value"><%= r.getDescrizione() %></div>
                </div>
                <div class="d-flex flex-column align-items-end gap-2">
                    <span class="badge bg-warning text-dark badge-stato">
                        <i class="bi bi-hourglass-split me-1"></i>In attesa
                    </span>
                    <form action="/HomeServices/pro-task-review" method="post" class="d-flex gap-2">
                        <input type="hidden" name="idRichiesta" value="<%= r.getId() %>">
                        <button name="azione" value="accetta" class="btn btn-pro-success btn-sm">
                            <i class="bi bi-check-lg me-1"></i>Accetta
                        </button>
                        <button name="azione" value="rifiuta" class="btn btn-pro-danger btn-sm">
                            <i class="bi bi-x-lg me-1"></i>Rifiuta
                        </button>
                    </form>
                </div>
            </div>
        </div>
        <% } } %>

        <%-- ========== IN CORSO ========== --%>
        <div class="section-title mt-3">
            <i class="bi bi-tools" style="color:#2563eb;"></i> In corso
        </div>
        <% if (inCorso.isEmpty()) { %>
            <p class="empty-section ms-1 mb-4">Nessuna richiesta in corso.</p>
        <% } else { for (Richiesta r : inCorso) {
               Utente mittente = mittentiMap != null ? mittentiMap.get(r.getUtenteRichiede().getId()) : null;
        %>
        <div class="card-richiesta">
            <div class="d-flex justify-content-between align-items-start flex-wrap gap-3">
                <div>
                    <div class="meta-label"><i class="bi bi-person me-1"></i>Mittente</div>
                    <div class="meta-value">
                        <% if (mittente != null) { %>
                            <%= mittente.getNome() %> <%= mittente.getCognome() %>
                            <span class="text-muted" style="font-size:0.8rem; font-weight:400;"> · <%= mittente.getEmail() %></span>
                        <% } else { %>
                            ID: <%= r.getUtenteRichiede().getId() %>
                        <% } %>
                    </div>
                    <div class="meta-label mt-2"><i class="bi bi-calendar3 me-1"></i>Data &amp; orario</div>
                    <div class="meta-value"><%= r.getData() %> &nbsp;·&nbsp; <%= r.getDa() %> – <%= r.getA() %></div>
                    <div class="meta-label mt-2"><i class="bi bi-geo-alt me-1"></i>Indirizzo</div>
                    <div class="meta-value"><%= r.getIndirizzo() %></div>
                    <% if (r.getProfessione() != null) { %>
                    <div class="meta-label mt-2"><i class="bi bi-briefcase me-1"></i>Tipo di servizio</div>
                    <div class="meta-value mt-1">
                        <span class="professione-pill">
                            <i class="bi bi-check-circle-fill"></i><%= r.getProfessione().getNome() %>
                        </span>
                    </div>
                    <% } %>
                    <div class="meta-label mt-2"><i class="bi bi-chat-left-text me-1"></i>Descrizione</div>
                    <div class="meta-value"><%= r.getDescrizione() %></div>
                </div>
                <div class="d-flex flex-column align-items-end gap-2">
                    <span class="badge bg-primary badge-stato">
                        <i class="bi bi-tools me-1"></i>In corso
                    </span>
                    <form action="/HomeServices/pro-task-review" method="post">
                        <input type="hidden" name="idRichiesta" value="<%= r.getId() %>">
                        <button name="azione" value="completa" class="btn btn-pro-primary btn-sm">
                            <i class="bi bi-check2-circle me-1"></i>Completa
                        </button>
                    </form>
                </div>
            </div>
        </div>
        <% } } %>

        <%-- ========== COMPLETATE ========== --%>
        <div class="section-title mt-3">
            <i class="bi bi-check-circle-fill" style="color:#16a34a;"></i> Completate
        </div>
        <% if (completate.isEmpty()) { %>
            <p class="empty-section ms-1 mb-4">Nessuna richiesta completata.</p>
        <% } else { for (Richiesta r : completate) {
               Recensione rec = (recensioniMap != null) ? recensioniMap.get(r.getId()) : null;
               Utente mittente = mittentiMap != null ? mittentiMap.get(r.getUtenteRichiede().getId()) : null;
        %>
        <div class="card-richiesta">

            <%-- Dati della richiesta --%>
            <div class="d-flex justify-content-between align-items-start flex-wrap gap-3">
                <div>
                    <div class="meta-label"><i class="bi bi-person me-1"></i>Mittente</div>
                    <div class="meta-value">
                        <% if (mittente != null) { %>
                            <%= mittente.getNome() %> <%= mittente.getCognome() %>
                            <span class="text-muted" style="font-size:0.8rem; font-weight:400;"> · <%= mittente.getEmail() %></span>
                        <% } else { %>
                            ID: <%= r.getUtenteRichiede().getId() %>
                        <% } %>
                    </div>
                    <div class="meta-label mt-2"><i class="bi bi-calendar3 me-1"></i>Data &amp; orario</div>
                    <div class="meta-value"><%= r.getData() %> &nbsp;·&nbsp; <%= r.getDa() %> – <%= r.getA() %></div>
                    <div class="meta-label mt-2"><i class="bi bi-geo-alt me-1"></i>Indirizzo</div>
                    <div class="meta-value"><%= r.getIndirizzo() %></div>
                    <% if (r.getProfessione() != null) { %>
                    <div class="meta-label mt-2"><i class="bi bi-briefcase me-1"></i>Tipo di servizio</div>
                    <div class="meta-value mt-1">
                        <span class="professione-pill">
                            <i class="bi bi-check-circle-fill"></i><%= r.getProfessione().getNome() %>
                        </span>
                    </div>
                    <% } %>
                    <div class="meta-label mt-2"><i class="bi bi-chat-left-text me-1"></i>Descrizione</div>
                    <div class="meta-value"><%= r.getDescrizione() %></div>
                </div>
                <span class="badge bg-success badge-stato align-self-start">
                    <i class="bi bi-check-circle me-1"></i>Completato
                </span>
            </div>

            <%-- Blocco recensione (solo se presente) --%>
            <% if (rec != null) { %>
            <div class="card-recensione">

                <div class="d-flex justify-content-between align-items-start flex-wrap gap-2">
                    <div>
                        <div class="meta-label mb-1"><i class="bi bi-star-fill me-1"></i>Recensione ricevuta</div>

                        <%-- Stelle --%>
                        <div class="stelle mb-1">
                            <% int voto = rec.getVoto(); %>
                            <% for (int i = 1; i <= 10; i++) { %>
                                <%= i <= voto ? "★" : "☆" %>
                            <% } %>
                            <span class="text-muted ms-1" style="font-size:0.8rem;">(<%= rec.getVoto() %>/10)</span>
                        </div>

                        <div class="meta-value"><%= rec.getDescrizione() != null ? rec.getDescrizione() : "Nessun commento" %></div>
                        <div class="meta-label mt-1"><i class="bi bi-calendar3 me-1"></i><%= rec.getData() %></div>
                    </div>

                    <%-- Pulsante segnala --%>
                    <button type="button"
                            class="btn btn-outline-danger btn-sm align-self-start"
                            onclick="toggleForm('<%= r.getId() %>')">
                        <i class="bi bi-flag me-1"></i>Segnala
                    </button>
                </div>

                <%-- Form di segnalazione inline --%>
                <div class="segnala-form" id="form-segnala-<%= r.getId() %>">
                    <p class="mb-2 fw-semibold" style="font-size:0.85rem; color:#b45309;">
                        <i class="bi bi-exclamation-triangle me-1"></i>Stai segnalando questa recensione
                    </p>
                    <form action="/HomeServices/pro-task-review" method="post">
                        <input type="hidden" name="idRichiesta"  value="<%= r.getId() %>">
                        <input type="hidden" name="idRecensione" value="<%= rec.getId() %>">
                        <input type="hidden" name="azione"       value="segnala">
                        <div class="mb-2">
                            <label class="form-label" style="font-size:0.82rem;">Motivazione</label>
                            <textarea name="motivazione" class="form-control form-control-sm"
                                      rows="3" placeholder="Descrivi il motivo della segnalazione..." required></textarea>
                        </div>
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-pro-danger btn-sm">
                                <i class="bi bi-send me-1"></i>Invia segnalazione
                            </button>
                            <button type="button" class="btn btn-outline-secondary btn-sm"
                                    onclick="chiudiForm('<%= r.getId() %>')">Annulla</button>
                        </div>
                    </form>
                </div>

            </div>
            <% } %>

        </div>
        <% } } %>

        <% } %>

    </main>

    <!-- ===== FOOTER ===== -->
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
                <ul>
                    <li><a href="/HomeServices/servizi">Tutti i servizi</a></li>
                </ul>
            </div>
            <div class="footer-column">
                <h4>Contatti:</h4>
                <ul>
                    <li><a href="mailto:homeservices@gmail.com?subject=Richiesta informazioni">Email: homeservices@gmail.com</a></li>
                    <li><a href="tel:+393920626721">Telefono: 392 062 6721</a></li>
                </ul>
            </div>
        </div>
        <div class="footer-bottom">
            <p>© 2026 HomeServices</p>
        </div>
    </footer>

<script>
    function toggleForm(idRichiesta) {
        const form = document.getElementById('form-segnala-' + idRichiesta);
        if (form) {
            form.classList.toggle('visible');
            if (form.classList.contains('visible')) {
                form.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            }
        }
    }
    function chiudiForm(idRichiesta) {
        const form = document.getElementById('form-segnala-' + idRichiesta);
        if (form) form.classList.remove('visible');
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
