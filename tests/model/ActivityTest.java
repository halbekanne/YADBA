package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ActivityTest {
	@Test
	public void testValidService() {
		Activity a1=new Activity("HI", 1, 4, 1);
		Activity a2=new Activity("HI", 1, 5, 1);
		Activity a3=new Activity("HI", 1, 6, 1);
		Activity a4=new Activity("HI", 1, 10, 0);
		Activity a5=new Activity("HI", 1, 10, -1);
		Activity a6=new Activity("HI", 1, 10, 1);
		assertFalse("Zu kurz",a1.validService());
		assertTrue("Mindestlänge",a2.validService());
		assertTrue("Dauer ok",a3.validService());
		assertFalse("Zu wenig Einheiten",a4.validService());
		assertFalse("Negative Anzahl Einheiten",a5.validService());
		assertTrue("Akzeptable Anzahl Einheiten",a6.validService());
	}

}
