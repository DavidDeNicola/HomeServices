package org.elis.homeservices.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
//import java.time.LocalDate;
import java.util.List;

import org.elis.homeservices.dao.definition.CittaDAO;
import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.FormProDAO;
import org.elis.homeservices.dao.definition.ProfessioneDAO;
import org.elis.homeservices.dao.jdbc.JdbcCittaDAO;
import org.elis.homeservices.dao.jdbc.JdbcProfessioneDAO;
import org.elis.homeservices.exception.UtenteGiaPresenteException;
import org.elis.homeservices.model.Citta;
import org.elis.homeservices.model.FormPro;
import org.elis.homeservices.model.Professione;
import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.enums.Ruolo;
import org.elis.homeservices.utility.DataSourceConfig;

/**
 * Servlet implementation class UserToProServlet
 */
@WebServlet("/user-to-pro")
public class UserToProServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FormProDAO formProDao;
	private CittaDAO cittaDao;
	private ProfessioneDAO professioneDao;
	
	@Override
	public void init() throws ServletException {
		formProDao = DaoFactory.getInstance().getFormProDAO();
		cittaDao = DaoFactory.getInstance().getCittaDAO();
		professioneDao = DaoFactory.getInstance().getProfessioneDAO();
		super.init();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

		if (utenteLoggato == null || !utenteLoggato.getRuolo().equals(Ruolo.USER)) {
	        response.sendRedirect("login");
	        return;
	    }
			
		CittaDAO cittaDao = new JdbcCittaDAO(DataSourceConfig.getDataSource());
		List<Citta> listaCitta = cittaDao.getListCitta();
		request.setAttribute("listCitta", listaCitta);

		ProfessioneDAO proDao = new JdbcProfessioneDAO(DataSourceConfig.getDataSource());
		List<Professione> listaProfessioni = proDao.getListProfessioni();
		request.setAttribute("listProfessioni", listaProfessioni);
		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/user-to-pro.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

		String codiceFiscale = request.getParameter("cfFormInput");
		String rate = request.getParameter("rateFormInput");
		BigDecimal tariffa = new BigDecimal(request.getParameter("rateFormInput"));
		
		Long idCitta = Long.parseLong(request.getParameter("idCitta"));
		Long idProfessione = Long.parseLong(request.getParameter("idProfessione"));
		
		Double hourlyRate = 0.0;

		try {
			if(rate != null && !rate.isEmpty()) {
				hourlyRate = Double.parseDouble(rate);

				if(hourlyRate<0 || hourlyRate>9999.99) {
					request.setAttribute("message", "Il valore della tariffa non è valido");
					doGet(request, response);
					return;
				}
			}else {
				request.setAttribute("message", "Inserisci una tariffa");
				response.sendRedirect("user-to-pro");
				return;
			}

			request.setAttribute("message", "Dati inseriti correttamente");
			
			FormPro fp = new FormPro(codiceFiscale, tariffa, cittaDao.getCityById(idCitta), professioneDao.findById(idProfessione), utenteLoggato);
			formProDao.add(fp);
			
			utenteLoggato.setFormPro(fp);
			session.setAttribute("utenteLoggato", utenteLoggato); 
			
			response.sendRedirect("user-home");
			return;

		}catch(NumberFormatException e) {
			e.printStackTrace();
		}catch(UtenteGiaPresenteException e) {
			request.setAttribute("message", "Hai già effettuato un form per diventare professionista.");
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		doGet(request, response);
	}

}
