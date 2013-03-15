/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.patient;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;

import model.Employee;
import model.Patient;
import view.export.KalenderExportierenDialog;
import controller.OfficeController;

/**
 * Ein Panel um Patientendaten anzuzeigen und zu bearbeiten.
 * 
 * @author Alex
 */
public class PatientenDatenPanel extends JPanel {

	/** default id damit Eclipse ruhig ist */
	private static final long serialVersionUID = 1L;

	/** der OfficeControler */
	protected final OfficeController officeCtrl;

	// Die ganzen Swing(x)-Komponenten
	private final JLabel lblKm;
	private final JSpinner spinnerKm;

	private final JLabel lblEmployee;
	private final JComboBox<Employee> cmbEmployee;

	private final JCheckBox cboxPrivat;

	private final JTextPane textNotice;

	private final JTextPane textDisease;

	private final JButton btnAppointments;

	private final JButton btnMaterial;

	private final JButton btnBill;

	private Patient patient;

	/**
	 * Erstellt einen neues Panel.
	 * 
	 * @param officeController
	 *            DER OfficeControler.
	 */
	public PatientenDatenPanel(OfficeController officeController) {
		super();

		this.officeCtrl = officeController;
		patient = null;

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 100, 0 };
		layout.columnWeights = new double[] { 0.0, 0.0, 1.0 };
		layout.rowHeights = new int[] { 30, 30, 30, 30, 30, 30, 30, 70, 30, 80 };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				1.0, 0.0, 1.0 };
		setLayout(layout);

		addRowHeader("Entfernung:", 0);
		this.lblKm = new JLabel();
		SpinnerNumberModel kmModel = new SpinnerNumberModel(0, 0,
				Integer.MAX_VALUE, 1);
		this.spinnerKm = new JSpinner(kmModel);
		GridBagConstraints gbcKm = new GridBagConstraints();
		gbcKm.fill = GridBagConstraints.HORIZONTAL;
		gbcKm.anchor = GridBagConstraints.NORTHWEST;
		gbcKm.insets = new Insets(0, 0, 5, 0);
		gbcKm.gridwidth = 2;
		gbcKm.gridx = 1;
		gbcKm.gridy = 0;
		add(this.lblKm, gbcKm);
		add(this.spinnerKm, gbcKm);

		addRowHeader("Termine:", 1);
		this.btnAppointments = new JButton("Kommende Termine anzeigen");
		this.btnAppointments.setMnemonic('t');
		this.btnAppointments.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				KalenderExportierenDialog dialog = new KalenderExportierenDialog(
						officeCtrl);
				dialog.changePreset(patient, null);
				dialog.setLocationRelativeTo(getParent());
				dialog.setVisible(true);
			}
		});
		GridBagConstraints gbcTermine = new GridBagConstraints();
		gbcTermine.fill = GridBagConstraints.HORIZONTAL;
		gbcTermine.anchor = GridBagConstraints.NORTHWEST;
		gbcTermine.insets = new Insets(0, 0, 5, 0);
		gbcTermine.gridwidth = 2;
		gbcTermine.gridx = 1;
		gbcTermine.gridy = 1;
		add(this.btnAppointments, gbcTermine);

		addRowHeader("Material:", 2);
		this.btnMaterial = new JButton("Material anzeigen / bearbeiten");
		this.btnMaterial.setMnemonic('m');
		GridBagConstraints gbcMaterial = new GridBagConstraints();
		gbcMaterial.fill = GridBagConstraints.HORIZONTAL;
		gbcMaterial.anchor = GridBagConstraints.NORTHWEST;
		gbcMaterial.insets = new Insets(0, 0, 5, 0);
		gbcMaterial.gridwidth = 2;
		gbcMaterial.gridx = 1;
		gbcMaterial.gridy = 2;
		add(this.btnMaterial, gbcMaterial);
		this.btnMaterial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PatientenMaterialDialog dialog = new PatientenMaterialDialog(
						officeCtrl, patient);
				dialog.setLocationRelativeTo(getParent());
				dialog.setVisible(true);
			}
		});

		addRowHeader("Rechnung:", 3);
		this.btnBill = new JButton("Rechnung erstellen");
		this.btnBill.setMnemonic('r');
		GridBagConstraints gbcRechnung = new GridBagConstraints();
		gbcRechnung.fill = GridBagConstraints.HORIZONTAL;
		gbcRechnung.anchor = GridBagConstraints.NORTHWEST;
		gbcRechnung.insets = new Insets(0, 0, 5, 0);
		gbcRechnung.gridwidth = 2;
		gbcRechnung.gridx = 1;
		gbcRechnung.gridy = 3;
		add(this.btnBill, gbcRechnung);
		this.btnBill.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showBill();
			}
		});

		addRowHeader("Lieblingsmitarbeiter/in:", 4);
		this.lblEmployee = new JLabel();
		int employeeCount = officeCtrl.getOffice().getEmployees().size();
		Employee[] employeeArray = officeCtrl.getOffice().getEmployees()
				.toArray(new Employee[employeeCount]);
		ComboBoxModel<Employee> employeeModel = new DefaultComboBoxModel<Employee>(
				employeeArray);
		this.cmbEmployee = new JComboBox<Employee>(employeeModel);
		GridBagConstraints gbcMitarbeiter = new GridBagConstraints();
		gbcMitarbeiter.fill = GridBagConstraints.HORIZONTAL;
		gbcMitarbeiter.anchor = GridBagConstraints.NORTHWEST;
		gbcMitarbeiter.insets = new Insets(0, 0, 5, 0);
		gbcMitarbeiter.gridwidth = 2;
		gbcMitarbeiter.gridx = 1;
		gbcMitarbeiter.gridy = 4;
		add(this.lblEmployee, gbcMitarbeiter);
		add(this.cmbEmployee, gbcMitarbeiter);

		addRowHeader("Privatversichert:", 5);
		this.cboxPrivat = new JCheckBox();
		this.cboxPrivat.setEnabled(false);
		GridBagConstraints gbcPrivat = new GridBagConstraints();
		gbcPrivat.fill = GridBagConstraints.HORIZONTAL;
		gbcPrivat.anchor = GridBagConstraints.NORTHWEST;
		gbcPrivat.insets = new Insets(0, 0, 5, 0);
		gbcPrivat.gridwidth = 2;
		gbcPrivat.gridx = 1;
		gbcPrivat.gridy = 5;
		add(this.cboxPrivat, gbcPrivat);

		addRowHeader("Wichtige Notizen:", 6);

		final JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbcNotiz = new GridBagConstraints();
		gbcNotiz.gridwidth = 3;
		gbcNotiz.insets = new Insets(0, 0, 5, 0);
		gbcNotiz.fill = GridBagConstraints.BOTH;
		gbcPrivat.anchor = GridBagConstraints.NORTHWEST;
		gbcNotiz.gridx = 0;
		gbcNotiz.gridy = 7;
		add(scrollPane, gbcNotiz);

		this.textNotice = new JTextPane();
		scrollPane.setBackground(Color.ORANGE);
		scrollPane.setViewportView(textNotice);

		addRowHeader("Krankheiten:", 8);

		final JScrollPane scrollPane2 = new JScrollPane();
		GridBagConstraints gbcAkte = new GridBagConstraints();
		gbcAkte.gridwidth = 3;
		gbcAkte.fill = GridBagConstraints.BOTH;
		gbcPrivat.anchor = GridBagConstraints.NORTHWEST;
		gbcAkte.gridx = 0;
		gbcAkte.gridy = 9;
		add(scrollPane2, gbcAkte);

		this.textDisease = new JTextPane();
		scrollPane2.setBackground(Color.YELLOW);
		scrollPane2.setViewportView(textDisease);
	}

	/**
	 * Füllt die Labels und Felder mit den Daten der Person.
	 * 
	 * @param distance
	 *            Die Anfahrtstrecke in KM.
	 * @param preferredEmployee
	 *            Der lieblings Mittarbeiter oder null.
	 * @param privat
	 *            Ist der Patient privat versichert?
	 * @param notice
	 *            Wichtige Notizen zum Patienten
	 * @param disease
	 *            Text &uuml;ber Behandlungen, ...
	 */
	public void fillComponents(Patient pat) {
		int distance;
		Employee preferredEmployee;
		boolean privat;
		String notice;
		String disease;

		patient = pat;

		if (patient == null) {
			distance = 0;
			preferredEmployee = null;
			privat = false;
			notice = "";
			disease = "";

		} else {
			distance = patient.getDistance();
			preferredEmployee = patient.getPreferredEmployee();
			privat = patient.isPrivat();
			notice = patient.getNotice();
			disease = patient.getDisease();
		}

		btnAppointments.setEnabled(patient != null);
		btnMaterial.setEnabled(patient != null);
		btnBill.setEnabled(officeCtrl.getPatientController()
				.shouldNewBillCreated(patient));

		lblKm.setText(distance + " km");
		spinnerKm.setValue(distance);

		lblEmployee.setText(preferredEmployee == null ? "" : preferredEmployee
				.getLastName()
				+ ", " + preferredEmployee.getFirstName());
		cmbEmployee.setSelectedItem(preferredEmployee);

		cboxPrivat.setSelected(privat);

		textNotice.setText(notice);
		textDisease.setText(disease);
	}

	/**
	 * Setzt editModus auf editable und passt die Komponenten an.
	 * 
	 * @param editable
	 *            Bearbeiten-Modus ja oder nein
	 */
	public void setEditModus(boolean editable) {
		lblKm.setVisible(!editable);
		spinnerKm.setVisible(editable);

		lblEmployee.setVisible(!editable);
		cmbEmployee.setVisible(editable);

		cboxPrivat.setEnabled(editable);

		textNotice.setEditable(editable);
		textDisease.setEditable(editable);
	}

	/**
	 * Schaut ob alle Eingabefelder OK sind.
	 * 
	 * @return true, wenn alle Eingaben OK sind.
	 */
	public boolean isAllInputValid() {
		return true;
	}

	/**
	 * Fügt in die 1. Spalte des JPanel mit GridBagLayout ein Label mit dem Text
	 * in die row-te Zeile.
	 * 
	 * @param panel
	 *            Das Panel in das das Label kommt.
	 * @param text
	 *            Der Text des Labels.
	 * @param row
	 *            Die gewünschte Zeile.
	 */
	private void addRowHeader(String text, int row) {
		final JLabel label = new JLabel(text);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = row;

		add(label, gbc);
	}

	/**
	 * Erstellt eine Rechnung f&uuml;r den Patienten.
	 */
	private void showBill() {
		if (officeCtrl.getPatientController().shouldNewBillCreated(patient)) {
			JFileChooser fc = new JFileChooser();

			if (fc.showSaveDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String sFile = file.getAbsolutePath();
				if (!sFile.endsWith(".txt")) {
					sFile += ".txt";
				}

				String bill = officeCtrl.getPatientController().makeBill(
						patient);

				if (bill == null) {
					JOptionPane.showMessageDialog(null,
							"Beim Erstellen der Rechnung traten fehler auf!",
							"Fehler bei der Rechungserstellung",
							JOptionPane.ERROR_MESSAGE);
				}

				if (!officeCtrl.getIOController().saveStringToFile(bill, sFile)) {
					JOptionPane.showMessageDialog(getParent(),
							"Konnte Datei nicht speichern!",
							"Fehler bei der Rechungserstellung",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null,
					"Es liegen keine Termine zur Abrechnung vor.",
					"Fehler bei der Rechungserstellung",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Gibt die Strecke f&uuml;r die Anfahrt zur&uumlck.
	 * 
	 * @return Wie weit der Patient entfernt wohnt.
	 */
	public int getDistance() {
		return ((SpinnerNumberModel) spinnerKm.getModel()).getNumber()
				.intValue();
	}

	/**
	 * Gibt den lieblings Mitarbeiter zur&uuml;ck.
	 * 
	 * @return Der leiblings Mitarbeiter oder null.
	 */
	public Employee getPreferredEmployee() {
		return (Employee) cmbEmployee.getSelectedItem();
	}

	/**
	 * Ist der Patient privatversichert?
	 * 
	 * @return true == Patient ist privatversichert.
	 */
	public boolean getPrivat() {
		return cboxPrivat.isSelected();
	}

	/**
	 * Gibt die wichtigen Notizen zum Patienten zur&uuml;ck.
	 * 
	 * @return Wichtige Notizen zum Patienten.
	 */
	public String getNotiz() {
		return textNotice.getText();
	}

	/**
	 * Gibt die Patientenakte, i.e. der Freitext, zur&uuml;ck.
	 * 
	 * @return Der Text zum Patienten.
	 */
	public String getAkte() {
		return textDisease.getText();
	}
}
