package org.elis.homeservices.controller;
 
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.SegnalazioneDAO;
import org.elis.homeservices.dao.definition.UtenteDAO;
import org.elis.homeservices.exception.SegnalazioneNonTrovataException;
import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.model.Segnalazione;
import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.enums.Ruolo;
 
@WebServlet("/admin-report")
public class AdminReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    private SegnalazioneDAO segnalazioneDao;
    private UtenteDAO utenteDao;
 
    @Override
    public void init() throws ServletException {
        segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDAO();
        utenteDao       = DaoFactory.getInstance().getUtenteDAO();
        super.init();
    }
 
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        HttpSession session = request.getSession();
        Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
 
        if (utenteLoggato == null || !utenteLoggato.getRuolo().equals(Ruolo.ADMIN)) {
            response.sendRedirect("login");
            return;
        }
 
        String cerca = request.getParameter("cerca");
        boolean ricercaAttiva = (cerca != null && !cerca.trim().isEmpty());
 
        List<Segnalazione> listaPendenti;
        List<Segnalazione> listaSanzionate;
 
        if (ricercaAttiva) {
            List<Utente> utentiTrovati = utenteDao.findByNomeCognome(cerca.trim());
 
            listaPendenti   = new ArrayList<>();
            listaSanzionate = new ArrayList<>();
 
            for (Utente u : utentiTrovati) {
                List<Segnalazione> tutte = segnalazioneDao.findByIdSegnalato(u.getId());
                if (tutte != null) {
                    for (Segnalazione s : tutte) {
                        if (s.getSanzionato()) {
                            listaSanzionate.add(s);
                        } else {
                            listaPendenti.add(s);
                        }
                    }
                }
            }
        } else {
            listaPendenti   = segnalazioneDao.getListAllSegnalazioni();
            listaSanzionate = segnalazioneDao.getListSegnalazioniSanzionate();
        }
 
        request.setAttribute("listSegnalazioni",           listaPendenti);
        request.setAttribute("listSegnalazioniSanzionate", listaSanzionate);
        request.setAttribute("cerca",         cerca != null ? cerca : "");
        request.setAttribute("ricercaAttiva", ricercaAttiva);
 
        Map<Long, Utente> utentiMap = new HashMap<>();
        caricaUtenti(listaPendenti,   utentiMap);
        caricaUtenti(listaSanzionate, utentiMap);
        request.setAttribute("utentiMap", utentiMap);
 
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/admin/admin-report.jsp");
        dispatcher.forward(request, response);
    }
 
    private void caricaUtenti(List<Segnalazione> lista, Map<Long, Utente> map) {
        if (lista == null) return;
        for (Segnalazione s : lista) {
            try {
                if (!map.containsKey(s.getUtenteSegnalato().getId()))
                    map.put(s.getUtenteSegnalato().getId(),
                            utenteDao.findById(s.getUtenteSegnalato().getId()));
                if (!map.containsKey(s.getUtenteSegnalante().getId()))
                    map.put(s.getUtenteSegnalante().getId(),
                            utenteDao.findById(s.getUtenteSegnalante().getId()));
            } catch (UtenteNonTrovatoException e) {
                e.printStackTrace();
            }
        }
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        HttpSession session = request.getSession();
        Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
 
        if (utenteLoggato == null || !utenteLoggato.getRuolo().equals(Ruolo.ADMIN)) {
            response.sendRedirect("login");
            return;
        }
 
        Long idSegnalazione = Long.parseLong(request.getParameter("idSegnalazione"));
        String azione = request.getParameter("azione");
 
        try {
            if ("chiudi".equals(azione)) {
                segnalazioneDao.removeById(idSegnalazione);
            } else if ("sanziona".equals(azione)) {
                segnalazioneDao.setSanzionatoTrue(idSegnalazione);
            }
        } catch (SegnalazioneNonTrovataException e) {
            e.printStackTrace();
        }
 
        String cerca = request.getParameter("cerca");
        if (cerca != null && !cerca.trim().isEmpty()) {
            response.sendRedirect("admin-report?cerca=" + java.net.URLEncoder.encode(cerca, "UTF-8"));
        } else {
            response.sendRedirect("admin-report");
        }
    }
}