<%@page import="org.elis.homeservices.model.Immagine"%>
<%@page import="org.elis.homeservices.model.enums.TipoImmagine"%>
<%@page import="org.elis.homeservices.model.Citta"%>
<%@page import="org.elis.homeservices.model.Veicolo"%>
<%@page import="org.elis.homeservices.model.enums.Ruolo"%>
<%@page import="org.elis.homeservices.dao.definition.DaoFactory"%>
<%@page import="org.elis.homeservices.dao.definition.UtenteDAO"%>
<%@page import="org.elis.homeservices.model.Utente"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="org.elis.homeservices.model.Professione" %>
<!doctype html>
<html lang="it">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Lista Professionisti</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="css/navbar.css" />
    <link rel="stylesheet" href="css/style.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/shared.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/index.css">
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
</head>
<body>

    <% UtenteDAO utenteDao = DaoFactory.getInstance().getUtenteDAO();
    @SuppressWarnings("unchecked")
    List<Citta> listaCitta = (List<Citta>) request.getAttribute("listaCitta");
    %>

    <nav class="navbar navbar-expand-lg sfondo navbar-dark bg-dark fixed-top">
        <div class="container-fluid">
            <a href="/HomeServices" class="navbar-brand">
                <img class="logo" src="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png" alt="HomeServiceLogo">
            </a>
            <a href="/HomeServices" class="navbar-brand">
                <img class="logo1" src="<%= request.getContextPath() %>/css/immagini/loghetto-finale2.png" alt="HomeServiceLogo">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
                aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="btn btn-outline-light" href="user-home">Indietro</a>
                    </li>
                </ul>
                <% Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
                   if (utenteLoggato != null && utenteLoggato.getRuolo() == Ruolo.USER) { %>
                <a class="btn btn-outline-light" href="user-to-pro">Diventa Pro</a>
                <% } %>
            </div>
            <% if (utenteLoggato != null) { %>
            <a href="/HomeServices/logout" class="btn btn-outline-danger ms-2">Logout</a>
            <% } %>
        </div>
    </nav>

    <div class="container mt-5 p-5">
        <% String messaggio = (String) request.getAttribute("messaggio"); %>
        <% if (messaggio != null) { %>
            <div class="alert alert-info alert-dismissible fade show" role="alert">
                <%= messaggio %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        <% } %>

        <div class="search-section p-4 shadow-sm bg-white rounded-4 mb-4">
            <form action="list-pro" method="POST">
                <div class="row g-3 align-items-center">
                    <div class="col-md-10">
                        <div class="input-group">
                            <span class="input-group-text bg-transparent border-end-0"><i class="fas fa-search text-muted"></i></span>
                            <input type="text" name="nome" class="form-control border-start-0 ps-0 py-3" placeholder="Cerca per nome o professione...">
                        </div>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-primary w-100 py-3 fw-bold rounded-3">Cerca</button>
                    </div>
                </div>

                <div class="mt-3">
                    <button class="btn btn-link text-decoration-none fw-bold p-0" type="button" data-bs-toggle="collapse" data-bs-target="#filtriAvanzati" style="color: #313D5A;">
                        Ricerca Avanzata <i class="fas fa-sliders-h ms-1"></i>
                    </button>
                </div>

                <div class="collapse mt-4" id="filtriAvanzati">
                    <div class="card card-body border-0 bg-light rounded-4 p-4">
                        <div class="row g-4">
                            <div class="col-md-3">
                                <label class="form-label fw-bold small text-muted">CITTÀ</label>
                                <select name="idCitta" class="form-select border-0 shadow-sm py-2">
                                    <option value="">Tutte le città</option>
                                    <% for (Citta c : listaCitta) { %>
                                    <option value="<%= c.getId() %>"><%= c.getNome() %></option>
                                    <% } %>
                                </select>
                            </div>

                            <div class="col-md-3">
                                <label for="vRange" class="form-label fw-bold small text-muted">VOTO MINIMO</label>
                                <input type="range" name="valutazione" class="form-range" min="0" max="10" value="0" id="vRange" oninput="vOut.value = vRange.value">
                                <div class="text-center mt-1">
                                    <span class="badge bg-primary px-3 py-2"><output id="vOut" style="vertical-align:-1.5px;">0</output> <i class="bi bi-star-fill"></i></span>
                                </div>
                            </div>

                            <div class="col-md-3">
                                <label class="form-label fw-bold small text-muted">DATA DISPONIBILE</label>
                                <input type="date" name="data" id="dataRicerca" class="form-control border-0 shadow-sm py-2">
                            </div>

                            <div class="col-md-3">
                                <label class="form-label fw-bold small text-muted text-uppercase">Fascia Oraria</label>
                                <div class="d-flex align-items-center gap-2">
                                    <div class="flex-fill">
                                        <input type="time" name="inizio" class="form-control border-0 shadow-sm py-2" min="08:00" max="20:00">
                                    </div>
                                    <span class="text-muted fw-bold">-</span>
                                    <div class="flex-fill">
                                        <input type="time" name="fine" class="form-control border-0 shadow-sm py-2" min="08:00" max="20:00">
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label for="ordine" class="form-label fw-bold small text-muted">ORDINA PER</label>
                                <select id="ordine" name="ordine" class="form-select border-0 shadow-sm py-2">
                                    <option value="voto"   <%= "voto".equals(request.getParameter("ordine"))   ? "selected" : "" %>>Miglior Voto</option>
                                    <option value="prezzo" <%= "prezzo".equals(request.getParameter("ordine")) ? "selected" : "" %>>Prezzo più basso</option>
                                </select>
                            </div>

                            <div class="col-md-6 d-flex align-items-end">
                                <button type="submit" class="btn btn-dark w-100 py-2 fw-bold rounded-3">
                                    <i class="fas fa-filter me-2"></i>Applica Filtri
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

            <%
            String flashSuccesso = (String) session.getAttribute("flashSuccesso");
            if (flashSuccesso != null) {
                session.removeAttribute("flashSuccesso");
            %>
            <div class="alert alert-success alert-dismissible fade show d-flex align-items-center gap-2 mt-3" role="alert">
                <i class="bi bi-check-circle-fill fs-5"></i>
                <span><%= flashSuccesso %></span>
                <button type="button" class="btn-close ms-auto" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% } %>
        </div>
    </div>

    <div class="container mt-5 mb-5">
        <h2 class="text-center mb-5 fw-bold" style="color: #313D5A;">Risultati Ricerca</h2>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4 justify-content-center">
            <%
            @SuppressWarnings("unchecked")
            List<Utente> lista = (List<Utente>) request.getAttribute("listaProfessionisti");

            if (lista != null && !lista.isEmpty()) {
                for (Utente u : lista) {

                    // Cerca la foto profilo attiva dell'utente
                    String fotoProfiloUrl = null;
                    if (u.getImmagini() != null) {
                        for (Immagine img : u.getImmagini()) {
                            if (Boolean.TRUE.equals(img.getIsFotoProfilo())) {
                                fotoProfiloUrl = request.getContextPath() + "/" + img.getPercorso();
                                break;
                            }
                        }
                    }
                    // Fallback: avatar con iniziali
                    if (fotoProfiloUrl == null) {
                        fotoProfiloUrl = "https://ui-avatars.com/api/?name="
                                + u.getNome() + "+" + u.getCognome()
                                + "&background=052c65&color=fff&size=128";
                    }
            %>
            <div class="col d-flex justify-content-center">
                <div class="card h-100 shadow-sm custom-card d-flex flex-column" style="max-width: 380px; width: 100%;">
                    <div class="text-center pt-4">
                        <img src="<%= fotoProfiloUrl %>"
                             class="rounded-circle shadow-sm border border-3 border-light"
                             style="width: 85px; height: 85px; object-fit: cover;"
                             alt="Profilo">
                    </div>
                    <div class="card-body text-start p-4 d-flex flex-column">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <h5 class="card-title fw-bold m-0" style="font-size: 1.2rem;">
                                <%= u.getNome() %> <%= u.getCognome() %>
                            </h5>
                            <div class="text-warning" style="font-size: 0.9rem;">
                                <i class="bi bi-star-fill text-warning"></i>
                                <span class="text-dark fw-bold"><%= u.getRating() %></span>
                            </div>
                        </div>

                        <div class="mb-3">
                            <% for (Professione p : u.getProfessioni()) { %>
                                <span class="badge bg-primary-subtle text-primary border border-primary-subtle text-uppercase me-1" style="font-size: 0.65rem;">
                                    <i class="bi bi-wrench-adjustable me-1"></i><%= p.getNome() %>
                                </span>
                            <% } %>
                        </div>

                        <ul class="list-group list-group-flush border-0 mb-3" style="font-size: 0.85rem;">
                            <li class="list-group-item border-0 px-0 py-1">
                                <i class="bi bi-geo-alt text-danger me-2"></i><b>Città:</b> <%= u.getCitta().getNome() %>
                            </li>
                            <li class="list-group-item border-0 px-0 py-1">
                                <i class="bi bi-cash-stack text-success me-2"></i><b>Tariffa:</b>
                                <span class="text-success fw-bold">
                                    <%= (u.getTariffa() != null) ? u.getTariffa() + " €/h" : "Da concordare" %>
                                </span>
                            </li>
                            <li class="list-group-item border-0 px-0 py-1 text-truncate">
                                <i class="bi bi-envelope text-primary me-2"></i><b>Email:</b> <%= u.getEmail() %>
                            </li>
                            <li class="list-group-item border-0 px-0 py-1">
                                <i class="bi bi-truck text-secondary me-2"></i><b>Mezzi:</b>
                                <% for (Veicolo v : u.getVeicoli()) { %>
                                    <span class="badge bg-dark text-light text-uppercase me-1" style="font-size: 0.65rem;">
                                        <%= v.getNome() %>
                                    </span>
                                <% } %>
                            </li>
                        </ul>

                        <div class="mt-auto">
                            <a href="request-pro?idpro=<%= u.getId() %>" class="btn btn-outline-primary w-100 fw-bold rounded-pill mb-2">
                                VISUALIZZA PROFILO
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <%
                }
            } else if (request.getMethod().equalsIgnoreCase("POST")) {
            %>
                <div class="col-12 text-center py-5">
                    <div class="p-5 border rounded-4 bg-white shadow-sm">
                        <i class="bi bi-search text-muted mb-3" style="font-size: 3rem;"></i>
                        <p class="text-muted fw-bold">Nessun professionista trovato per questi criteri.</p>
                    </div>
                </div>
            <% } else { %>
                <div class="col-12 text-center py-5">
                    <p class="text-muted">Esegui una ricerca per visualizzare i professionisti.</p>
                </div>
            <% } %>
        </div>
    </div>

    <%@ include file="/WEB-INF/includes/footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const dateInput = document.querySelector('input[name="dataRichiesta"]');
        if (dateInput) {
            const today = new Date().toISOString().split('T')[0];
            dateInput.setAttribute('min', today);
            dateInput.value = today;
        }
        const searchDateInput = document.getElementById('dataRicerca');
        if (searchDateInput) {
            searchDateInput.setAttribute('min', new Date().toISOString().split('T')[0]);
        }
        const timeInput = document.querySelector('input[name="oraRichiesta"]');
        if (timeInput) {
            timeInput.addEventListener('change', function () {
                if (this.value < "08:00" || this.value > "20:00") {
                    alert("Spiacenti, i nostri professionisti lavorano dalle 08:00 alle 20:00.");
                    this.value = "08:00";
                }
            });
        }
    </script>
</body>
</html>
