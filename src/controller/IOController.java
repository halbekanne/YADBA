/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Appointment;
import model.Course;
import model.Employee;
import model.Office;
import model.Patient;
import model.PracticalAppointment;
import model.Room;
import model.TimeInterval;
import model.Treatment;
import model.Vacation;

public class IOController {

	private final OfficeController o;

	public IOController(OfficeController o) {
		this.o = o;
	}

	/**
	 * Lädt die gesamte Praxis aus einer Datei.
	 * 
	 * @param fileName
	 *            Der Name der Datei.
	 * @return true wenn kein Fehler aufgetreten ist, sonst false.
	 */
	public boolean loadBackUp(String fileName) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					fileName));
			o.setOffice((Office) ois.readObject());
			ois.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (ClassNotFoundException e) {
			return false;
		}

		return true;
	}

	/**
	 * Speichert die gesamte Praxis in eine Datei.
	 * 
	 * @param fileName
	 *            Der Name der Datei.
	 * @return true wenn kein Fehler aufgetreten ist, sonst false.
	 */
	public boolean saveBackUp(String fileName) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(fileName));
			oos.writeObject(o.getOffice());
			oos.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Exportiert den Kalender eines Mitarbeiters
	 * 
	 * @param e
	 *            Der zu exportierende Mitarbeiter
	 * @param t
	 *            Das zu exportierende Zeitintervall
	 * @return true wenn kein Fehler aufgetreten ist, sonst false.
	 */
	public String exportCalendar(Employee e, TimeInterval t) {
		if (e == null || t == null) {
			return "";
		}
		String sExport = "BEGIN:VCALENDAR\nPRODID:-//Sopra/YADBA//DE\nVERSION:2.0\n";
		List<Appointment> list = o.getEmployeeController()
				.getAppointmentsInTime(e, t);
		if (list.size() == 0) {
			return "";
		}
		for (Appointment ap : list) {
			sExport += "BEGIN:VEVENT\n";

			String sBegin = dateTOiCalendarFormat(ap.getTime().getBegin());
			String sEnd = dateTOiCalendarFormat(ap.getTime().getEnd());
			if (sBegin.isEmpty() || sEnd.isEmpty()) {
				return "";
			}
			sExport += "DTSTART:" + sBegin + "\n";
			sExport += "DTEND:" + sEnd + "\n";

			if (ap instanceof Vacation) {
				sExport += "SUMMARY:Fehlzeit\n";
			} else if (ap instanceof PracticalAppointment) {
				PracticalAppointment apPrac = (PracticalAppointment) ap;
				sExport += "LOCATION:" + apPrac.getRoom().getName() + "\n";
				sExport += "SUMMARY:" + apPrac.getActivity().getName() + "\n";
				if (ap instanceof Course) {
					sExport += "DESCRIPTION:Kurs\n";

				} else if (ap instanceof Treatment) {
					Treatment apT = (Treatment) apPrac;
					sExport += "DESCRIPTION:" + apT.getPatient().toString()
							+ "\n";
				} else {
					return "";
				}
			} else {
				return "";
			}
			sExport += "END:VEVENT\n";
		}
		sExport += "END:VCALENDAR";
		return sExport;
	}

	/**
	 * Exportiert den Kalender eines Patienten
	 * 
	 * @param p
	 *            Der zu exportierende Patient
	 * @param t
	 *            Das zu exportierende Zeitintervall
	 * @return true wenn kein Fehler aufgetreten ist, sonst false.
	 */
	public String exportCalendar(Patient p, TimeInterval t) {
		if (p == null || t == null) {
			return "";
		}
		String sExport = "BEGIN:VCALENDAR\nPRODID:-//Sopra/YADBA//DE\nVERSION:2.0\n";
		List<PracticalAppointment> list = o.getPatientController()
				.getPracticalAppointmentsInTime(p, t);
		if (list.size() == 0) {
			return "";
		}
		for (Appointment ap : list) {
			sExport += "BEGIN:VEVENT\n";

			String sBegin = dateTOiCalendarFormat(ap.getTime().getBegin());
			String sEnd = dateTOiCalendarFormat(ap.getTime().getEnd());
			if (sBegin.isEmpty() || sEnd.isEmpty()) {
				return "";
			}
			sExport += "DTSTART:" + sBegin + "\n";
			sExport += "DTEND:" + sEnd + "\n";

			if (ap instanceof PracticalAppointment) {
				PracticalAppointment apPrac = (PracticalAppointment) ap;
				sExport += "LOCATION:" + apPrac.getRoom().getName() + "\n";
				sExport += "SUMMARY:" + apPrac.getActivity().getName() + "\n";
				if (ap instanceof Course) {
					sExport += "DESCRIPTION:Kurs bei "
							+ apPrac.getEmployee().toString() + "\n";

				} else if (ap instanceof Treatment) {
					sExport += "DESCRIPTION:" + apPrac.getEmployee().toString()
							+ "\n";
				} else {
					return "";
				}
			} else {
				return "";
			}
			sExport += "END:VEVENT\n";
		}
		sExport += "END:VCALENDAR";
		return sExport;
	}

	/**
	 * Exportiert den Kalender eines Raumes
	 * 
	 * @param r
	 *            Der zu exportierende Raum
	 * @param t
	 *            Das zu exportierende Zeitintervall
	 * @return true wenn kein Fehler aufgetreten ist, sonst false.
	 */
	public String exportCalendar(Room r, TimeInterval t) {
		if (r == null || t == null) {
			return "";
		}
		String sExport = "BEGIN:VCALENDAR\nPRODID:-//Sopra/YADBA//DE\nVERSION:2.0\n";
		List<PracticalAppointment> list = o.getRoomController()
				.getPracticalAppoinmentsInTime(r, t);
		if (list.size() == 0) {
			return "";
		}
		for (Appointment ap : list) {
			sExport += "BEGIN:VEVENT\n";

			String sBegin = dateTOiCalendarFormat(ap.getTime().getBegin());
			String sEnd = dateTOiCalendarFormat(ap.getTime().getEnd());
			if (sBegin.isEmpty() || sEnd.isEmpty()) {
				return "";
			}
			sExport += "DTSTART:" + sBegin + "\n";
			sExport += "DTEND:" + sEnd + "\n";

			if (ap instanceof PracticalAppointment) {
				PracticalAppointment apPrac = (PracticalAppointment) ap;
				sExport += "DESCRIPTION:" + apPrac.getEmployee().toString()
						+ "\n";
				if (ap instanceof Course) {
					sExport += "SUMMARY:Kurs:" + apPrac.getActivity().getName()
							+ "\n";
				} else if (apPrac instanceof Treatment) {
					sExport += "SUMMARY:" + apPrac.getActivity().getName()
							+ "\n";
				} else {
					return "";
				}
			} else {
				return "";
			}
			sExport += "END:VEVENT\n";
		}
		sExport += "END:VCALENDAR";
		return sExport;
	}

	/**
	 * Speichert den zu exportierenden String in der angegbenen Datei.
	 * 
	 * @param sContent
	 *            Der String, Formatierung wird nicht geaendert.
	 * @param FileName
	 *            Die Zieldatei, Endung wird nicht ueberprueft.
	 * @return boolean, ob das Speichern erfolgreich war.
	 */
	public boolean saveStringToFile(String sContent, String FileName) {
		try {
			FileWriter fw = new FileWriter(FileName);
			fw.write(sContent);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// Fehlermeldung wird durch den return-Wert gegeben
			return false;
		}
		return true;
	}

	/**
	 * Erzeugt aus einem Zeitpunkt den passenden String im iCalendar-Zeitformat
	 * 
	 * @param d
	 *            Der Zeitpunkt
	 * @return Der String
	 */
	public String dateTOiCalendarFormat(Date d) {
		if (d == null) {
			return "";
		}
		Calendar timePoint = Calendar.getInstance();
		timePoint.setTime(d);
		int iYear, iMonth, iDay, iHour, iMinute;
		iYear = timePoint.get(Calendar.YEAR);
		iMonth = timePoint.get(Calendar.MONTH) + 1;
		iDay = timePoint.get(Calendar.DAY_OF_MONTH);
		iHour = timePoint.get(Calendar.HOUR_OF_DAY);
		iMinute = timePoint.get(Calendar.MINUTE);
		String sYear, sMonth, sDay, sHour, sMinute;
		if (iYear < 1000) {
			sYear = "0" + String.valueOf(iYear);
		} else {
			sYear = String.valueOf(iYear);
		}
		if (iMonth < 10) {
			sMonth = "0" + String.valueOf(iMonth);
		} else {
			sMonth = String.valueOf(iMonth);
		}
		if (iDay < 10) {
			sDay = "0" + String.valueOf(iDay);
		} else {
			sDay = String.valueOf(iDay);
		}
		if (iHour < 10) {
			sHour = "0" + String.valueOf(iHour);
		} else {
			sHour = String.valueOf(iHour);
		}
		if (iMinute < 10) {
			sMinute = "0" + String.valueOf(iMinute);
		} else {
			sMinute = String.valueOf(iMinute);
		}
		String sAusgabe = sYear + sMonth + sDay + "T" + sHour + sMinute + "00";
		return sAusgabe;
	}

}
