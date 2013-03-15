/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Ein Mitarbeiter.
 */
public class Employee extends Person {
	private static final long serialVersionUID = -4787847779879113501L;

	/**
	 * Passwort des Mitarbeiters. Gehasht als MD5.
	 */
	private byte[] password;

	/**
	 * Array von Zeitintervallen für die Arbeitszeiten. Ein Element steht für einen Wochentag
	 * Datum der Zeitpunkt im Zeitintervall wirdignoriert, es ist nur die Uhrzeit interessant
	 */
	private TimeOfDayInterval[] workingTime;

	/**
	 * Liste der Termine (Behandlungstermine und Fehlzeiten) des Mitarbeiters in beliebiger Reihenfolge
	 */
	private List<Appointment> appointments;

	/**
	 * Liste von Behandlungen, zu denen der Mitarbeiter qualifiziert ist.
	 */
	private List<Activity> activities;

	/**
	 * Rang des Mitarbeiters
	 */
	private Rank rank;

	public Employee(Contact contact, String firstName, String lastName,
			String title, String password, TimeOfDayInterval[] workingTime, List<Activity> activities, Rank rank) {
		super(contact, firstName, lastName, title);
		if (!password.isEmpty()){
			setPassword(password);
		} else {
			this.password = null;
		}
		this.workingTime = workingTime; 
		appointments = new LinkedList<Appointment>();
		this.activities = activities;
		this.rank = rank;
	}
	
	/**
	 * Hasht das Passwort mit dem MD5-Algorithmus.
	 * @param password Das zu hashende Passwort.
	 * @return Der Hash als byte-Array.
	 */
	private static byte[] getHash(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(password.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e1) {
		} catch (UnsupportedEncodingException e) {}
		throw new RuntimeException("Fatal error!");
	}

	/**
	 * Prüft ob das Passwort richtig ist.
	 * @return true, wenn die Passwörter übereinstimmen.
	 */
	public boolean checkPassword(String password) {
		byte[] hash = getHash(password);
		return Arrays.equals(this.password, hash);
	}

	/**
	 * Setze das Passwort.
	 */
	public void setPassword(String password) {
		this.password = getHash(password);
	}
	
	/**
	 * Zum Mitarbeiter ändern.
	 * @return Gehashtes Passwort
	 */
	public byte[] getPassword() {
		return password;
	}

	/**
	 * Setze das gehashte Passwort
	 */
	public void setPassword(byte[] password){
		this.password = password;
	}
	
	/**
	 * @return Die Arbeitszeiten als 7-elementiges Array von Zeitintervallen
	 */
	public TimeOfDayInterval[] getWorkingTime() {
		return workingTime;
	}

	/**
	 * @param workingTime Die Arbeitszeiten als 7-elementiges Array von Zeitintervallen
	 */
	public void setWorkingTime(TimeOfDayInterval[] workingTime) {
		this.workingTime = workingTime;
	}

	/**
	 * Testet ob der Mitarbeiter zu diesem Zeitpunkt arbeitet. Fehlzeiten werden nicht betrachtet.
	 * @param t der Zeitpunkt
	 * @return true, falls t in den Arbeitszeiten liegt
	 */
	public boolean isInWorkingTime(Date t){
		int dayOfWeek = CalendarUtil.getDayOfWeek(t);

		if (workingTime[dayOfWeek] == null)
			return false;
		return (workingTime[dayOfWeek].contains(t));
	}
	
	/**
	 * Testet ob der Mitarbeiter in diesem Zeitintervall arbeitet. Fehlzeiten werden nicht betrachtet.
	 * @param t das Zeitintervall
	 * @return true, falls t in den Arbeitszeiten liegt
	 */
	public boolean isInWorkingTime(TimeInterval t){
		return (CalendarUtil.sameDay(t.getBegin(), t.getEnd()) && 
				this.isInWorkingTime(t.getBegin()) && this.isInWorkingTime(t.getEnd()));
	}
	
	/**
	 * Liste der Behandlungstermine.
	 */
	public List<Appointment> getAppointments() {
		return appointments;
	}

	/**
	 * Liste der Behandlungen, zu denen Der Mitarbeiter qualifiziert ist.
	 */
	public List<Activity> getActivities() {
		return activities;
	}

	/**
	 * Setze die Liste der Behandlungen, zu denen Der Mitarbeiter qualifiziert ist.
	 */
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	/**
	 * Rang des Mitarbeiters
	 */
	public Rank getRank() {
		return rank;
	}

	/**
	 * Setze den Rang des Mitarbeiters.
	 */
	public void setRank(Rank rank) {
		this.rank = rank;
	}

	/**
	 * Testet, ob der Mitarbeiter an einem bestimmten Zeitraum zur Verfügung steht
	 * @param t Der Zeitraum
	 * @return true, falls der Mitarbeiter frei ist, sonst false
	 */
	public boolean isAvailable(TimeInterval t) {
		if (!isInWorkingTime(t))
			return false;
		for (Appointment a: appointments)
			if (a.getTime().overlapsWith(t))
				return false;
		return true;
	}


	/**
	 * Testet, ob der Mitarbeiter eine bestimmte Behandlung durchführen kann
	 * @param a Die Behandlung
	 * @return true, falls er die Behandlung durchführen kann, sonst false
	 */
	public boolean isAble(Activity a) {
		return activities.contains(a);
	}
	
	/**
	 * Testet, ob der Mitarbeiter formal korrekt ist, also alle Objekte korrekt initialisiert sind 
	 * @return true, falls alles in Ordnung ist
	 */
	public boolean validEmployee(){
		return !(appointments == null || activities == null || workingTime == null ||workingTime.length != 7);		
	}
}
