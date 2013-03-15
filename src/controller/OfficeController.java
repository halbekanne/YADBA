/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package controller;

import model.Appointment;
import model.CalendarUtil;
import model.Contact;
import model.Employee;
import model.Office;
import model.TimeOfDayInterval;
import view.Main;

public class OfficeController {
	/** Der Momentan eingeloggte Nutzer, oder <b>null</b>. */
	private Employee user;

	/** Die Praxis(tm). */
	private Office office;

	// Alle controller.
	private final AppointmentController appointmentController = new AppointmentController(
			this);
	private final EmployeeController employeeController = new EmployeeController(
			this);
	private final IOController ioController = new IOController(this);
	private final PatientController patientController = new PatientController(
			this);
	private final RoomController roomController = new RoomController(this);
	private final ServiceController serviceController = new ServiceController(
			this);

	public OfficeController() {
		this.office = new Office();
	}

	/** Gibt den AppointmentController für diese Instanz zurück. */
	public AppointmentController getAppointmentController() {
		return appointmentController;
	}

	/** Gibt den EmployeeController für diese Instanz zurück. */
	public EmployeeController getEmployeeController() {
		return employeeController;
	}

	/** Gibt den IOController für diese Instanz zurück. */
	public IOController getIOController() {
		return ioController;
	}

	/** Gibt den PatientController für diese Instanz zurück. */
	public PatientController getPatientController() {
		return patientController;
	}

	/** Gibt den RoomController für diese Instanz zurück. */
	public RoomController getRoomController() {
		return roomController;
	}

	/** Gibt den Servicecontroller für diese Instanz zurück */
	public ServiceController getServiceController() {
		return serviceController;
	}

	/**
	 * Ermittle den momentan eingeloggten Nutzer
	 * 
	 * @return Den Nutzer oder <b>null</b>, wenn niemand eingeloggt ist.
	 */
	public Employee getUser() {
		return user;
	}

	/**
	 * Gibt die Praxis zurück.
	 */
	public Office getOffice() {
		return office;
	}

	/**
	 * Die Praxis ersetzen.
	 */
	public void setOffice(Office office) {
		this.office = office;
	}

	/**
	 * Setzt die Öffnungszeiten für jede Woche, beginnend ab dieser.
	 * 
	 * @param ts
	 *            Ein Array von 7 Intervallen, für jeden Wochentag.
	 * @return true wenn erfolgreich gesetzt, sonst existieren Konflikte.
	 */
	public boolean setOpeningTime(TimeOfDayInterval[] ts) {

		if (ts.length != 7) {
			return false;
		}
		for (Appointment ap : office.getAppointments()) {
			// Ignoriere Termine in der Vergangenheit.
			if (ap.getTime().compareToNow() < 0) {
				continue;
			}

			int day = CalendarUtil.getDayOfWeek(ap.getTime().getBegin());
			assert day == CalendarUtil.getDayOfWeek(ap.getTime().getEnd());

			// Wenn geschlossen ist, kann dort kein Termin liegen.
			if (ts[day] == null) {
				return false;
			}

			// Wenn Anfang und Ende des Termins in den Öffnungszeiten liegen =>
			// Ok.
			// Wir kürzen das Ende um eine Minute, damit genaue Überlappungen
			// möglich sind.
			if (!ts[day].contains(ap.getTime().getBegin())
					|| !ts[day].contains(CalendarUtil.addMinutes(ap.getTime()
							.getEnd(), -1))) {
				return false;
			}
		}
		this.office.setOpeningTime(ts);
		return true;
	}

	/**
	 * Logt einen Nutzer nach Prüfung von Name und Passwort ein.
	 * 
	 * @param employee
	 *            Nutzername in der Form "vorname nachname"
	 * @param password
	 *            Das zu prüfende Passwort
	 * @return true wenn der Login erfolgreich war.
	 */
	public boolean login(String employee, String password) {
		if (Main.DEBUG_MODE) {
			this.user = new Employee(null, employee, "", "", "", null, null,
					null);
			return true;
		}
		// Schon jemand eingeloggt?
		if (this.user != null) {
			return false;
		}

		// Nach vorname nachname trennen.
		String[] elements = employee.split(" ");
		if (elements.length != 2) {
			return false;
		}

		// Prüfen, ob das Passwort stimmt.
		Employee e = getEmployeeController().getEmployeeByName(elements[0],
				elements[1]);
		if (e == null || !e.checkPassword(password)) {
			return false;
		}

		// Angemeldeten Nutzer merken.
		this.user = e;
		return true;
	}

	/**
	 * Logt den aktuellen Nutzer aus.
	 * 
	 * @return true
	 */
	public boolean logout() {
		this.user = null;
		return true;
	}

	/**
	 * Holt den Standardpfad für das Speichern der Datenbasis.
	 */
	private static String getDefaultDatabaseLocation() {
		return System.getProperty("user.home") + "/data.yadba";
	}

	/**
	 * Lädt die Datenbasis beim starten des Programms.
	 * 
	 * @return true, wenn das Laden erfolgreich war.
	 */
	public boolean startUp() {
		if (!getIOController().loadBackUp(getDefaultDatabaseLocation())) {
			office = new Office();
			return false;
		}
		return true;
	}

	/**
	 * Speichert die Datenbasis beim Beeenden des Programms.
	 * 
	 * @return true, wenn das Speichern erfolgreich war.
	 */
	public boolean shutDown() {
		return getIOController().saveBackUp(getDefaultDatabaseLocation());
	}

	/**
	 * Setzt die Kontaktinformationen der Praxis neu.
	 * 
	 * @param name
	 *            Der neue Name der Praxis.
	 * @param taxNo
	 *            Die neue Steuernummer der Praxis.
	 * @param newContact
	 *            Die neuen Kontaktinformationen.
	 * @return true, wenn die neue Kontaktinformationen &uuml;bernommen wurde.
	 */
	public boolean editOffice(String name, String taxNo, Contact newContact) {
		office.setName(name);
		office.setTaxNo(taxNo);
		office.setContact(newContact);
		return true;
	}

}
