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
import java.util.LinkedList;
import java.util.List;
import java.util.Date;
import model.*;

public class AppointmentController {

	private OfficeController o;
	
	public AppointmentController(OfficeController o){
		this.o = o;
	}
	
	/**
	 * Fügt eine Einzelbehandlung in die entsprechenden Listen des Mitarbeiters, des Patienten und das Raums ein. Ein Termin, für den diese Methode
	 * nicht aufgerufen wird, existiert im Grunde für den Rest des Programms nicht. Testet, ob der Termin alle nötigen Attribute enthält und ruft
	 * collision auf
	 * 
	 * @param t Der Einzeltermin
	 * @return true, falls der Termin erfolgreich gespeichert wurde, false, falls es Konflikte/Probleme gab und der Termin nicht registriet wurde
	 */
	public boolean saveTreatment(Treatment t) {
		if (!t.validAppointment())
			return false;
		if (collision(t))
			return false;
		t.getEmployee().getAppointments().add(t);
		t.getPatient().getTreatments().add(t);
		t.getRoom().getPracticalAppointments().add(t);
		o.getOffice().getAppointments().add(t);
		return true;
	}

	/**
	 * Speichert einen Kurs in den diversen Listen ab
	 * @param c der Kurs
	 * @return true, falls das speichern erfolgreich war, false, falls nichts passiert ist
	 */
	public boolean saveCourse(Course c) {
		if (!c.validAppointment()){
			System.out.println("notvalid");
			return false;
		}
		if (collision(c)){
			System.out.println("collision");
			return false;
		}
		c.getEmployee().getAppointments().add(c);
		// eigentlich sollten keine Patienten im Kurs gespeichert sein
		for(Patient p : c.getPatients()){
			p.getCourses().add(c);
		}
		c.getRoom().getPracticalAppointments().add(c);
		o.getOffice().getAppointments().add(c);
		return true;
	}

	/**
	 * Erzeugt ein neues Course-Objekt, an welchem eine Kette von 9 weiteren Course-Objekten haengt. 
	 * Es wird also eine Reihe von 10 Kursen erzeugt, die periodisch zu den Zeitpunkten first 
	 * und wenn vorhanden second woechentlich stattfinden.
	 * 
	 * @param first der Zeitpunkt des ersten Course in der anfangs Woche
	 * @param second der Zeitpunkt des zweiten Course in der anfangWoche. 
	 * Kann null sein, wenn der Kurs nur einmal pro Wochen stattfinden soll.
	 * @param lengthInMinutes gibt die Dauer der Activity an
	 * @param e der Employee, der den Course anbieten soll
	 * @param a die Activity die angeboten werden soll
	 * @return Course, wenn erfolgreich, ansonsten null
	 */
	public Course newCourse(Date first, Date second, int lengthInMinutes, Employee e, Activity a, int minKapa) {
		if(first!=null) {
			Office office = o.getOffice();
			int n = a.getNumberOfSessions();
			if (n < 1)
				return null;
			if(second==null) {
				//lege n Kurse fuer 10 Wochen an
				
				// Oeffnungszeiten checken
				TimeInterval t = new TimeInterval(first, lengthInMinutes);
				if(!office.isOpen(t.getBegin()) || !office.isOpen(t.getEnd())){
					return null;
				}
				
				Course course = null;
				Room room = null;
				
				// beliebigen Raum waehlen
				List<Room> rooms = o.getRoomController().getAvailableRooms(t, a);
				if (rooms == null)
					return null;
				for (Room r : rooms){
					if (r.getCapacity() >= minKapa){
						room = r;
						break;
					}
				}
				if (room == null){
					return null;
				}
				
				// Mitarbeiter checken
				if(!e.isAble(a) || !e.isAvailable(t)){
					return null;
				}
				
				// Ersten Kurs anlegen
				course = new Course(t, e, room, a, null, null);
				
				// naechsten 9 Wochen anlegen/testen
				for(int i = 0; i<n-1; ++i){
					Date startNew = CalendarUtil.increaseDay(t.getBegin(), 7);
					t = new TimeInterval(startNew, lengthInMinutes);
					
					// beliebigen Raum waehlen
					rooms = o.getRoomController().getAvailableRooms(t, a);
					if (rooms == null)
						return null;
					for (Room r : rooms){
						if (r.getCapacity() >= minKapa){
							room = r;
							break;
						}
					}
					if (room == null){
						return null;
					}
					
					if(!e.isAvailable(t)){
						return null;
					}
					
					// springe zum letzten Course der ReferenzKette
					Course lastRef = course;
					while(lastRef.getNextCourse()!=null){
						lastRef = lastRef.getNextCourse();
					}
					
					// neuen Kurs als letztes Element in die ReferenzKette einfuegen
					Course newCourse = new Course(t, e, room, a, lastRef, null);
					lastRef.setNextCourse(newCourse);
				}
				
				return course;
				
			} else if(first.before(second)) {
				if (n < 2)
					return null;
				//lege n Kurse fuer n/2 Wochen an
				
				// Oeffnungszeiten checken
				TimeInterval termin1 = new TimeInterval(first, lengthInMinutes);
				if(!office.isOpen(termin1.getBegin()) || !office.isOpen(termin1.getEnd())){
					return null;
				}
				TimeInterval termin2 = new TimeInterval(second, lengthInMinutes);
				if(!office.isOpen(termin2.getBegin()) || !office.isOpen(termin2.getEnd())){
					return null;
				}
				
				Course course = null;
				Course course2 = null;
				Room room1 = null;
				Room room2 = null;
				
				// beliebigen Raum waehlen
				List<Room> rooms = o.getRoomController().getAvailableRooms(termin1, a);
				if (rooms == null)
					return null;
				for (Room r : rooms){
					if (r.getCapacity() >= minKapa){
						room1 = r;
						break;
					}
				}
				if (room1 == null){
					return null;
				}
				
				// beliebigen Raum waehlen
				rooms = o.getRoomController().getAvailableRooms(termin2, a);
				if (rooms == null)
					return null;
				for (Room r : rooms){
					if (r.getCapacity() >= minKapa){
						room2 = r;
						break;
					}
				}
				if (room2 == null){
					return null;
				}
				
				
				// Mitarbeiter checken
				if(!e.isAble(a) || !e.isAvailable(termin1) || !e.isAvailable(termin2)){
					return null;
				}
				
				// Ersten Kurs anlegen
				course = new Course(termin1, e, room1, a, null, null);
				course2 = new Course(termin2, e, room2, a, null, null);
				course2.setPrevCourse(course);
				course.setNextCourse(course2);
				
				// naechsten (n/2)-1 Wochen anlegen/testen
				for(int i = 0; i<((n%2==0)?(n/2)-1:n/2); ++i){
					boolean lastWeekOneAppointment = (n%2==1 && i == (n/2)-1);
					Date startNew1 = CalendarUtil.increaseDay(termin1.getBegin(), 7);
					Date startNew2 = CalendarUtil.increaseDay(termin2.getBegin(), 7);
					
					termin1 = new TimeInterval(startNew1, lengthInMinutes);
					termin2 = new TimeInterval(startNew2, lengthInMinutes);
					
					// beliebigen Raum waehlen
					rooms = o.getRoomController().getAvailableRooms(termin1, a);
					if (rooms == null)
						return null;
					for (Room r : rooms){
						if (r.getCapacity() >= minKapa){
							room1 = r;
							break;
						}
					}
					if (room1 == null){
						return null;
					}
					
					// beliebigen Raum waehlen
					rooms = o.getRoomController().getAvailableRooms(termin2, a);
					if (rooms == null)
						return null;
					for (Room r : rooms){
						if (r.getCapacity() >= minKapa){
							room2 = r;
							break;
						}
					}
					if (room2 == null){
						return null;
					}
					
					if(!e.isAvailable(termin1) || (!lastWeekOneAppointment && !e.isAvailable(termin2))){
						return null;
					}
					
					// springe zum letzten Course der ReferenzKette
					Course lastRef = course;
					while(lastRef.getNextCourse()!=null){
						lastRef = lastRef.getNextCourse();
					}
					
					// neuen Kurs1 als letztes Element in die ReferenzKette einfuegen
					Course newCourse1 = new Course(termin1, e, room1, a, lastRef, null);
					lastRef.setNextCourse(newCourse1);
					
					if (!lastWeekOneAppointment){
						//neuen Kurs2 als letztes Element in die ReferenzKette einfuegen
						Course newCourse2 = new Course(termin2, e, room2, a, newCourse1, null);
						newCourse1.setNextCourse(newCourse2);
					}
				}
				
				return course;
			}
		}
		return null;
	}

	/**
	 * Schlägt mögliche Einzeltermine für gegebene Rahmenbedingungen vor.
	 * @param start Der frühestmögliche Startzeitpunkt des Termins
	 * @param end Der spätestmögliche STARTzeitpunkt des Termins
	 * @param length Die Länge des gewünschten Termins
	 * @param a Die durchzuführende Behandlung
	 * @param p Der zu behandelnde Patient
	 * @param e Der gewünschte Mitarbeiter oder null, falls ein beliebiger Mitarbeiter gewählt werden soll
	 * @return LinkedList von Treatments. Für jeden Zeitpunkt zwischen start und end im Abstand von 5 Minuten
	 * wird, falls kollisionsfrei möglich, ein Terminobjekt erstellt (aber nicht gespeichert)
	 * Maximal werden 100 Termine vorgeschlagen
	 */
	public List<Treatment> proposeTreatments(Date start, Date end, int length, Activity a, Patient p, Employee e, boolean visit, List<Treatment> vorgemerkt) {
		if (start.compareTo(end) >= 0 || length < 5 || a == null || p == null)
			return null;
		LinkedList<Treatment> vorschlaege = new LinkedList<Treatment>();

		// Initialisierte t mit start, oder, falls die Praxis da nicht geöffnet ist, mit der nächsten Öffnungszeit
		start = CalendarUtil.getNextSlot(start);
		end = CalendarUtil.getPrevSlot(end);
		Date t = o.getOffice().nextOpening(start);
		if (t == null) // Praxis ist permanent geschlossen -> Leere Liste zurückgeben
			return vorschlaege;
		
		// Begrenze die Anzahl der Vorschläge auf 100.
		while (t.compareTo(end) < 0 && vorschlaege.size() < 100) {
			TimeInterval timeframe = new TimeInterval(t, length);
			boolean possible = true;
			for (Treatment vorgemerktT : vorgemerkt)
				if (vorgemerktT.getTime().overlapsWith(timeframe) && vorgemerktT.getPatient().equals(p))
					possible = false;
			if (!o.getOffice().isOpen(timeframe.getEnd())) { // timeframe liegt außerhalb der Öffnungszeiten oder Patient hat dort keine Zeit
				t = o.getOffice().nextOpening(timeframe.getEnd()); // Zum nächsten Öffnungszeitpunkt springen
				continue;
			}
			List<Room> rooms;
			if (!visit){
				List<Room> roomsRaw = o.getRoomController().getAvailableRooms(timeframe, a);
				rooms = new LinkedList<Room>(roomsRaw);
				for (Room r : roomsRaw)
					for (Treatment vorgemerktT : vorgemerkt)
						if (vorgemerktT.getTime().overlapsWith(timeframe) && vorgemerktT.getRoom().equals(r))
							rooms.remove(r);
			} else { // Erstelle für Hausbesuch neues Haus
				rooms = new LinkedList<Room>();
				rooms.add(new Home(o.getOffice().getActivities()));
			}
			List<Employee> employeesRaw = o.getEmployeeController().getAvailableEmployees(timeframe, a);
			List<Employee> employees = new LinkedList<Employee>(employeesRaw);
			for (Employee emp : employeesRaw)
				for (Treatment vorgemerktT : vorgemerkt)
					if (vorgemerktT.getTime().overlapsWith(timeframe) && vorgemerktT.getEmployee().equals(emp))
						employees.remove(emp);
			if (!rooms.isEmpty() && p.isAvailable(timeframe) && possible && !employees.isEmpty() && (e == null || employees.contains(e))) { // Termin möglich
				Employee employee = e == null ? employees.get(0) : e; // Bevorzugten oder erstbesten Mitarbeiter verwenden
				if (!visit){
					vorschlaege.add(new Treatment(timeframe, employee, rooms.get(0), a, p, false));
				} else {
					vorschlaege.add(new Visit(timeframe, employee, (Home) rooms.get(0), a, p, false, 0, 0)); //Fahrzeit und Distanz wird von GUI gesetzt
				}
			}
			t = CalendarUtil.addMinutes(t, 5); // Vorschäge im 5 Minuten-Abstand erstellen, evtl. zu dicht
		}

		return vorschlaege;
	}

	/**
	 * Speichert einen Fehlzeittermin ab
	 * @param v Ein Fehlzeittermin
	 * @return true, wenns geklappt hat, false, falls Kollisionen vorkommen oder v ungültig ist.
	 */
	public boolean saveVacation(Vacation v){
		if (v == null || !v.validAppointment())
			return false;
		if (collision(v))
			return false;
		o.getOffice().getAppointments().add(v);
		v.getEmployee().getAppointments().add(v);
		return true;
	}
	
	/**
	 * Löscht einen Urlaubstermin
	 * 
	 * @param v Der Urlaubstermin
	 * @return false, falls der übergebene Termin fehlerhaft war, sonst true
	 */
	public boolean removeVacation(Vacation v) {
		if (v == null || !v.validAppointment())
			return false;
		v.getEmployee().getAppointments().remove(v);
		o.getOffice().getAppointments().remove(v);
		return true;
	}

	/**
	 * Versucht einen Einzeltermin zu ändern. Dazu wird versucht, den alten Termin zu löschen
	 * und den neuen zu speichern.
	 * @param oldT Der Termin, der ersetzt werden soll
	 * @param newT Der Termin, der gespeichert werden soll
	 * @return true, falls der Termin ersetzt wurde, false, falls es einen Fehler gab und nichts passiert ist.
	 */
	public boolean editTreatment(Treatment oldT, Treatment newT) {
		if (oldT == null || newT == null || !oldT.validAppointment() || !newT.validAppointment())
			return false;
		if (!removeTreatment(oldT)) //Alten Termin entfernen
			return false;
		if (collision(newT)){ //Schauen, ob neuer Termin kollidiert
			if (!saveTreatment(oldT)) //Kollision: Alten Termin wieder abspeichern und abbrechen
				throw new RuntimeException("Interner Fehler bei Edit Treatment - Alter Termin konnte nicht wieder registriert werden");
			return false;
		}else{ // Keine Kollision: Neuen Termin abspeichern
			saveTreatment(newT);
			return true;
		}
	}

	/**
	 * Testet, ob ein Termin zeitlich in irgendeiner Form kollidiert, d.h., je nach Termintyp, ob
	 * - der Mitarbeiter Zeit hat
	 * - der Mitarbeiter qualifiziert ist
	 * - der Raum "qualifiziert" ist
	 * - der Termin in den Öffnungszeiten liegt
	 * - der Termin in den Arbeitszeiten des Mitarbeiters liegt
	 * - der Raum frei ist
	 * @param a der Termin
	 * @return true, falls es eine Kollision gibt, false, falls der Termin so wie er ist gespeichert werden kann
	 */
	public boolean collision(Appointment a) {
		if (a == null || !a.validAppointment())
			throw new NullPointerException();

		for (Appointment appointment : a.getEmployee().getAppointments()){ //Konflikt mit Terminen des Mitarbeiters
			if (appointment.getTime().overlapsWith(a.getTime())) // Einziger für Urlaubszeiten nötiger Test
				return true;
		}
		if (a instanceof PracticalAppointment){
			if (!o.getOffice().isOpen(a.getTime())) //liegt der Termin in den Öffnungszeiten?
				return true;
			if (!a.getEmployee().isInWorkingTime(a.getTime())) // liegt der Termin in den Arbeitszeiten?
				return true;
			PracticalAppointment practicalA = (PracticalAppointment)a;
			if (!practicalA.getEmployee().isAble(practicalA.getActivity())) //Mitarbeiter qualifiziert
				return true;
			if (!practicalA.getRoom().isAble(practicalA.getActivity())) //Raum "qualifiziert"
				return true;
			if (!(a instanceof Visit)){ // Ein Hausbesuch kann keine Raumkollision haben
				for (PracticalAppointment appointment : practicalA.getRoom().getPracticalAppointments()){ //Konflikt mit Raum
					if (appointment.getTime().overlapsWith(practicalA.getTime()))
						return true;
				}
			}
			//Patient wird nicht getestet ist, weil das dessen Problem ist und nur Probleme macht...
		}
		return false;
	}

	/**
	 * Testet, ob noch Plätze in einem Kurs frei sind, <br>
	 * sprich die Kapazität der Räume ausreicht und ob der Patient Zeit hat
	 * @param c Der erste Termin des Kurses
	 * @param p Der Patient
	 * @return true, falls noch mindestend ein Platz frei ist, sonst false
	 */
	public boolean spotLeft(Course c, Patient p) {
		if (c==null|| !c.validAppointment() || c.getPrevCourse()!=null){
			return false;
		}
		while (c!= null){
			if (c.getRoom().getCapacity() <= c.getPatients().size()){
				return false;
			}
			if(!p.isAvailable(c.getTime())){
				return false;
			}
			c = c.getNextCourse();
		}
		return true;
	}

	/**
	 * Schließt einen Behandlungstermin ab
	 * @param a der Behandlungstermin
	 * @return false, falls es sich um einen ungültigen Termin handelt
	 */
	public boolean close(PracticalAppointment a) {
		if (a==null||!a.validAppointment())
			return false;
		a.setOpen(false);
		return true;
	}

	/**
	 * Gibt die Termine zurück, die mit dem Zeitintervall <b>t</b> überlappen.
	 * 
	 * @param t Zeitinterval, nach dem gesucht werden soll.
	 * @return Liste von Terminen.
	 */
	public List<Appointment> getAppointmentsInTime(TimeInterval t) {

		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		if (t==null)
			return appointments;
		for (Appointment ap : o.getOffice().getAppointments())
			if (ap.getTime().overlapsWith(t))
				appointments.add(ap);
		return appointments;
	}

	/**
	 * Entfernt eine Einzelbehandlung aus allen Listen
	 * @preconditions Die Behandlung muss valide sein und in der Zukunft liegen
	 * @param t Die Einzelbehandlung
	 * @return false, falls die Behandlung die Vorbedingungen nicht erfüllt, sonst true
	 */
	public boolean removeTreatment(Treatment t) {
		if (t== null||!t.validAppointment() || !t.isOpen())
			return false;
		t.getEmployee().getAppointments().remove(t);
		t.getPatient().getTreatments().remove(t);
		t.getRoom().getPracticalAppointments().remove(t);
		o.getOffice().getAppointments().remove(t);
		return true;
	}

	/**
	 * Fügt einen Patienten zu einem Kurs hinzu
	 * 
	 * @preconditions Kurs muss valide sein, außerdem muss es sich um den ersten Termin in der Reihe handeln
	 * @param p Der Patient, der hinzugefügt wird
	 * @param c Der erste Termin des Kurses, zu dem der Patient hinzugefügt werden soll
	 * @return false, wenn die Aktion nicht gültig ist.
	 */
	public boolean addPatient(Patient p, Course c) {
		if (c==null || !c.validAppointment() || c.getPrevCourse() != null || p == null || !spotLeft(c,p)) // Kurstermin muss vollständig sein und muss der Erste des Kurses sein
			return false;
		while (c != null) {
			c.addPatient(p);
			p.getCourses().add(c);
			c = c.getNextCourse();
		}
		return true;
	}
	
	/**
	 * Testet ob zwei Termine mit einander kollidieren.
	 * 
	 * @param a
	 *            Der erste Termin.
	 * @param b
	 *            Der zweite Termin.
	 * @return true, wenn die Termine kollidieren.
	 */
	public boolean collides(Appointment a, Appointment b) {
		// null kollidiert mit nichts
		if (a == null || b == null) {
			return false;
		}

		// nur gultige Termine werden ueberprueft
		if (!a.validAppointment() || !b.validAppointment()) {
			throw new IllegalArgumentException("ungultiger Termin");
		}

		// zeitlich keine Kollision
		if (!a.getTime().overlapsWith(b.getTime())) {
			return false;
		}

		// Mitarbeiter kollidieren
		if (a.getEmployee().equals(b.getEmployee())) {
			return true;
		}

		// Raum checken
		if (a instanceof PracticalAppointment
				&& b instanceof PracticalAppointment) {
			if (((PracticalAppointment) a).getRoom().equals(
					((PracticalAppointment) b).getRoom())) {
				return true;
			}

			// Patienten checken
			if (a instanceof Treatment && b instanceof Treatment) {
				if (((Treatment) a).getPatient().equals(
						((Treatment) b).getPatient())) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Testet ob ein Termin mit einer Liste von Terminen kollidiert.
	 * 
	 * @param appointment
	 *            Der Termin der getestet wird.
	 * @param candidates
	 *            Die Liste der Kandidaten.
	 * @return true, wenn der Termin mit mindestens einem Kandidaten kollidiert.
	 */
	public boolean collides(Appointment appointment,
			List<Appointment> candidates) {
		if (appointment == null | candidates == null) {
			return false;
		}

		for (Appointment app : candidates) {
			if (collides(appointment, app)) {
				return true;
			}
		}

		return false;
	}

}
