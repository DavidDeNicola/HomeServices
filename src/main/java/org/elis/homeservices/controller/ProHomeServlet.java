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
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.time.LocalTime;

import org.elis.homeservices.dao.definition.DaoFactory;
import org.elis.homeservices.dao.definition.DisponibilitaDAO;
import org.elis.homeservices.dao.definition.ProfessioneDAO;
import org.elis.homeservices.dao.definition.UtenteDAO;
import org.elis.homeservices.dao.definition.VeicoloDAO;
import org.elis.homeservices.exception.DisponibilitaNonTrovataException;
import org.elis.homeservices.exception.DisponibilitaTimeOverlapException;
import org.elis.homeservices.exception.ProfessioneGiaAssociataException;
import org.elis.homeservices.exception.ProfessioneNonAssociataException;
import org.elis.homeservices.exception.ProfessioneNonTrovataException;
import org.elis.homeservices.exception.UtenteNonTrovatoException;
import org.elis.homeservices.exception.VeicoloGiaAssociatoException;
import org.elis.homeservices.exception.VeicoloNonAssociatoException;
import org.elis.homeservices.model.Disponibilita;
import org.elis.homeservices.model.Professione;
import org.elis.homeservices.model.Utente;
import org.elis.homeservices.model.Veicolo;
import org.elis.homeservices.model.enums.Ruolo;

/**
 * Servlet implementation class ProHomeServlet
 */
@WebServlet("/pro-home")
public class ProHomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private VeicoloDAO veicoloDAO;
    private UtenteDAO utenteDAO;
    private ProfessioneDAO professioneDAO;
    private DisponibilitaDAO disponibilitaDAO;
    
	@Override
	public void init() throws ServletException{
		veicoloDAO = DaoFactory.getInstance().getVeicoloDAO();
		utenteDAO = DaoFactory.getInstance().getUtenteDAO();
		professioneDAO = DaoFactory.getInstance().getProfessioneDAO();
		disponibilitaDAO = DaoFactory.getInstance().getDisponibilitaDAO();
		super.init();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Utente utenteLoggato = (Utente)session.getAttribute("utenteLoggato");
		
		if (utenteLoggato == null || !utenteLoggato.getRuolo().equals(Ruolo.PRO)) {
	        response.sendRedirect("login");
	        return;
	    }
		
		
		List<Veicolo> listaVeicoli = veicoloDAO.getListaVeicoli();
		List<Professione> listaProfessoni = professioneDAO.getListProfessioni();
		List<Professione> listaProfessioniUtente;
		try {
			listaProfessioniUtente = utenteDAO.showPro(utenteLoggato.getId());
		} catch (UtenteNonTrovatoException e) {
			listaProfessioniUtente = new ArrayList<>();
			e.printStackTrace();
		}
		List<Veicolo> listaVeicoliUtente = null;
		
		try {
			listaVeicoliUtente = veicoloDAO.getListaVeicoliUtente(utenteLoggato.getId());
		} catch (UtenteNonTrovatoException e) {
			e.printStackTrace();
		}
		
		request.setAttribute("listaProfessioniUtente", listaProfessioniUtente);
		request.setAttribute("listaVeicoli", listaVeicoli);
		request.setAttribute("listaProfessioni", listaProfessoni);
		request.setAttribute("listaVeicoliUtente", listaVeicoliUtente);
		request.setAttribute("listaDisponibilitaUtente",
				DaoFactory.getInstance().getDisponibilitaDAO().findByIdPro(utenteLoggato.getId()));

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pro/pro-home.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session= request.getSession();
		Utente utenteLoggato = (Utente)session.getAttribute("utenteLoggato");
		String action = request.getParameter("action"); 
		
		if("modifyRate".equals(action)) {
			String nuovaTariffa= request.getParameter("nuovaTariffa");
			Double nuovTariffa= Double.parseDouble(nuovaTariffa);
			BigDecimal tariffa = BigDecimal.valueOf(nuovTariffa);
			if(tariffa.compareTo(BigDecimal.ZERO)>0 && tariffa.compareTo(BigDecimal.valueOf(10000))<0) {
				utenteDAO.modifyRate(utenteLoggato.getId(), tariffa);
				utenteLoggato.setTariffa(tariffa);
				request.setAttribute("messaggio", "tariffa cambiata con successo");
				doGet(request,response);
				
			}else {
				request.setAttribute("messaggio", "tariffa non valida");
				doGet(request, response);
				
			}
		}else if("removeVeicolo".equals(action)) {
			String idVeicolo= request.getParameter("idVeicolo");
			Long idRemoveVeicolo= Long.parseLong(idVeicolo);
			Veicolo daRimuovere=veicoloDAO.findById(idRemoveVeicolo);
			try {
				utenteDAO.removeVehicle(utenteLoggato, daRimuovere);
				doGet(request, response);
			} catch (VeicoloNonAssociatoException e) {
				e.printStackTrace();
			}
		}else if("addVeicolo".equals(action)) {
			String idVeicolo= request.getParameter("idAddVeicolo");
			Long idVeicoloLong= Long.parseLong(idVeicolo);
			Veicolo daAggiungere=veicoloDAO.findById(idVeicoloLong);
			
			try {
				utenteDAO.addVehicle(utenteLoggato, daAggiungere);
				doGet(request, response);
			} catch (VeicoloGiaAssociatoException e) {
				e.printStackTrace();
				request.setAttribute("messaggioVeicolo", "Il veicolo è già presente" );
				doGet(request, response);
			}
		}else if("addProfessione".equals(action)) {
			String idProfessione=request.getParameter("idProfessione");
			Long idAddProfessione=Long.parseLong(idProfessione);
			
			try {
				Professione professione=professioneDAO.findById(idAddProfessione);
				professione.setId(idAddProfessione);
				
				try {
					utenteDAO.addPro(utenteLoggato, professione);
					request.setAttribute("messaggioPro", "Professione aggiunta con successo");
					doGet(request, response);
				} catch (ProfessioneGiaAssociataException e) {
					request.setAttribute("messaggioPro", "Professione già associata");
					doGet(request, response);
					e.printStackTrace();
				}
			} catch (ProfessioneNonTrovataException e) {
				request.setAttribute("messaggioPro", "Professione non trovata");
				doGet(request, response);
				e.printStackTrace();
			}
		}else if("removeProfessione".equals(action)) {
			String idProfessione=request.getParameter("idProfessioni");
			Long idRemoveProf=Long.parseLong(idProfessione);
			
			try {
				Professione professione=professioneDAO.findById(idRemoveProf);
				
				
				try {
					utenteDAO.removePro(utenteLoggato, professione);
					doGet(request, response);
				} catch (ProfessioneNonAssociataException e) {
					doGet(request, response);
					e.printStackTrace();
				}
			} catch (ProfessioneNonTrovataException e) {
				doGet(request, response);
				e.printStackTrace();
			}
			
		} else if ("addDisponibilita".equals(action)) {
			String dataStr = request.getParameter("dataDisp");
			String daStr   = request.getParameter("daDisp");
			String aStr    = request.getParameter("aDisp");
			try {
				LocalDate data = LocalDate.parse(dataStr);
				LocalTime da   = LocalTime.parse(daStr);
				LocalTime a    = LocalTime.parse(aStr);

				if (!data.isAfter(LocalDate.now().minusDays(1))) {
					request.setAttribute("messaggioDisp", "La data deve essere futura.");
					request.setAttribute("scrollTo", "disponibilita");
					doGet(request, response);
					return;
				}
				if (!da.isBefore(a)) {
					request.setAttribute("messaggioDisp", "L'ora di inizio deve essere precedente all'ora di fine.");
					request.setAttribute("scrollTo", "disponibilita");
					doGet(request, response);
					return;
				}
				
				Utente stub = new Utente();
				stub.setId(utenteLoggato.getId());

				Disponibilita nuova = new Disponibilita(data, da, a, stub);
				disponibilitaDAO.add(nuova);
				request.setAttribute("messaggioDisp", "Disponibilità aggiunta con successo.");
			} catch (DisponibilitaTimeOverlapException e) {
				request.setAttribute("messaggioDisp", "La fascia oraria si sovrappone a una già esistente.");
			} catch (Exception e) {
				request.setAttribute("messaggioDisp", "Dati non validi. Controlla data e orari.");
				e.printStackTrace();
			}
			request.setAttribute("scrollTo", "disponibilita");
			doGet(request, response);

		} else if ("removeDisponibilita".equals(action)) {
			String idDispStr = request.getParameter("idDisponibilita");
			try {
				Long idDisp = Long.parseLong(idDispStr);
				Disponibilita daRimuovere = new Disponibilita();
				daRimuovere.setId(idDisp);
				disponibilitaDAO.remove(daRimuovere);
			} catch (DisponibilitaNonTrovataException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			request.setAttribute("scrollTo", "disponibilita");
			doGet(request, response);
        } else if ("addDisponibilitaSettimanale".equals(action)) {
            String[] giorni = request.getParameterValues("giorni");
            if (giorni == null || giorni.length == 0) {
                request.setAttribute("messaggioDispSett", "Seleziona almeno un giorno.");
                request.setAttribute("scrollTo", "disponibilita");
                doGet(request, response);
                return;
            }

            int totaleCreate = 0;
            int totaleErrori = 0;
            StringBuilder sbErrori = new StringBuilder();

            LocalDate oggi = LocalDate.now();

            for (String giornoStr : giorni) {
                java.time.DayOfWeek dow;
                try {
                    dow = java.time.DayOfWeek.valueOf(giornoStr);
                } catch (IllegalArgumentException e) {
                    continue;
                }

                int slotIdx = 1;
                while (true) {
                    String daParam = "da_" + giornoStr + "_" + slotIdx;
                    String aParam  = "a_"  + giornoStr + "_" + slotIdx;
                    String daStr   = request.getParameter(daParam);
                    String aStr    = request.getParameter(aParam);
                    if (daStr == null || aStr == null || daStr.isBlank() || aStr.isBlank()) break;

                    LocalTime da, a;
                    try {
                        da = LocalTime.parse(daStr);
                        a  = LocalTime.parse(aStr);
                    } catch (Exception e) {
                        slotIdx++;
                        continue;
                    }

                    if (!da.isBefore(a)) {
                        sbErrori.append("Fascia non valida per " + giornoStr + " (inizio >= fine). ");
                        totaleErrori++;
                        slotIdx++;
                        continue;
                    }

                    for (int settimana = 0; settimana < 4; settimana++) {
                        LocalDate primaOccorrenza = oggi.plusDays(1);
                        while (primaOccorrenza.getDayOfWeek() != dow) {
                            primaOccorrenza = primaOccorrenza.plusDays(1);
                        }
                        LocalDate dataTarget = primaOccorrenza.plusWeeks(settimana);

                        Utente stub = new Utente();
                        stub.setId(utenteLoggato.getId());
                        Disponibilita nuova = new Disponibilita(dataTarget, da, a, stub);
                        try {
                            disponibilitaDAO.add(nuova);
                            totaleCreate++;
                        } catch (DisponibilitaTimeOverlapException e) {
                            sbErrori.append("Sovrapposizione il " + dataTarget + ". ");
                            totaleErrori++;
                        } catch (Exception e) {
                            sbErrori.append("Errore il " + dataTarget + ". ");
                            totaleErrori++;
                            e.printStackTrace();
                        }
                    }
                    slotIdx++;
                }
            }

            if (totaleCreate > 0 && totaleErrori == 0) {
                request.setAttribute("messaggioDispSett", "Operazione completata con successo: " + totaleCreate + " disponibilità create.");
            } else if (totaleCreate > 0) {
                request.setAttribute("messaggioDispSett", totaleCreate + " disponibilità create. Attenzione: " + sbErrori.toString().trim());
            } else {
                request.setAttribute("messaggioDispSett", "Nessuna disponibilità creata. " + sbErrori.toString().trim());
            }
            request.setAttribute("scrollTo", "disponibilita");
            doGet(request, response);
        }
	}
}