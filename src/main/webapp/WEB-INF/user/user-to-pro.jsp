<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.elis.homeservices.model.Citta"%>
<%@page import="org.elis.homeservices.model.Professione"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Diventa Professionista</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/navbar.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/shared.css">
	<link rel="stylesheet" href="<%= request.getContextPath() %>/css/index.css">
	<link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
</head>
<body>
	<%
	@SuppressWarnings("unchecked")
	List<Citta> listCitta = (List<Citta>) request.getAttribute("listCitta");
	@SuppressWarnings("unchecked")
	List<Professione> listProfessioni = (List<Professione>) request.getAttribute("listProfessioni");
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
            			<a class="nav-link" href="user-home">Indietro</a>
        			</li>
        			<li class="nav-item">
            			<a class="nav-link" href="list-pro">Professionisti</a>
        			</li>
    			</ul>

    			<a class="btn btn-outline-light" href="user-to-pro">Diventa Pro</a>
			</div>
            <a href="/HomeServices/logout" class="btn btn-outline-danger ms-2">Logout</a>
        </div>
    </nav>

   <main class="container mt-5 p-5">
    <div class="card p-4 shadow">
        <h2 class="mb-4">Completa il tuo profilo per diventare un Professionista</h2>
        
        <form action='user-to-pro' method='post'>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Tariffa Oraria (€)</label>
                    <input type='number' min="0.01" max="9999.99" name='rateFormInput' class="form-control" step="0.01" required>
                </div>
                
                <div class="col-md-6 mb-3">
                    <label class="form-label">Codice Fiscale</label>
                    <input type='text' name='cfFormInput' class="form-control" maxlength="16" required>
                </div>
            </div>

            <div class="mb-3">
                <input type="hidden" name="action" value="selectCitta">
				<select name="idCitta" class="form-select mb-2" required>
					<option value="" selected disabled>Citta di lavoro</option>
					<% for (Citta c : listCitta) { %>
						<option value="<%=c.getId()%>"><%=c.getNome()%></option>
					<% } %>
				</select>
            </div>

            <div class="mb-3">
                <input type="hidden" name="action" value="selectProfessione">
				<select name="idProfessione" class="form-select mb-2" required>
					<option value="" selected disabled>Tua Professione principale</option>
					<% for (Professione p : listProfessioni) { %>
						<option value="<%=p.getId()%>"><%=p.getNome()%></option>
					<% } %>
				</select>
            </div>

            <button type="submit" class="btn btn-warning w-100 fw-bold">Invia</button>
        </form>

        <% String message = (String)request.getAttribute("message");
           if(message != null) { %>
            <div class="alert alert-info mt-3"><%= message %></div>
        <% } %>
    </div>
</main>
</body>
</html>