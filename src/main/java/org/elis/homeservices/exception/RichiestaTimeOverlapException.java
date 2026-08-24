package org.elis.homeservices.exception;

@SuppressWarnings("serial")
public class RichiestaTimeOverlapException extends Exception {
	public RichiestaTimeOverlapException(String messaggio) {
		super(messaggio);
	}
}
