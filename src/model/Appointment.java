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

/**
 * Abstrakte Oberklasse für Termine. Enthält lediglich Mitarbeiter und Zeitraum.
 */
public abstract class Appointment implements Serializable {
	private static final long serialVersionUID = -4741820827953071117L;

	/**
	 * Das Zeitintervall dieses Termins
	 */
	private TimeInterval time;

	/**
	 * Der Mitarbeiter, der für den Termin zuständig ist.
	 */
	private Employee employee;

	/**
	 * Terminkonstruktor mit allen Parametern
	 * @param time Das Zeitintervall des Termins
	 * @param employee Der Mitarbeiter, der für den Termin zuständig ist
	 */
	public Appointment(TimeInterval time, Employee employee) {
		this.time = time;
		this.employee = employee;
	}

	/**
	 * Gebe das Zeitintervall des Termins zurück.
	 */
	public TimeInterval getTime() {
		return time;
	}

	/**
	 * Setze das Zeitintervall des Termins.
	 */
	public void setTime(TimeInterval time) {
		this.time = time;
	}

	/**
	 * Gebe den Mitarbeiter des Termins zurück.
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * Setze den Mitarbeiter, der für den Termin zuständig ist.
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	/**
	 * Testet, ob ein Termin rein formal vollständig ist, und keine Referenzen null sind.
	 * Es wird NICHT auf Kollisionen etc. getestet.
	 * @return true, falls alle Attribute des Termins ungleich null sind
	 */
	public boolean validAppointment(){
		return employee != null && time != null;
	}
	
	public String toString(){
		if (!validAppointment())
			return "Ungüliger Termin";
		else
			return CalendarUtil.dateTimeToString(time.getBegin()) + " - " + CalendarUtil.dateTimeToString(time.getEnd());
	}
}
