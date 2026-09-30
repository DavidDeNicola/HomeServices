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
import java.util.Properties;

import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.UtenteDAO;
import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.model.Utente;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private UtenteDAO utenteDao; 
	
    @Override
    public void init() throws ServletException {
    	utenteDao = DaoFactory.getInstance().getUtenteDAO();
    	super.init();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String arrivoDa = (String)session.getAttribute("arrivoDa");
		if("2FA".equals(arrivoDa)) {
			request.setAttribute("message2FA", "Registrazione avvenuta con successo!");		
		}
		
		String warningLogin = (String) session.getAttribute("warningLogin");
		
		if(warningLogin != null) {
			if(!warningLogin.isBlank()) {
				request.setAttribute("warningLogin", warningLogin);
				session.setAttribute("warningLogin", null);
			}
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/views/login.jsp");
		dispatcher.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		try {
			Utente utente = utenteDao.findByEmailPass(email, password);
			HttpSession session = request.getSession();
			session.setAttribute("utente", utente);
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
			
			session.setAttribute("arrivoDa", "login");
			session.setAttribute("email", email);
			response.sendRedirect("auth");
			
			
			
			
		} catch (UtenteNonTrovatoException e) {
			request.setAttribute("message", "Email o Password non validi.");
			e.printStackTrace();
			RequestDispatcher dispatcher = request.getRequestDispatcher("/views/login.jsp");
			dispatcher.forward(request, response);
		}
	}

}
