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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import model.CalendarUtil;
import model.Contact;
import model.Course;
import model.Employee;
import model.Material;
import model.Patient;
import model.PracticalAppointment;
import model.Rank;
import model.TimeInterval;
import model.Treatment;
import model.Visit;

/**
 * @author Stefan Noll
 * 
 */
public class PatientController {

	private final OfficeController officeCtrl;

	private static int billCounter = 1;

	public PatientController(OfficeController o) {
		this.officeCtrl = o;
	}

	/**
	 * Prüft, ob <b>p</b> ein gültiger Patient ist und trägt diesen in die
	 * Praxis ein.
	 * 
	 * @return true, wenn der Patient eingetragen werden konnte, false, wenn der
	 *         Patient ungültig war
	 */
	public boolean savePatient(Patient p) {
		if (p == null || !p.validPerson()
				|| getPatientByName(p.getFirstName(), p.getLastName()) != null) {
			return false;
		}
		officeCtrl.getOffice().getPatients().add(p);
		return true;
	}

	/**
	 * Ersetzt, wenn möglich, die Daten von <b>oldP</b> durch die von
	 * <b>newP</b> Es werden nur die statischen Patientendaten geändert, die
	 * Terminlisten werden nicht angefasst!
	 * 
	 * @param oldP3
	 *            der Patient, der geändert werden soll
	 * @param newP
	 *            Patientenobjekt mit den neuen Daten
	 * @return true, wenn die Daten eingetragen werden konnten
	 */
	public boolean editPatient(Patient oldP, Patient newP) {
		if (oldP == null || newP == null || !oldP.validPerson()
				|| !newP.validPerson()) {
			return false;
		}
		if ((oldP.getFirstName().compareTo(newP.getFirstName()) != 0 || oldP
				.getLastName().compareTo(newP.getLastName()) != 0)
				&& getPatientByName(newP.getFirstName(), newP.getLastName()) != null) {
			return false; // Neuer Patientenname ist bereits vergeben
		}
		oldP.setContact(newP.getContact());
		oldP.setDisease(newP.getDisease());
		oldP.setDistance(newP.getDistance());
		oldP.setFirstName(newP.getFirstName());
		oldP.setLastName(newP.getLastName());
		oldP.setNotice(newP.getNotice());
		oldP.setPreferredEmployee(newP.getPreferredEmployee());
		oldP.setPrivat(newP.isPrivat());
		oldP.setTitle(newP.getTitle());
		return true;
	}

	/**
	 * Löscht den Patienten <b>p</b> aus der Datenbank.
	 * 
	 * @return true, wenn der Patient gelöscht werden konnte.
	 */
	public boolean removePatient(Patient p) {
		if (p == null || !p.validPerson()) {
			return false;
		}
		// FIXME So wie es jetzt ist, wird der Patient aus der globalen Liste
		// gelöscht,
		// bleibt aber eventuell in seinen Terminen abrufbar. Könnte so
		// funktionieren,
		// evtl. lohnt es sich aber, das "archivieren" nochmal richtig zu machen
		officeCtrl.getOffice().getPatients().remove(p);
		return true;
	}

	/**
	 * Gibt den Patienten mit Namen firstName lastName zurück.
	 * 
	 * @param firstName
	 *            Vorname des Patienten.
	 * @param lastName
	 *            Nachname des Patienten.
	 * @return Der Patient oder null.
	 */
	public Patient getPatientByName(String firstName, String lastName) {
		for (Patient p : officeCtrl.getOffice().getPatients()) {
			if (p.getFirstName().equals(firstName)
					&& p.getLastName().equals(lastName)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Pr&uuml;ft ob der Patient Termine hat, die noch nicht abgerechnet wurden.
	 * 
	 * @param p
	 *            Der Patient, an den die Rechnung gesendet wird.
	 * @return true, wenn eine neue Rechnung erstellt werden sollte.
	 */
	public boolean shouldNewBillCreated(Patient p) {
		return p != null && p.isPrivat() && !p.getTreatments().isEmpty();
	}

	/**
	 * Erzeugt eine Rechnung über alle Leistungen, von denen noch keine Rechnung
	 * erstellt wurde.
	 * 
	 * @param p
	 *            Der Patient, an den die Rechnung gesendet wird.
	 * @return Die fertige Rechnung als String oder null, wenn p nicht
	 *         Privatpatient ist.
	 */
	public String makeBill(Patient p) {

		if (p == null || !p.validPerson() || !p.isPrivat()) {
			return null;
		}

		// Rechnungskopf
		billCounter++;
		Contact contact = p.getContact();
		String out = p.getTitle() + "\n";
		out += p.getFirstName() + " " + p.getLastName() + "\n";
		out += contact.getStreet() + "\n";
		out += contact.getZipCode() + " " + contact.getCity() + "\n";

		out += "\n\n\n";

		out += "Rechnung:\n";

		out += "\t\t\t\t\t\tRechnungsdatum  "
				+ CalendarUtil.dateToString(new Date()) + "\n";
		out += "\t\t\t\t\t\tRechnungsnummer "
				+ CalendarUtil.dateTimeToStringNormed(new Date()) + billCounter
				+ "\n";
		out += "\t\t\t\t\t\tSteuernummer\t" + officeCtrl.getOffice().getTaxNo()
				+ "\n\n";

		out += String.format("%-10s%-10s%-20s%-20s%-20s\n", "Menge", "MwSt",
				"Einzelpreis EUR", "Gesamt EUR", "Bezeichnung");

		out += "----------------------------------------------------------------------------------------------\n";

		List<Treatment> treatments = p.getTreatments();
		List<Treatment> treatmentsPaid = p.getTreatmentsPaid();
		List<Course> courses = p.getCourses();
		List<Course> coursesPaid = p.getCoursesPaid();

		int gesamt = 0;
		int strecke = 0;

		for (Treatment t : new LinkedList<Treatment>(treatments)) {
			if (t.isOpen() || (!t.isSelfPaid() && !p.isPrivat())) {
				continue;
			}

			String tName = t.getActivity().getName();
			List<Treatment> sameTreatments = new ArrayList<Treatment>();
			// sameTreatments.add(t);
			int counter = 0; // 1;

			// gleichen Treatments suchen, zaehlen und merken(zum
			// Entfernen)
			for (Treatment t2 : treatments) {
				if (tName.equals(t2.getActivity().getName()) && !t2.isOpen()) {
					sameTreatments.add(t2);
					counter++;
					if (t2 instanceof Visit) {
						strecke += ((Visit) t2).getDriveDistanceInMeters();
					}
				}
			}

			// gesammelten Treatments aus treatments loeschen, zu
			// treatmentsPaid hinzufuegen
			for (Treatment t3 : sameTreatments) {
				treatments.remove(t3);
				treatmentsPaid.add(t3);
			}

			if (counter != 0) {
				int tPrice = t.getActivity().getPrice();
				gesamt += counter * tPrice;
				tPrice /= 100;

				out += String.format("%-10d%-10s%-20s%-20s%-20s\n", counter,
						"19,00", tPrice + ",00 EUR", (counter * tPrice)
								+ ",00 EUR", tName);
			}
		}

		for (Course c : new LinkedList<Course>(courses)) {
			if (c.getNextCourse() != null || c.isOpen()) {
				continue;
			}
			String tName = c.getActivity().getName();
			List<Course> sameCourses = new ArrayList<Course>();
			// sameTreatments.add(t);
			int counter = 0; // 1;

			// gleichen Treatments suchen, zaehlen und merken(zum
			// Entfernen)
			for (Course c2 : courses) {
				if (tName.equals(c2.getActivity().getName()) && !c2.isOpen()
						&& c2.getNextCourse() == null) {
					sameCourses.add(c2);
					counter++;
				}
			}

			// gesammelten Treatments aus treatments loeschen, zu
			// treatmentsPaid hinzufuegen
			for (Course c3 : sameCourses) {
				courses.remove(c3);
				coursesPaid.add(c3);
			}

			if (counter != 0) {
				int tPrice = c.getActivity().getPrice();
				gesamt += counter * tPrice;
				tPrice /= 100;

				out += String.format("%-10d%-10s%-20s%-20s%-20s\n", counter,
						"19,00", tPrice + ",00 EUR", (counter * tPrice)
								+ ",00 EUR", tName);
			}
		}
		for (Material m : new LinkedList<Material>(p.getMaterial())) {
			int counter = 0;
			LinkedList<Material> sameMaterial = new LinkedList<Material>();
			for (Material m2 : p.getMaterial()) {
				if (m.getName().equals(m2.getName())) {
					sameMaterial.add(m2);
					p.getMaterialPaid().add(m2);
					counter++;
				}
			}
			for (Material m3 : sameMaterial){
				p.getMaterial().remove(m3);
			}
			if (!sameMaterial.isEmpty()) {
				out += String.format("%-10d%-10s%-20s%-20s%-20s\n", counter,
						"19,00", m.getPrice() / 100 + "," + m.getPrice() % 100
								+ " EUR", (counter * m.getPrice()) / 100 + ","
								+ (counter * m.getPrice()) % 100 + " EUR", m
								.getName());
			}
		}

		int fahrtkosten = (strecke / 1000) * 40;
		gesamt += fahrtkosten;
		if (fahrtkosten > 0) {
			out += String.format("%-10d%-10s%-20s%-20s%-20s\n", strecke / 1000,
					"19,00", "0,40 EUR", fahrtkosten / 100 + "," + fahrtkosten
							% 100 + " EUR", "Fahrtkosten");
		}

		// Rechnungsabschluss
		int mwstSumme = (gesamt / 100) * 19;
		int nettoSumme = gesamt - mwstSumme;

		out += "----------------------------------------------------------------------------------------------\n";
		out += String.format("%-10s%-30s%-20s\n", " ", "Netto-Summe",
				(nettoSumme / 100) + "," + (nettoSumme % 100) + " EUR");
		out += String.format("%-10s%-10s%-20s%-20s\n", " ", "Mwst", "19,00 %",
				(mwstSumme / 100) + "," + (mwstSumme % 100) + " EUR");
		out += String.format("%-10s%-30s%-20s\n", " ", "Gesamt", (gesamt / 100)
				+ ",00 EUR");

		// Boss-Name und Office-Adresse eingeben

		Employee boss = null;
		List<Employee> employees = officeCtrl.getOffice().getEmployees();
		if (employees != null) {
			for (Employee e : employees) {
				if (e.getRank() == Rank.BOSS) {
					boss = e;
					break;
				}
			}
		}

		Contact officeContact = officeCtrl.getOffice().getContact();

		out += "\n\n\n\n\n\n__________________________\n";
		out += boss.getFirstName() + " " + boss.getLastName() + "\n\n\n\n";
		out += boss.getFirstName() + " " + boss.getLastName() + "\n";
		out += officeCtrl.getOffice().getName() + "\n";
		out += officeContact.getStreet() + "\n";
		out += officeContact.getZipCode() + " " + officeContact.getCity()
				+ "\n";
		out += officeContact.getPhone() + "\n";
		out += officeContact.getMobile() + "\n";

		return out;
	}

	/**
	 * Gibt für einen Patienten <b>p</b> alle Termine zurück, die das Intervall
	 * <b>t</b> betreffen.
	 * 
	 * @param p
	 *            Patient
	 * @param t
	 *            Zeitintervall
	 * @return Liste an Terminen
	 */
	public List<PracticalAppointment> getPracticalAppointmentsInTime(Patient p,
			TimeInterval t) {
		if (p != null && t != null) {
			ArrayList<PracticalAppointment> pracappointments = new ArrayList<PracticalAppointment>();
			for (PracticalAppointment pa : p.getTreatments()) {
				if (pa.getTime().overlapsWith(t)) {
					pracappointments.add(pa);
				}
			}
			for (PracticalAppointment pa : p.getTreatmentsPaid()) {
				if (pa.getTime().overlapsWith(t)) {
					pracappointments.add(pa);
				}
			}
			for (PracticalAppointment pa : p.getCourses()) {
				if (pa.getTime().overlapsWith(t)) {
					pracappointments.add(pa);
				}
			}
			for (PracticalAppointment pa : p.getCoursesPaid()) {
				if (pa.getTime().overlapsWith(t)) {
					pracappointments.add(pa);
				}
			}
			return pracappointments;
		}
		return null;
	}

	/**
	 * Fügt ein Material zum Patienten hinzu
	 * 
	 * @param p
	 *            der Patient
	 * @param m
	 *            das Material
	 * @return true, wenns geklappt hat, sonst false
	 */
	public boolean addMaterial(Patient p, Material m) {
		if (p == null || m == null || !p.validPerson() || !m.validService()) {
			return false;
		}
		p.getMaterial().add(m);
		return true;
	}

	/**
	 * Entfernt ein Material vom Patienten
	 * 
	 * @param p
	 *            der Patient
	 * @param m
	 *            das Material
	 * @return true, wenns geklappt hat, sonst false
	 */
	public boolean removeMaterial(Patient p, Material m) {
		if (p == null || m == null || !p.validPerson() || !m.validService()) {
			return false;
		}
		p.getMaterial().remove(m);
		return true;
	}

}
