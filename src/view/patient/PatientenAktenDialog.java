/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.patient;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JSeparator;

import model.Contact;
import model.Employee;
import model.Patient;
import view.management.PersonenFormular;
import controller.OfficeController;
import controller.PatientController;

/**
 * Die Patientenakte zum Anzeigen oder Bearbeiten.
 * 
 * @author Alexander Schieweck
 */
public class PatientenAktenDialog extends PersonenFormular {

	/** default id damit Eclipse ruhig ist */
	private static final long serialVersionUID = 1L;

	// Die ganzen Swing(x)-Komponenten
	private final PatientenDatenPanel patientenPanel;

	/**
	 * Dialog für einen neuen Patienten erzeugen.
	 */
	public PatientenAktenDialog(OfficeController officeController) {
		this(officeController, null, true);

		setEditModus(true);
	}

	/**
	 * Erzeugt einen Dialog mit der Patientenakte des Patienten.
	 * 
	 * @wbp.parser.constructor
	 */
	public PatientenAktenDialog(OfficeController officeController,
			Patient patient, boolean forEdit) {
		super(officeController, patient);

		// Allgemeines
		setBounds(100, 100, 450, 750);

		GridBagConstraints gbcSeperator = new GridBagConstraints();
		gbcSeperator.fill = GridBagConstraints.HORIZONTAL;
		gbcSeperator.anchor = GridBagConstraints.NORTHWEST;
		gbcSeperator.insets = new Insets(0, 0, 5, 0);
		gbcSeperator.gridx = 0;
		gbcSeperator.gridy = 3;
		contentPanel.add(new JSeparator(), gbcSeperator);

		patientenPanel = new PatientenDatenPanel(officeController);
		GridBagConstraints gbcPatient = new GridBagConstraints();
		gbcPatient.fill = GridBagConstraints.HORIZONTAL;
		gbcPatient.anchor = GridBagConstraints.NORTHWEST;
		gbcPatient.insets = new Insets(0, 0, 5, 0);
		gbcPatient.gridx = 0;
		gbcPatient.gridy = 4;
		gbcPatient.weighty = 1.0;
		contentPanel.add(patientenPanel, gbcPatient);

		fillComponents();
		setEditModus(forEdit);

		if (!forEdit) {
			rootPane.setDefaultButton(btnClose);
		}
	}

	@Override
	protected boolean saveInput() {
		PatientController patCtrl = officeCtrl.getPatientController();

		String title = namePanel.getTitle();

		String firstName = namePanel.getFirstName();

		String lastName = namePanel.getLastName();

		Contact contact = contactPanel.getContact();

		int distance = patientenPanel.getDistance();

		Employee preferredEmployee = patientenPanel.getPreferredEmployee();

		String notiz = patientenPanel.getNotiz();

		String akte = patientenPanel.getAkte();

		boolean privat = patientenPanel.getPrivat();

		Patient newPatient = new Patient(contact, firstName, lastName, title,
				akte, notiz, privat, distance, preferredEmployee);

		if (person == null) {
			return patCtrl.savePatient(newPatient);
		} else {
			return patCtrl.editPatient((Patient) person, newPatient);
		}
	}

	@Override
	protected void fillComponents() {
		super.fillComponents();

		Patient patient = (Patient) person;

		if (patient == null) {
			setTitle("Neuen Patienten anlegen");
		} else {
			setTitle("Patientenakte von " + patient.getFirstName() + " "
					+ patient.getLastName());
		}

		patientenPanel.fillComponents(patient);
	}

	@Override
	public void setEditModus(boolean editable) {
		super.setEditModus(editable);
		patientenPanel.setEditModus(editable);
	}

	@Override
	protected boolean isAllInputValid() {
		return super.isAllInputValid() && patientenPanel.isAllInputValid();
	}
}
