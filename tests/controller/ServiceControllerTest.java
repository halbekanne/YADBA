package controller;

import static org.junit.Assert.*;

import model.Activity;
import model.Material;
import model.Service;

import org.junit.Test;

public class ServiceControllerTest extends ControllerTest {
	
	/**
	 * Testet saveService mit null, ungültigen Parametern, Duplikaten und gültigen Parametern, jeweils für
	 * Activity und Material
	 */
	@Test
	public void testSaveService(){
		
		assertFalse(officeCtrl.getServiceController().saveService(null)); //null Parameter abgefangen?
		assertFalse(officeCtrl.getServiceController().saveService(new Material(null, 0))); //Ungültiger Parameter abgefangen?
		assertFalse(officeCtrl.getServiceController().saveService(new Activity("Test", 120, 15, -5))); //Ungültiger Parameter abgefangen?
		assertTrue(officeCtrl.getServiceController().saveService(new Activity("Test1", 120, 15, 1))); //Regulärer Ablauf
		assertTrue(officeCtrl.getServiceController().saveService(new Activity("Test2", 120, 15, 1))); //Regulärer Ablauf
		assertFalse(officeCtrl.getServiceController().saveService(new Activity("Test1", 120, 15, 1))); //Duplikat abgefangen?
		assertTrue(officeCtrl.getServiceController().saveService(new Material("Test3", 120))); //Regulärer Ablauf
		assertFalse(officeCtrl.getServiceController().saveService(new Material("Test3", 120))); //Duplikat abgefangen?
		assertFalse(officeCtrl.getServiceController().saveService(new Material("Test1", 120))); //Duplikat abgefangen?
		setup();
	}
	
	/**
	 * Testet getServiceByName mit null, nicht enthaltenen Namen und existierende Namen von Activities und Materials
	 */
	@Test
	public void testGetServiceByName(){
		assertTrue(officeCtrl.getServiceController().getServiceByName(null) == null); //null Parameter abgefangen?
		assertTrue(officeCtrl.getServiceController().getServiceByName("28975") == null);//Nicht vorhandenen Namen testen
		
		for (Activity a : officeCtrl.getOffice().getActivities()){ //Regulären Ablauf für alle Behandlungen
			assertEquals(a, officeCtrl.getServiceController().getServiceByName(a.getName())); //Regulärer Ablauf
		}
		for (Material a : officeCtrl.getOffice().getMaterials()){ //Regulärer Ablauf für alle Materialen
			assertEquals(a, officeCtrl.getServiceController().getServiceByName(a.getName())); //Regulärer Ablauf
		}
	}
	
	/**
	 * Testet remove Service mit null, ungültigen Parametern und gülptigen Parametern, jeweils für Activity und Material
	 */
	@Test
	public void testRemoveService(){
		assertFalse(officeCtrl.getServiceController().removeService(null)); //null Parameter abgefangen?
		assertFalse(officeCtrl.getServiceController().removeService(new Activity("", -5, 2, 3))); //Ungültiger Parameter abgefangen?
		Service deletedService;
		if (!officeCtrl.getOffice().getActivities().isEmpty()){
			deletedService = officeCtrl.getOffice().getActivities().get(0);
			assertTrue(officeCtrl.getServiceController().removeService(deletedService)); //Regulärer Ablauf
			assertTrue(officeCtrl.getServiceController().getServiceByName(deletedService.getName()) == null); //Wurde die Behandlung auch wirklich gelöscht
		}
		if (!officeCtrl.getOffice().getMaterials().isEmpty()){
			deletedService = officeCtrl.getOffice().getMaterials().get(0);
			assertTrue(officeCtrl.getServiceController().removeService(deletedService)); //Regulärer Ablauf
			assertTrue(officeCtrl.getServiceController().getServiceByName(deletedService.getName()) == null); //Wurde das Material auch wirklich gelöscht
		}
		setup();
	}
}
