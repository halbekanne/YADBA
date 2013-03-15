package controller;

import model.*;

import org.junit.Before;

public class ControllerTest {
	
	OfficeController officeCtrl;
	
	@Before
	public void setup(){
		officeCtrl = DataFaker.generateOfficeController();
	}
		
//		officeCtrl = new OfficeController();
//		
//		Date morgens = new Date(23, 4, 13, 8, 0);
//		Date abends = new Date(23, 4, 13, 18, 0);
//		TimeOfDayInterval[] oeffnungszeiten = {
//				new TimeOfDayInterval(morgens, abends),
//				new TimeOfDayInterval(morgens, abends),
//				new TimeOfDayInterval(morgens, abends),
//				new TimeOfDayInterval(morgens, abends),
//				new TimeOfDayInterval(morgens, abends),
//				null,
//				null}; 
//		officeCtrl.getOffice().setOpeningTime(oeffnungszeiten);
//		
//		Activity act1 = new Activity("Massage (20 Minuten)",1500, 20, 1);
//		Activity act2 = new Activity("Massage (30 Minuten)",2000, 30, 1);
//		Activity act3 = new Activity("Manuelle Lymphdrainage",2500, 30, 1);
//		Activity act4 = new Activity("Yoga",11900, 75, 10);
//		Material mat1 = new Material("Funktioneller Tapeverband (klein)", 800);
//		Material mat2 = new Material("Funktioneller Tapeverband (groß)", 1500);
//		Material mat3 = new Material("Heiße Rolle", 1200);
//		officeCtrl.getServiceController().saveService(act1);
//		officeCtrl.getServiceController().saveService(act2);
//		officeCtrl.getServiceController().saveService(act3);
//		officeCtrl.getServiceController().saveService(act4);
//		officeCtrl.getServiceController().saveService(mat1);
//		officeCtrl.getServiceController().saveService(mat2);
//		officeCtrl.getServiceController().saveService(mat3);
//		
//		List<Activity> activities = new LinkedList<Activity>();
//		activities.addAll(officeCtrl.getOffice().getActivities());
//		Room r1 = new Room("Einzelraum 1", 1, activities);
//		activities.remove(officeCtrl.getServiceController().getServiceByName("Manuelle Lymphdrainage"));
//		Room r2 = new Room("Einzelraum 2", 1, activities);
//		Room r3 = new Room("Gruppenraum 3", 15, activities);
//		officeCtrl.getRoomController().saveRoom(r1);
//		officeCtrl.getRoomController().saveRoom(r2);
//		officeCtrl.getRoomController().saveRoom(r3);
//		
//		Contact dummyContact = new Contact("Otto-Hahn-Straße", "44227", "Dortmund", "", "", "");
//		Employee chef = new Employee(dummyContact, "Max", "Mustermann", "Herr", "1234", oeffnungszeiten, activities, Rank.BOSS);
//		Employee employee1 = new Employee(dummyContact, "Karl", "Stelzner", "Herr", "1234", oeffnungszeiten, activities, Rank.EMPLOYEE);
//		Employee employee2 = new Employee(dummyContact, "Dominik", "Halfkann", "Herr", "1234", oeffnungszeiten, activities, Rank.EMPLOYEE);
//		officeCtrl.getEmployeeController().saveEmployee(chef);
//		officeCtrl.getEmployeeController().saveEmployee(employee1);
//		officeCtrl.getEmployeeController().saveEmployee(employee2);
//		
//		Patient patient = new Patient(dummyContact, "Florian", "Kasper", "Herr", "", "", false, 10, null);
//		officeCtrl.getPatientController().savePatient(patient);
//		
//		Vacation v = new Vacation(new TimeInterval(new Date(113, 4, 18, 7, 0), new Date(113, 4, 25, 17, 0)), employee2);
//		officeCtrl.getAppointmentController().saveVacation(v);
//		Treatment t1 = new Treatment(
//				new TimeInterval(new Date(113, 4, 18, 9, 0), new Date(113, 4, 18, 9, 45)),
//				employee1, r1, act2, patient, false);
//		Treatment t2 = new Treatment(
//				new TimeInterval(new Date(113, 4, 18, 10, 0), new Date(113, 4, 18, 11, 15)),
//				employee1, r1, act3, patient, false);
//		officeCtrl.getAppointmentController().saveTreatment(t1);
//		officeCtrl.getAppointmentController().saveTreatment(t2);
//	}
		
}
