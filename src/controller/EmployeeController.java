/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package controller;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class EmployeeController {

	private OfficeController officeCtrl;
	
	public EmployeeController(OfficeController o){
		this.officeCtrl = o;
	}
	

	/**
	 * Trägt Mitarbeiter im Office ein
	 * @param e der Mitarbeiter
	 * @return true, wenns geklappt hat, false, wenn schon ein Mitarbeiter mit diesem Namen existiert
	 */
	public boolean saveEmployee(Employee e) {
		if (e==null)
			throw new NullPointerException();
		if (!e.validEmployee())
			return false;
		for (Employee employee : officeCtrl.getOffice().getEmployees())
			if (e.getFirstName().equals(employee.getFirstName()) && e.getLastName().equals(employee.getLastName()))
				return false;
		officeCtrl.getOffice().getEmployees().add(e);
		return true;

	}

	/**
	 * Versucht einen Mitarbeiter zu ändern.
	 * @param oldE Der Mitarbeiter
	 * @param newE Mitarbeiterobjekt, in dem die geänderten Daten gespeichert sind
	 * @return true, wenns geklappt hat, false, wenn Kollisionen entstehen würden und nichts geändert wurde
	 */
	public boolean editEmployee(Employee oldE, Employee newE) {
		if (newE == null || oldE == null)
			throw new NullPointerException();
		if (!oldE.validEmployee() || !newE.validEmployee())
			return false;
		
		if (oldE.getRank() == Rank.BOSS && newE.getRank() != Rank.BOSS){ // Wird eine Chefin entmachtet...
			boolean success = false;
			for (Employee e : officeCtrl.getOffice().getEmployees())
				if (!e.equals(oldE) && e.getRank() == Rank.BOSS) //Muss es mindestens eine andere Chefin geben
					success = true;
			if (!success)
				return false;
		}
		for (Appointment a : oldE.getAppointments()){
			if (a.getTime().compareToNow() == 1 && a instanceof PracticalAppointment){
				PracticalAppointment pracitcalA = (PracticalAppointment) a;
				if (!newE.isAvailable(pracitcalA.getTime()) || !newE.isAble(pracitcalA.getActivity())){
					return false;
				}
			}
		}
		oldE.setActivities(newE.getActivities());
		oldE.setContact(newE.getContact());
		oldE.setFirstName(newE.getFirstName());
		oldE.setLastName(newE.getLastName());
		if (newE.getPassword() != null)
			oldE.setPassword(newE.getPassword());
		oldE.setRank(newE.getRank());
		oldE.setTitle(newE.getTitle());
		oldE.setWorkingTime(newE.getWorkingTime());
		return true;
	}

	/**
	 * Löscht Mitarbeiter
	 * @param e der Mitarbeiter
	 * @return true, wenns geklappt hat
	 */
	public boolean removeEmployee(Employee e) {
		if (e == null)
			throw new NullPointerException();
		officeCtrl.getOffice().getEmployees().remove(e);
		return true;
	}

	/**
	 * Fügt einen neuen Fehlzeittermin hinzu.
	 * @param e der Mitarbeiter
	 * @param t der Zeitraum der Fehlzeit
	 * @return true, wenns geklappt hat, false, wenn der Termin Kollisionen erzeugen würde
	 */
	public boolean setAbsence(Employee e, TimeInterval t) {
		if (e == null || t == null)
			throw new NullPointerException();
		Vacation v = new Vacation(t, e);
		if (officeCtrl.getAppointmentController().collision(v))
			return false;
		e.getAppointments().add(v);
		return true;
	}

	/**
	 * Gibt die zum Zeitpunkt <b>t</b> verfügbaren Mitarbeiter, die <b>a</b>
	 * können zurück.
	 * 
	 * @param t
	 *            Zeitintervall in dem der Mitarbeiter verfügbar sein soll.
	 * @param a
	 *            Leistung die der Mitarbeiter beherrschen soll.
	 * @return Liste an Mitarbeitern.
	 */
	public List<Employee> getAvailableEmployees(TimeInterval t, Activity a) {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		for (Employee e : getAvailableEmployees(a))
			if (e.isAvailable(t))
				employees.add(e);
		return employees;
	}

	/**
	 * Gibt die Mitarbeiter zurück, die <b>a</b> können.
	 * 
	 * @param a
	 *            Leistung die der Mitarbeiter beherrschen soll.
	 * @return Liste an Mitarbeitern.
	 */
	public List<Employee> getAvailableEmployees(Activity a) {
		ArrayList<Employee> employees = new ArrayList<Employee>();
		for (Employee e : officeCtrl.getOffice()
				.getEmployees())
			if (e.isAble(a))
				employees.add(e);
		return employees;
	}

	/**
	 * Gibt für einen Mitarbeiter <b>e</b> alle Termine zurück, die das
	 * Intervall <b>t</b> betreffen.
	 * 
	 * @param e
	 *            Mitarbeiter
	 * @param t
	 *            Zeitintervall
	 * @return Liste an Terminen
	 */
	public List<Appointment> getAppointmentsInTime(Employee e, TimeInterval t) {
		if(e==null||t==null||!e.validEmployee())
			return null;
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		for (Appointment ap : e.getAppointments())
			if (ap.getTime().overlapsWith(t))
				appointments.add(ap);
		return appointments;
	}

	/**
	 * Gibt den Mitarbeiter mit Namen firstName lastName zurück.
	 * 
	 * @param firstName
	 *            Vorname des Mitarbeiters.
	 * @param lastName
	 *            Nachname des Mitarbeiters.
	 * @return Der Mitarbeiter oder null.
	 */
	public Employee getEmployeeByName(String firstName, String lastName) {
		for (Employee employee : officeCtrl.getOffice().getEmployees())
			if (employee.getFirstName().equals(firstName)
					&& employee.getLastName().equals(lastName))
				return employee;
		return null;
	}
	
	public boolean setWorkingTime(Employee e, TimeOfDayInterval[] interval){
		if (e == null || !e.validEmployee() || interval == null || interval.length != 7)
			return false;
		for (int i = 0; i < 7; ++i){
			if (interval[i] != null && !officeCtrl.getOffice().getOpeningTime()[i].contains(interval[i]))
				return false;
		}
		for (Appointment ap: e.getAppointments()) {
			// Ignoriere Fehlzeiten und Termine in der Vergangenheit.
			if (ap.getTime().compareToNow() < 0 || ap instanceof Vacation)
				continue;

			int day = CalendarUtil.getDayOfWeek(ap.getTime().getBegin());
			assert day == CalendarUtil.getDayOfWeek(ap.getTime().getEnd());
			
			// Wenn der Mitarbeiter frei hat, kann dort kein Termin liegen.
			if (interval[day] == null)
				return false;

			// Wenn Anfang und Ende des Termins in den Arbeitszeiten liegen => Ok.
			// Wir kürzen das Ende um eine Minute, damit genaue Überlappungen möglich sind.
			if (!interval[day].contains(ap.getTime().getBegin()) || !interval[day].contains(CalendarUtil.addMinutes(ap.getTime().getEnd(), -1)))
				return false;
		}
		e.setWorkingTime(interval);
		return true;
	}

}
