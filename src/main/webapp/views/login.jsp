<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/shared.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">
<link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
</head>
<body class="sfondo">
	<%String message = (String) request.getAttribute("message"); %>
	<%String message2FA = (String) request.getAttribute("message2FA"); %>
	<%String warningLogin = (String) request.getAttribute("warningLogin"); %>

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



	<div class="container login-container">
		<div class="login-card shadow-lg rounded">
			<div class="text-center mb-4">
				<h2 class="fw-bold">Login</h2>
				<%if(warningLogin!=null){ %>
					<h5 style="color: red"><%=warningLogin %></h5>
				<%} %>
				<p class="text-secondary">Inserisci le tue credenziali per accedere</p>
			</div>

			<form action="<%= request.getContextPath() %>/login" method="post">
				<div class="mb-3">
					<label class="form-label small fw-bold">Email</label>
					<div class="input-group">
						<span class="input-group-text bg-white border-end-0"><i class="bi bi-envelope text-muted"></i></span>
						<input type="email" class="form-control" name="email" placeholder="Inserisci la tua email">
					</div>
				</div>
				
				<div class="mb-3">		
					<label class="form-label small fw-bold">Password</label>
					<div class="input-group">
						<span class="input-group-text bg-white border-end-0"><i class="bi bi-lock text-muted"></i></span>
						<input type="password" id="pass" class="form-control" name="password" placeholder="Inserisci la tua password">
						<button class="input-group-text bg-white" type="button" id="togglePassword">
							<i class="bi bi-eye" id="eyeIcon"></i>
						</button>
					</div>
				</div>

				<div class="d-grid mb-3">
					<button type="submit" class="btn btn-login">Accedi</button>
				</div>

				<div class="text-center mb-2">
					<a href="<%= request.getContextPath() %>/register" class="text-secondary">Non hai un account? Registrati</a>
				</div>
				
			</form>
			<div class="text-center mb-4">
			<%if(message!=null){ %>
				<p style="color: red"><%=message %></p>
			<%} %>
			
			<%if(message2FA!=null){ %>
				<p style="color: green"><%=message2FA %></p>
			<%} %>
			</div>
		</div>
	</div>
	
	<%@ include file="/WEB-INF/includes/footer.jsp" %>
	
	<script src="<%= request.getContextPath() %>/js/login.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>