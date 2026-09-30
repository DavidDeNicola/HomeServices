package org.elis.homeservices.controller;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import org.elis.homeservices.dao.definition.CittaDAO;
import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.UtenteDAO;
import org.elis.homeservices.exception.CittaNonTrovataException;
import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.model.Citta;
import org.elis.homeservices.model.Utente;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UtenteDAO utenteDao; 
	private CittaDAO cittaDao;

	@Override
	public void init() throws ServletException {
		utenteDao = DaoFactory.getInstance().getUtenteDAO();
		cittaDao = DaoFactory.getInstance().getCittaDAO();
		super.init();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Citta> listaCitta = cittaDao.getListCitta();
		request.setAttribute("listCitta", listaCitta);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/views/register.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");
		session.setAttribute("email", email);
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPasswordFormInput");
		String dataNascitaString = request.getParameter("dataNascita");
		String idCittaString = request.getParameter("cityForm");
		LocalDate dataNascita = LocalDate.parse(dataNascitaString);
		LocalDate dataValida = LocalDate.now().minusYears(18);
		Long idCitta = Long.parseLong(idCittaString);

		if(nome == null || nome.trim().isEmpty()) {
			request.setAttribute("message", "Nome non valido!");
			doGet(request, response);
		}
		else if(cognome == null || cognome.trim().isEmpty()) {
			request.setAttribute("message", "Cognome non valido!");
			doGet(request, response);
		}
		else if(email == null || email.trim().isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			request.setAttribute("message", "Email non valida!");
			doGet(request, response);
		}
		else if(password == null || password.trim().isEmpty() || password.length() < 8 || password.length() > 16 || 
				!password.chars().anyMatch(Character::isUpperCase) || !password.chars().anyMatch(Character::isLowerCase) || 
				!password.chars().anyMatch(Character::isDigit) || !password.chars().anyMatch(ch -> "!@#$%^&*()-_=+".indexOf(ch) >= 0)) {
			request.setAttribute("message", "Password non valida!");
			doGet(request, response);
		}
		else if(!confirmPassword.equals(password)) {
			request.setAttribute("message", "Le password non combaciano!");
			doGet(request, response);
		}
		else if(!confirmPassword.equals(password)) {
			request.setAttribute("message", "Le password non combaciano!");
			doGet(request, response);
		}
		else if(dataNascita.isAfter(dataValida)) {
			request.setAttribute("message", "Devi essere maggiorenne!");
			doGet(request, response);
		}
		else {
			try {
				Utente emailTrovata = utenteDao.findByEmail(email);
				if(emailTrovata != null) {
					request.setAttribute("message", "L'email è già registrata.");
					doGet(request, response);
				}
			} catch (UtenteNonTrovatoException e) {
				Citta citta = null;
				try {
					citta = cittaDao.getCityById(idCitta);
				} catch (CittaNonTrovataException e1) {
					e1.printStackTrace();
				}
				
				Utente daAggiungere = new Utente();
				daAggiungere.setNome(nome);
				daAggiungere.setCognome(cognome);
				daAggiungere.setEmail(email);
				daAggiungere.setPassword(password);
				daAggiungere.setDataNascita(dataNascita);
				daAggiungere.setCitta(citta);
											
				session.setAttribute("utente", daAggiungere);

				boolean demo = "true".equals(System.getenv("DEMO_MODE"));

				String codice;
				if (demo) {
				    codice = "123456";
				} else {
				    codice = "";
				    for (int i = 0; i < 6; i++) {
				        codice += (int) (Math.floor(Math.random() * 10));
				    }
				}

				System.out.println(codice);
				session.setAttribute("codiceVerifica", codice);

				if (!demo) {
				    final String username = System.getenv("MAIL_USER");
				    final String password2 = System.getenv("MAIL_PASS");

				    Properties props = new Properties();
				    props.put("mail.smtp.auth", "true");
				    props.put("mail.smtp.starttls.enable", "true");
				    props.put("mail.smtp.host", "smtp.gmail.com");
				    props.put("mail.smtp.port", "587");

				    Session session2 = Session.getInstance(props, new Authenticator() {
				        protected PasswordAuthentication getPasswordAuthentication() {
				            return new PasswordAuthentication(username, password2);
				        }
				    });

				    try {
				        Message message = new MimeMessage(session2);
				        message.setFrom(new InternetAddress(username));
				        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
				        message.setSubject("Team di supporto HomeServices");
				        message.setText("Salve! Il tuo codice monouso è: " + codice);
				        Transport.send(message);
				        System.out.println("Email inviata!");
				    } catch (MessagingException er) {
				        er.printStackTrace();
				    }
				}
				session.setAttribute("arrivoDa", "register");
				response.sendRedirect("auth");
			}


		}
	}

}
