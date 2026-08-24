<%@page import="org.elis.homeservices.model.Utente"%>
<%@page import="org.elis.homeservices.model.Segnalazione"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Report</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin-home.css">
<link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
<style>
    .label-col {
        font-size: 0.7rem;
        text-transform: uppercase;
        letter-spacing: 0.06em;
        font-weight: 700;
        margin-bottom: 2px;
        color: #6b7280;
    }
    .info-nome  { font-size: 0.95rem; font-weight: 600; color: #111827; }
    .info-email { font-size: 0.78rem; color: #6b7280; }
    .info-motiv { font-size: 0.9rem;  color: #374151; }

    .seg-grid {
        display: grid;
        grid-template-columns: 80px 1fr 1fr 1.3fr;
        gap: 0.75rem;
        align-items: start;
    }
    @media (max-width: 768px) {
        .seg-grid { grid-template-columns: 1fr 1fr; }
        .seg-id, .seg-motiv { grid-column: 1 / -1; }
    }
    @media (max-width: 480px) {
        .seg-grid { grid-template-columns: 1fr; }
        .seg-id, .seg-motiv { grid-column: unset; }
    }

    .card-seg {
        border-radius: 8px;
        padding: 1rem;
        margin-top: 0.6rem;
    }
    .card-seg-pendente { border: 1px solid #e5e7eb; background: #ffffff; box-shadow: 0 1px 4px rgba(0,0,0,0.06); }
    .card-seg-storico  { border: 1px solid #e5e7eb; background: #f9fafb; }

    .section-header {
        font-size: 0.78rem;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.07em;
        padding: 0.5rem 0.75rem;
        border-radius: 6px;
        margin-bottom: 0.25rem;
    }
    .section-header-pendente { background: #fee2e2; color: #b91c1c; }
    .section-header-storico  { background: #dcfce7; color: #166534; }

    /* Barra di ricerca */
    .search-bar {
        background: #f3f4f6;
        border: 1px solid #d1d5db;
        border-radius: 10px;
        padding: 0.85rem 1rem;
        margin-bottom: 1.5rem;
    }
    .search-bar input {
        background: #ffffff;
        border: 1px solid #d1d5db;
        color: #111827;
        border-radius: 6px;
    }
    .search-bar input::placeholder { color: #9ca3af; }
    .search-bar input:focus {
        background: #ffffff;
        border-color: #3b82f6;
        color: #111827;
        box-shadow: 0 0 0 3px rgba(59,130,246,0.15);
    }
    .badge-ricerca {
        background: #eff6ff;
        color: #1d4ed8;
        border: 1px solid #bfdbfe;
        border-radius: 20px;
        padding: 0.25rem 0.75rem;
        font-size: 0.8rem;
    }
</style>
</head>
<body>

    <nav class="navbar navbar-expand-lg sfondo navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">HomeServices
                <span class="badge bg-primary ms-2" style="font-size:0.6rem;">ADMIN</span>
            </a>
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="/HomeServices/admin-home">Torna indietro</a>
                </li>
            </ul>
            <div class="ms-auto">
                <a href="/HomeServices" class="btn btn-sm btn-outline-danger">Logout</a>
            </div>
        </div>
    </nav>

    <main class="container sfondo2 my-5 max-width-900 pb-5 shadow rounded">

        <div class="mb-4 pb-2">
            <h2 class="fw-bold">Segnalazioni Utenti</h2>
            <p class="text-secondary">Monitora e gestisci i report per comportamenti scorretti</p>
        </div>

        <%
        @SuppressWarnings("unchecked")
        List<Segnalazione> listPendenti = (List<Segnalazione>) request.getAttribute("listSegnalazioni");
        @SuppressWarnings("unchecked")
        List<Segnalazione> listStorico  = (List<Segnalazione>) request.getAttribute("listSegnalazioniSanzionate");
        @SuppressWarnings("unchecked")
        Map<Long, Utente> utentiMap     = (Map<Long, Utente>) request.getAttribute("utentiMap");
        String cerca        = (String)  request.getAttribute("cerca");
        Boolean ricercaAttiva = (Boolean) request.getAttribute("ricercaAttiva");
        if (cerca == null) cerca = "";
        if (ricercaAttiva == null) ricercaAttiva = false;
        %>

        <%-- ======= BARRA DI RICERCA ======= --%>
        <div class="search-bar">
            <form action="/HomeServices/admin-report" method="GET" class="d-flex gap-2 align-items-center flex-wrap">
                <div class="flex-grow-1">
                    <div class="input-group">
                        <span class="input-group-text" style="background:#f9fafb; border-color:#d1d5db; color:#6b7280;">
                            <i class="bi bi-search"></i>
                        </span>
                        <input type="text"
                               name="cerca"
                               class="form-control"
                               placeholder="Cerca segnalazioni per nome o cognome dell'utente segnalato..."
                               value="<%= cerca %>">
                    </div>
                </div>
                <button type="submit" class="btn btn-primary btn-sm px-3">Cerca</button>
                <% if (ricercaAttiva) { %>
                <a href="/HomeServices/admin-report" class="btn btn-outline-secondary btn-sm px-3">
                    <i class="bi bi-x me-1"></i>Tutti
                </a>
                <% } %>
            </form>
            <% if (ricercaAttiva) { %>
            <div class="mt-2">
                <span class="badge-ricerca">
                    <i class="bi bi-funnel me-1"></i>Risultati per: <strong><%= cerca %></strong>
                    &nbsp;·&nbsp; <%= (listPendenti != null ? listPendenti.size() : 0) + (listStorico != null ? listStorico.size() : 0) %> segnalazioni trovate
                </span>
            </div>
            <% } %>
        </div>

        <!-- SEZIONE IN ATTESA -->
        <div class="section-header section-header-pendente">
            <i class="bi bi-clock-history me-1"></i> Report in attesa di approvazione
        </div>

        <% if (listPendenti == null || listPendenti.isEmpty()) { %>
            <div class="card-seg card-seg-pendente text-muted">
                <%= ricercaAttiva ? "Nessuna segnalazione in attesa per \"" + cerca + "\"." : "Nessuna segnalazione in attesa." %>
            </div>
        <% } else { for (Segnalazione s : listPendenti) {
               Utente segnalato  = utentiMap != null ? utentiMap.get(s.getUtenteSegnalato().getId())  : null;
               Utente segnalante = utentiMap != null ? utentiMap.get(s.getUtenteSegnalante().getId()) : null;
        %>
        <div class="card-seg card-seg-pendente">
            <div class="seg-grid">
                <div class="seg-id text-center">
                    <div class="label-col fs-6">N°</div>
                    <div class="fw-bold fs-5"><%= s.getId() %></div>
                </div>
                <div>
                    <div class="label-col" style="color:#dc2626;">Utente Segnalato</div>
                    <% if (segnalato != null) { %>
                        <div class="info-nome"><%= segnalato.getNome() %> <%= segnalato.getCognome() %></div>
                        <div class="info-email"><%= segnalato.getEmail() %></div>
                    <% } else { %>
                        <div class="text-muted">ID: <%= s.getUtenteSegnalato().getId() %></div>
                    <% } %>
                </div>
                <div>
                    <div class="label-col" style="color:#d97706;">Utente Segnalante</div>
                    <% if (segnalante != null) { %>
                        <div class="info-nome"><%= segnalante.getNome() %> <%= segnalante.getCognome() %></div>
                        <div class="info-email"><%= segnalante.getEmail() %></div>
                    <% } else { %>
                        <div class="text-muted">ID: <%= s.getUtenteSegnalante().getId() %></div>
                    <% } %>
                </div>
                <div class="seg-motiv">
                    <div class="label-col text-muted">Motivazione</div>
                    <div class="info-motiv"><%= s.getMotivazione() %></div>
                </div>
            </div>
            <div class="mt-3">
                <form action="/HomeServices/admin-report" method="POST" class="d-inline">
                    <input type="hidden" name="idSegnalazione" value="<%= s.getId() %>">
                    <input type="hidden" name="cerca" value="<%= cerca %>">
                    <button type="submit" name="azione" value="sanziona" class="btn btn-danger btn-sm px-3 me-2">
                        <i class="bi bi-hammer me-1"></i>Sanziona
                    </button>
                    <button type="submit" name="azione" value="chiudi" class="btn btn-secondary btn-sm px-3">
                        <i class="bi bi-x-circle me-1"></i>Chiudi
                    </button>
                </form>
            </div>
        </div>
        <% } } %>

        <%-- ======= SEZIONE STORICO ======= --%>
        <div class="section-header section-header-storico mt-4">
            <i class="bi bi-archive me-1"></i> Storico segnalazioni approvate
        </div>

        <% if (listStorico == null || listStorico.isEmpty()) { %>
            <div class="card-seg card-seg-storico text-muted">
                <%= ricercaAttiva ? "Nessuna segnalazione nello storico per \"" + cerca + "\"." : "Nessuna segnalazione approvata." %>
            </div>
        <% } else { for (Segnalazione s : listStorico) {
               Utente segnalato  = utentiMap != null ? utentiMap.get(s.getUtenteSegnalato().getId())  : null;
               Utente segnalante = utentiMap != null ? utentiMap.get(s.getUtenteSegnalante().getId()) : null;
        %>
        <div class="card-seg card-seg-storico">
            <div class="seg-grid">
                <div class="seg-id text-center">
                    <div class="label-col fs-6">N°</div>
                    <div class="fw-bold fs-5"><%= s.getId() %></div>
                    <span class="badge bg-success mt-1" style="font-size:0.65rem;">Sanzionato</span>
                </div>
                <div>
                    <div class="label-col" style="color:#dc2626;">Utente Sanzionato</div>
                    <% if (segnalato != null) { %>
                        <div class="info-nome"><%= segnalato.getNome() %> <%= segnalato.getCognome() %></div>
                        <div class="info-email"><%= segnalato.getEmail() %></div>
                    <% } else { %>
                        <div class="text-muted">ID: <%= s.getUtenteSegnalato().getId() %></div>
                    <% } %>
                </div>
                <div>
                    <div class="label-col" style="color:#d97706;">Segnalato da</div>
                    <% if (segnalante != null) { %>
                        <div class="info-nome"><%= segnalante.getNome() %> <%= segnalante.getCognome() %></div>
                        <div class="info-email"><%= segnalante.getEmail() %></div>
                    <% } else { %>
                        <div class="text-muted">ID: <%= s.getUtenteSegnalante().getId() %></div>
                    <% } %>
                </div>
                <div class="seg-motiv">
                    <div class="label-col text-muted">Motivazione</div>
                    <div class="info-motiv"><%= s.getMotivazione() %></div>
                </div>
            </div>
        </div>
        <% } } %>

    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
