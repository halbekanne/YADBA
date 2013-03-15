package model;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class TimeOfDayIntervalTest {
	TimeOfDayInterval todi;
	Date now;

	@Before
	public void setUp() throws Exception {
		now = new Date();
		todi = new TimeOfDayInterval(new TimeInterval(now, 10));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testContainsDate() {
		Date davor, anfang, mittendrin, ende, danach;
		davor = new Date(now.getYear(), now.getMonth(), now.getDate(), now
				.getHours(), now.getMinutes() - 1);
		anfang = now;
		mittendrin = new Date(now.getYear(), now.getMonth(), now.getDate(), now
				.getHours(), now.getMinutes() + 5);
		ende = new Date(now.getYear(), now.getMonth(), now.getDate(), now
				.getHours(), now.getMinutes() + 10);
		danach = new Date(now.getYear(), now.getMonth(), now.getDate(), now
				.getHours(), now.getMinutes() + 11);
		assertFalse("Davor", todi.contains(davor));
		assertTrue("Anfang", todi.contains(anfang));
		assertTrue("Mittendrin", todi.contains(mittendrin));
		assertTrue("Ende", todi.contains(ende));
		assertFalse("danach", todi.contains(danach));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testContainsTimeOfDayInterval() {
		TimeOfDayInterval davor, anfang, mittendrin, ende, danach;
		davor = new TimeOfDayInterval(new TimeInterval(new Date(now.getYear(),
				now.getMonth(), now.getDate(), now.getHours(),
				now.getMinutes() - 11), 10));
		anfang = new TimeOfDayInterval(new TimeInterval(new Date(now.getYear(),
				now.getMonth(), now.getDate(), now.getHours(),
				now.getMinutes() - 5), 10));
		mittendrin = new TimeOfDayInterval(new TimeInterval(new Date(now
				.getYear(), now.getMonth(), now.getDate(), now.getHours(), now
				.getMinutes() + 2), 5));
		ende = new TimeOfDayInterval(new TimeInterval(new Date(now.getYear(),
				now.getMonth(), now.getDate(), now.getHours(),
				now.getMinutes() + 5), 10));
		danach = new TimeOfDayInterval(new TimeInterval(new Date(now.getYear(),
				now.getMonth(), now.getDate(), now.getHours(),
				now.getMinutes() + 11), 10));
		assertFalse("Davor", todi.contains(davor));
		assertFalse("Anfang", todi.contains(anfang));
		assertTrue("Mittendrin", todi.contains(mittendrin));
		assertFalse("Ende", todi.contains(ende));
		assertFalse("danach", todi.contains(danach));
		}

}
