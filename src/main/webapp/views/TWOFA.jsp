<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Verifica 2FA</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/shared.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">
<link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
</head>
<body>

    <nav class="navbar navbar-expand-lg sfondo navbar-dark bg-dark fixed-top">
    <div class="container-fluid">
        <a href="/HomeServices" class="navbar-brand d-flex align-items-center gap-2">
            <img class="logo" src="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png" alt="HomeServiceLogo">
            <img class="logo1" src="<%= request.getContextPath() %>/css/immagini/loghetto-finale2.png" alt="HomeServiceLogo">
        </a>
    </div>
</nav>

    <div class="container login-container">
        <div class="login-card shadow-lg rounded">
            <div class="text-center mb-4">
                <h2 class="fw-bold">Autenticazione</h2>
                <p class="text-secondary">Abbiamo inviato un codice all'email: <%= session.getAttribute("email") %></p>
            </div>
            <form action="" method="post">
                <label class="form-label small fw-bold">Codice</label>
                <div class="input-group">
                    <input type="text" class="form-control" name="formCodice" placeholder="">
                </div>
                <% String message = (String) request.getAttribute("messaggio");
                   if (message != null) { %>
                    <p class="text-danger mt-2"><%= message %></p>
                <% } %>
                <button type="submit" class="btn btn-login mt-2">Invia</button>
            </form>
        </div>
    </div>

    <%@ include file="/WEB-INF/includes/footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>