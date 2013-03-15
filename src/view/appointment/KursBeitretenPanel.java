/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.appointment;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Appointment;
import model.Course;
import model.Patient;
import view.util.FilteredJList;
import controller.OfficeController;

/**
 * 
 * @author Stefan Noll
 * 
 */
public class KursBeitretenPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OfficeController oc;
	private JTextField textFieldPatients;
	private JTextField textFieldCourses;
	private FilteredJList listPatients;
	private FilteredJList listCourses;
	private JCheckBox chckbxselfPaid;
	private JButton btnKursPruefen;
	private JLabel lblstatus;
	private JButton okButton;
	private boolean ok = false;
	private Patient p;
	private Course c;

	/**
	 * @param okButton
	 *            the okButton to set
	 */
	public void setOkButton(JButton okButton) {
		this.okButton = okButton;
	}

	/**
	 * Create the panel.
	 */
	@SuppressWarnings("unchecked")
	public KursBeitretenPanel(OfficeController oc_) {
		oc = oc_;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 78, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 16, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblKurs = new JLabel("Patient");
		lblKurs.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblKurs = new GridBagConstraints();
		gbc_lblKurs.fill = GridBagConstraints.BOTH;
		gbc_lblKurs.insets = new Insets(0, 0, 5, 5);
		gbc_lblKurs.gridwidth = 2;
		gbc_lblKurs.gridx = 0;
		gbc_lblKurs.gridy = 0;
		add(lblKurs, gbc_lblKurs);

		JLabel lblKurs_1 = new JLabel("Kurs");
		lblKurs_1.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblKurs_1 = new GridBagConstraints();
		gbc_lblKurs_1.fill = GridBagConstraints.BOTH;
		gbc_lblKurs_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblKurs_1.gridwidth = 2;
		gbc_lblKurs_1.gridx = 2;
		gbc_lblKurs_1.gridy = 0;
		add(lblKurs_1, gbc_lblKurs_1);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);

		listPatients = new FilteredJList();
		listPatients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(listPatients);
		listPatients.setCellRenderer(new DefaultListCellRenderer() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				Patient p = (Patient) value;
				Course c = ((Course)listCourses.getSelectedValue());
				setText(p.toString() + (c != null && c.getPatients().contains(p)?" (nimmt teil)":""));
				return this;
			}
		});

		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridwidth = 2;
		gbc_scrollPane_1.gridx = 2;
		gbc_scrollPane_1.gridy = 1;
		add(scrollPane_1, gbc_scrollPane_1);

		listCourses = new FilteredJList();
		listCourses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listCourses.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				listPatients.repaint();
			}
		});
		scrollPane_1.setViewportView(listCourses);

		textFieldPatients = listPatients.getFilterField();
		GridBagConstraints gbc_textFieldPatients = new GridBagConstraints();
		gbc_textFieldPatients.gridwidth = 2;
		gbc_textFieldPatients.anchor = GridBagConstraints.NORTH;
		gbc_textFieldPatients.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPatients.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPatients.gridx = 0;
		gbc_textFieldPatients.gridy = 2;
		add(textFieldPatients, gbc_textFieldPatients);
		textFieldPatients.setColumns(10);

		textFieldCourses = listCourses.getFilterField();
		GridBagConstraints gbc_textFieldCourses = new GridBagConstraints();
		gbc_textFieldCourses.gridwidth = 2;
		gbc_textFieldCourses.anchor = GridBagConstraints.NORTH;
		gbc_textFieldCourses.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldCourses.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldCourses.gridx = 2;
		gbc_textFieldCourses.gridy = 2;
		add(textFieldCourses, gbc_textFieldCourses);
		textFieldCourses.setColumns(10);

		chckbxselfPaid = new JCheckBox("Selbstzahler");
		GridBagConstraints gbc_chckbxselfPaid = new GridBagConstraints();
		gbc_chckbxselfPaid.anchor = GridBagConstraints.WEST;
		gbc_chckbxselfPaid.fill = GridBagConstraints.VERTICAL;
		gbc_chckbxselfPaid.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxselfPaid.gridx = 0;
		gbc_chckbxselfPaid.gridy = 3;
		add(chckbxselfPaid, gbc_chckbxselfPaid);

		btnKursPruefen = new JButton("Kurs pruefen");
		btnKursPruefen.setMnemonic('k');
		btnKursPruefen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int indexPatient = listPatients.getSelectedIndex();
				int indexCourse = listCourses.getSelectedIndex();

				if (indexPatient >= 0) {
					p = (Patient) listPatients.getModel().getElementAt(
							indexPatient);
				} else {
					lblstatus.setText("Es muss ein Patient ewaehlt werden.");
					lblstatus.setForeground(Color.RED);
				}
				if (indexCourse >= 0) {
					c = (Course) listCourses.getModel().getElementAt(
							indexCourse);
				} else {
					lblstatus.setText("Es muss ein Kurs ewaehlt werden.");
					lblstatus.setForeground(Color.RED);
				}

				ok = oc.getAppointmentController().spotLeft(c, p);

				if (ok) {
					okButton.setEnabled(true);
					lblstatus.setText("OK");
					lblstatus.setForeground(Color.GREEN);
				} else {
					lblstatus
							.setText("Der Patient kann an dem Kurs nicht teilnehmen.");
					lblstatus.setForeground(Color.RED);
				}

			}
		});
		GridBagConstraints gbc_btnKursPruefen = new GridBagConstraints();
		gbc_btnKursPruefen.gridwidth = 3;
		gbc_btnKursPruefen.anchor = GridBagConstraints.EAST;
		gbc_btnKursPruefen.fill = GridBagConstraints.VERTICAL;
		gbc_btnKursPruefen.insets = new Insets(0, 0, 5, 0);
		gbc_btnKursPruefen.gridx = 1;
		gbc_btnKursPruefen.gridy = 3;
		add(btnKursPruefen, gbc_btnKursPruefen);

		lblstatus = new JLabel("");
		lblstatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblstatus.setForeground(Color.RED);
		GridBagConstraints gbc_lblstatus = new GridBagConstraints();
		gbc_lblstatus.fill = GridBagConstraints.BOTH;
		gbc_lblstatus.gridwidth = 4;
		gbc_lblstatus.gridx = 0;
		gbc_lblstatus.gridy = 4;
		add(lblstatus, gbc_lblstatus);

		init();

	}

	private void init() {
		if (oc != null && oc.getOffice() != null) {
			List<Patient> patients = oc.getOffice().getPatients();
			if (patients != null) {
				for (Patient p : patients) {
					listPatients.addItem(p);
				}
			}
			List<Appointment> courses = oc.getOffice().getAppointments();
			if (courses != null) {
				for (Appointment a : courses) {
					if (a instanceof Course) {
						if (((Course) a).getPrevCourse() == null) {
							listCourses.addItem(a);
						}
					}
				}
			}
		}
	}

	public boolean save() {
		if (ok && (p != null) && (c != null)) {
			return oc.getAppointmentController().addPatient(p, c);
		} else {
			return false;
		}
	}
	
	public void selectCourse (Course c) {
		if (c == null)
			return;
		while (c.getPrevCourse() != null)
			c = c.getPrevCourse();
		listCourses.setSelectedValue(c, true);
	}
}
