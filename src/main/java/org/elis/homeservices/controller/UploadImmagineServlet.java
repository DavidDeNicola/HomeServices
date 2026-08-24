package org.elis.homeservices.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.ImmagineDAO;
import org.elis.homeservices.dao.definition.VeicoloDAO;
import org.elis.homeservices.exception.ImmagineGiaNelDatabaseException;
import org.elis.homeservices.exception.ImmagineNonTrovataException;
import org.elis.homeservices.model.Immagine;
import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.Veicolo;
import org.elis.homeservices.model.enums.Ruolo;
import org.elis.homeservices.model.enums.TipoImmagine;

@WebServlet("/upload-immagini")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,       // 1 MB in memoria prima di scrivere su disco
    maxFileSize       = 5 * 1024 * 1024,   // 5 MB per singolo file
    maxRequestSize    = 20 * 1024 * 1024   // 20 MB per l'intera request
)
public class UploadImmagineServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final List<String> ESTENSIONI_VALIDE = List.of("jpg", "jpeg", "png", "webp");

    private static final String BASE_DIR = "uploads";

    private ImmagineDAO immagineDao;
    private VeicoloDAO  veicoloDao;

    @Override
    public void init() throws ServletException {
        immagineDao = DaoFactory.getInstance().getImmagineDAO();
        veicoloDao  = DaoFactory.getInstance().getVeicoloDAO();
        super.init();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

        if (utenteLoggato == null || !utenteLoggato.getRuolo().equals(Ruolo.PRO)) {
            response.sendRedirect("login");
            return;
        }

        try {
            List<Veicolo> veicoli = veicoloDao.getListaVeicoliUtente(utenteLoggato.getId());
            request.setAttribute("listaVeicoliUtente", veicoli);
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/WEB-INF/pro/upload-immagini.jsp").forward(request, response);
    }
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

        if (utenteLoggato == null || !utenteLoggato.getRuolo().equals(Ruolo.PRO)) {
            response.sendRedirect("login");
            return;
        }

        String tipoStr = request.getParameter("tipo");
        TipoImmagine tipo;
        try {
            tipo = TipoImmagine.valueOf(tipoStr.toUpperCase());
        } catch (Exception e) {
            request.setAttribute("errore", "Tipo immagine non valido.");
            doGet(request, response);
            return;
        }

        Part filePart = request.getPart("immagine");
        if (filePart == null || filePart.getSize() == 0) {
            request.setAttribute("errore", "Nessun file selezionato.");
            doGet(request, response);
            return;
        }

        String nomeOriginale = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String estensione    = estensione(nomeOriginale);
        if (!ESTENSIONI_VALIDE.contains(estensione)) {
            request.setAttribute("errore", "Formato non supportato. Usa JPG, PNG o WEBP.");
            doGet(request, response);
            return;
        }

        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            request.setAttribute("errore", "Il file caricato non è un'immagine.");
            doGet(request, response);
            return;
        }

        String sottoDir;
        Veicolo veicolo = null;

        switch (tipo) {
            case PROFILO -> sottoDir = "profili";
            case LAVORO  -> sottoDir = "lavori";
            case VEICOLO -> {
                sottoDir = "veicoli";
                String idVeicoloStr = request.getParameter("idVeicolo");
                if (idVeicoloStr == null || idVeicoloStr.isBlank()) {
                    request.setAttribute("errore", "Seleziona il veicolo da fotografare.");
                    doGet(request, response);
                    return;
                }
                try {
                    veicolo = veicoloDao.findById(Long.parseLong(idVeicoloStr));
                    if (veicolo == null) {
                        request.setAttribute("errore", "Veicolo non trovato.");
                        doGet(request, response);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    request.setAttribute("errore", "ID veicolo non valido.");
                    doGet(request, response);
                    return;
                }
            }
            default -> sottoDir = "varie";
        }


        String uploadRoot = getServletContext().getRealPath("") + File.separator
                            + BASE_DIR + File.separator + sottoDir;
        File dir = new File(uploadRoot);
        if (!dir.exists()) dir.mkdirs();


        String nomeFile   = utenteLoggato.getId() + "_" + System.currentTimeMillis() + "." + estensione;
        String percorsoFs = uploadRoot + File.separator + nomeFile;
        String percorsoRel = BASE_DIR + "/" + sottoDir + "/" + nomeFile;

        try (InputStream in = filePart.getInputStream()) {
            Files.copy(in, Paths.get(percorsoFs), StandardCopyOption.REPLACE_EXISTING);
        }


        boolean isProfilo = (tipo == TipoImmagine.PROFILO);

        Immagine immagine;
        if (tipo == TipoImmagine.VEICOLO) {
            immagine = new Immagine(nomeOriginale, percorsoRel, false, tipo, veicolo, utenteLoggato);
        } else {
            immagine = new Immagine(nomeOriginale, percorsoRel, isProfilo, tipo, utenteLoggato);
        }

        try {
            immagineDao.add(immagine);

            if (isProfilo) {
                utenteLoggato.getImmagini().removeIf(
                    img -> img.getIsFotoProfilo() != null && img.getIsFotoProfilo()
                );
                session.setAttribute("fotoProfilo", percorsoRel);
            }

            if (tipo == TipoImmagine.VEICOLO && veicolo != null) {
                final Long idVeicolo = veicolo.getId();
                
                List<Immagine> vecchie = utenteLoggato.getImmagini().stream()
                    .filter(img -> img.getTipo() == TipoImmagine.VEICOLO
                                && img.getVeicolo() != null
                                && img.getVeicolo().getId().equals(idVeicolo))
                    .collect(Collectors.toList());

                for (Immagine vecchia : vecchie) {
                    try {
                        immagineDao.remove(vecchia);
                    } catch (ImmagineNonTrovataException ex) {
                        ex.printStackTrace();
                    }
                    String fsPath = getServletContext().getRealPath("") + File.separator
                                    + vecchia.getPercorso().replace("/", File.separator);
                    File vecchioFile = new File(fsPath);
                    if (vecchioFile.exists()) {
                        vecchioFile.delete();
                    }
                }

                utenteLoggato.getImmagini().removeIf(
                    img -> img.getTipo() == TipoImmagine.VEICOLO
                        && img.getVeicolo() != null
                        && img.getVeicolo().getId().equals(idVeicolo)
                );
            }

            utenteLoggato.getImmagini().add(immagine);
            session.setAttribute("utenteLoggato", utenteLoggato);

            request.setAttribute("successo", "Immagine caricata con successo!");

        } catch (ImmagineGiaNelDatabaseException e) {
            request.setAttribute("errore", "Questa immagine è già presente nel sistema.");
        }

        doGet(request, response);
    }


    private String estensione(String nomeFile) {
        int dot = nomeFile.lastIndexOf('.');
        return (dot >= 0) ? nomeFile.substring(dot + 1).toLowerCase() : "";
    }
}