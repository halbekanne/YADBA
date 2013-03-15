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

import model.Activity;
import model.Course;
import model.PracticalAppointment;
import model.Room;
import model.TimeInterval;

public class RoomController {

	private final OfficeController officeCtrl;

	public RoomController(OfficeController o) {
		this.officeCtrl = o;
	}

	/**
	 * Speichert gegebenes Raumobjekt im Office ab. Muss einzigartigen Namen
	 * haben.
	 * 
	 * @param r
	 *            Der Raum
	 * @return true, wenns geklappt hat, false, wenn der Raum nicht korrekt ist
	 *         und nicht gespeichert wurde
	 */
	public boolean saveRoom(Room r) {
		if (r == null || !r.validRoom()) {
			return false;
		}
		for (Room room : officeCtrl.getOffice().getRooms()) {
			if (room.getName().equals(r.getName())) {
				return false;
			}
		}
		officeCtrl.getOffice().getRooms().add(r);

		return true;
	}

	/**
	 * Versucht, die Daten eines Raumes zu ändern. Name muss einzigartig bleiben
	 * 
	 * @param oldR
	 *            Der Raum, der geändert werden soll
	 * @param newR
	 *            Raumobjekt, dass die neuen Daten beinhaltet
	 * @return true, wenns geklappt hat, false, wenn es eine Kollision gibt,
	 *         oder die Objekte ungültig sind
	 */
	public boolean editRoom(Room oldR, Room newR) {
		if (oldR == null || newR == null || !oldR.validRoom()
				|| !newR.validRoom()) {
			return false;
		}
		for (PracticalAppointment a : oldR.getPracticalAppointments()) {
			if (a.getTime().compareToNow() >= 0) { // Nur Termine in der Zukunft
				// betrachten
				if (!newR.isAble(a.getActivity())) {
					return false;
				}
				if (a instanceof Course) {
					Course c = (Course) a;
					if (c.getPatients().size() > newR.getCapacity()) {
						return false;
					}
				}
			}
		}
		if (!oldR.getName().equals(newR.getName())) { // Wenn der Name geändert
			// wird...
			for (Room room : officeCtrl.getOffice().getRooms()) {
				if (room.getName().equals(newR.getName())) {
					return false;
				}
			}
		}
		oldR.setCapacity(newR.getCapacity()); // Alles OK, Daten in alten Raum
		// übernehmen
		oldR.setName(newR.getName());
		oldR.setActivity(newR.getActivity());
		return true;
	}

	/**
	 * Gibt eine Liste aller Räume zurück, in denen in einem bestimmten
	 * Zeitintervall eine bestimmte Behandlung ausgeführt werden kann
	 * 
	 * @param t
	 *            Ein Zeitintervall
	 * @param a
	 *            eine Behandlung
	 * @return Liste aller möglichen Räume
	 */
	public List<Room> getAvailableRooms(TimeInterval t, Activity a) {
		if (t == null || a == null || !a.validService()) {
			return null;
		}
		LinkedList<Room> result = new LinkedList<Room>();
		for (Room room : getAvailableRooms(a)) {
			boolean available = true;
			for (PracticalAppointment appointment : room
					.getPracticalAppointments()) {
				if (appointment.getTime().overlapsWith(t)) {
					available = false;
					break;
				}
			}
			if (available) {
				result.add(room);
			}
		}
		return result;
	}

	/**
	 * Gibt Liste von Räumen zurück, in denen eine Behandlung stattfinden kann
	 * 
	 * @param a
	 *            Behandlung, die durchführbar sein soll
	 * @return Liste der Räume, in denen a stattfinden kann.
	 */
	public List<Room> getAvailableRooms(Activity a) {
		if (a == null || !a.validService()) {
			return null;
		}
		LinkedList<Room> result = new LinkedList<Room>();
		for (Room room : officeCtrl.getOffice().getRooms()) {
			if (room.isAble(a)) {
				result.add(room);
			}
		}
		return result;
	}

	/**
	 * Löscht den gegebenen Raum aus der Liste vom Office. Eventuelle Referenzen
	 * von Terminen bleiben bestehen. Gibt es in der Zukunft noch Termine in
	 * diesem Raum, wird abgebrochen
	 * 
	 * @param r
	 *            Der Raum
	 * @return true, wenns geklappt hat, false, wenn r ungültig ist oder noch
	 *         Termine in der Zukunft hat
	 */
	public boolean removeRoom(Room r) {
		if (r == null || !r.validRoom()) {
			return false;
		}
		for (PracticalAppointment a : r.getPracticalAppointments()) {
			if (a.getTime().compareToNow() >= 0) {
				return false;
			}
		}
		officeCtrl.getOffice().getRooms().remove(r);
		return true;
	}

	/**
	 * Gibt für einen Raum <b>r</b> alle Termine zurück, die das Intervall
	 * <b>t</b> betreffen.
	 * 
	 * @param r
	 *            Raum
	 * @param t
	 *            Zeitintervall
	 * @return Liste an Terminen
	 */
	public List<PracticalAppointment> getPracticalAppoinmentsInTime(Room r,
			TimeInterval t) {
		if (r == null || t == null || !r.validRoom()) {
			return null;
		}
		ArrayList<PracticalAppointment> pracappointments = new ArrayList<PracticalAppointment>();
		for (PracticalAppointment pa : r.getPracticalAppointments()) {
			if (pa.getTime().overlapsWith(t)) {
				pracappointments.add(pa);
			}
		}
		return pracappointments;
	}

	/**
	 * Gibt den Raum mit dem Namen <b>roomName</b> zurück.
	 * @param roomName Der Name des zu suchenden Raums.
	 * @return Referenz auf den Raum, wenn er nicht gefunden werden kann null.
	 */
	public Room getRoomByName(String roomName) {
		for (Room room : officeCtrl.getOffice().getRooms()) {
			if (room.getName().equals(roomName)) {
				return room;
			}
		}
		return null;
	}

}
