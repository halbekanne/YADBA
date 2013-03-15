/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.management;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JSeparator;

import model.Activity;
import model.Contact;
import model.Employee;
import model.Rank;
import model.TimeOfDayInterval;
import controller.EmployeeController;
import controller.OfficeController;


public class MitarbeiterFormular extends PersonenFormular {

	/** default id damit Eclipse ruhig ist */
	private static final long serialVersionUID = 1L;
	
	// Die ganzen Swing(x)-Komponenten
	private final EmployeeDataPanel emplyeePanel;
	
	/**
	 * Create the dialog.
	 */
	public MitarbeiterFormular(OfficeController officeController) {
		this(officeController, null);
	}

	/**
	 * 
	 * @param officeController
	 * @param employee
	 * @wbp.parser.constructor
	 */
	public MitarbeiterFormular(OfficeController officeController, Employee employee) {
		super(officeController, employee);
		
		setBounds(100, 100, 400, 604);
		
		GridBagConstraints gbcSeperator = new GridBagConstraints();
		gbcSeperator.fill = GridBagConstraints.HORIZONTAL;
		gbcSeperator.anchor = GridBagConstraints.NORTHWEST;
		gbcSeperator.insets = new Insets(0, 0, 5, 0);
		gbcSeperator.gridx = 0;
		gbcSeperator.gridy = 3;
		contentPanel.add(new JSeparator(), gbcSeperator);
		
		emplyeePanel = new EmployeeDataPanel(officeController, employee);
		GridBagConstraints gbcPatient = new GridBagConstraints();
		gbcPatient.fill = GridBagConstraints.HORIZONTAL;
		gbcPatient.anchor = GridBagConstraints.NORTHWEST;
		gbcPatient.insets = new Insets(0, 0, 5, 0);
		gbcPatient.gridx = 0;
		gbcPatient.gridy = 4;
		gbcPatient.weighty = 1.0;
		contentPanel.add(emplyeePanel, gbcPatient);
		
		fillComponents();
		setEditModus(true);
		
		// Mitarbeiter werden nur bearbeitet, kein umschalten
		btnEdit.removeActionListener(btnEdit.getActionListeners()[0]);
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	@Override
	protected boolean saveInput() {
		EmployeeController emplCtrl = officeCtrl.getEmployeeController();
		
		String title = namePanel.getTitle();
		
		String firstName = namePanel.getFirstName();
		
		String lastName = namePanel.getLastName();
		
		Contact contact = contactPanel.getContact();
		
		String password = emplyeePanel.getPassword();
		
		Rank rank = emplyeePanel.getRank();
		
		TimeOfDayInterval[] workingTime;
		if (person == null) {
			workingTime = emplyeePanel.getWorkingTimes();
		} else {
			workingTime = ((Employee) person).getWorkingTime();
		}
		
		List<Activity> activities = emplyeePanel.getActivities();
		
		Employee newEmployee= new Employee(contact,
				firstName,
				lastName,
				title,
				password,
				workingTime,
				activities,
				rank
				);
	
		if (person == null) {
			return emplCtrl.saveEmployee(newEmployee);
		} else {
			return emplCtrl.editEmployee((Employee)person, newEmployee);
		}
	}

	@Override
	protected void fillComponents() {
		super.fillComponents();
		
		Employee empl = (Employee) person;
		
		Rank position;
		
		if (empl == null) {
			setTitle("Neuen Mitarbeiter anlegen");
			
			position = Rank.EMPLOYEE;
		} else {
			setTitle("Mitarbeiter: " + person.getFirstName() + " " + person.getLastName());
			
			position = empl.getRank();
		}
		
		emplyeePanel.fillComponents(position);
	}
}
