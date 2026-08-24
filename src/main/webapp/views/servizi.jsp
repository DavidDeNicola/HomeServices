<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Servizi</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/shared.css">
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/scopri.css">
<link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
</head>
<body>

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
                
                
            </div>
        </div>
    </nav>
    
<main class="container d-flex flex-column align-items-center justify-content-top">
    <h2 class="text-center mb-5 fw-bold">I Nostri Servizi</h2>
    <div class="row g-4">
        <div class="col-md-4">
            <div class="card h-100 shadow-sm border-0">
                <div class="card-body text-center p-4">
                    <div class="fs-1 text-primary mb-3">🚽​</div>
                    <h5 class="card-title fw-bold">Idraulico</h5>
                    <p class="card-text text-muted">Dalla riparazione di una perdita alla ristrutturazione completa del bagno.</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 shadow-sm border-0">
                <div class="card-body text-center p-4">
                    <div class="fs-1 text-primary mb-3">⚡​</div>
                    <h5 class="card-title fw-bold">Servizi Elettrici Professionali</h5>
                    <p class="card-text text-muted">Installazione, manutenzione e messa a norma impianti.</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100 shadow-sm border-0">
                <div class="card-body text-center p-4">
                    <div class="fs-1 text-primary mb-3">🧱​</div>
                    <h5 class="card-title fw-bold">Opere Edili e Ristrutturazioni</h5>
                    <p class="card-text text-muted">Piccoli interventi murari o rifacimento interni.</p>
                </div>
            </div>
        </div>
    </div>
</main>

	<%@ include file="/WEB-INF/includes/footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>