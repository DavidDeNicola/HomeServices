	<%@page import="org.elis.homeservices.model.Utente"%>
<%@page import="org.elis.homeservices.model.Professione"%>
<%@page import="org.elis.homeservices.model.Richiesta"%>
<%@page import="org.elis.homeservices.model.enums.Stato"%>
<%@page import="org.elis.homeservices.dao.definition.RichiestaDAO"%>
<%@page import="org.elis.homeservices.dao.definition.UtenteDAO"%>
<%@page import="org.elis.homeservices.dao.definition.ProfessioneDAO"%>
<%@page import="org.elis.homeservices.dao.definition.RecensioneDAO"%>
<%@page import="org.elis.homeservices.dao.jdbc.JdbcRichiestaDAO"%>
<%@page import="org.elis.homeservices.dao.jdbc.JdbcUtenteDAO"%>
<%@page import="org.elis.homeservices.dao.jdbc.JdbcProfessioneDAO"%>
<%@page import="org.elis.homeservices.utility.DataSourceConfig"%>
<%@page import="org.elis.homeservices.dao.definition.DaoFactory"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
    <title>HomeServices – Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
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

        /* ---- Sezione titolo ---- */
        .section-title {
            font-weight: 700;
            color: #1e293b;
            font-size: 1.25rem;
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
        .req-card {
            background: rgba(237,242,244,0.92);
            backdrop-filter: blur(6px);
            border: none;
            border-radius: 14px;
            box-shadow: 0 2px 12px rgba(0,0,0,0.10);
            transition: transform 0.2s, box-shadow 0.2s;
            overflow: hidden;
        }
        .req-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 24px rgba(0,0,0,0.14);
        }
        .req-card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 0.5rem;
        }
        .req-card-title { font-weight: 700; font-size: 1.05rem; color: #1e293b; margin: 0; }
        .req-meta {
            font-size: 0.8rem;
            color: #64748b;
            margin-bottom: 0.75rem;
            display: flex;
            gap: 1rem;
            flex-wrap: wrap;
        }
        .req-meta span { display: flex; align-items: center; gap: 0.25rem; }
        .req-descrizione { color: #374151; font-size: 0.92rem; margin-bottom: 0; }

        /* ---- Box recensione ---- */
        .recensione-box {
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 10px;
            padding: 1rem 1.25rem;
            margin-top: 1rem;
        }
        .recensione-box-title {
            font-size: 0.78rem;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.06em;
            color: #416FDD;
            margin-bottom: 0.75rem;
        }
        .voto-badge {
            background: linear-gradient(135deg, #313D5A, #416FDD);
            color: white;
            font-weight: 700;
            font-size: 1rem;
            border-radius: 8px;
            padding: 0.4rem 0.75rem;
            min-width: 60px;
            text-align: center;
        }
        .commento-text {
            font-style: italic;
            color: #374151;
            font-size: 0.9rem;
        }

        /* ---- Box lascia recensione ---- */
        .lascia-rec-box {
            background: #f0f7ff;
            border: 1px solid #bfdbfe;
            border-radius: 10px;
            padding: 1rem 1.25rem;
            margin-top: 1rem;
        }
        .lascia-rec-box .box-title {
            font-size: 0.78rem;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.06em;
            color: #1d4ed8;
            margin-bottom: 0.75rem;
        }

        /* ---- Stato badge ---- */
        .badge-stato-completato { background-color: #16a34a; }
        .badge-stato-in-corso   { background-color: #2563eb; }
        .badge-stato-in-attesa  { background-color: #d97706; }

        /* ---- Alert vuoto ---- */
        .empty-state {
            background: rgba(237,242,244,0.85);
            border-radius: 12px;
            padding: 2rem;
            text-align: center;
            color: #64748b;
        }
    </style>
</head>
<body>

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
                        <a class="nav-link" href="list-pro">Professionisti</a>
                    </li>
                </ul>
                <div class="d-flex align-items-center gap-2 flex-wrap">
                    <% if (!isPro) { %>
                    <a href="user-to-pro" class="btn btn-outline-light btn-sm">Diventa Pro</a>
                    <% } else { %>
                    <a href="pro-home" class="btn btn-sm"
                       style="background:#416FDD; color:white; border:none;">Pro Home</a>
                    <% } %>
                    <a href="/HomeServices/logout" class="btn btn-outline-danger btn-sm">Logout</a>
                </div>
            </div>
        </div>
    </nav>

    <!-- MAIN -->
    <main class="container" style="max-width: 860px;">

        <!-- Welcome -->
        <div class="welcome-banner">
            <h2><i class="bi bi-house-heart-fill me-2"></i>Benvenuto, <%= utenteLoggato.getNome() %>!</h2>
            <p>Qui trovi un riepilogo delle tue richieste di servizio.</p>
        </div>

        <!-- Ultime richieste -->
        <div class="section-title">
            <i class="bi bi-list-check" style="color:#416FDD;"></i> Le mie richieste
        </div>

        <%
            @SuppressWarnings("unchecked")
            List<Richiesta> listaRichieste = (List<Richiesta>) request.getAttribute("richiesteEffettuate");

            ProfessioneDAO professioneDao = DaoFactory.getInstance().getProfessioneDAO();
            RichiestaDAO richiestaDao     = DaoFactory.getInstance().getRichiestaDAO();
            RecensioneDAO recensioneDao   = DaoFactory.getInstance().getRecensioneDAO();

            if (listaRichieste != null && !listaRichieste.isEmpty()) {
                for (Richiesta r : listaRichieste) {
                    String nomeProfessione = professioneDao.findById(r.getProfessione().getId()).getNome();
                    boolean completata = Stato.COMPLETATO.equals(r.getStato());
                    boolean haRecensione = completata && richiestaDao.getIdRecensioneById(r.getId()) != null;
                    String badgeClass = completata ? "badge-stato-completato"
                                      : Stato.IN_CORSO.equals(r.getStato()) ? "badge-stato-in-corso"
                                      : "badge-stato-in-attesa";
        %>
        <div class="req-card p-3 mb-3">
        	<h3 class="req-card-title">
                	<%= r.getUtenteRiceve().getNome() %> <%= r.getUtenteRiceve().getCognome() %> 
            </h3>
            <div class="req-card-header">
            	
                <p>
                    <i class="bi bi-tools me-1" style="color:#416FDD;"></i><%= nomeProfessione %>
                </p>
                <span class="badge <%= badgeClass %>" style="font-size:0.8rem;">
                    <%= r.getStato().getNome() %>
                </span>
            </div>
            <div class="req-meta">
                <span><i class="bi bi-calendar3"></i> <%= r.getData() %></span>
                <span><i class="bi bi-clock"></i> <%= r.getDa() %> – <%= r.getA() %></span>
                <span><i class="bi bi-geo-alt"></i> <%= r.getIndirizzo() %></span>
            </div>
            <p class="req-descrizione"><%= r.getDescrizione() %></p>

            <% if (completata) { %>
                <% if (!haRecensione) { %>
                <!-- Form lascia recensione -->
                <div class="lascia-rec-box">
                    <div class="box-title"><i class="bi bi-star me-1"></i>Lascia una recensione</div>
                    <form action="" method="POST">
                        <input type="hidden" name="idPro" value="<%= r.getUtenteRiceve().getId() %>">
                        <input type="hidden" name="idReq" value="<%= r.getId() %>">
                        <div class="row g-2 align-items-end">
                            <div class="col-md-7">
                                <textarea name="description" class="form-control form-control-sm"
                                          rows="2" placeholder="Scrivi il tuo commento..."></textarea>
                            </div>
                            <div class="col-md-2">
                                <input type="number" name="rating" min="0" max="10"
                                       class="form-control form-control-sm"
                                       placeholder="Voto" required>
                            </div>
                            <div class="col-md-3">
                                <button type="submit" class="btn btn-sm w-100"
                                        style="background:#416FDD; color:white; border:none;">
                                    <i class="bi bi-send me-1"></i>Invia
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
                <% } else { %>
                <!-- Mostra recensione esistente -->
                <%
                    Long idRec = richiestaDao.getIdRecensioneById(r.getId());
                    org.elis.homeservices.model.Recensione rec = recensioneDao.findById(idRec);
                %>
                <div class="recensione-box">
                    <div class="recensione-box-title"><i class="bi bi-star-fill me-1"></i>La tua recensione</div>
                    <div class="d-flex align-items-center gap-3">
                        <div class="voto-badge"><%= rec.getVoto() %>/10</div>
                        <div class="vr" style="height:40px;"></div>
                        <p class="commento-text mb-0">"<%= rec.getDescrizione() %>"</p>
                    </div>
                </div>
                <% } %>
            <% } %>
        </div>
        <%
                }
            } else {
        %>
        <div class="empty-state">
            <i class="bi bi-inbox fs-1 d-block mb-2" style="color:#94a3b8;"></i>
            Non hai ancora effettuato nessuna richiesta.
        </div>
        <% } %>

    </main>

    <%@ include file="/WEB-INF/includes/footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
