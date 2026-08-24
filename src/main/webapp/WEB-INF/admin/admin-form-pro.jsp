<%@page import="org.elis.homeservices.model.Utente"%>
<%@page import="org.elis.homeservices.model.FormPro"%>
<%@page import="org.elis.homeservices.model.Professione"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin - Richieste Professionisti</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/admin-home.css">
<link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
</head>
<body>

	<!-- Navigation bar -->
	<nav class="navbar navbar-expand-lg sfondo navbar-dark bg-dark">
		<div class="container-fluid">
			<a class="navbar-brand" href="#">HomeServices
				<span class="badge bg-primary ms-2" style="font-size: 0.6rem;">ADMIN</span>
			</a>
			<ul class="navbar-nav me-auto mb-2 mb-lg-0">
				<li class="nav-item">
					<a class="nav-link" href="<%= request.getContextPath() %>/admin-home">Torna indietro</a>
				</li>
			</ul>
			<div class="ms-auto">
				<a href="<%= request.getContextPath() %>/logout" class="btn btn-sm btn-outline-danger">Logout</a>
			</div>
		</div>
	</nav>

	<!-- Pagina principale -->
	<main class="container sfondo2 my-5 max-width-900 pb-5 shadow rounded">
		<div class="mb-5">
			<h2 class="fw-bold">Richieste per diventare professionisti</h2>
			<p class="text-secondary">Valuta i profili degli utenti che desiderano diventare professionisti</p>
		</div>

		<div class="list-group">
			<div class="list-group-item bg-light fw-bold small">Candidature in attesa</div>

			<%
			@SuppressWarnings("unchecked")
			List<FormPro> forms = (List<FormPro>) request.getAttribute("listaFormPro");
			if (forms != null && !forms.isEmpty()) {
				for (FormPro fp : forms) {
					Utente u = fp.getUtente();
			%>
			<div class="list-group-item py-4 border border-dark rounded mb-2">
				<div class="row align-items-center">

					<!-- Nome e cognome utente -->
					<div class="col-md-4 mb-2">
						<h6 class="mb-0 fw-bold text-dark">
							<%= u.getNome() %> <%= u.getCognome() %>
						</h6>
						<small class="font-monospace text-dark bg-light border px-1">
							<%= u.getEmail() %>
						</small>
					</div>

					<!-- Codice Fiscale -->
					<div class="col-md-3 mb-2">
						<small class="text-uppercase d-block">Codice Fiscale:</small>
						<span class="font-monospace text-dark bg-light border px-1">
							<%= fp.getCf() %>
						</span>
					</div>

					<!-- Tariffa -->
					<div class="col-md-2 mb-2">
						<small class="text-uppercase d-block">Tariffa (€/h):</small>
						<span class="font-monospace text-dark bg-light border px-1">
							<%= fp.getTariffa() %>
						</span>
					</div>

					<!-- Professione -->
					<div class="col-md-3 mb-2">
						<small class="text-uppercase d-block">Professione:</small>
						<span class="font-monospace text-dark bg-light border px-1">
							<%= fp.getProfessione().getNome() %>
						</span>
					</div>
				</div>

				<!-- Bottoni approva / rifiuta -->
				<div class="row mt-3">
					<div class="col text-end">
						<form action="<%= request.getContextPath() %>/admin-form-pro" method="POST" class="d-inline">
							<input type="hidden" name="idForm" value="<%= fp.getId() %>">
							<button type="submit" name="azione" value="approva"
								class="btn btn-success px-3 me-1">
								<i class="bi bi-check-lg"></i> Approva
							</button>
							<button type="submit" name="azione" value="rifiuta"
								class="btn btn-outline-danger px-3">
								<i class="bi bi-x-lg"></i> Rifiuta
							</button>
						</form>
					</div>
				</div>
			</div>
			<% } } else { %>
			<div class="list-group-item text-center border border-dark rounded py-4">
				Non ci sono nuove candidature da visualizzare.
			</div>
			<% } %>
		</div>
	</main>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
