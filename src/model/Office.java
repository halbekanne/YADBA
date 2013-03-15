/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Die Praxis. Die Mutter aller Objekte!
 */
public class Office implements Serializable {
	private static final long serialVersionUID = 1834985926906233296L;

	/** Kontaktinformationen */
	private Contact contact;

	/**
	 * Öffnungszeiten, ein Array von 7 Zeitintervallen. Ist ein Eintrag null,
	 * ist die Praxis an diesem Tag geschlossen
	 */
	private TimeOfDayInterval[] openingTime = new TimeOfDayInterval[7];

	/** Liste aller Mitarbeiter der Praxis. */
	private List<Employee> employees;

	/** Liste aller Räume der Praxis. Enthält nur Räume, keine Homes. */
	private List<Room> rooms;

	/** Liste aller Behandlungen, die von der Praxis angeboten werden. */
	private List<Activity> activities;

	/** Liste aller Materialien, die von der Praxis verkauft werden. */
	private List<Material> materials;

	/** Liste aller Termine. */
	private List<Appointment> appointments;

	/** Liste aller Patienten. */
	private List<Patient> patients;

	/** Der Name der Praxis */
	private String name;

	/** Die Steuernummer der Praxis */
	private String taxNo;

	public Office() {
		this.employees = new LinkedList<Employee>();
		this.rooms = new LinkedList<Room>();
		this.activities = new LinkedList<Activity>();
		this.materials = new LinkedList<Material>();
		this.appointments = new LinkedList<Appointment>();
		this.patients = new LinkedList<Patient>();
	}

	/**
	 * Hole die Kontaktinformationen der Praxis.
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * Setze die Kontaktinformationen der Praxis
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * Hole das Array mit den Öffnungszeiten.
	 */
	public TimeOfDayInterval[] getOpeningTime() {
		return openingTime;
	}

	/**
	 * Setze das Array mit den Öffnungszeiten.
	 */
	public void setOpeningTime(TimeOfDayInterval[] openingTime) {
		this.openingTime = openingTime;
	}

	/**
	 * Testet, ob die Praxis zu einem bestimmten Zeitpunkt geöffnet ist
	 * 
	 * @param t
	 *            Der Zeitpunkt
	 * @return true, falls die Praxis geöffnet ist, sonst false
	 */
	public boolean isOpen(Date t) {
		int dayOfWeek = CalendarUtil.getDayOfWeek(t);

		if (openingTime[dayOfWeek] == null) {
			return false;
		}
		return (openingTime[dayOfWeek].contains(t));
	}

	/**
	 * Testet ob die Praxis in diesem Zeitintervall offen hat.
	 * 
	 * @param t
	 *            das Zeitintervall
	 * @return true, falls die Praxis geöffnet ist.
	 */
	public boolean isOpen(TimeInterval t) {
		return (CalendarUtil.sameDay(t.getBegin(), t.getEnd())
				&& this.isOpen(t.getBegin()) && this.isOpen(t.getEnd()));
	}

	/**
	 * Sucht den nächsten Zeitpunkt, ab dem die Praxis geöffnet ist, beginnend
	 * mit Zeitpunkt t
	 * 
	 * @param t
	 *            Ein Zeitpunkt
	 * @return Der nächste Zeitpunkt, nach t, an dem die Praxis geöffnet ist.
	 *         Null, falls die Praxis permanent geschlossen ist.
	 */
	public Date nextOpening(Date t) {
		if (isOpen(t)) {
			return t;
		}

		int dayOfWeek = CalendarUtil.getDayOfWeek(t);

		if (openingTime[dayOfWeek] != null
				&& CalendarUtil.standardizeDate(t).after(
						openingTime[dayOfWeek].getEnd())) { // Falls t nach den
															// Öffnungszeiten
															// ist, auf jeden
															// Fall einen Tag
															// weitergehen.
			dayOfWeek = (dayOfWeek + 1) % 7;
			t = CalendarUtil.getNextDay(t);
		}

		int counter = 0; // Zähler, um, für den Fall, das die Praxis permanent
							// geschlossen ist, nicht in einer Endlosschleife zu
							// landen
		while (openingTime[dayOfWeek] == null) { // Nächsten Tag finden, an dem
													// die Praxis geöffnet ist
			dayOfWeek = (dayOfWeek + 1) % 7;
			t = CalendarUtil.getNextDay(t);
			counter++;
			if (counter > 7) {
				return null;
			}
		}
		return CalendarUtil.combineDateTime(t,
				openingTime[dayOfWeek].getBegin());
	}

	/**
	 * Hole die Liste aller Mitarbeiter.
	 */
	public List<Employee> getEmployees() {
		return employees;
	}

	/**
	 * Setze die Liste aller Mitarbeiter.
	 */
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	/**
	 * Hole die Liste aller Räume.
	 */
	public List<Room> getRooms() {
		return rooms;
	}

	/**
	 * Setze die Liste aller Räume.
	 */
	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	/**
	 * Hole die Liste aller Behandlungen.
	 */
	public List<Activity> getActivities() {
		return activities;
	}

	/**
	 * Setze die Liste aller Behandlungen.
	 */
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	/**
	 * Hole die Liste aller Materialien.
	 */
	public List<Material> getMaterials() {
		return materials;
	}

	/**
	 * Setze die Liste aller Materialien.
	 */
	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}

	/**
	 * Hole die Liste aller Termine.
	 */
	public List<Appointment> getAppointments() {
		return appointments;
	}

	/**
	 * Setze die Liste aller Termine.
	 */
	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	/**
	 * Hole die Liste aller Patienten.
	 */
	public List<Patient> getPatients() {
		return patients;
	}

	/**
	 * Setze die Liste aller Patienten.
	 */
	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}

	/**
	 * Gibt den Namen der Praxis zurück.
	 * 
	 * @return Der Name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Namen der Praxis neu.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gibt die Steuernummer der Praxis zurück.
	 * 
	 * @return the taxNo
	 */
	public String getTaxNo() {
		return taxNo;
	}

	/**
	 * Setzt die Steuernummer der Praxis neu.
	 * 
	 * @param taxNo
	 *            the taxNo to set
	 */
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

}
