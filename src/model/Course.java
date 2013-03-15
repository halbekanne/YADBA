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
 * Klasse für Termine von Kursen, an denen mehrere Patienten teilnehmen und die über mehrere Termine gehen.
 * Hat Referenzen auf den nächsten und den vorherigen Termin des Kurses und eine Liste von teilnehmenden Patienten.
 */
public class Course extends PracticalAppointment {
	private static final long serialVersionUID = 8315293538697773398L;

	//TODO Sicherstellten, dass alle Termine in der Reihe sich auf den gleichen Kurs beziehen,
	//also zumindest alle die gleiche Behandlung haben 
	
	/**
	 * Der nächste Termin in diesem Kurs
	 */
	private Course nextCourse;
	
	/**
	 * Der vorherige Termin in diesem Kurs.
	 */
	private Course prevCourse;

	/**
	 * Die Patienten, die an diesem Kurs teilnehmen
	 */
	private List<Patient> patients;

	/**
	 * Konstruktor für Kurs mit allen Parametern außer Patienten. Diese werden erst nachträglich hinzugefügt.
	 * @param time Zeitraum des Kurses
	 * @param employee Mitarbeiter, der die Behandlung durchführt
	 * @param room Raum, in dem die Behandlung durchgeführt wird
	 * @param activity Behandlung, die an diesem Termin durchgeführt wird
	 * @param prevCourse Der verherige Termin dieses Kurses
	 * @param nextCourse Der nächste Termin dieses Kurses
	 */
	public Course(TimeInterval time, Employee employee, Room room,
			Activity activity, Course prevCourse, Course nextCourse) {
		super(time, employee, room, activity);
		this.prevCourse = prevCourse;
		this.nextCourse = nextCourse;
		this.patients = new LinkedList<Patient>();
	}

	/**
	 * Der nächste Termin des Kurses. Handelt es sich um den letzten Termin des Kurses, wird null zurückgegeben.
	 */
	public Course getNextCourse() {
		return nextCourse;
	}

	/**
	 * Setze den nächste Termin des Kurses.
	 */
	public void setNextCourse(Course nextCourse) {
		this.nextCourse = nextCourse;
	}

	/**
	 * Der vorherige Termin des Kurses. Handelt es sich um den ersten Termin des Kurses, wird null zurückgegeben.
	 */
	public Course getPrevCourse() {
		return prevCourse;
	}

	/**
	 * Setze den vorherige Termin des Kurses. 
	 */
	public void setPrevCourse(Course prevCourse) {
		this.prevCourse = prevCourse;
	}

	/**
	 * Die Liste der Patienten
	 */
	public List<Patient> getPatients() {
		return patients;
	}

	/**
	 * Fügt neuen Patienten zu diesem Termin hinzu
	 * @param patient Der neue Patient.
	 */
	public void addPatient(Patient patient) {
		this.patients.add(patient);
	}

	//TODO andere sinnvolle Implementierung: dass leere Kurse erzeugt werden koennen und die Verkettung getestet wird, ...
	@Override
	public boolean validAppointment() {
		if ((!super.validAppointment() || patients == null)) //Leerer Kurs ist valid, da patients mit leerer Liste initialisiert wird
			return false;
		Course first=this;
		while (first.getPrevCourse() != null)
			first=first.getPrevCourse();
		Course temp = first;
		while(temp != null){
			if (temp.getPatients() == null || temp.getEmployee() == null || temp.getActivity() == null)
				return false;
			if (!temp.getEmployee().equals(first.getEmployee()) || 
				!temp.getActivity().equals(first.getActivity())){
					return false;
			}
			temp = temp.getNextCourse();
		}
		return true;
	}
	
	
	public String toString(){
		return this.getActivity().getName()+" ("+CalendarUtil.dateTimeToString(this.getTime().getBegin())+")";
//		return this.getActivity().getName()+" am "+this.getTime().getBegin().getDate()+"."+this.getTime().getBegin().getMonth()+". - "+CalendarUtil.getHourMinuteString(this.getTime().getBegin());
	}
}
