package model;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.*;

public class TimeIntervalTest {
	private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	private Date d1, d2, d3, d4;
	private TimeInterval t1, t2, t3, t4, t5;

	/**
	 * Erzeuge eine Reihe von Testobjekten.
	 */
	@Before
	public void setUp() throws ParseException {
		d1 = formatter.parse("01.01.2013 23:10");
		d2 = formatter.parse("02.01.2013 23:10");
		d3 = formatter.parse("01.01.2013 22:23");
		d4 = formatter.parse("02.01.2013 23:23");

		t1 = new TimeInterval(d1, d2);
		t2 = new TimeInterval(d1, d4);
		t3 = new TimeInterval(d2, d4);
		t4 = new TimeInterval(d3, d1);
		t5 = new TimeInterval(d3, d4);
	}

	/**
	 * Testet die Konstruktoren und getDurationInMinutes.<br><br>
	 * 
	 * Testfall überlappt ein Datum.
	 */
	@Test
	public void testBasic() throws ParseException {
		TimeInterval t1 = new TimeInterval(d1, formatter.parse("02.01.2013 01:50"));
		assertEquals(50+60+50, t1.getDurationInMinutes());

		TimeInterval t2 = new TimeInterval(d1, 50+60+50);
		assertEquals(t1, t2);

		TimeInterval t3 = new TimeInterval(d1, 60);
		assertFalse(t2.equals(t3));
	}

	/**
	 * Testet, ob der Konstruktor im Fehlerfall eine Exception wirft.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidInterval() throws ParseException {
		new TimeInterval(formatter.parse("01.01.2013 23:10"),
				formatter.parse("01.01.2013 01:50"));
	}

	/**
	 * Testet, ob der Konstruktor im Fehlerfall eine Exception wirft.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidMinute() throws ParseException {
		new TimeInterval(formatter.parse("01.01.2013 23:10"), -42);
	}

	/**
	 * Testet contains() mit verschiedenen Daten.<br><br>
	 * 
	 * Sie liegen an verschiedenen Tagen.
	 */
	@Test
	public void testContains() {
		assertTrue(t1.contains(d1));
		assertTrue(t1.contains(d2));
		assertFalse(t1.contains(d3));
		assertFalse(t1.contains(d4));
	}

	/**
	 * Tested overlapsWith() mit verschiednen Intervallen.<br><br>
	 * 
	 * Fälle:<br>
	 *   - Komplett innerhalb.<br>
	 *   - Komplett außerhalb.<br>
	 *   - Nur Anfang überlappt.<br>
	 *   - Nur Ende überlappt.<br>
	 *   - Ende des ersten Intervalls = Anfang des Zweiten (keine Überlappung)
	 */
	@Test
	public void testOverlapsWith() {
		assertTrue(t1.overlapsWith(t1));
		assertTrue(t2.overlapsWith(t1));
		assertFalse(t3.overlapsWith(t1));
		assertFalse(t4.overlapsWith(t1));
		assertTrue(t5.overlapsWith(t1));
		assertFalse(t4.overlapsWith(t3));
		assertFalse(t3.overlapsWith(t4));
	}

}
