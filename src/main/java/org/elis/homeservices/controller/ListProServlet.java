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

import org.elis.homeservices.dao.definition.CittaDAO;
import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.UtenteDAO;
import org.elis.homeservices.exception.ProfessionistaNonTrovatoException;
import org.elis.homeservices.model.Utente;

/**
 * Servlet implementation class ListProServlet
 */
@WebServlet("/list-pro")
public class ListProServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UtenteDAO utenteDao;
	private CittaDAO cittaDAO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	@Override
    public void init() throws ServletException {
    	utenteDao = DaoFactory.getInstance().getUtenteDAO();
    	cittaDAO = DaoFactory.getInstance().getCittaDAO();
    	super.init();
    }
	
    public ListProServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("listaCitta", cittaDAO.getListCitta());
		if(request.getAttribute("listaProfessionisti") == null) {
			try {
				List<Utente> pro =  utenteDao.ricercaAvanzata(null, null, null, null, null, null, null);
				HttpSession session = request.getSession();
				Utente utenteLoggato = (Utente)session.getAttribute("utenteLoggato");
				if(utenteLoggato != null) {
					pro.removeIf(u -> u.getId().equals(utenteLoggato.getId()));
				}
				request.setAttribute("listaProfessionisti",pro);
			} catch (ProfessionistaNonTrovatoException e) {
				e.printStackTrace();
			}
		}
		RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/user/list-pro.jsp");
		dispatcher.forward(request, response);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String nome = request.getParameter("nome");
	    String idCittaStr = request.getParameter("idCitta");
	    String val = request.getParameter("valutazione");
	    String dataStr = request.getParameter("data");
	    String inizioStr = request.getParameter("inizio");
	    String fineStr = request.getParameter("fine");
	    String ordine = request.getParameter("ordine");

	    try {
	        Long idCitta = (idCittaStr != null && !idCittaStr.isEmpty()) ? Long.parseLong(idCittaStr) : null;
	       
	        Double valutazione = (val != null && !val.isEmpty() && Double.parseDouble(val) > 0) ? Double.parseDouble(val) : null;
	        LocalDate data = (dataStr != null && !dataStr.isEmpty()) ? LocalDate.parse(dataStr) : null;
	        if (data != null && data.isBefore(LocalDate.now())) {
	            request.setAttribute("messaggio", "Non è possibile ricercare disponibilità per date passate.");
	            data = null; 
	        }
	        LocalTime inizio = (inizioStr != null && !inizioStr.isEmpty()) ? LocalTime.parse(inizioStr) : null;
	        LocalTime fine = (fineStr != null && !fineStr.isEmpty()) ? LocalTime.parse(fineStr) : null;

	        List<Utente> professionisti = utenteDao.ricercaAvanzata(nome, idCitta, valutazione, data, inizio, fine, ordine);
	        
	        // Rimozione utenteLoggato da ricerca
	        Utente loggato = (Utente) request.getSession().getAttribute("utenteLoggato");
	        if(loggato != null) professionisti.removeIf(u -> u.getId().equals(loggato.getId()));

	        request.setAttribute("listaProfessionisti", professionisti);
	        
	    } catch (ProfessionistaNonTrovatoException e) {
	        request.setAttribute("messaggio", "Nessun professionista trovato.");
	    } catch (Exception e) {
	        request.setAttribute("errore", "Errore nei parametri di ricerca.");
	    }
	    
	    
	    request.setAttribute("listaCitta", cittaDAO.getListCitta());
	    request.getRequestDispatcher("/WEB-INF/user/list-pro.jsp").forward(request, response);
	}
}