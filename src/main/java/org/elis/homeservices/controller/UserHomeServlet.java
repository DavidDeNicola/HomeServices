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

import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.RecensioneDAO;
import org.elis.homeservices.dao.definition.RichiestaDAO;
import org.elis.homeservices.dao.definition.UtenteDAO;
import org.elis.homeservices.exception.RecensioneStessoUtenteException;
import org.elis.homeservices.exception.RichiestaNonTrovataException;
import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.model.Recensione;
import org.elis.homeservices.model.Utente;


/**
 * Servlet implementation class UserHomeServlet
 */
@WebServlet("/user-home")
public class UserHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private RichiestaDAO richiestaDao;
	private RecensioneDAO recensioneDao;
	private UtenteDAO utenteDao;
	
	
	@Override
    public void init() throws ServletException {
    	richiestaDao = DaoFactory.getInstance().getRichiestaDAO();
    	recensioneDao = DaoFactory.getInstance().getRecensioneDAO();
    	utenteDao = DaoFactory.getInstance().getUtenteDAO();
    	super.init();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
		
		if(utenteLoggato != null) {
			request.setAttribute("richiesteEffettuate", richiestaDao.findByUtenteRichiede(utenteLoggato));
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/user-home.jsp");
			dispatcher.forward(request, response);
		}
		else {
			session.setAttribute("warningLogin", "Non hai effettuato l'accesso!");
			response.sendRedirect("login");
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
		
		if(utenteLoggato == null) {
			session.setAttribute("warningLogin", "Non hai effettuato l'accesso!");
			response.sendRedirect("login");
		}
		
		String description = request.getParameter("description");
		Byte rating = 0;
		if(request.getParameter("rating") != null) {
			rating = Byte.parseByte(request.getParameter("rating"));
		}
		
		LocalDate dataCorrente = LocalDate.now();
		
		Utente utenteScrive = (Utente) session.getAttribute("utenteLoggato");
		
		Utente utenteRiceve = null;
		Long idUtenteRiceve = null;
		if(request.getParameter("idPro") != null) {
			idUtenteRiceve = Long.parseLong(request.getParameter("idPro")); 
			try {
				utenteRiceve = utenteDao.findById(idUtenteRiceve);
			} catch (UtenteNonTrovatoException e) {
				e.printStackTrace();
			}
		}
		
		try {
			Recensione nuovaRecensione = new Recensione(rating, description, dataCorrente, utenteScrive, utenteRiceve);
			recensioneDao.add(nuovaRecensione);
			
			richiestaDao.setIdRecensione(recensioneDao.findIdUltimaRecensione(), Long.parseLong(request.getParameter("idReq")));
			
			request.setAttribute("recensioneInviata", "Recensione inviata con successo.");
		} catch (RecensioneStessoUtenteException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RichiestaNonTrovataException e) {
			e.printStackTrace();
		}
		
		doGet(request, response);
	}

}
