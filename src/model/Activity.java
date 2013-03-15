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
 * Behandlung, die an einem Termin durchgeführt werden kann. Enthält zusätzlich zu den von Service geerbten Attributen (Name und Preis)
 * noch die Dauer der Behandlung in Minuten und die Anzahl der Sitzungen, die die Behandlung umfasst.
 */
public class Activity extends Service {
	private static final long serialVersionUID = -5272241295274216287L;

	/**
	 * Die Dauer der Behandlung in Minuten
	 */
	private int duration;

	/**
	 * Die Anzahl der Sitzungen (Termine) dieses Kurses
	 */
	private int numberOfSessions;

	/**
	 * Erstellt neue Behandlung mit allen Parametern
	 * @param name Der Name der Behandlung
	 * @param price Der Preis der Behandlung
	 * @param duration Die Dauer der Behandlung in Minuten
	 * @param numberOfSessions Die Anzahl der Sitzungen (Termine), die dieser Kurs beinhaltet 
	 */
	public Activity(String name, int price, int duration, int numberOfSessions){
		super(name, price);
		this.duration = duration;
		this.numberOfSessions = numberOfSessions;
	}
	
	/**
	 * Gebe die Dauer der Behandlung in Minuten zurück.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Setze die neue Dauer der Behandlung in Minuten.
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Gebe die Anzahl der Sitzungen (Termine), die dieser Kurs beinhaltet zurück.
	 */
	public int getNumberOfSessions() {
		return numberOfSessions;
	}

	/**
	 * Setze die Anzahl der Sitzungen (Termine), die dieser Kurs beinhaltet.
	 */
	public void setNumberOfSessions(int numberOfSessions) {
		this.numberOfSessions = numberOfSessions;
	}

	@Override
	public boolean validService(){
		return super.validService() && duration >= 5 && numberOfSessions >0;
	}
}
