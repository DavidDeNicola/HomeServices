<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.elis.homeservices.model.Utente"%>
<%@page import="org.elis.homeservices.model.enums.Ruolo"%>
<% Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/shared.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/index.css">
<link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
<title>Home</title>
</head>
<body>
<%
    boolean isPro = utenteLoggato != null && utenteLoggato.getRuolo() != null
                    && utenteLoggato.getRuolo().name().equals("PRO");
%>
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
                <% if(utenteLoggato !=null ){ %>
                <div class="d-flex align-items-center gap-2 flex-wrap">
                    <% if (!isPro) { %>
                    <a href="user-to-pro" class="btn btn-outline-light btn-sm">Diventa Pro</a>
                    <% } %>
                    <a href="/HomeServices/logout" class="btn btn-outline-danger btn-sm">Logout</a>
                </div>
                <%} %>                
            </div>
        </div>
    </nav>
    
   
        	
        	
        <main class="container d-flex align-items-center justify-content-center" style="min-height: 100vh;">
    	<div class="text-center p-5 shadow-lg card-welcome rounded">
    		<h2 class="mb-4 fw-bold">Benvenuto su HomeServices</h2>
        	<p class="mb-5">Trova i migliori professionisti per la tua casa o offri i tuoi servizi.</p>
        	
        	<div class="d-grid gap-3 d-sm-flex justify-content-sm-center">
        	<%

        	if(utenteLoggato != null) { 
        		if(utenteLoggato.getRuolo().equals(Ruolo.USER)) { %>
        			<a href="user-home" class="btn btn-outline-primary btn-lg px-5 shadow-sm fw-bold">User Home</a>
        		<% } else if(utenteLoggato.getRuolo().equals(Ruolo.PRO)) { %>
        			<a href="user-home" class="btn btn-outline-primary btn-lg px-5 shadow-sm fw-bold">User Home</a>
        			<a href="pro-home" class="btn btn-outline-primary btn-lg px-5 shadow-sm fw-bold">Pro Home</a>  
        		<% } else if(utenteLoggato.getRuolo().equals(Ruolo.ADMIN)) { %>
        			<a href="admin-home" class="btn btn-outline-primary btn-lg px-5 shadow-sm fw-bold">Admin Home</a>
        		<% } %>
        		
        	<% } else { %>
        		<a href='login' class="btn btn-outline-primary btn-lg px-5 shadow-sm fw-bold">Accedi</a>
            	<a href='register' class="btn btn-outline-secondary btn-lg px-5 shadow-sm fw-bold">Registrati</a>
        	<% } %>
        	</div>
        	        	        	
    	</div>
    </main>
    	
    	
    
    
    <section class="py-4 bg-light border-top border-bottom">
    
    <div class="container pb-4">
    	<h4 class="text-center mb-4 fw-bold text-secondary">Come funziona HomeServices</h4>
    	<div class="row text-center">
            
            <div class="col-md-4 mb-3 mb-md-0">
                <div class="d-flex flex-column align-items-center">
                    <div class="text-primary mb-2">
                    
            			<div class="rounded-circle bg-primary text-white d-inline-flex align-items-center justify-content-center mb-3" style="width: 40px; height: 40px;">1</div>
            			<h4>Descrivi il lavoro</h4>
            			<p class="text-muted">Dicci di cosa hai bisogno e dove ti trovi.</p>
        			</div>
                </div>
            </div>
            
        	<div class="col-md-4 mb-3 mb-md-0">
                <div class="d-flex flex-column align-items-center">
                    <div class="text-primary mb-2">
                    
            			<div class="rounded-circle bg-primary text-white d-inline-flex align-items-center justify-content-center mb-3" style="width: 40px; height: 40px;">2</div>
            			<h4>Scegli il professionista</h4>
            			<p class="text-muted">Confronta profili, prezzi e recensioni reali.</p>
        			</div>
                </div>
            </div>
        	
        	<div class="col-md-4 mb-3 mb-md-0">
                <div class="d-flex flex-column align-items-center">
                    <div class="text-primary mb-2">
                    
            			<div class="rounded-circle bg-primary text-white d-inline-flex align-items-center justify-content-center mb-3" style="width: 40px; height: 40px;">3</div>
            			<h4>Prenota e Rilassati</h4>
            			<p class="text-muted">Il professionista arriva e risolve il tuo problema.</p>
        			</div>
                </div>
            </div>
    	</div>
    </div>
    
    
    
    <div class="container mt-5">
        <h4 class="text-center mb-4 fw-bold text-secondary">La tua sicurezza, la nostra priorità</h4>
        
        <div class="row text-center">
            <div class="col-md-4 mb-3 mb-md-0">
                <div class="d-flex flex-column align-items-center">
                    <div class="text-primary mb-2">
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-patch-check-fill" viewBox="0 0 16 16">
                          <path d="M10.067.87a2.89 2.89 0 0 0-4.134 0l-.622.638-.89-.011a2.89 2.89 0 0 0-2.924 2.924l.01.89-.636.622a2.89 2.89 0 0 0 0 4.134l.637.622-.011.89a2.89 2.89 0 0 0 2.924 2.924l.89-.01.622.636a2.89 2.89 0 0 0 4.134 0l.622-.637.89.011a2.89 2.89 0 0 0 2.924-2.924l-.01-.89.636-.622a2.89 2.89 0 0 0 0-4.134l-.637-.622.011-.89a2.89 2.89 0 0 0-2.924-2.924l-.89.01-.622-.636zM12.5 8a4.5 4.5 0 1 1-9 0 4.5 4.5 0 0 1 9 0zm-5.904 2.302l1.837-3.123a.5.5 0 1 1 .86.506l-2.144 3.644a.5.5 0 0 1-.746.127l-1.331-1.11a.5.5 0 1 1 .638-.77l.886.726z"/>
                        </svg>
                    </div>
                    <h6 class="fw-bold mb-1">Certificati</h6>
                    <p class="small text-muted mb-0">Tasker verificati uno ad uno.</p>
                </div>
            </div>

            <div class="col-md-4 mb-3 mb-md-0">
                <div class="d-flex flex-column align-items-center">
                    <div class="text-primary mb-2">
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-chat-quote-fill" viewBox="0 0 16 16">
                          <path d="M16 8c0 3.866-3.582 7-8 7a9.06 9.06 0 0 1-2.347-.306c-.584.296-1.925.864-4.181 1.234-.2.032-.352-.176-.273-.362.354-.836.674-1.95.77-2.966C.744 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7zM7.194 6.766a.5.5 0 0 0-.227.272l-.739 2.455a.5.5 0 0 0 .96.29l.738-2.455a.5.5 0 0 0-.732-.562zm3.194 0a.5.5 0 0 0-.227.272l-.739 2.455a.5.5 0 0 0 .96.29l.738-2.455a.5.5 0 0 0-.732-.562z"/>
                        </svg>
                    </div>
                    <h6 class="fw-bold mb-1">Affidabili</h6>
                    <p class="small text-muted mb-0">Oltre 500 recensioni a 5 stelle.</p>
                </div>
            </div>

            <div class="col-md-4">
                <div class="d-flex flex-column align-items-center">
                    <div class="text-primary mb-2">
                        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-credit-card-2-back-fill" viewBox="0 0 16 16">
                          <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v5H0V4zm11.5 1a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h2a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-2zM0 11v1a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-1H0z"/>
                        </svg>
                    </div>
                    <h6 class="fw-bold mb-1">Garantiti</h6>
                    <p class="small text-muted mb-0">Pagamenti sicuri e tracciati.</p>
                </div>
            </div>
        </div>
    </div>
</section>
    
    <%@ include file="/WEB-INF/includes/footer.jsp" %>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>