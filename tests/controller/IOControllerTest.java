package controller;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.regex.*;

import model.*;

import org.junit.Before;
import org.junit.Test;

public class IOControllerTest extends ControllerTest {

	IOController ioCtrl;
	TimeInterval t;
	String today;

	@Before
	public void setUp() throws Exception {
		ioCtrl = officeCtrl.getIOController();
		today=makeToday();
	}
	
	@SuppressWarnings("deprecation")
	private String makeToday(){
		int day=new Date().getDate();
		t = new TimeInterval(new Date(113, 2, day, 00, 00), new Date(113, 2, day,
				23, 59));
		String s="";
		if(day<10)
			s="0";
		s+=Integer.toString(day);
		return s;
	}

	@Test
	public void testExportCalendarEmployeeTimeInterval() {
		String sExpected = "BEGIN:VCALENDAR\nPRODID:-//Sopra/YADBA//DE\nVERSION:2.0\nBEGIN:VEVENT\nDTSTART:201303"+today+"T100000\nDTEND:201303"+today+"T103000\nLOCATION:K10\nSUMMARY:Manuelle Therapie\nDESCRIPTION:Scott, Montgomery\nEND:VEVENT\nBEGIN:VEVENT\nDTSTART:201303"+today+"T153000\nDTEND:201303"+today+"T155000\nLOCATION:K10\nSUMMARY:Massage 20\nDESCRIPTION:Scott, Montgomery\nEND:VEVENT\nEND:VCALENDAR";
		Employee e = officeCtrl.getEmployeeController().getEmployeeByName(
				"Sebastian", "Venier");
		System.out.print(e.getFirstName());
		assertEquals("Falscher Ausgabestring", sExpected, ioCtrl
				.exportCalendar(e, t));
		assertEquals("Kein TimeIntervall", "", ioCtrl.exportCalendar(e, null));
		e = null;
		assertEquals("Kein Employee", "", ioCtrl.exportCalendar(e, t));
	}

	@Test
	public void testExportCalendarPatientTimeInterval() {
		String sExpected = "BEGIN:VCALENDAR\nPRODID:-//Sopra/YADBA//DE\nVERSION:2.0\nBEGIN:VEVENT\nDTSTART:201303"
				+ today
				+ "T100000\nDTEND:201303"
				+ today
				+ "T103000\nLOCATION:U11\nSUMMARY:Manuelle Therapie\nDESCRIPTION:Schmedding, Doris\nEND:VEVENT\nEND:VCALENDAR";
		Patient p = officeCtrl.getPatientController().getPatientByName("James",
				"Kirk");
		assertEquals("Falscher Ausgabestring", sExpected, ioCtrl
				.exportCalendar(p, t));
		assertEquals("Kein TimeIntervall", "", ioCtrl.exportCalendar(p, null));
		p = null;
		assertEquals("Kein Patient", "", ioCtrl.exportCalendar(p, t));
	}

	@Test
	public void testExportCalendarRoomTimeInterval() {
		String sExpected = "BEGIN:VCALENDAR\nPRODID:-//Sopra/YADBA//DE\nVERSION:2.0\nBEGIN:VEVENT\nDTSTART:201303"
				+ today
				+ "T100000\nDTEND:201303"
				+ today
				+ "T103000\nDESCRIPTION:Kasper, Florian\nSUMMARY:Krankengymnastik / Physiotherapie 30\nEND:VEVENT\nEND:VCALENDAR";
		List<Room> list = officeCtrl.getOffice().getRooms();
		Room r = null;
		for (Room rAudimax : list) {
			if (rAudimax.getName().contentEquals("Audimax"))
				r = rAudimax;
		}
		assertEquals("Falscher Ausgabestring", sExpected, ioCtrl
				.exportCalendar(r, t));
		assertEquals("Kein TimeIntervall", "", ioCtrl.exportCalendar(r, null));
		r = null;
		assertEquals("Raum", "", ioCtrl.exportCalendar(r, t));
	}

	@Test
	public void testDateTOiCalendarFormat() {
		Date d = new Date();
		String sReturn = ioCtrl.dateTOiCalendarFormat(d);
		Pattern muster = Pattern
				.compile("^2013(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])T([01][0-9]|2[0-3])[0-5][0-9]00$");
		Matcher match = muster.matcher(sReturn);
		if (!match.matches())
			fail("Falsches Format");
		d = null;
		sReturn = ioCtrl.dateTOiCalendarFormat(d);
		assertEquals("Kein Date", sReturn, "");
	}
}
