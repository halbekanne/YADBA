package model;

import static org.junit.Assert.*;

import org.junit.Test;

import controller.OfficeController;

public class EmployeeTest {
	OfficeController octrl = DataFaker.generateOfficeController();


	/**
	 * Testet ob Passwörter korrekt gespeichert und überprüft werden.
	 * 
	 * Sonderfälle: Richtige Behandlung von Umlauten und Längenänderungen. 
	 */
	@Test
	public void testPassword() {
		Employee e = new Employee(null, "foo", "bar", "Prof. Dr.", "äöü!!!!", null, null, Rank.EMPLOYEE);

		assertTrue(e.checkPassword("äöü!!!!"));
		assertFalse(e.checkPassword("äöü!!!"));

		e.setPassword("hallo");
		assertFalse(e.checkPassword("äöü!!!!"));
		assertTrue(e.checkPassword("hallo"));		
	}

	/** Teste ob die Arbeitszeitmethoden funktionieren. */
	@Test
	public void testisInWorkingTime() {
		Employee doris = octrl.getEmployeeController().getEmployeeByName("Doris", "Schmedding");
		Appointment a = octrl.getOffice().getAppointments().get(0);
		Appointment b = octrl.getOffice().getAppointments().get(octrl.getOffice().getAppointments().size()-1);
		assertTrue(doris.isInWorkingTime(a.getTime().getBegin()));
		assertTrue(doris.isInWorkingTime(a.getTime()));
		assertTrue(doris.isInWorkingTime(b.getTime().getBegin()));
		assertTrue(doris.isInWorkingTime(b.getTime()));

		assertFalse(doris.isAvailable(a.getTime()));
		assertFalse(doris.isAvailable(b.getTime()));
	}

	/** Teste ob die isAble-Methode funktioniert. */
	@Test
	public void isAble() {
		Employee doris = octrl.getEmployeeController().getEmployeeByName("Doris", "Schmedding");

		assertTrue(doris.isAble(octrl.getOffice().getActivities().get(0)));
		assertFalse(doris.isAble(new Activity("test", 4, 5, 1)));
	}

}
