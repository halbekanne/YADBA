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
import java.util.LinkedList;
import java.util.List;

/**
 * Klasse für Räume mit Name, Kapazität, Behandlungsterminen
 */
public class Room implements Serializable {
	private static final long serialVersionUID = -3355289787605817945L;

	/**
	 * Der Name des Raums
	 */
	private String name;

	/**
	 * Die Kapazität des Raums (Anzahl der Patienten, die hier behandelt werden können)
	 */
	private int capacity;

	/**
	 * Liste der Behandlungstermine, die in diesem Raum stattfinden
	 */
	private List<PracticalAppointment> practicalAppointments;

	/**
	 * Liste der Behandlungen, die in diesem Raum durchgeführt werden können
	 */
	private List<Activity> activity;
	
	/**
	 * Konstruktor für Raum. Initialisiert die Liste der Behandlungstermine mit einer leeren LinkedList.
	 * @param name Der Name des Raums
	 * @param capacity Die Kapazität des Raums
	 * @param activity Liste der in diesem Raum möglichen Aktivitäten
	 */
	public Room(String name, int capacity, List<Activity> activity) {
		this.name = name;
		this.capacity = capacity;
		this.practicalAppointments = new LinkedList<PracticalAppointment>();
		this.activity = activity;
	}

	/**
	 * Name des Raums.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setze den Namen des Raums.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Kapazität des Raums
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Setze die Kapazität des Raums.
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Die Liste der hier stattfindenden Behandlungstermine
	 */
	public List<PracticalAppointment> getPracticalAppointments() {
		return practicalAppointments;
	}

	/**
	 * Die Liste der hier möglichen Behandlungen.
	 */
	public List<Activity> getActivity() {
		return activity;
	}
	

	/**
	 * Setze die Liste der hier möglichen Behandlungen.
	 */
	public void setActivity(List<Activity> activity) {
		this.activity = activity;
	}

	/**
	 * Testet ob ein Raum in einem bestimmten Zeitraum frei ist
	 * @param t Der Zeitraum
	 * @return true, falls der Raum frei ist, sonst false
	 */
	public boolean isAvailable(TimeInterval t) {
		for (Appointment a: practicalAppointments)
			if (a.getTime().overlapsWith(t))
				return false;
		return true;
	}

	/**
	 * Testet, ob eine Behandlung in diesem Raum durchgeführt werden kann
	 * @param a Die Behandlung, die getestet wird
	 * @return true, falls die Behandlung hier durchgeführt werden kann, sonst false
	 */
	public boolean isAble(Activity a) {
		return activity.contains(a);
	}
	
	/**
	 * Testet, ob ein Raum formal korrekt ist, d. h. keine Referenzen null sind
	 * @return true, falls alles in Ordnung ist, sonst false
	 */
	public boolean validRoom(){
		return practicalAppointments != null && activity != null && name != null && !name.isEmpty()&& capacity>0;
	}

}
