<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.elis.homeservices.model.Utente"%>
<%@page import="org.elis.homeservices.model.enums.Ruolo"%>
<%
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
    boolean isPro = utenteLoggato != null && utenteLoggato.getRuolo() != null
                    && utenteLoggato.getRuolo().name().equals("PRO");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chi Siamo – HomeServices</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/shared.css">
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
    <style>
        body {
            background-color: #f4f6fb;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        main { flex: 1 0 auto; padding-top: 80px; padding-bottom: 60px; }

       
        .hero {
            background: linear-gradient(135deg, #313D5A 0%, #416FDD 100%);
            color: white;
            border-radius: 16px;
            padding: 3.5rem 2.5rem;
            margin-bottom: 3rem;
            box-shadow: 0 6px 30px rgba(49,61,90,0.30);
            position: relative;
            overflow: hidden;
        }
        .hero::before {
            content: "";
            position: absolute;
            top: -60px; right: -60px;
            width: 280px; height: 280px;
            border-radius: 50%;
            background: rgba(255,255,255,0.06);
        }
        .hero::after {
            content: "";
            position: absolute;
            bottom: -80px; left: -40px;
            width: 220px; height: 220px;
            border-radius: 50%;
            background: rgba(255,255,255,0.04);
        }
        .hero h1 { font-weight: 800; font-size: 2.4rem; margin-bottom: 0.75rem; }
        .hero p  { font-size: 1.1rem; opacity: 0.88; max-width: 600px; margin: 0; }

        
        .value-card {
            background: white;
            border-radius: 14px;
            padding: 2rem 1.75rem;
            box-shadow: 0 2px 14px rgba(49,61,90,0.09);
            height: 100%;
            transition: transform 0.22s, box-shadow 0.22s;
        }
        .value-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 8px 28px rgba(65,111,221,0.18);
        }
        .value-card .icon-wrap {
            width: 56px; height: 56px;
            border-radius: 14px;
            background: linear-gradient(135deg, #416FDD, #313D5A);
            display: flex; align-items: center; justify-content: center;
            margin-bottom: 1.1rem;
        }
        .value-card .icon-wrap i { font-size: 1.5rem; color: white; }
        .value-card h5 { font-weight: 700; color: #313D5A; margin-bottom: 0.5rem; }
        .value-card p  { color: #6c757d; font-size: 0.93rem; margin: 0; }

        
        .team-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: center; 
            gap: 1.5rem;
        }

        .team-card {
            background: white;
            border-radius: 14px;
            padding: 1.5rem;
            box-shadow: 0 2px 14px rgba(49,61,90,0.09);
            text-align: center;
            transition: transform 0.22s;
            width: 200px; 
        }
        .team-card:hover { transform: translateY(-4px); }
        .team-img-container {
            width: 100px; 
            height: 100px;
            margin: 0 auto 1rem;
            overflow: hidden;
            border-radius: 50%;
            border: 3px solid #f4f6fb;
            box-shadow: 0 4px 10px rgba(65,111,221,0.2);
        }

        .team-img-container img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .team-card h6 { 
            font-weight: 700; 
            color: #313D5A; 
            margin-bottom: 0.2rem; 
            font-size: 0.95rem; 
        }
        .team-avatar {
            width: 80px; height: 80px;
            border-radius: 50%;
            background: linear-gradient(135deg, #416FDD, #313D5A);
            display: flex; align-items: center; justify-content: center;
            margin: 0 auto 1rem;
        }
        .team-avatar i { font-size: 2rem; color: white; }
        .team-card h6 { font-weight: 700; color: #313D5A; margin-bottom: 0.2rem; }
        .team-card small { color: #416FDD; font-weight: 600; }
        .team-card p { font-size: 0.88rem; color: #6c757d; margin-top: 0.5rem; margin-bottom: 0; }

    
        .mission-banner {
            background: linear-gradient(135deg, #313D5A 0%, #416FDD 100%);
            border-radius: 14px;
            color: white;
            padding: 2.5rem 2rem;
            text-align: center;
            box-shadow: 0 4px 20px rgba(49,61,90,0.25);
        }
        .mission-banner h3 { font-weight: 700; margin-bottom: 0.75rem; }
        .mission-banner p  { opacity: 0.88; margin: 0 auto; max-width: 560px; }

  
        .section-title { font-weight: 800; color: #313D5A; margin-bottom: 0.3rem; }
        .section-sub   { color: #6c757d; margin-bottom: 2rem; }
        

      
        .stat-box {
            background: white;
            border-radius: 12px;
            padding: 1.5rem 1rem;
            box-shadow: 0 2px 12px rgba(49,61,90,0.08);
            text-align: center;
        }
        .stat-box .num { font-size: 2rem; font-weight: 800; color: #416FDD; }
        .stat-box .lbl { font-size: 0.88rem; color: #6c757d; margin-top: 0.2rem; }
    </style>
</head>
<body>

 
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top sfondo">
        <div class="container-fluid">
            <a href="<%= request.getContextPath() %>/" class="navbar-brand">
                <img class="logo" src="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png" alt="HomeServices Logo">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navMenu" aria-controls="navMenu" aria-expanded="false" aria-label="Menu">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navMenu">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/list-pro">Professionisti</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="<%= request.getContextPath() %>/chi-siamo">Chi Siamo</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/FAQ">FAQ</a>
                    </li>
                </ul>
                <div class="d-flex align-items-center gap-2 flex-wrap">
                    <% if (utenteLoggato != null) { %>
                        <% if (!isPro) { %>
                            <a href="<%= request.getContextPath() %>/user-to-pro" class="btn btn-outline-light btn-sm">Diventa Pro</a>
                        <% } else { %>
                            <a href="<%= request.getContextPath() %>/pro-home" class="btn btn-sm"
                               style="background:#416FDD; color:white; border:none;">Pro Home</a>
                        <% } %>
                        <a href="<%= request.getContextPath() %>/logout" class="btn btn-outline-danger btn-sm">Logout</a>
                    <% } else { %>
                        <a href="<%= request.getContextPath() %>/login" class="btn btn-outline-light btn-sm">Accedi</a>
                        <a href="<%= request.getContextPath() %>/register" class="btn btn-primary btn-sm">Registrati</a>
                    <% } %>
                </div>
            </div>
        </div>
    </nav>

    <main class="container">

    
        <div class="hero">
            <h1><i class="bi bi-house-heart-fill me-2"></i>Chi Siamo</h1>
            <p>HomeServices nasce dalla volontà di semplificare il modo in cui le persone trovano professionisti
               affidabili per la propria casa. Connecting people, one service at a time.</p>
        </div>

  
        <div class="row g-3 mb-5">
            <div class="col-6 col-md-3">
                <div class="stat-box">
                    <div class="num">500+</div>
                    <div class="lbl">Professionisti</div>
                </div>
            </div>
            <div class="col-6 col-md-3">
                <div class="stat-box">
                    <div class="num">3.000+</div>
                    <div class="lbl">Utenti registrati</div>
                </div>
            </div>
            <div class="col-6 col-md-3">
                <div class="stat-box">
                    <div class="num">50+</div>
                    <div class="lbl">Città coperte</div>
                </div>
            </div>
            <div class="col-6 col-md-3">
                <div class="stat-box">
                    <div class="num">4.8★</div>
                    <div class="lbl">Rating medio</div>
                </div>
            </div>
        </div>

  
        <div class="row align-items-center mb-5">
            <div class="col-md-6 mb-4 mb-md-0">
                <h2 class="section-title">La nostra storia</h2>
                <p class="text-muted mb-3">
                    HomeServices è nata dall'idea semplice che trovare un idraulico di fiducia, un elettricista
                    qualificato o un giardiniere esperto non dovrebbe richiedere ore di ricerca e telefonate a vuoto.
                </p>
                <p class="text-muted mb-3">
                    Il nostro team ha costruito una piattaforma che mette in contatto direttamente clienti e
                    professionisti verificati, garantendo trasparenza sulle tariffe, disponibilità in tempo reale
                    e un sistema di recensioni autentico.
                </p>
                <p class="text-muted mb-0">
                    Ogni professionista sulla piattaforma viene valutato dal nostro team prima di essere approvato,
                    così puoi affidarti a chi sai già essere qualificato.
                </p>
            </div>
            <div class="col-md-6">
                <div class="row g-3">
                    <div class="col-6">
                        <div class="value-card">
                            <div class="icon-wrap"><i class="bi bi-shield-check"></i></div>
                            <h5>Affidabilità</h5>
                            <p>Ogni professionista è verificato e approvato prima di essere pubblicato.</p>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="value-card">
                            <div class="icon-wrap"><i class="bi bi-transparency"></i></div>
                            <h5>Trasparenza</h5>
                            <p>Tariffe chiare, profili dettagliati e recensioni reali di altri utenti.</p>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="value-card">
                            <div class="icon-wrap"><i class="bi bi-lightning-charge"></i></div>
                            <h5>Velocità</h5>
                            <p>Trova il professionista giusto e invia una richiesta in pochi clic.</p>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="value-card">
                            <div class="icon-wrap"><i class="bi bi-people"></i></div>
                            <h5>Comunità</h5>
                            <p>Una rete di persone e professionisti che si aiutano a vicenda.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

      
        <h2 class="section-title text-center">Il nostro team</h2>
        <p class="section-sub text-center">Le persone dietro HomeServices</p>
        
        <div class="team-container mb-5">
          
            <div class="team-card">
                <div class="team-avatar">
                    <i class="bi bi-person-fill"></i>
                </div>
                <h6>David De Nicola</h6>
            </div>

          
            <div class="team-card">
                <div class="team-avatar">
                    <i class="bi bi-person-fill"></i>
                </div>
                <h6>Emanuele Germano</h6>
            </div>

         
            <div class="team-card">
                <div class="team-avatar">
                    <i class="bi bi-person-fill"></i>
                </div>
                <h6>Dario Balella</h6>
            </div>

           
            <div class="team-card">
                <div class="team-avatar">
                    <i class="bi bi-person-fill"></i>
                </div>
                <h6>Cristina Perez</h6>
            </div>

        
            <div class="team-card">
                <div class="team-avatar">
                    <i class="bi bi-person-fill"></i>
                </div>
                <h6>Leonardo Di Pierro</h6>
            </div>
        </div>

     
        <div class="mission-banner">
            <h3><i class="bi bi-bullseye me-2"></i>La nostra missione</h3>
            <p>Rendere accessibile a tutti un'ampia rete di professionisti qualificati, costruendo
               fiducia attraverso la trasparenza e semplificando ogni aspetto della ricerca e della
               prenotazione di servizi per la casa.</p>
        </div>

    </main>

    <footer class="text-center py-4 mt-5" style="background:#313D5A; color:rgba(255,255,255,0.6); font-size:0.88rem;">
        &copy; 2024 HomeServices &mdash;
        <a href="<%= request.getContextPath() %>/chi-siamo" class="text-white-50 text-decoration-none">Chi Siamo</a> &middot;
        <a href="<%= request.getContextPath() %>/faq" class="text-white-50 text-decoration-none">FAQ</a>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
