package controller;

import static org.junit.Assert.*;

import java.util.Date;

import model.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import view.Main;

public class OfficeControllerTest extends ControllerTest {
	@Before
	public void setUp() {
		Main.DEBUG_MODE = false;
	}

	@After
	public void tearDown() {
		Main.DEBUG_MODE = true;
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSetOpeningTime() {
		TimeOfDayInterval todi = new TimeOfDayInterval(new Date(0, 1, 1, 9, 0),
				new Date(0, 1, 1, 16, 0));
		TimeOfDayInterval[] ts6 = { todi, todi, todi, todi, todi, todi };
		int day = (new Date().getDay() + 6) % 7;
		TimeOfDayInterval[] ts7 = { todi, todi, todi, todi, todi, todi, todi };
		ts7[day] = null;
		TimeOfDayInterval[] ts8 = { todi, todi, todi, todi, todi, todi, todi,
				todi };
		TimeOfDayInterval[] ts = { todi, todi, todi, todi, todi, todi, todi };
		assertFalse("Zu wenig Tage", officeCtrl.setOpeningTime(ts6));
		assertFalse("Praxis zu, trotz Terminen", officeCtrl.setOpeningTime(ts7));
		assertFalse("Zu viele Tage", officeCtrl.setOpeningTime(ts8));
		assertTrue("Korrekte Werte nicht angenommen", officeCtrl
				.setOpeningTime(ts));
		for (int i = 0; i < 7; i++)
			assertTrue("Nicht korrekt uebernommen", officeCtrl.getOffice()
					.getOpeningTime()[i] == ts[i]);
	}

	// public boolean setOpeningTime(TimeOfDayInterval[] ts) {
	//
	//		
	// // Wenn Anfang und Ende des Termins in den Öffnungszeiten liegen => Ok.
	// // Wir kürzen das Ende um eine Minute, damit genaue Überlappungen möglich
	// sind.
	// if (!ts[day].contains(ap.getTime().getBegin()) ||
	// !ts[day].contains(CalendarUtil.addMinutes(ap.getTime().getEnd(), -1)))
	// return false;
	// }
	// this.office.setOpeningTime(ts);
	// return true;
	// }

	@Test
	public void testLogin() {
		Employee e = officeCtrl.getOffice().getEmployees().get(0);
		assertFalse("Passwortabfrage funktioniert nicht", officeCtrl.login(e
				.getFirstName()
				+ " " + e.getLastName(), "2"));
		assertFalse("Invaliden Namen akzeptiert", officeCtrl.login(e
				.getFirstName()
				+ " " + e.getLastName() + " " + e.getLastName(), "1"));
		assertTrue("Korrekte Anmeldung abgelehnt", officeCtrl.login(e
				.getFirstName()
				+ " " + e.getLastName(), "1"));
		assertTrue("Falschen Benutzer angemeldet", officeCtrl.getUser() == e);
		e = officeCtrl.getOffice().getEmployees().get(1);
		assertFalse("Doppelte Anmeldung akzeptiert", officeCtrl.login(e
				.getFirstName()
				+ " " + e.getLastName(), "1"));
	}

	@Test
	public void testLogout() {
		Employee e = officeCtrl.getOffice().getEmployees().get(0);
		officeCtrl.login(e.getFirstName() + " " + e.getLastName(), "1");
		assertTrue("Ausloggen fehlgeschlagen", officeCtrl.logout());
		assertTrue("Immer noch ein angemeldeter Mitarbeiter", officeCtrl
				.getUser() == null);
	}

	@Test
	public void testStartUp() {
		String def = System.getProperty("user.home") + "/data.yadba";
		if (!officeCtrl.getIOController().loadBackUp(def)) {
			assertFalse("Erstellen neuer Office fehlgeschlagen", officeCtrl
					.startUp());
		} else {
			assertTrue("Laden existierende Office fehlgeschlagen", officeCtrl
					.startUp());
		}
	}

	@Test
	public void testShutDown() {
		assertTrue("Nicht ordentlich gesichert", officeCtrl.shutDown());
	}

}
