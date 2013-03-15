/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package model;

import java.util.LinkedList;
import java.util.List;

/**
 * Ein Patient.
 */
public class Patient extends Person {
	private static final long serialVersionUID = 1430099668256257402L;

	/** Die Krankheiten des Patienten in Textform. */
	private String disease;

	/** Notizen zum Patienten. */
	private String notice;

	/** Ist der Patient privat versichert? */
	private boolean privat;

	/** Distanz zum Wohnort des Patienten in Kilometern. */
	private int distance;

	/** Noch nicht bezahlte Behandlungen des Patienten. */
	private List<Treatment> treatments;

	/** Schon bezahlte Behandlungen des Patienten. */
	private List<Treatment> treatmentsPaid;

	/** Noch nicht bezahlte Kurse des Patienten. */
	private List<Course> courses;

	/** Schon bezahlte Kurse des Patienten. */
	private List<Course> coursesPaid;

	/** Der Lieblingsmitarbeiter des Patienten. */
	private Employee preferredEmployee;
	
	/** Das vom Patienten gekaufte, aber nicht bezahlte Material */
	private List<Material> material;
	
	/** Das vom Patienten bezahlte Material */
	private List<Material> materialPaid;

	/**
	 * Erzeuge einen neuen Patienten
	 * 
	 * @param contact
	 *            Kontaktinformationen
	 * @param firstName
	 *            Vorname
	 * @param lastName
	 *            Nachname
	 * @param title
	 *            Titel
	 * @param disease
	 *            Krankheiten
	 * @param notice
	 *            Notizen
	 * @param privat
	 *            Ist der Patient privatversichert?
	 * @param distance
	 *            Distanz zum Wohnort in Kilometern
	 * @param preferredEmployee
	 *            Lieblingsmitarbeiter
	 */
	public Patient(Contact contact, String firstName, String lastName,
			String title, String disease, String notice, boolean privat,
			int distance, Employee preferredEmployee) {
		super(contact, firstName, lastName, title);
		this.disease = disease;
		this.notice = notice;
		this.privat = privat;
		this.distance = distance;
		this.treatments = new LinkedList<Treatment>();
		this.treatmentsPaid = new LinkedList<Treatment>();
		this.courses = new LinkedList<Course>();
		this.coursesPaid = new LinkedList<Course>();
		this.preferredEmployee = preferredEmployee;
		this.material = new LinkedList<Material>();
		this.materialPaid = new LinkedList<Material>();
	}

	/**
	 * Hole die Krankheiten des Patienten in Textform.
	 */
	public String getDisease() {
		return disease;
	}

	/**
	 * Setze die Krankheiten des Patienten in Textform.
	 */
	public void setDisease(String disease) {
		this.disease = disease;
	}

	/**
	 * Hole Notizen
	 */
	public String getNotice() {
		return notice;
	}

	/**
	 * Setze Notizen
	 */
	public void setNotice(String notice) {
		this.notice = notice;
	}

	/**
	 * Prüft, ob der Patient privatversichert ist.
	 */
	public boolean isPrivat() {
		return privat;
	}

	/**
	 * Setzt den Privatversicherungszustand des Patienten.
	 */
	public void setPrivat(boolean privat) {
		this.privat = privat;
	}

	/**
	 * Hole die Distanz zum Wohnort des Patienten in Kilometern.
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * Setze die Distanz zum Wohnort des Patienten in Kilometern.
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * Hole die Liste der noch nicht bezahlten Behandlungen des Patienten.
	 */
	public List<Treatment> getTreatments() {
		return treatments;
	}

	/**
	 * Setze die Liste der noch nicht bezahlten Behandlungen des Patienten.
	 */
	public void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}

	/**
	 * Hole die Liste der schon bezahlten Behandlungen des Patienten.
	 */
	public List<Treatment> getTreatmentsPaid() {
		return treatmentsPaid;
	}

	/**
	 * Setze die Liste der schon bezahlten Behandlungen des Patienten.
	 */
	public void setTreatmentsPaid(List<Treatment> treatmentsPaid) {
		this.treatmentsPaid = treatmentsPaid;
	}

	/**
	 * Hole die Liste der noch nicht bezahlten Kurse des Patienten.
	 */
	public List<Course> getCourses() {
		return courses;
	}

	/**
	 * Setze die Liste der noch nicht bezahlten Kurse des Patienten.
	 */
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	/**
	 * Hole die Liste der schon bezahlten Kurse des Patienten.
	 */
	public List<Course> getCoursesPaid() {
		return coursesPaid;
	}

	/**
	 * Setze die Liste der schon bezahlten Kurse des Patienten.
	 */
	public void setCoursesPaid(List<Course> coursesPaid) {
		this.coursesPaid = coursesPaid;
	}

	/**
	 * Hole den Lieblingsmitarbeiter des Patienten, oder <b>null</b>.
	 */
	public Employee getPreferredEmployee() {
		return preferredEmployee;
	}

	/**
	 * Setze den Lieblingsmitarbeiter des Patienten, oder <b>null</b>.
	 */
	public void setPreferredEmployee(Employee preferredEmployee) {
		this.preferredEmployee = preferredEmployee;
	}

	public List<Material> getMaterial() {
		return material;
	}

	public List<Material> getMaterialPaid() {
		return materialPaid;
	}

	@Override
	public boolean validPerson() {
		return (super.validPerson() && treatments != null
				&& treatmentsPaid != null && courses != null && coursesPaid != null
				&& material != null && materialPaid != null);
	}

	/**
	 * Testet, ob der Patient an einem bestimmten Zeitraum Zeit hat
	 * 
	 * @param t Der Zeitraum
	 * @return true, falls der Patient Zeit hat, sonst false
	 */
	public boolean isAvailable(TimeInterval t) {
		for (Appointment a : treatments) {
			if (a.getTime().overlapsWith(t)) {
				return false;
			}
		}
		for (Course c : courses) {
			Course nextCourse = c;
			while(nextCourse!=null){
				if (nextCourse.getTime().overlapsWith(t)) {
					return false;
				}
				nextCourse = nextCourse.getNextCourse();
			}
		}
		return true;
	}
}
