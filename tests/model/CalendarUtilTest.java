package model;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class CalendarUtilTest {
	/** Teste ob getNextDay funktioniert. */
	@Test
	public void testGetNextDay(){
		Date d = new Date();
		Date d2 = CalendarUtil.getNextDay(d);
		assertEquals(86400000, d2.getTime()-d.getTime());
	}

	/** Teste ob addMinutes funktioniert. */
	@Test
	public void testGetNext5Minutes(){
		Date d = new Date();
		Date d2 = CalendarUtil.addMinutes(d, 5);
		assertEquals(300000, d2.getTime()-d.getTime());
	}

	/** Teste ob addMinutes mit negativen Minuten funktioniert. */
	@Test
	public void testLastNext5Minutes(){
		Date d = new Date();
		Date d2 = CalendarUtil.addMinutes(d, -5);
		assertEquals(-300000, d2.getTime()-d.getTime());
	}

	/** Teste ob getDayOfWeek funktioniert. */
	@Test
	public void testGetWeekday(){
		Calendar c = Calendar.getInstance();
		c.set(2013, Calendar.MARCH, 7, 15, 29, 25);
		int dayOfWeek = CalendarUtil.getDayOfWeek(c.getTime());
		assertEquals(3, dayOfWeek); // Der 7.3.13 war ein Donnerstag -> 3

		c.set(2013, Calendar.FEBRUARY, 25, 15, 2, 0);
		dayOfWeek = CalendarUtil.getDayOfWeek(c.getTime());
		assertEquals(0, dayOfWeek); // Der 25.2.13 war ein Montag -> 0
	}

	/** Teste ob combineDateTime funktioniert. */
	@Test
	public void testCombineDateTime(){
		Calendar cDate = Calendar.getInstance();
		cDate.set(2013, Calendar.FEBRUARY, 25, 15, 2, 0);

		Calendar cTime = Calendar.getInstance();
		cTime.set(2013, Calendar.MARCH, 7, 15, 29, 25);

		Calendar cResult = Calendar.getInstance();
		cResult.setTime(CalendarUtil.combineDateTime(cDate.getTime(), cTime.getTime()));

		Calendar cRightResult = Calendar.getInstance();
		cRightResult.clear();
		cRightResult.set(2013, Calendar.FEBRUARY, 25, 15, 29, 25);
		assertEquals(cRightResult, cResult);
	}
}
