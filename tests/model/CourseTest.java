package model;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controller.OfficeController;

public class CourseTest {
	OfficeController officeCtrl;

	@Before
	public void setUp() throws Exception {
		officeCtrl=DataFaker.generateOfficeController();
	}

	@Test
	public void testValidAppointment() {
		List<Appointment> list=officeCtrl.getOffice().getAppointments();
		for(Appointment a:list){
			if(a instanceof Course){
				Course c=(Course)a;
				while(c.getPrevCourse()!=null)
					c=c.getPrevCourse();
				TimeInterval t=new TimeInterval(new Date(), 60);
				Course cTest=new Course(t, officeCtrl.getOffice().getEmployees().get(0), officeCtrl.getOffice().getRooms().get(0), officeCtrl.getOffice().getActivities().get(0),c,null);
				c.setNextCourse(cTest);
				assertTrue(cTest.validAppointment());
			}
		}
	}
}