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
 * Klasse für Hausbesuche. Erweitert Einzeltermine (Treatment) um Fahrzeit und Distanz.
 * Findet immer im "Home" und nicht in regulären Räumen statt.
 */
public class Visit extends Treatment {
	private static final long serialVersionUID = -1368420908412387978L;

	/**
	 * Fahrzeit in Viertelstunden (45 Minuten Fahrzeit -> driveTime = 3)
	 */
	private int driveTime;

	/**
	 * Gefahrene Strecke in 100 Metern (1,5km -> driveDistance = 15)
	 */
	private int driveDistance;

	/** Konstruktor für Visit mit allen Parametern
	 * @param time Der Gesamtzeitraum des Termins, der geblockt wird
	 * @param employee Mitarbeiter, der den Hausbesuch durchführt
	 * @param home Hausobjekt. Im Wesentlichen als Platzhalter verwendet.
	 * @param activity Durchgeführte Behandlung
	 * @param patient Behandelter Patient
	 * @param selfPaid ob die Behandlung vom Patienten bezahlt werden soll
	 * @param driveTime Fahrzeit in Viertelstunden (45 Minuten Fahrzeit -> driveTime = 3)
	 * @param driveDistance Gefahrene Strecke in 100 Metern (1,5km -> driveDistance = 15)
	 */
	public Visit(TimeInterval time, Employee employee, Home home,
			Activity activity, Patient patient, boolean selfPaid,
			int driveTime, int driveDistance) {
		super(time, employee, home, activity, patient, selfPaid);
		this.driveTime = driveTime;
		this.driveDistance = driveDistance;
	}

	/**
	 * Fahrzeit in Viertelstunden
	 */
	public int getDriveTime() {
		return driveTime;
	}
	
	/**
	 * Fahrtzeit in Minuten
	 */
	public int getDriveTimeInMinutes() {
		return driveTime * 15;
	}

	/**
	 * Setze Fahrzeit in Viertelstunden (45 Minuten Fahrzeit -> driveTime = 3).
	 */
	public void setDriveTime(int driveTime) {
		this.driveTime = driveTime;
	}

	/**
	 * Die gefahrene Distanz in 100 Metern (1,5km -> driveDistance = 15)
	 */
	public int getDriveDistance() {
		return driveDistance;
	}
	
	/**
	 * Die gefahrene Distanz in Metern
	 */
	public int getDriveDistanceInMeters() {
		return driveDistance * 100;
	}

	/**
	 * Setze die gefahrene Distanz in 100 Metern (1,5km -> driveDistance = 15).
	 */
	public void setDriveDistance(int driveDistance) {
		this.driveDistance = driveDistance;
	}

}
