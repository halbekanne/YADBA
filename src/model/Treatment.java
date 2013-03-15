/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package model;

/**
 * Klasse für Einzeltermine mit einnem Patienten. Enthält zusätzlich zu den
 * Paramtern von PracticalAppointment eine Referenz auf den Patienten.
 */
public class Treatment extends PracticalAppointment {
	private static final long serialVersionUID = 9202189525558751966L;

	/**
	 * Patient, der an diesem Einzeltermin behandelt wird.
	 */
	private Patient patient;
	/**
	 * true, wenn das Treatment vom Patienten selbst bezahlt wird, ansonsten false
	 */
	private boolean selfPaid;

	/**
	 * Konstruktor für Einzeltermine mit allen Parametern
	 * 
	 * @param time Zeitraum der Behandlung
	 * @param employee Mitarbeiter, der die Behandlung durchführt
	 * @param room Raum, in dem die Behandlung durchgeführt wird
	 * @param activity Behandlung, die an diesem Termin durchgeführt wird
	 * @param patient Patient, der behandelt wird.
	 * @param selfPaid ob die Behandlung vom Patienten bezahlt werden soll
	 */
	public Treatment(TimeInterval time, Employee employee, Room room, Activity activity, Patient patient, boolean selfPaid) {
		super(time, employee, room, activity);
		this.patient = patient;
		this.selfPaid = selfPaid;
	}
	
	/**
	 * Der Patient, der behandelt wird.
	 */
	public Patient getPatient() {
		return patient;
	}

	/**
	 * Setze den Patienten, der behandlelt wird.
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 * Ob das Treatment selbst bezahlt werden soll
	 */
	public boolean isSelfPaid() {
		return selfPaid;
	}

	/**
	 * Setze ob das Treatment selbst bezahlt werden soll.
	 */
	public void setSelfPaid(boolean selfPaid) {
		this.selfPaid = selfPaid;
	}

	/**
	 * Testet, ob ein Einzeltermin rein formal vollständig ist, und keine Referenzen null sind.
	 * Es wird NICHT auf Kollisionen etc. getestet.
	 * @return true, falls alle Attribute des Termins ungleich null sind
	 */
	@Override
	public boolean validAppointment() {
		return super.validAppointment() && getPatient() != null;
	}
}
