<%@page import="org.elis.homeservices.model.Citta"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/shared.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/login.css">
<link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
</head>
<body class="sfondo">
	
		<!--Navigation bar-->
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
                    	<a class="nav-link" href="/HomeServices/list-pro">Professionisti</a>
                    </li>
                </ul>
                
            </div>
        </div>
    </nav>
	
	
	<div class="container login-container mt-3 mb-3">
		<div class="login-card shadow-lg rounded">

			<div class="text-center mb-4">
				<h2 class="fw-bold">Registrazione</h2>
				<p class="text-secondary">Crea un nuovo account per accedere ai
					nostri servizi</p>
			</div>

			<form action="<%=request.getContextPath()%>/register" method="post">
				<div class="row">
					<div class="col-md-6 mb-3">
						<label class="form-label small fw-bold">Nome </label> <input
							type="text" class="form-control" name="nome"
							placeholder="Inserisci nome">
					</div>
					<div class="col-md-6 mb-3">
						<label class="form-label small fw-bold">Cognome </label> <input
							type="text" class="form-control" name="cognome"
							placeholder="Inserisci cognome">
					</div>
				</div>

				<div class="mb-3">
					<label class="form-label small fw-bold">Email </label>
					<div class="input-group">
						<span class="input-group-text bg-white border-end-0"><i
							class="bi bi-envelope text-muted"></i></span> <input type="email"
							class="form-control" name="email"
							placeholder="Inserisci la tua email">
					</div>
				</div>

				<div class="mb-3">
					<label class="form-label small fw-bold">Password</label>
					<div class="input-group">
						<span class="input-group-text bg-white border-end-0"><i
							class="bi bi-lock text-muted"></i></span> <input type="password"
							id="pass" class="form-control" name="password"
							placeholder="Inserisci la tua password">
						<button class="input-group-text bg-white" type="button"
							id="togglePassword">
							<i class="bi bi-eye" id="eyeIcon"></i>
						</button>
					</div>
				</div>

				<div class="mb-3">
					<label class="form-label small fw-bold">Conferma Password</label>
					<div class="input-group">
						<span class="input-group-text bg-white"><i
							class="bi bi-shield-lock text-muted"></i></span> <input type="password"
							id="confirmPasswordInput" class="form-control"
							name="confirmPasswordFormInput" placeholder="Ripeti la password"
							required>
					</div>
					<div id="passwordMatchMessage" class="small mt-1"
						style="display: none;"></div>
				</div>

				<div class="mb-3">
					<label class="form-label small fw-bold">Data di Nascita: </label> <input
						type="date" class="form-control" name="dataNascita"
						placeholder="Inserisci la tua data di nascita">
				</div>

				<div class="mb-4">
					<label class="form-label small fw-bold">Citta: </label> <select
						class="form-select" name="cityForm">
						<option value="" selected disabled>Seleziona la tua città</option>
						<%
						@SuppressWarnings("unchecked")
						List<Citta> citta = (List<Citta>) request.getAttribute("listCitta");
						if (citta != null) {
							for (Citta c : citta) {
						%>
						<option value="<%=c.getId()%>"><%=c.getNome()%></option>
						<%
						}
						}
						%>
					</select>
				</div>

				<div class="d-grid mb-3">
					<button type="submit" class="btn btn-login">Registrati</button>
				</div>

				<div class="text-center mb-2">
					<a href="<%=request.getContextPath()%>/login"
						class="text-secondary">Hai già un account? Accedi</a>
				</div>
			</form>

			<%
			String message = (String) request.getAttribute("message");
			if (message != null) {
			%>
			<div class="alert alert-info text-center"><%=message%></div>
			<%
			}
			%>
		</div>
	</div>


	<%@ include file="/WEB-INF/includes/footer.jsp" %>
	
	
	<script src="<%=request.getContextPath()%>/js/register.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>