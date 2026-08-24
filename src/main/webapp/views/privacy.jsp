<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Privacy</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/scopri.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
</head>
<body>
<nav class="navbar navbar-expand-lg sfondo navbar-dark bg-dark">
        <div class="container-fluid">
        	<a href="/HomeServices" class="navbar-brand">
            	<img class="logo" src="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png" alt="HomeServiceLogo">
            </a>
            <a href="/HomeServices" class="navbar-brand">
            	<img class="logo1" src="<%= request.getContextPath() %>/css/immagini/loghetto-removebg.png" alt="HomeServiceLogo">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
                aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                    	<a class="nav-link text-dark" href="/HomeServices">Indietro</a>
                    </li>
                </ul>
                
            </div>
        </div>
    </nav>
<main class="container py-5">
    <div class="bg-white p-5 shadow-sm rounded">
        <h2 class="fw-bold mb-4">Informativa sulla Privacy e Cookie</h2>
        <h4>1. Raccolta dei Dati</h4>
        <p>Raccogliamo i dati necessari per fornirti i nostri servizi domestici, come nome, indirizzo e-mail e posizione.</p>
        <h4>2. Utilizzo dei Cookie</h4>
        <p>Utilizziamo cookie tecnici per migliorare la tua esperienza di navigazione sul portale HomeServices.</p>
        </div>
</main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>