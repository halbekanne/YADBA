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
 * Abstrakte Oberklasse für Behandlungstermine. Enthält zusätzlich zu den von Appointment geerbtern Attributen (Zeitraum, Mitarbeiter)
 * einen Raum und eine Behandlung, aber noch keine(n) Patienten. Dies kommt erst in den Unterklassen für Einkeltermine und Kurse dazu.
 */
public abstract class PracticalAppointment extends Appointment {
	private static final long serialVersionUID = -985192734404834669L;

	/**
	 * Raum, in dem der Behandlungstermin stattfindet
	 */
	private Room room;

	/**
	 * Die Behandlung, die während des Termins durchgeführt wird
	 */
	private Activity activity;
	
	/**
	 * True, falls der Termin noch offen ist, false, falls er abgearbeitet wurde
	 */
	private boolean open;

	/**
	 * Konstruktor für Behandlungstermine mit allen Parametern. Erzeugt nur das Objekt,
	 * es wird nicht in den entsprechenden Listen registriert.
	 * @param time Das Zeitintervall des Behandlungstermins
	 * @param employee Der Mitarbeiter, der die Behandlung durchführt
	 * @param room Der Raum, in dem der Behandlungstermin stattfindet
	 * @param activity Die Behandlung, die durchgeführt wird
	 */
	public PracticalAppointment(TimeInterval time, Employee employee,
			Room room, Activity activity) {
		super(time, employee);
		this.room = room;
		this.activity = activity;
		open = true;
	}

	/**
	 * Der Raum, in dem der Behandlungstermin stattfindet.
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * Setze den Raum, in dem der Behandlungstermin stattfindet.
	 */
	public void setRoom(Room room) {
		this.room = room;
	}

	/**
	 * Die Behandlung, die während des Termins durchgeführt wird.
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * Setze die Behandlung, die während des Termins durchgeführt wird.
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	/**
	 * Gibt true zurück, falls der Termin offen ist, sonst false.
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Setze, ob der Termin offen ist.
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}

	@Override
	public boolean validAppointment() {
		return super.validAppointment() && getRoom() != null && getActivity() != null;
	}

}
