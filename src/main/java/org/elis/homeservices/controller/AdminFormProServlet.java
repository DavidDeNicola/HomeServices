package org.elis.homeservices.controller;
 
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
 
import java.io.IOException;
import java.util.List;
 
import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.FormProDAO;
import org.elis.homeservices.dao.definition.UtenteDAO;
import org.elis.homeservices.exception.FormProNonTrovatoException;
import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.model.FormPro;
import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.enums.Ruolo;
 
/**
 * Servlet implementation class AdminFormProServlet
 */
@WebServlet("/admin-form-pro")
public class AdminFormProServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	private FormProDAO formProDao;
	private UtenteDAO utenteDao;
 
	@Override
	public void init() throws ServletException {
		formProDao = DaoFactory.getInstance().getFormProDAO();
		utenteDao  = DaoFactory.getInstance().getUtenteDAO();
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
 
		List<FormPro> listaFormPro = formProDao.getForms();
		request.setAttribute("listaFormPro", listaFormPro);
 
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/admin/admin-form-pro.jsp");
		dispatcher.forward(request, response);
	}
 

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 
		HttpSession session = request.getSession();
		Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
 
		if (utenteLoggato == null || !utenteLoggato.getRuolo().equals(Ruolo.ADMIN)) {
			session.setAttribute("warningLogin", "Non hai effettuato l'accesso!");
			response.sendRedirect("login");
			return;
		}
 
		String idFormStr = request.getParameter("idForm");
		String action    = request.getParameter("azione");
 
		if (idFormStr == null || idFormStr.isBlank() || action == null || action.isBlank()) {
			response.sendRedirect("admin-form-pro");
			return;
		}
 
		try {
			Long idForm = Long.parseLong(idFormStr);
			FormPro form = formProDao.getById(idForm);
 
			if ("approva".equals(action)) {
				Utente utente = form.getUtente();
				utenteDao.toPro(utente.getId(), form.getCf());
				
				utenteDao.modifyRate(utente.getId(), form.getTariffa());
				formProDao.remove(idForm);
 
			} else if ("rifiuta".equals(action)) {
				formProDao.remove(idForm);
			}
 
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FormProNonTrovatoException e) {
			e.printStackTrace();
		} catch (UtenteNonTrovatoException e) {
			e.printStackTrace();
		}
 
		response.sendRedirect("admin-home");
	}
}