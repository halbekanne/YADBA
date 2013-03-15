/**
 * 
 */
package controller;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import model.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Stefan Noll
 *
 */
@SuppressWarnings("unused")
public class PatientControllerTest{

	private OfficeController oc;
	private PatientController pc;
	private Patient p1;
	private Patient p2;
	private Patient p3;
	
	private Patient patient1, patient2, patient3, patient4, patient5;
	private Employee employee1, employee2, employee3, employee4, employee5;
	private Activity activity1, activity2, activity3, activity4, activity5;
	private Material material1, material2, material3, material4, material5;
	private Room room1, room2, room3, room4, room5;
	private Treatment treatment1, treatment2, treatment3, treatment4, treatment5;
	
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {
		oc = new OfficeController();
		pc = oc.getPatientController();
		Contact c1 = new Contact("street1", "plz1", "city1", "phone1", "mobile1", "email1");
		Contact c2 = new Contact("street2", "plz2", "city2", "phone2", "mobile2", "email2");
		p1 = new Patient(c1,"firstNameP1", "lastNameP1", "titleP1", "diseasesP1", "noticeP1", true, 0, null);
		p2 = new Patient(null,"firstNameP3", "lastNameP3", "titleP3", "diseasesP3", "noticeP3", true, 0, null);
		p3 = new Patient(c2,"firstNameP2", "lastNameP2", "titleP2", "diseasesP2", "noticeP2", false, 22, null);
		
		Date morgens = new Date(23, 4, 13, 8, 0);
		Date abends = new Date(23, 4, 13, 18, 0);
		TimeOfDayInterval[] oeffnungszeiten = {
				new TimeOfDayInterval(morgens, abends),
				new TimeOfDayInterval(morgens, abends),
				new TimeOfDayInterval(morgens, abends),
				new TimeOfDayInterval(morgens, abends),
				new TimeOfDayInterval(morgens, abends),
				null,
				null}; 
		oc.getOffice().setOpeningTime(oeffnungszeiten);
		
		List<Activity> activities = oc.getOffice().getActivities();
		activity1 = new Activity("Massage",1500, 20, 1);
		activity2 = new Activity("Massage",2000, 30, 1);
		activity3 = new Activity("Manuelle Lymphdrainage",2500, 30, 1);
		activity4 = new Activity("Yoga",11900, 75, 10);
		activity5 = new Activity("Hotstone-Massage",2500, 30, 1);
		activities.add(activity1);
		activities.add(activity2);
		activities.add(activity3);
		activities.add(activity4);
		activities.add(activity5);
		
		List<Material> materials = oc.getOffice().getMaterials();
		material1 = new Material("Funktioneller Tapeverband (klein)", 800);
		material2 = new Material("Funktioneller Tapeverband (groß)", 1500);
		material3 = new Material("Heiße Rolle", 1200);
		material4 = new Material("Rotlich", 600);
		material5 = new Material("Kaelteanwendung", 1000);
		materials.add(material1);
		materials.add(material2);
		materials.add(material3);
		materials.add(material4);
		materials.add(material5);
		
		List<Room> rooms = oc.getOffice().getRooms();
		room1 = new Room("Einzelraum 1", 1, activities);
		room2 = new Room("Einzelraum 2", 1, activities);
		room3 = new Room("Gruppenraum 1", 10, activities);
		room4 = new Room("Gruppenraum 2", 20, activities);
		room5 = new Room("Gruppenraum 3", 30, activities);
		rooms.add(room1);
		rooms.add(room2);
		rooms.add(room3);
		rooms.add(room4);
		rooms.add(room5);
		
		Contact dummyContact = new Contact("Otto-Hahn-Straße", "44227", "Dortmund", "...", "...", "...");
		List<Employee> employees = oc.getOffice().getEmployees();
		employee1 = new Employee(dummyContact, "Stefan", "Noll", "Herr", "1234", oeffnungszeiten, activities, Rank.BOSS);
		employee2 = new Employee(dummyContact, "Max", "Mustermann", "Herr", "1234", oeffnungszeiten, activities, Rank.REPLACEMENT);
		employee3 = new Employee(dummyContact, "Karl", "Stelzner", "Herr", "1234", oeffnungszeiten, activities, Rank.EMPLOYEE);
		employee4 = new Employee(dummyContact, "Dominik", "Halfkann", "Herr", "1234", oeffnungszeiten, activities, Rank.EMPLOYEE);
		employee5 = new Employee(dummyContact, "Der", "Horst", "Herr", "1234", oeffnungszeiten, activities, Rank.EMPLOYEE);
		
		List<Patient> patients = oc.getOffice().getPatients();
		patient1 = new Patient(dummyContact, "Florian", "Kasper", "Herr", "diseases", "notice...", true, 10, null);
		patient2 = new Patient(dummyContact, "Benjamin", "Kasper", "Herr", "diseases", "notice...", false, 15, null);
		patient3 = new Patient(dummyContact, "Michael", "Kasper", "Frau", "diseases", "notice...", false, 7, employee3);
		patient4 = new Patient(dummyContact, "Fabian", "Kasper", "Herr", "diseases", "notice...", false, 5, employee2);
		patient5 = new Patient(dummyContact, "Walter", "Wurst", "Herr Prof. Dr.", "diseases", "notice...", true, 11, employee1);
		
		Vacation v = new Vacation(new TimeInterval(new Date(113, 4, 18, 7, 0), new Date(113, 4, 25, 17, 0)), employee1);
		oc.getAppointmentController().saveVacation(v);
		
		treatment1 = new Treatment(new TimeInterval(new Date(113, 4, 18, 9, 0), new Date(113, 4, 18, 9, 45)), employee1, room1, activity2, patient1, false);
		treatment2 = new Treatment(new TimeInterval(new Date(113, 4, 18, 10, 0), new Date(113, 4, 18, 11, 15)), employee1, room1, activity2, patient1, true);
		treatment3 = new Treatment(new TimeInterval(new Date(113, 4, 17, 11, 0), new Date(113, 4, 18, 11, 45)), employee2, room1, activity2, patient3, false);
		treatment4 = new Treatment(new TimeInterval(new Date(113, 4, 17, 12, 0), new Date(113, 4, 18, 12, 15)), employee2, room1, activity2, patient4, true);
		treatment5 = new Treatment(new TimeInterval(new Date(113, 4, 17, 12, 0), new Date(113, 4, 18, 12, 15)), employee2, room1, activity1, patient5, false);
		patient1.getTreatments().add(treatment1);
		patient1.getTreatments().add(treatment2);
	}

	/**
	 * Test savePatient(Patient p): Prüft, ob <b>p</b> ein gültiger Patient ist und trägt diesen in die Praxis ein.<br>
	 * Testfaelle<br>
	 * 1. hinzufuegen eines null-Patienten
	 * 2. hinzufuegen eines gueltigen Patienten zu leerer Liste
	 * 3. hinzufuegen einen gueltigen Patienten der bereits in der Liste ist
	 * 4. hinzufuegen eines ungueltigen Patienten
	 */
	@Test
	public void testSavePatient() {
		assertFalse("null als Parameter sollte false zurueckgeben", pc.savePatient(null));
		
		assertTrue("Patient sollte zur anfangs leeren Liste hinzugefuegt werden koennen", pc.savePatient(p1));
		
		assertTrue("Patient sollte zur anfangs leeren Liste hinzugefuegt worden sein", oc.getOffice().getPatients().contains(p1));
		
		int sizeBefore = oc.getOffice().getPatients().size();
		pc.savePatient(p1);
		int sizeAfter = oc.getOffice().getPatients().size();
		assertSame(sizeBefore, sizeAfter);
		
		assertFalse("Patient(ungueltig) soll nicht zur Liste hinzugefuegt werden koennen", pc.savePatient(p2));
	}

	/**
	 * Test editPatient(Patient oldP, Patient newP): Ersetzt, wenn möglich, die Daten von <b>oldP</b> durch die von <b>newP</b>
	 * Es werden nur die statischen Patientendaten geändert, die Terminlisten werden nicht angefasst!<br>
	 * Testfaelle:<br>
	 * 1. null-Patienten<br>
	 * 2. ungueltige Patienten<br>
	 * 3. gueltige Patienten: ob die statischen Patientendaten des neuen P in den alten P uebernommen werden<br>
	 */
	@Test
	public void testEditPatient() {
		assertFalse("Parameter duerfen nicht null sein", pc.editPatient(p1, null));
		assertFalse("Parameter duerfen nicht null sein", pc.editPatient(null, p2));
		assertFalse("Parameter duerfen nicht null sein", pc.editPatient(null, null));
		
		assertFalse("Zweiter Patient ist ungueltig, und darf nicht uebernommen werden",pc.editPatient(p1, p2));
		
		pc.editPatient(p1, p3);
		assertSame(p1.getContact(), p3.getContact());
		assertSame(p1.getDisease(), p3.getDisease());;
		assertSame(p1.getDistance(), p3.getDistance());
		assertSame(p1.getFirstName(), p3.getFirstName());
		assertSame(p1.getLastName(), p3.getLastName());
		assertSame(p1.getNotice(), p3.getNotice());
		assertSame(p1.getPreferredEmployee(), p3.getPreferredEmployee());
		assertSame(p1.getTitle(), p3.getTitle());
	}

	/**
	 * Test removePatient(Patient p): Loescht den Patienten <b>p</b> aus der Datenbank.<br>
	 * Testfaelle:<br>
	 * 1. null-Patient<br>
	 * 2. Patient aus leerer Liste entfernen<br>
	 * 3. Patient aus Liste entfernen, die Patien enthaelt<br>
	 */
	@Test
	public void testRemovePatient() {
		//TODO evtl. Testfaelle an "archivieren" anpassen
		assertFalse("null-Patient soll nicht entfernt werden", pc.removePatient(null));
		
		oc.getOffice().getPatients().clear();
		assertSame(oc.getOffice().getPatients().size(),0);
		assertTrue("Patient soll aus leerer Liste entfernt werden koennen", pc.removePatient(p1));
		
		oc.getOffice().getPatients().add(p3);
		assertTrue("Patient aus Liste entfernen", pc.removePatient(p3));
		assertFalse("Patient soll nicht mehr in Liste sein", oc.getOffice().getPatients().contains(p3));
		assertSame(oc.getOffice().getPatients().size(),0);
	}

	/**
	 * Test getPatientByName(String firstName, String lastName): Gibt den Patienten mit Namen firstName lastName zurück.<br>
	 * Testfaelle:<br>
	 * 1. null-Strings<br>
	 * 2. Patienten Name, der nicht vorkommt<br>
	 * 3. Patienten Name, der vorkommt<br>
	 */
	@Test
	public void testGetPatientByName() {
		assertSame(pc.getPatientByName(null, null), null);
		
		assertSame(pc.getPatientByName(p1.getFirstName(), p1.getLastName()), null);
		
		oc.getOffice().getPatients().add(p1);
		assertSame(pc.getPatientByName(p1.getFirstName(), p1.getLastName()), p1);
	}

	/**
	 * Test makeBill(Patient: Erzeugt eine Rechnung über alle Leistungen, von denen noch keine Rechnung
	 * erstellt wurde, sofern der Patient privat versichert ist.<br>
	 * Testfaelle:<br>
	 * 1. null-Patient<br>
	 * 2. PrivatPatient ohne Termine<br>
	 * 3. PrivatPatient mit Terminen<br>
	 * 4. Patient ohne Termine<br>
	 * 5. Patient mit Terminen<br>
	 */
	@Test
	public void testMakeBill() {
		//TODO Test
//		System.out.println(pc.makeBill(patient1));  //geht doch!
	}

	/**
	 * Test getPracticalAppointmentsInTime(Patient p, TimeInterval t): Gibt für einen Patienten <b>p</b> alle Termine zurück, die das Intervall
	 * <b>t</b> betreffen.<br>
	 * Testfaelle:<br>
	 * 1. null-Parameter<br>
	 * 2. Patient mit Terminen in t
	 * 3. Patient ohne Termine in t
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testGetPracticalAppointmentsInTime() {
		assertSame(pc.getPracticalAppointmentsInTime(null, null),null);
		
		List<PracticalAppointment> pracApps = pc.getPracticalAppointmentsInTime(patient1, new TimeInterval(new Date(113, 4, 18, 8, 0), new Date(113, 4, 18, 11, 15)));
		if(pracApps != null){
			assertTrue(pracApps.contains(treatment1));
			assertTrue(pracApps.contains(treatment2));
		} else {
			fail("pracApps sollte nicht null sein");
		}
		
		pracApps = null;
		pracApps = pc.getPracticalAppointmentsInTime(patient1, new TimeInterval(new Date(110, 4, 18, 8, 0), new Date(110, 4, 18, 11, 15)));
		if(pracApps != null){
		assertTrue(pracApps.isEmpty());
		} else {
			fail("pracApps sollte nicht null sein");
		}
	}

}
