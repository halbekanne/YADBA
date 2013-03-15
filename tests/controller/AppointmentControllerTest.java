package controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import model.Activity;
import model.Appointment;
import model.CalendarUtil;
import model.Employee;
import model.Home;
import model.Patient;
import model.Room;
import model.TimeInterval;
import model.Treatment;
import model.Vacation;
import model.Visit;

import org.junit.Test;

public class AppointmentControllerTest extends ControllerTest {

	@Test
	public void testPropose(){
		//Zur Zeit nur zum debuggen da
		Date now = Calendar.getInstance().getTime();
		
		Date a = CalendarUtil.combineDateTime(now, CalendarUtil.getDefaultDate(7, 00));
		Date b = CalendarUtil.combineDateTime(now, CalendarUtil.getDefaultDate(10, 30));
		@SuppressWarnings("unused")
		Date c = CalendarUtil.combineDateTime(now, CalendarUtil.getDefaultDate(11, 30));
		Date d = CalendarUtil.combineDateTime(now, CalendarUtil.getDefaultDate(14, 30));
		
		List<Treatment> t = officeCtrl.getAppointmentController().proposeTreatments(a, d, 40, officeCtrl.getOffice().getActivities().get(0),
				officeCtrl.getOffice().getPatients().get(0), officeCtrl.getOffice().getEmployees().get(0), false, new LinkedList<Treatment>());
		assertTrue(t.size()>0);
		t = officeCtrl.getAppointmentController().proposeTreatments(a, b, 60, officeCtrl.getOffice().getActivities().get(0),
				officeCtrl.getOffice().getPatients().get(0), officeCtrl.getOffice().getEmployees().get(0), false, new LinkedList<Treatment>());
		assertTrue(t.size() == 0);
		return;
	}
	@Test
	public void testCollides() {
		// Daten vom DataFaker holen
		AppointmentController ctrl = officeCtrl.getAppointmentController();

		Calendar cal = Calendar.getInstance();
		cal.set(2013, 01, 01, 9, 00);
		Date date1 = cal.getTime();
		cal.set(2013, 01, 01, 16, 00);
		Date date2 = cal.getTime();

		TimeInterval time1 = new TimeInterval(date1, 20);
		TimeInterval time2 = new TimeInterval(date2, 20);

		Employee employee1 = officeCtrl.getOffice().getEmployees().get(0);
		Employee employee2 = officeCtrl.getOffice().getEmployees().get(1);

		Room room1 = officeCtrl.getOffice().getRooms().get(0);
		Room room2 = officeCtrl.getOffice().getRooms().get(1);

		Activity activity = officeCtrl.getOffice().getActivities().get(0);

		Patient patient1 = officeCtrl.getOffice().getPatients().get(0);
		Patient patient2 = officeCtrl.getOffice().getPatients().get(1);

		Treatment illigalTreatment = new Treatment(null, null, null, null,
				null, false);

		Treatment treatment1 = new Treatment(time1, employee1, room1, activity,
				patient1, false);

		Treatment treatment2 = new Treatment(time2, employee2, room2, activity,
				patient2, false);

		Treatment treatment3 = new Treatment(time1, employee1, room2, activity,
				patient2, false);

		Treatment treatment4 = new Treatment(time2, employee1, room2, activity,
				patient2, false);

		Treatment treatment5 = new Treatment(time1, employee2, room2, activity,
				patient2, false);

		Treatment treatment6 = new Treatment(time1, employee2, room1, activity,
				patient2, false);

		Treatment treatment7 = new Treatment(time1, employee2, room2, activity,
				patient2, false);

		Treatment treatment8 = new Treatment(time1, employee2, room2, activity,
				patient1, false);

		Treatment treatment9 = new Treatment(time2, employee2, room2, activity,
				patient1, false);

		Vacation vacation1 = new Vacation(time1, employee1);

		Visit visit1 = new Visit(time1, employee2, new Home(officeCtrl
				.getOffice().getActivities()), activity, patient2, false, 15, 1);

		// ungueltige Appointments sollten abgefangen werden
		try {
			ctrl.collides(treatment1, illigalTreatment);
			fail("Ungueltige Appointments erzugen keine Exception");
		} catch (IllegalArgumentException e) {
			// das wollen wir erreichen
		}

		// null sollte mit nichts kollidieren
		assertFalse("Ein Appointment kollidiert nicht mit null.",
				ctrl.collides(treatment1, (Appointment) null));
		assertFalse("Ein Appointment kollidiert nicht mit null.",
				ctrl.collides(null, treatment1));

		// zwei Termine die nichts gemeinsam haben, sollten nicht kollidieren
		assertFalse(
				"die Behandlungen haben nicht gemeinsam, sollte also nicht kollidieren",
				ctrl.collides(treatment1, treatment2));

		// zwei Termine zur selben Zeit beim selben Mitarbeiter kollidieren
		assertTrue(
				"ein Mitarbeiter kann nicht zwei Termine gleichzeitg machen",
				ctrl.collides(treatment1, treatment3));

		// zwei Termine zuuntersiedlichen Zeiten beim selben Mitarbeiter
		// kollidieren nicht
		assertFalse("ein Mitarbeiter kann zwei Termine hintereinander machen",
				ctrl.collides(treatment1, treatment4));

		// zwei Mitarbeite koennen gleichzeitig behandeln
		assertFalse("zwei Mitarbeite koennen gleichzeitig behandeln",
				ctrl.collides(treatment1, treatment5));
		// Fehlzeiten
		assertTrue("ein fehldner Mitarbeiter kann nicht behandeln",
				ctrl.collides(treatment1, vacation1));

		// zwei Termine zur selben Zeit im selben Raum kollidieren
		assertTrue(
				"in einem Raum kann zur einer Zeit nur eine Bahndlung statt finden",
				ctrl.collides(treatment1, treatment6));
		// zwei Termine zuuntersiedlichen Zeiten im selben Raum
		// kollidieren nicht
		assertFalse("zwei Raeume koennen gleichzeitig genutzt werden",
				ctrl.collides(treatment1, treatment7));
		// Hausbesuche kollidieren nicht raeumlich
		assertFalse("Hausbesuche kollidieren nicht raeumlich",
				ctrl.collides(treatment1, visit1));

		// zwei Termine zur selben Zeit bei fuer einen Patienten kollidieren
		assertTrue(
				"ein Patient kann nur eine Behandlung gleichzeitig bekommen",
				ctrl.collides(treatment1, treatment8));
		// zwei Termine zu untersiedlichen Zeiten fuer einen Patienten
		// kollidieren nicht
		assertFalse("ein Patient kann zwei Behandlung hintereinander bekommen",
				ctrl.collides(treatment1, treatment9));

	}
}
