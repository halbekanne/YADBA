package model;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import controller.OfficeController;

public class OfficeTest {
	Office o;

	@Before
	public void setUp() throws Exception {
		OfficeController officeCtrl = DataFaker.generateOfficeController();
		o = officeCtrl.getOffice();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testIsOpenDate() {
		Date vorher, anfang, mitte, ende, nachher;
		vorher = new Date(113, 2, 11, 8, 30);
		anfang = new Date(113, 2, 11, 9, 0);
		mitte = new Date(113, 2, 11, 12, 30);
		ende = new Date(113, 2, 11, 16, 30);
		nachher = new Date(113, 2, 11, 17, 0);
		assertFalse("vorher", o.isOpen(vorher));
		assertTrue("anfang", o.isOpen(anfang));
		assertTrue("mitte", o.isOpen(mitte));
		assertTrue("ende", o.isOpen(ende));
		assertFalse("nacher", o.isOpen(nachher));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testIsOpenTimeInterval() {
		Date vorher1, vorher2, anfang, mitte1, mitte2, ende, nachher1, nachher2;
		vorher1 = new Date(113, 2, 11, 8, 30);
		vorher2 = new Date(113, 2, 11, 8, 59);
		anfang = new Date(113, 2, 11, 9, 0);
		mitte1 = new Date(113, 2, 11, 9, 1);
		mitte2 = new Date(113, 2, 11, 16, 29);
		ende = new Date(113, 2, 11, 16, 30);
		nachher1 = new Date(113, 2, 11, 16, 31);
		nachher2 = new Date(113, 2, 11, 17, 0);
		assertFalse("vorher-vorher", o
				.isOpen(new TimeInterval(vorher1, vorher2)));
		assertFalse("vorher-anfang", o
				.isOpen(new TimeInterval(vorher2, anfang)));
		assertTrue("anfang-mitte", o.isOpen(new TimeInterval(anfang, mitte1)));
		assertTrue("mitte-mitte", o.isOpen(new TimeInterval(mitte1, mitte2)));
		assertTrue("mitte-ende", o.isOpen(new TimeInterval(mitte2, ende)));
		assertFalse("ende-nachher", o.isOpen(new TimeInterval(ende, nachher1)));
		assertFalse("nachher-nachher", o.isOpen(new TimeInterval(nachher1,
				nachher2)));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testNextOpening() {
		Date offen=new Date(113, 2, 11, 9, 1);
		assertTrue("Gerade offen",offen.equals(o.nextOpening(offen)));
		Date schonZu=new Date(113, 2, 11, 16, 50);
		Date wiederOffen=new Date(113, 2, 12, 9, 0);
		assertTrue("Komm morgen wieder",wiederOffen.equals(o.nextOpening(schonZu)));
		TimeOfDayInterval[] nul = { null, null, null, null, null, null, null };
		o.setOpeningTime(nul);
		assertTrue("NIEMALS! HA HA HA!",null==o.nextOpening(schonZu));
	}

}
