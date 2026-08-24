package org.elis.homeservices.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.Citta;
import org.elis.homeservices.model.Professione;
import org.elis.homeservices.model.enums.Ruolo;
import org.elis.homeservices.exception.CittaGiaNelDatabaseException;
import org.elis.homeservices.exception.CittaNonTrovataException;
import org.elis.homeservices.exception.ProfessioneGiaNelDatabaseException;
import org.elis.homeservices.exception.ProfessioneNonTrovataException;
import org.elis.homeservices.dao.definition.CittaDAO;
import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.ProfessioneDAO;

import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class AdminHomeServlet
 */
@WebServlet("/admin-home")
public class AdminHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CittaDAO cittaDao;
	private ProfessioneDAO proDao;
	
	@Override
	public void init() throws ServletException {
		cittaDao = DaoFactory.getInstance().getCittaDAO();
		proDao = DaoFactory.getInstance().getProfessioneDAO();
		super.init();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

		List<Citta> listaCitta = cittaDao.getListCitta();
		request.setAttribute("listCitta", listaCitta);

		List<Professione> listaProfessioni = proDao.getListProfessioni();
		request.setAttribute("listProfessioni", listaProfessioni);

		if(utenteLoggato.getRuolo().equals(Ruolo.ADMIN)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/admin/admin-home.jsp");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect("login");
			return;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");

		// form addCitta
		if("addCitta".equals(action)) {
			String nomeCitta = request.getParameter("nomeCitta");

			if(nomeCitta == null || nomeCitta.trim().isEmpty())  {
				request.setAttribute("message", "Nome della città non valido");
			} else {

				try {
					cittaDao.addCity(nomeCitta);
				} catch (CittaGiaNelDatabaseException e) {
					request.setAttribute("message", "Città "+nomeCitta+" già presente nel database");
					e.printStackTrace();

					doGet(request, response);
					return;
				}

				request.setAttribute("message", "Città "+nomeCitta+" aggiunta con successo");
			}

			doGet(request, response);
			return;
		}

		// form editCitta
		if("editCitta".equals(action)) {
			Long idCitta = Long.parseLong(request.getParameter("idCitta"));
			String CittaName = request.getParameter("CittaName") != null ? request.getParameter("CittaName") : "il nuovo nome della citta non deve essere vuoto";

		
			
			try {
		        this.cittaDao.editCityNameById(idCitta, CittaName);
		        request.setAttribute("updateMessage", "Città modificata con successo.");
		    } catch (CittaGiaNelDatabaseException e) {
		        request.setAttribute("updateMessage", "Errore: Città già presente.");
		    } catch (CittaNonTrovataException e) {
		        request.setAttribute("updateMessage", "Errore: Il nome '" + CittaName + "' esiste già nel database.");
		    }

			doGet(request, response);
			return;
		}

		// form addProfessione
		if("addProfessione".equals(action)) {
			String nomeProfessione = request.getParameter("nomeProfessione");
			Professione p = new Professione(nomeProfessione);

			if(nomeProfessione == null || nomeProfessione.trim().isEmpty())  {
				request.setAttribute("messageProfessione", "Nome della professione non valido");
			} else {

				try {
					proDao.add(p);
				} catch (ProfessioneGiaNelDatabaseException e) {
					request.setAttribute("messageProfessione", "Professione "+nomeProfessione+" già presente nel database");
					e.printStackTrace();

					doGet(request, response);
					return;
				}

				request.setAttribute("messageProfessione", "Professione "+nomeProfessione+" aggiunta con successo");
			}

			doGet(request, response);
			return;
		}

		// form removeProfessione
		if("removeProfessione".equals(action)) {
			Long idProfessione = Long.parseLong(request.getParameter("idProfessione"));

			try {
				String nomeProfessione = ((Professione) proDao.findById(idProfessione)).getNome();
				Professione professione = new Professione(nomeProfessione);
				professione.setId(idProfessione);
				proDao.remove(professione);
				request.setAttribute("removePro", "Professione "+nomeProfessione+" rimossa con successo");
			} catch (ProfessioneNonTrovataException e) {
				request.setAttribute("removePro", "Id professione "+idProfessione+" non trovato");
				e.printStackTrace();
			}

			doGet(request, response);
			return;
		}

		doGet(request, response);
	}

}
