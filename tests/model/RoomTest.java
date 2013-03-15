package model;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controller.OfficeController;

public class RoomTest {

	OfficeController octrl = DataFaker.generateOfficeController();
	Room ra0, ra1, ra2, ra3, ra4;
	List<Activity> activityList;
	List<Appointment> appointmentList;

	@Before
	public void setUp() throws Exception {
		activityList = octrl.getOffice().getActivities();
		appointmentList = octrl.getOffice().getAppointments();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsAvailable() {
		
		Date now = Calendar.getInstance().getTime();
		Date start = CalendarUtil.combineDateTime(now, CalendarUtil.getDefaultDate(10, 00));
		Date end = CalendarUtil.combineDateTime(now, CalendarUtil.getDefaultDate(10, 30));
		TimeInterval times1 = new TimeInterval(start, end);
		Date start2 = CalendarUtil.combineDateTime(now, CalendarUtil.getDefaultDate(11, 30));
		Date end2 = CalendarUtil.combineDateTime(now, CalendarUtil.getDefaultDate(13, 30));
		TimeInterval times2 = new TimeInterval(start2, end2);
		Room ra6 = octrl.getOffice().getRooms().get(0);
		Room ra7 = octrl.getOffice().getRooms().get(3);
		//Raum hat Termin im gegebenem Zeitraum
		assertFalse("Überdeckung nicht erkannt", ra6.isAvailable(times1) );
		//Raum hat keinen Termin im angegebenen Zeitraum
		assertTrue("Freiraum nicht erkannt", ra7.isAvailable(times2));
	}

	@Test
	public void testIsAble() {
		Room ra5 = new Room("E29",1,activityList);
		//Raum auf aktivität überprüfen, die er beinhaltet
		assertTrue("Existierende Activity nicht gefunden",ra5.isAble(activityList.get(0)));
		//Raum auf aktivität überprüfen, die er nicht beinhaltet
		assertFalse("Unbekannte Aktivität erkannt", ra5.isAble(new Activity("Thai Massage", 5000, 60, 1)));
		//Raum auf uninitialisierte aktivität testen
		assertFalse("Activity null nicht entdeckt", ra5.isAble(null));
	}

	@Test
	public void testValidRoom() {
		ra0 = new Room("E29", 0, activityList); // fehler -> Kapazität zu gering
		ra1 = new Room(null, 1, activityList); // fehler -> Name = null
		ra2 = new Room("", 1, activityList); // fehler -> leerer Name
		ra3 = new Room("E29", 1, null); // fehler -> ActivityList uninizialisiert
		ra4 = new Room("E29", 1, activityList); // Valid
		// fehler -> Kapazität zu gering
		assertFalse("Failer!", ra0.validRoom());
		// fehler -> Name = null
		assertFalse("Kein Name", ra1.validRoom());
		// fehler -> leerer Name
		assertFalse("leerer Name", ra2.validRoom());
		// ActivityList uninizialisiert
		assertFalse("Keine ActivityList", ra3.validRoom());
		// Valid
		assertTrue("Fehler", ra4.validRoom());
	}

}
