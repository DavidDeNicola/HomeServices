package org.elis.homeservices.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.DisponibilitaDAO;
import org.elis.homeservices.dao.definition.RecensioneDAO;
import org.elis.homeservices.dao.definition.RichiestaDAO;
import org.elis.homeservices.dao.definition.SegnalazioneDAO;
import org.elis.homeservices.dao.definition.UtenteDAO;
import org.elis.homeservices.exception.DisponibilitaTimeOverlapException;
import org.elis.homeservices.exception.RecensioneNonTrovataException;
import org.elis.homeservices.exception.RichiestaNonTrovataException;
import org.elis.homeservices.exception.StatoNonAggiornabileException;
import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.model.Disponibilita;
import org.elis.homeservices.model.Recensione;
import org.elis.homeservices.model.Richiesta;
import org.elis.homeservices.model.Segnalazione;
import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.enums.Stato;

@WebServlet("/pro-task-review")
public class ProTaskReviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private RichiestaDAO richiestaDao;
	private RecensioneDAO recensioneDAO;
	private SegnalazioneDAO segnalazioneDAO;
	private UtenteDAO utenteDAO;
	private DisponibilitaDAO disponibilitaDAO;

	@Override
	public void init() throws ServletException {
		richiestaDao  = DaoFactory.getInstance().getRichiestaDAO();
		recensioneDAO = DaoFactory.getInstance().getRecensioneDAO();
		segnalazioneDAO = DaoFactory.getInstance().getSegnalazioneDAO();
		utenteDAO     = DaoFactory.getInstance().getUtenteDAO();
		disponibilitaDAO = DaoFactory.getInstance().getDisponibilitaDAO();
		super.init();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    HttpSession session = request.getSession();
	    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

	    // 1. Recupera la lista aggiornata delle richieste per il professionista
	    List<Richiesta> richieste = richiestaDao.findByUtenteRiceve(utenteLoggato);

	    Map<Long, Recensione> recensioniMap = new HashMap<>();
	    Map<Long, Utente> mittentiMap = new HashMap<>();

	    if (richieste != null) {
	        for (Richiesta r : richieste) {
	            // 2. CARICAMENTO FORZATO DELLE RECENSIONI
	            // Se la richiesta è completata, cerchiamo se esiste una recensione associata nel DB
	            if (r.getStato() == Stato.COMPLETATO) {
	                try {
	                    // Usiamo il metodo del DAO che interroga direttamente il DB per l'ID recensione
	                    Long idRecensione = richiestaDao.getIdRecensioneById(r.getId());
	                    
	                    if (idRecensione != null) {
	                        Recensione rec = recensioneDAO.findById(idRecensione);
	                        recensioniMap.put(r.getId(), rec);
	                    }
	                } catch (RichiestaNonTrovataException | RecensioneNonTrovataException e) {
	                    // Se non esiste la recensione o la richiesta, semplicemente non la aggiungiamo alla mappa
	                    // Non blocchiamo il caricamento della pagina
	                }
	            }

	            // 3. Caricamento dei mittenti (già presente, lo manteniamo)
	            Long idMittente = r.getUtenteRichiede().getId();
	            if (!mittentiMap.containsKey(idMittente)) {
	                try {
	                    mittentiMap.put(idMittente, utenteDAO.findById(idMittente));
	                } catch (UtenteNonTrovatoException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }

	    request.setAttribute("richieste", richieste);
	    request.setAttribute("recensioniMap", recensioniMap);
	    request.setAttribute("mittentiMap", mittentiMap);

	    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pro/pro-task-review.jsp");
	    dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
		Long idUtenteSegnalante = utenteLoggato.getId();

		Long idRichiesta = Long.parseLong(request.getParameter("idRichiesta"));
		String action = request.getParameter("azione");

		try {
			if ("accetta".equals(action) || "completa".equals(action)) {
				richiestaDao.updateStato(idRichiesta);

			} else if ("rifiuta".equals(action)) {
				try {
					Richiesta richiesta = richiestaDao.getById(idRichiesta);
					Utente proStub = new Utente();
					proStub.setId(richiesta.getUtenteRiceve().getId());
					Disponibilita dispRipristinata = new Disponibilita(
						richiesta.getData(),
						richiesta.getDa(),
						richiesta.getA(),
						proStub
					);
					disponibilitaDAO.add(dispRipristinata);
				} catch (DisponibilitaTimeOverlapException e) {
					e.printStackTrace();
				}
				richiestaDao.remove(idRichiesta);

			} else if ("segnala".equals(action)) {
				Long idRecensione = Long.parseLong(request.getParameter("idRecensione"));
				Long idUtenteSegnalato = recensioneDAO.getIdUtenteScriveById(idRecensione);
				String motivazione = request.getParameter("motivazione");
				Utente utenteSegnalato = utenteDAO.findById(idUtenteSegnalato);
				Utente utenteSegnalante = utenteDAO.findById(idUtenteSegnalante);
				segnalazioneDAO.add(new Segnalazione(motivazione, utenteSegnalato, utenteSegnalante));
			}

		} catch (RichiestaNonTrovataException | StatoNonAggiornabileException | RecensioneNonTrovataException | UtenteNonTrovatoException e) {
			e.printStackTrace();
		}

		doGet(request, response);
	}
}
