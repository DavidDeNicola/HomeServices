<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="org.elis.homeservices.model.Utente"%>
<%@ page import="org.elis.homeservices.model.Veicolo"%>
<%@ page import="org.elis.homeservices.model.Immagine"%>
<%@ page import="org.elis.homeservices.model.enums.TipoImmagine"%>
<%
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

    @SuppressWarnings("unchecked")
    List<Veicolo> listaVeicoli = (List<Veicolo>) request.getAttribute("listaVeicoliUtente");

    String successo = (String) request.getAttribute("successo");
    String errore   = (String) request.getAttribute("errore");

    // Foto profilo attiva (se presente nella sessione)
    String fotoProfilo = (String) session.getAttribute("fotoProfilo");
    if (fotoProfilo == null && utenteLoggato.getImmagini() != null) {
        for (Immagine img : utenteLoggato.getImmagini()) {
            if (Boolean.TRUE.equals(img.getIsFotoProfilo())) {
                fotoProfilo = img.getPercorso();
                break;
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Carica Immagini – HomeServices</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/shared.css">
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png">
    <style>
        body {
            background-image: url("<%= request.getContextPath() %>/css/immagini/sfondoIndex.webp");
            background-size: cover;
            background-position: center top;
            background-attachment: fixed;
            background-repeat: no-repeat;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        main { flex: 1 0 auto; padding-top: 90px; padding-bottom: 50px; }

        /* Hero */
        .hero {
            background: linear-gradient(135deg, #313D5A 0%, #416FDD 100%);
            color: white;
            border-radius: 14px;
            padding: 1.75rem 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 4px 20px rgba(49,61,90,0.35);
        }
        .hero h2 { font-weight: 700; margin: 0; font-size: 1.5rem; }
        .hero p  { margin: 0.2rem 0 0; opacity: 0.85; font-size: 0.93rem; }

        /* Cards upload */
        .upload-card {
            background: rgba(255,255,255,0.94);
            border-radius: 14px;
            padding: 1.75rem;
            box-shadow: 0 2px 16px rgba(49,61,90,0.10);
            height: 100%;
        }
        .upload-card .card-icon {
            width: 52px; height: 52px;
            border-radius: 12px;
            background: linear-gradient(135deg, #416FDD, #313D5A);
            display: flex; align-items: center; justify-content: center;
            margin-bottom: 1rem;
        }
        .upload-card .card-icon i { font-size: 1.4rem; color: white; }
        .upload-card h5 { font-weight: 700; color: #313D5A; margin-bottom: 0.3rem; }
        .upload-card p.sub { color: #6c757d; font-size: 0.88rem; margin-bottom: 1.25rem; }

        /* Drop zone */
        .drop-zone {
            border: 2px dashed #bdc8e2;
            border-radius: 10px;
            padding: 1.5rem;
            text-align: center;
            cursor: pointer;
            transition: border-color 0.2s, background 0.2s;
            background: #f8faff;
            margin-bottom: 1rem;
        }
        .drop-zone:hover, .drop-zone.over {
            border-color: #416FDD;
            background: #eef2ff;
        }
        .drop-zone i  { font-size: 2rem; color: #416FDD; display: block; margin-bottom: 0.4rem; }
        .drop-zone span { font-size: 0.88rem; color: #6c757d; }
        .drop-zone input[type=file] { display: none; }

        /* Preview */
        .preview-img {
            width: 100%; max-height: 140px;
            object-fit: cover;
            border-radius: 8px;
            margin-bottom: 0.75rem;
            display: none;
        }

        /* Foto profilo attuale */
        .current-avatar {
            width: 80px; height: 80px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #416FDD;
        }
        .current-avatar-placeholder {
            width: 80px; height: 80px;
            border-radius: 50%;
            background: linear-gradient(135deg, #416FDD, #313D5A);
            display: flex; align-items: center; justify-content: center;
        }
        .current-avatar-placeholder i { font-size: 2rem; color: white; }

        /* Bottone upload */
        .btn-upload {
            background: linear-gradient(135deg, #416FDD, #313D5A);
            color: white;
            border: none;
            border-radius: 8px;
            padding: 0.55rem 1.4rem;
            font-weight: 600;
            font-size: 0.9rem;
            transition: opacity 0.2s;
            width: 100%;
        }
        .btn-upload:hover { opacity: 0.88; color: white; }

        /* Alert inline */
        .alert-inline {
            border-radius: 10px;
            font-size: 0.9rem;
            padding: 0.65rem 1rem;
        }

        /* Formato accettato badge */
        .fmt-badge {
            font-size: 0.75rem;
            background: #e9ecef;
            color: #495057;
            border-radius: 4px;
            padding: 2px 6px;
            margin-right: 3px;
        }

        /* Back link */
        .back-link {
            font-size: 0.88rem;
            color: rgba(255,255,255,0.8);
            text-decoration: none;
        }
        .back-link:hover { color: white; }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top sfondo">
    <div class="container-fluid">
        <a href="<%= request.getContextPath() %>/" class="navbar-brand">
            <img class="logo" src="<%= request.getContextPath() %>/css/immagini/logogrande-removebg.png" alt="Logo">
        </a>
        <div class="ms-auto d-flex align-items-center gap-2">
            <a href="<%= request.getContextPath() %>/pro-home" class="btn btn-sm btn-outline-light">
                <i class="bi bi-arrow-left me-1"></i>Pro Home
            </a>
            <a href="<%= request.getContextPath() %>/logout" class="btn btn-sm btn-outline-danger">Logout</a>
        </div>
    </div>
</nav>

<main class="container">

    <!-- Hero -->
    <div class="hero">
        <h2><i class="bi bi-images me-2"></i>Gestione Immagini</h2>
        <p>Carica la tua foto profilo, le foto dei tuoi veicoli e i lavori che hai eseguito.</p>
    </div>

    <!-- Alert successo / errore -->
    <% if (successo != null) { %>
    <div class="alert alert-success alert-inline d-flex align-items-center mb-4" role="alert">
        <i class="bi bi-check-circle-fill me-2"></i> <%= successo %>
    </div>
    <% } %>
    <% if (errore != null) { %>
    <div class="alert alert-danger alert-inline d-flex align-items-center mb-4" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i> <%= errore %>
    </div>
    <% } %>

    <div class="row g-4">

        <!-- ======================== FOTO PROFILO ======================== -->
        <div class="col-md-4">
            <div class="upload-card">
                <div class="card-icon"><i class="bi bi-person-fill"></i></div>
                <h5>Foto Profilo</h5>
                <p class="sub">La tua foto visibile agli utenti che cercano un professionista.</p>

                <!-- Foto attuale -->
                <div class="d-flex align-items-center gap-3 mb-3">
                    <% if (fotoProfilo != null && !fotoProfilo.isBlank()) { %>
                    <img src="<%= request.getContextPath() %>/<%= fotoProfilo %>"
                         alt="Foto profilo" class="current-avatar">
                    <% } else { %>
                    <div class="current-avatar-placeholder">
                        <i class="bi bi-person-fill"></i>
                    </div>
                    <% } %>
                    <span class="text-muted" style="font-size:0.85rem;">Foto attuale</span>
                </div>

                <form action="<%= request.getContextPath() %>/upload-immagini"
                      method="POST" enctype="multipart/form-data" id="formProfilo">
                    <input type="hidden" name="tipo" value="PROFILO">

                    <div class="drop-zone" id="dzProfilo"
                         onclick="document.getElementById('fileProfilo').click()">
                        <i class="bi bi-cloud-arrow-up"></i>
                        <span>Clicca o trascina qui la tua foto</span>
                        <input type="file" id="fileProfilo" name="immagine"
                               accept="image/jpeg,image/png,image/webp"
                               onchange="preview(this,'prevProfilo','dzProfilo')">
                    </div>
                    <img id="prevProfilo" class="preview-img" alt="Anteprima">

                    <div class="mb-2">
                        <span class="fmt-badge">JPG</span>
                        <span class="fmt-badge">PNG</span>
                        <span class="fmt-badge">WEBP</span>
                        <span class="text-muted" style="font-size:0.78rem;">max 5 MB</span>
                    </div>

                    <button type="submit" class="btn btn-upload">
                        <i class="bi bi-upload me-1"></i>Carica foto profilo
                    </button>
                </form>
            </div>
        </div>

        <!-- ======================== FOTO VEICOLO ======================== -->
        <div class="col-md-4">
            <div class="upload-card">
                <div class="card-icon"><i class="bi bi-car-front-fill"></i></div>
                <h5>Foto Veicolo</h5>
                <p class="sub">Mostra il veicolo che usi per i lavori. Seleziona il veicolo e carica una foto.</p>

                <form action="<%= request.getContextPath() %>/upload-immagini"
                      method="POST" enctype="multipart/form-data" id="formVeicolo">
                    <input type="hidden" name="tipo" value="VEICOLO">

                    <!-- Select veicolo -->
                    <div class="mb-3">
                        <label for="idVeicolo" class="form-label fw-semibold" style="font-size:0.88rem;">
                            Veicolo da fotografare
                        </label>
                        <select class="form-select form-select-sm" name="idVeicolo" id="idVeicolo" required>
                            <option value="" disabled selected>-- Seleziona veicolo --</option>
                            <% if (listaVeicoli != null) {
                                for (Veicolo v : listaVeicoli) { %>
                            <option value="<%= v.getId() %>"><%= v.getNome() %></option>
                            <%  }
                            } %>
                        </select>
                        <% if (listaVeicoli == null || listaVeicoli.isEmpty()) { %>
                        <div class="form-text text-warning">
                            <i class="bi bi-exclamation-triangle me-1"></i>
                            Nessun veicolo associato al tuo profilo.
                            <a href="<%= request.getContextPath() %>/pro-home">Aggiungine uno</a>.
                        </div>
                        <% } %>
                    </div>

                    <div class="drop-zone" id="dzVeicolo"
                         onclick="document.getElementById('fileVeicolo').click()">
                        <i class="bi bi-cloud-arrow-up"></i>
                        <span>Clicca o trascina qui la foto</span>
                        <input type="file" id="fileVeicolo" name="immagine"
                               accept="image/jpeg,image/png,image/webp"
                               onchange="preview(this,'prevVeicolo','dzVeicolo')">
                    </div>
                    <img id="prevVeicolo" class="preview-img" alt="Anteprima">

                    <div class="mb-2">
                        <span class="fmt-badge">JPG</span>
                        <span class="fmt-badge">PNG</span>
                        <span class="fmt-badge">WEBP</span>
                        <span class="text-muted" style="font-size:0.78rem;">max 5 MB</span>
                    </div>

                    <button type="submit" class="btn btn-upload"
                        <%= (listaVeicoli == null || listaVeicoli.isEmpty()) ? "disabled" : "" %>>
                        <i class="bi bi-upload me-1"></i>Carica foto veicolo
                    </button>
                </form>
            </div>
        </div>

        <!-- ======================== FOTO LAVORO ======================== -->
        <div class="col-md-4">
            <div class="upload-card">
                <div class="card-icon"><i class="bi bi-tools"></i></div>
                <h5>Foto Lavoro</h5>
                <p class="sub">Aggiungi foto dei lavori che hai eseguito per mostrare le tue competenze.</p>

                <form action="<%= request.getContextPath() %>/upload-immagini"
                      method="POST" enctype="multipart/form-data" id="formLavoro">
                    <input type="hidden" name="tipo" value="LAVORO">

                    <div class="drop-zone" id="dzLavoro"
                         onclick="document.getElementById('fileLavoro').click()">
                        <i class="bi bi-cloud-arrow-up"></i>
                        <span>Clicca o trascina qui la foto</span>
                        <input type="file" id="fileLavoro" name="immagine"
                               accept="image/jpeg,image/png,image/webp"
                               onchange="preview(this,'prevLavoro','dzLavoro')">
                    </div>
                    <img id="prevLavoro" class="preview-img" alt="Anteprima">

                    <div class="mb-2">
                        <span class="fmt-badge">JPG</span>
                        <span class="fmt-badge">PNG</span>
                        <span class="fmt-badge">WEBP</span>
                        <span class="text-muted" style="font-size:0.78rem;">max 5 MB</span>
                    </div>

                    <button type="submit" class="btn btn-upload">
                        <i class="bi bi-upload me-1"></i>Carica foto lavoro
                    </button>
                </form>
            </div>
        </div>
    </div><!-- /row -->

    <!-- ======================== GALLERIA IMMAGINI CARICATE ======================== -->
    <%
    List<Immagine> tutteImmagini = utenteLoggato.getImmagini();
    boolean haImmagini = tutteImmagini != null && !tutteImmagini.isEmpty();
    %>
    <% if (haImmagini) { %>
    <div class="mt-5">
        <h5 class="fw-bold text-white mb-3"><i class="bi bi-grid me-2"></i>Le tue immagini caricate</h5>

        <!-- Profilo -->
        <%
        boolean haFotoProfilo = false;
        for (Immagine img : tutteImmagini) {
            if (img.getTipo() != null && img.getTipo().name().equals("PROFILO")) { haFotoProfilo = true; break; }
        }
        %>
        <% if (haFotoProfilo) { %>
        <p class="text-white-50 small mb-2">Foto profilo</p>
        <div class="row g-2 mb-4">
            <% for (Immagine img : tutteImmagini) {
                if (img.getTipo() == null || !img.getTipo().name().equals("PROFILO")) continue; %>
            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                <div class="position-relative">
                    <img src="<%= request.getContextPath() %>/<%= img.getPercorso() %>"
                         class="img-thumbnail w-100" style="height:110px; object-fit:cover; border-radius:8px;"
                         alt="Foto profilo">
                    <% if (Boolean.TRUE.equals(img.getIsFotoProfilo())) { %>
                    <span class="position-absolute top-0 start-0 badge bg-primary m-1" style="font-size:0.65rem;">Attiva</span>
                    <% } %>
                </div>
            </div>
            <% } %>
        </div>
        <% } %>

        <!-- Veicoli -->
        <%
        boolean haFotoVeicolo = false;
        for (Immagine img : tutteImmagini) {
            if (img.getTipo() != null && img.getTipo().name().equals("VEICOLO")) { haFotoVeicolo = true; break; }
        }
        %>
        <% if (haFotoVeicolo) { %>
        <p class="text-white-50 small mb-2">Foto veicoli</p>
        <div class="row g-2 mb-4">
            <% for (Immagine img : tutteImmagini) {
                if (img.getTipo() == null || !img.getTipo().name().equals("VEICOLO")) continue; %>
            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                <img src="<%= request.getContextPath() %>/<%= img.getPercorso() %>"
                     class="img-thumbnail w-100" style="height:110px; object-fit:cover; border-radius:8px;"
                     alt="Foto veicolo">
                <% if (img.getVeicolo() != null) { %>
                <div class="text-white-50 text-center" style="font-size:0.75rem; margin-top:3px;">
                    <%= img.getVeicolo().getNome() %>
                </div>
                <% } %>
            </div>
            <% } %>
        </div>
        <% } %>

        <!-- Lavori -->
        <%
        boolean haFotoLavoro = false;
        for (Immagine img : tutteImmagini) {
            if (img.getTipo() != null && img.getTipo().name().equals("LAVORO")) { haFotoLavoro = true; break; }
        }
        %>
        <% if (haFotoLavoro) { %>
        <p class="text-white-50 small mb-2">Foto lavori</p>
        <div class="row g-2">
            <% for (Immagine img : tutteImmagini) {
                if (img.getTipo() == null || !img.getTipo().name().equals("LAVORO")) continue; %>
            <div class="col-6 col-sm-4 col-md-3 col-lg-2">
                <img src="<%= request.getContextPath() %>/<%= img.getPercorso() %>"
                     class="img-thumbnail w-100" style="height:110px; object-fit:cover; border-radius:8px;"
                     alt="Foto lavoro">
            </div>
            <% } %>
        </div>
        <% } %>
    </div>
    <% } %>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Anteprima immagine selezionata nella drop zone
    function preview(input, previewId, dzId) {
        const file = input.files[0];
        const prev = document.getElementById(previewId);
        const dz   = document.getElementById(dzId);
        if (!file) { prev.style.display = 'none'; return; }
        const reader = new FileReader();
        reader.onload = e => {
            prev.src = e.target.result;
            prev.style.display = 'block';
            dz.style.borderColor = '#416FDD';
        };
        reader.readAsDataURL(file);
    }

    // Drag & drop per ogni drop zone
    document.querySelectorAll('.drop-zone').forEach(dz => {
        dz.addEventListener('dragover', e => { e.preventDefault(); dz.classList.add('over'); });
        dz.addEventListener('dragleave', ()  => dz.classList.remove('over'));
        dz.addEventListener('drop', e => {
            e.preventDefault();
            dz.classList.remove('over');
            const fileInput = dz.querySelector('input[type=file]');
            if (e.dataTransfer.files.length) {
                fileInput.files = e.dataTransfer.files;
                fileInput.dispatchEvent(new Event('change'));
            }
        });
    });
</script>
</body>
</html>
