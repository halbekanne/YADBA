package controller;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import model.*;

import org.junit.Test;

public class RoomControllerTest extends ControllerTest {
	
	/**
	 * Testet saveRoom mit null, ungültigen Eingaben, Duplikaten und gültigen Räumen
	 */
	@Test
	public void testSaveRoom(){
		assertFalse(officeCtrl.getRoomController().saveRoom(null)); //Null Parameter abgefangen?
		assertFalse(officeCtrl.getRoomController().saveRoom(new Room("", -3, null))); //Ungültiger Parameter abgefangen?
		assertFalse(officeCtrl.getRoomController().saveRoom(officeCtrl.getOffice().getRooms().get(0))); //Duplikat abgefangen?
		
		Room newRoom = new Room ("Raum 1408", 1, officeCtrl.getOffice().getActivities()); //Gültiger Raum
		assertTrue(officeCtrl.getRoomController().saveRoom(newRoom)); //Gültiger Ablauf
		assertTrue(officeCtrl.getOffice().getRooms().contains(newRoom)); //Neuer Raum auch wirklich gespeichert?
		
		setup();
	}
	
	/**
	 * Testet getAvailableRooms mit null, ungültigen Eingaben, regularen Eingaben mit leerer Ergebnismenge und
	 * regulären Eingaben mit nicht-leerer Ergebnismenge
	 */
	@Test
	public void testGetAvailableRooms(){
		assertTrue(officeCtrl.getRoomController().getAvailableRooms(null) == null);
		assertTrue(officeCtrl.getRoomController().getAvailableRooms(new Activity("", -4, 3, 2)) == null);
		assertTrue(officeCtrl.getRoomController().getAvailableRooms(new Activity("dsa", 4, 30, 2)).isEmpty());
		for (Activity a : officeCtrl.getOffice().getActivities()){
			List<Room> result = new LinkedList<Room>();
			for (Room r : officeCtrl.getOffice().getRooms())
				if (r.isAble(a))
					result.add(r);
			assertEquals(result, officeCtrl.getRoomController().getAvailableRooms(a));
		}
	}
	
	@Test
	public void testGetPracticalAppointmentsInTime(){
		assertNull(officeCtrl.getRoomController().getPracticalAppoinmentsInTime(null, null));
	}
}
