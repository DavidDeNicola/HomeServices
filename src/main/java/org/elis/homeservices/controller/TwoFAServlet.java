package org.elis.homeservices.controller;

import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.UtenteDAO; 
import org.elis.homeservices.exception.UtenteGiaPresenteException;
import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.enums.Ruolo;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet implementation class TwoFAServlet
 */
@WebServlet("/auth")
public class TwoFAServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private UtenteDAO utenteDao; 
	
    @Override
    public void init() throws ServletException {
    	utenteDao = DaoFactory.getInstance().getUtenteDAO();
    	super.init();
    }

    public TwoFAServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/views/TWOFA.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String arrivoDa = (String)session.getAttribute("arrivoDa");
		
		if("register".equals(arrivoDa)) {
			Utente utente = (Utente) session.getAttribute("utente");
			String codiceInserito = request.getParameter("formCodice");
			String codiceVerifica = (String) session.getAttribute("codiceVerifica");
			
			if(!codiceInserito.equals(codiceVerifica)) {
				request.setAttribute("messaggio", "Il codice inserito non è valido. Riprova!!!");
				doGet(request, response);
				return;
			}
			
			try {
				utenteDao.add(utente);
				session.setAttribute("arrivoDa", "2FA");
				response.sendRedirect("login");
			} catch (UtenteGiaPresenteException e) {
				e.printStackTrace();
			}
		}
		else if("login".equals(arrivoDa)) {
			Utente utente = (Utente) session.getAttribute("utente");
			String codiceInserito = request.getParameter("formCodice");
			String codiceVerifica = (String) session.getAttribute("codiceVerifica");
			
			if(!codiceInserito.equals(codiceVerifica)) {
				request.setAttribute("messaggio", "Il codice inserito non è valido. Riprova!!!");
				doGet(request, response);
				return;
			}

			session.setAttribute("utenteLoggato", utente);
			session.removeAttribute("utente");
			session.removeAttribute("arrivoDa");
			session.removeAttribute("codiceVerifica");
			Utente utenteLoggato = (Utente)session.getAttribute("utenteLoggato");
			
			if(utenteLoggato.getRuolo().equals(Ruolo.USER)) {
				response.sendRedirect("user-home");
				
			} else if(utenteLoggato.getRuolo().equals(Ruolo.PRO)) {
				response.sendRedirect("pro-home");
				
			} else if(utenteLoggato.getRuolo().equals(Ruolo.ADMIN)) {
				response.sendRedirect("admin-home");
				
			}
		}
		
	}

}
