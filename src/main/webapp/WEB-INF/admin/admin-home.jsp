<%@page import="org.elis.homeservices.model.Professione"%>
<%@page import="org.elis.homeservices.model.Citta"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Home</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<link rel="stylesheet" type="text/css"
	href="<%= request.getContextPath() %>/css/admin-home.css">
<link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
</head>
<body>
	<%
	@SuppressWarnings("unchecked")
	List<Citta> listCitta = (List<Citta>) request.getAttribute("listCitta");
	@SuppressWarnings("unchecked")
	List<Professione> listProfessioni = (List<Professione>) request.getAttribute("listProfessioni");
	%>
	<!--Navigation bar-->
	<nav class="navbar navbar-expand-lg sfondo navbar-dark bg-dark">
		<div class="container-fluid">
			<a class="navbar-brand" href="#">HomeServices<span
				class="badge bg-primary ms-2" style="font-size: 0.6rem;">ADMIN</span></a>
			<div class="ms-auto">
				<a href="/HomeServices/logout" class="btn btn-sm btn-danger">Logout</a>
			</div>
		</div>
	</nav>

	<!-- Pagina principale -->
	<main class="container sfondo2 my-5 max-width-900 pb-1">
		<div class="mb-5">
			<h2 class="fw-bold">Pannello Amministrativo</h2>
			<p class="text-secondary">Gestione contenuti e sicurezza del
				portale</p>
		</div>

		<div class="list-group shadow-sm mb-4">
			<div class="list-group-item bg-light fw-bold small text-uppercase">
				Approvazioni e Segnalazioni 
				<a href="<%= request.getContextPath() %>/admin-form-pro" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center py-3">
				Richieste diventare Professionista <i class="bi bi-chevron-right"></i>
				</a> 
				<a href="<%= request.getContextPath() %>/admin-report" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center py-3">
					Segnalazioni e Report Utenti <i class="bi bi-chevron-right"></i>
				</a>
			</div>
		</div>

		<div class="list-group shadow-sm rounded">
			<div class="card shadow-sm mb-3 border-0">
				<div class="card-header bg-light fw-bold small text-uppercase">
					Gestione Città</div>
				<div class="card-body">
					<div class="row">
						<div class="col-md-6 border-end">
							<h5 class="fw-bold">Aggiungi una nuova città</h5>
							<form action="" method="post">
								<input type="hidden" name="action" value="addCitta">
								<div class="form-floating">
									<input type="text" name="nomeCitta" id="floatingInput"
										class="form-control mb-2"
										placeholder="Inserisci città da aggiungere"> <label
										for="floatingInput">Inserisci nome città</label>
								</div>
								<button type="submit" class="btn btn-success">Aggiungi</button>
								<%
								String message = (String) request.getAttribute("message");
								if (message != null) {
								%>
								<p><%=message%></p>
								<%
								}
								%>
							</form>
						</div>
						<div class="col-md-6">
							<h5 class="fw-bold">Modifica una città esistente</h5>
							<form action="" method="post">
								<input type="hidden" name="action" value="editCitta">
								<select name="idCitta" class="form-select mb-2" required>
									<option value="" selected disabled>Seleziona città da
										modificare</option>
									<%
									for (Citta c : listCitta) {
									%>
									
										<option value="<%=c.getId()%>"><%=c.getNome()%></option>
									<%
										}
									%>
								</select>
								<input type="text" name="CittaName" required>
								<button type="submit" class="btn btn-danger">Modifica</button>
								<%
								String updateMessage = (String) request.getAttribute("updateMessage");
								if (updateMessage != null) {
								%>
								<p><%=updateMessage%></p>
								<%
								}
								%>
							</form>
						</div>
					</div>
				</div>
			</div>

			<div class="card shadow-sm mb-3 border-0">
				<div class="card-header bg-light fw-bold small text-uppercase">
					Gestione Professioni</div>
				<div class="card-body">
					<div class="row">
						<div class="col-md-6 border-end">
							<h5 class="fw-bold">Aggiungi una nuova professione</h5>
							<form action="" method="post">
								<input type="hidden" name="action" value="addProfessione">
								<div class="form-floating">
									<input type="text" name="nomeProfessione" id="floatingInput"
										class="form-control mb-2"
										placeholder="Inserisci professione da aggiungere"> <label
										for="floatingInput">Inserisci nome professione</label>
								</div>
								<button type="submit" class="btn btn-success">Aggiungi</button>
								<%
								String messageProfessione = (String) request.getAttribute("messageProfessione");
								if (messageProfessione != null) {
								%>
								<p><%=messageProfessione%></p>
								<%
								}
								%>
							</form>
						</div>
						<div class="col-md-6">
							<h5 class="fw-bold">Rimuovi una professione</h5>
							<form action="" method="post">
								<input type="hidden" name="action" value="removeProfessione">
								<select name="idProfessione" class="form-select mb-2" required>
									<option value="" selected disabled>Seleziona
										professione da rimuovere</option>
									<%
									for (Professione p : listProfessioni) {
									%>
									<option value="<%=p.getId()%>"><%=p.getNome()%></option>
									<%
									}
									%>
								</select>
								<button type="submit" class="btn btn-danger">Rimuovi</button>
								<%
								String removePro = (String) request.getAttribute("removePro");
								if (removePro != null) {
								%>
								<p><%=removePro%></p>
								<%
								}
								%>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</main>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>