package com.ftn.owpproject.listeners;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

import com.ftn.owpproject.controller.KnjigeController;
import com.ftn.owpproject.model.Knjiga;

@Component
public class InitHttpSessionListener implements HttpSessionListener {

	/** kod koji se izvrsava po kreiranju sesije */
	public void sessionCreated(HttpSessionEvent arg0) {
		System.out.println("Inicijalizacija sesisje HttpSessionListener...");
//		
//		//pri kreiranju sesije inicijalizujemo je ili radimo neke dodatne aktivnosti
		List<Knjiga> zaIznajmljivanje = new ArrayList<Knjiga>();
		HttpSession session  = arg0.getSession();
		System.out.println("session id korisnika je "+session.getId());
		session.setAttribute(KnjigeController.KNJIGE_ZA_IZNAJMLJIVANJE, zaIznajmljivanje);
		
		
//		
		System.out.println("Uspeh HttpSessionListener!");
	}
	
	/** kod koji se izvrsava po brisanju sesije */
	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("Brisanje sesisje HttpSessionListener...");
		
		System.out.println("Uspeh HttpSessionListener!");
	}

}


