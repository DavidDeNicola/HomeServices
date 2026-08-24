package org.elis.homeservices.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.DisponibilitaDAO;
import org.elis.homeservices.dao.definition.RecensioneDAO;
import org.elis.homeservices.dao.definition.RichiestaDAO;
import org.elis.homeservices.dao.definition.UtenteDAO;
import org.elis.homeservices.exception.RichiestaStessoUtenteException;
import org.elis.homeservices.exception.RichiestaTimeOverlapException;
import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.model.Professione;
import org.elis.homeservices.model.Richiesta;
import org.elis.homeservices.model.Utente;

@WebServlet("/request-pro")
public class RequestProServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UtenteDAO utenteDao;
    private RecensioneDAO recensioneDao;
    private DisponibilitaDAO disponibilitaDao;
    private RichiestaDAO richiestaDao;

    @Override
    public void init() throws ServletException {
        utenteDao       = DaoFactory.getInstance().getUtenteDAO();
        recensioneDao   = DaoFactory.getInstance().getRecensioneDAO();
        disponibilitaDao = DaoFactory.getInstance().getDisponibilitaDAO();
        richiestaDao    = DaoFactory.getInstance().getRichiestaDAO();
        super.init();
    }

    public RequestProServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utenteLoggato") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idParam = request.getParameter("idpro");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/list-pro");
            return;
        }

        try {
            Long idProScelto = Long.parseLong(idParam);
            Utente proScelto = utenteDao.findById(idProScelto);

            request.setAttribute("proScelto",   proScelto);
            request.setAttribute("nRecensioni", recensioneDao.findByUtenteRiceve(proScelto).size());
            request.setAttribute("dispPro",     disponibilitaDao.findByIdPro(idProScelto));
            request.setAttribute("profPro",     utenteDao.getListaProfessioni(idProScelto));

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/list-pro");
            return;
        } catch (UtenteNonTrovatoException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/list-pro");
            return;
        }

        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/WEB-INF/user/request-pro.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utenteLoggato") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
        Utente utenteRichiedeStub = new Utente();
        utenteRichiedeStub.setId(utenteLoggato.getId());

        String descrizione    = request.getParameter("descrizione");
        String indirizzo      = request.getParameter("indirizzo");
        String fasciaOraria   = request.getParameter("fasciaOraria");   // formato: yyyy-MM-dd-HH:mm-HH:mm
        String idProStr        = request.getParameter("idProfessionista");
        String idProfessioneStr = request.getParameter("idProfessione");

        if (descrizione == null || descrizione.isBlank()
                || indirizzo == null || indirizzo.isBlank()
                || fasciaOraria == null || fasciaOraria.isBlank()
                || idProStr == null || idProStr.isBlank()
                || idProfessioneStr == null || idProfessioneStr.isBlank()) {

            request.setAttribute("errore", "Tutti i campi sono obbligatori.");
            doGet(request, response);
            return;
        }

       
        LocalDate data;
        LocalTime da;
        LocalTime a;

        try {
            data = LocalDate.parse(fasciaOraria.substring(0, 10));
            da   = LocalTime.parse(fasciaOraria.substring(11, 16));
            a    = LocalTime.parse(fasciaOraria.substring(17, 22));
        } catch (Exception e) {
            request.setAttribute("errore", "Fascia oraria non valida. Riprova.");
            doGet(request, response);
            return;
        }

        Long idProfessionista;
        Utente proScelto;
        Professione professione;

        try {
            idProfessionista = Long.parseLong(idProStr);
            Long idProfessione = Long.parseLong(idProfessioneStr);

            proScelto = new Utente();
            proScelto.setId(idProfessionista);

            List<Professione> professioni = utenteDao.getListaProfessioni(idProfessionista);
            if (professioni == null || professioni.isEmpty()) {
                request.setAttribute("errore", "Il professionista non ha professioni associate.");
                doGet(request, response);
                return;
            }
            professione = professioni.stream()
                    .filter(p -> p.getId().equals(idProfessione))
                    .findFirst()
                    .orElse(null);
            if (professione == null) {
                request.setAttribute("errore", "Professione non valida per questo professionista.");
                doGet(request, response);
                return;
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/list-pro");
            return;
        } catch (UtenteNonTrovatoException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/list-pro");
            return;
        }

        
        try {
            Richiesta nuovaRichiesta = new Richiesta(
                    descrizione,
                    data,
                    da,
                    a,
                    indirizzo,
                    utenteRichiedeStub,
                    proScelto,
                    professione
            );

            richiestaDao.add(nuovaRichiesta);

            List<org.elis.homeservices.model.Disponibilita> dispPro =
                    disponibilitaDao.findByIdPro(idProfessionista);
            if (dispPro != null) {
                for (org.elis.homeservices.model.Disponibilita disp : dispPro) {
                    if (disp.getData().equals(data)
                            && disp.getDa().equals(da)
                            && disp.getA().equals(a)) {
                        try {
                            disponibilitaDao.remove(disp);
                        } catch (org.elis.homeservices.exception.DisponibilitaNonTrovataException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                }
            }

        } catch (RichiestaStessoUtenteException e) {
            request.setAttribute("errore", "Non puoi inviare una richiesta a te stesso.");
            doGet(request, response);
            return;
        } catch (RichiestaTimeOverlapException e) {
            request.setAttribute("errore", "La fascia oraria selezionata si sovrappone a un'altra richiesta esistente.");
            doGet(request, response);
            return;
        }

        
        session.setAttribute("flashSuccesso", "Richiesta inviata con successo!");
        response.sendRedirect(request.getContextPath() + "/list-pro");
    }
}
