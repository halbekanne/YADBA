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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Activity;
import model.Course;
import model.Employee;
import view.util.FilteredJList;
import controller.OfficeController;
import javax.swing.SpinnerNumberModel;

/**
 * 
 * @author Stefan Noll
 * 
 */
public class NeuerKursPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OfficeController oc;
	private JTextField textFieldCourses;
	private JTextField textFieldEmployees;
	@SuppressWarnings("unused")
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JSpinner spinner2;
	private JSpinner spinner1;
	private final JSpinner spinnerMinkapa;
	@SuppressWarnings("unused")
	private JTextField textFieldRooms;
	private FilteredJList listCourses;
	private FilteredJList listEmployees;
	private JLabel lblstatus;
	private Course c;
	private boolean ok = false;
	private JButton okButton;

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
	@SuppressWarnings({ "deprecation", "unchecked" })
	public NeuerKursPanel(OfficeController oc_) {
		oc = oc_;
		Date dateNow = new Date();
		Date dateNowLater = new Date();
		if (dateNow.getMinutes() != 0) {
			dateNow.setMinutes(dateNow.getMinutes() - 1);
		} else {
			dateNowLater.setMinutes(dateNow.getMinutes() + 1);
		}
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 16, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 1.0,
				1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0,
				0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblKurs = new JLabel("Kurs");
		lblKurs.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblKurs = new GridBagConstraints();
		gbc_lblKurs.anchor = GridBagConstraints.NORTH;
		gbc_lblKurs.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblKurs.insets = new Insets(0, 0, 5, 5);
		gbc_lblKurs.gridwidth = 3;
		gbc_lblKurs.gridx = 0;
		gbc_lblKurs.gridy = 0;
		add(lblKurs, gbc_lblKurs);

		JLabel lblNewLabel = new JLabel("Mitarbeiter");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridwidth = 3;
		gbc_lblNewLabel.gridx = 3;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridheight = 3;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);

		listCourses = new FilteredJList();
		scrollPane.setViewportView(listCourses);
		listCourses.setCellRenderer(new DefaultListCellRenderer() {

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
				Activity a = (Activity) value;
				setText(a.toString() + " ("+a.getNumberOfSessions()+" Termine)");
				return this;
			}
		});
		listCourses.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				int index = listCourses.getSelectedIndex();
				if (index >= 0) {
					Activity a = (Activity) listCourses.getModel()
							.getElementAt(index);
					if (a != null && oc != null && oc.getOffice() != null
							&& oc.getOffice().getEmployees() != null) {
						List<Employee> employees = oc.getOffice()
								.getEmployees();
						listEmployees.clear();
						for (Employee e : employees) {
							if (e.isAble(a)) {
								listEmployees.addItem(e);
							}
						}
					}
				}
			}
		});

		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.gridheight = 3;
		gbc_scrollPane_1.gridwidth = 3;
		gbc_scrollPane_1.gridx = 3;
		gbc_scrollPane_1.gridy = 1;
		add(scrollPane_1, gbc_scrollPane_1);

		listEmployees = new FilteredJList();
		scrollPane_1.setViewportView(listEmployees);
		listEmployees.setCellRenderer(new DefaultListCellRenderer() {

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
				Employee e = (Employee) value;
				setText(e.toString());
				return this;
			}
		});

		textFieldCourses = listCourses.getFilterField();
		GridBagConstraints gbc_textFieldCourses = new GridBagConstraints();
		gbc_textFieldCourses.anchor = GridBagConstraints.NORTH;
		gbc_textFieldCourses.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldCourses.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldCourses.gridwidth = 3;
		gbc_textFieldCourses.gridx = 0;
		gbc_textFieldCourses.gridy = 4;
		add(textFieldCourses, gbc_textFieldCourses);
		textFieldCourses.setColumns(10);

		textFieldEmployees = listEmployees.getFilterField();
		GridBagConstraints gbc_textFieldEmployees = new GridBagConstraints();
		gbc_textFieldEmployees.gridwidth = 3;
		gbc_textFieldEmployees.anchor = GridBagConstraints.NORTH;
		gbc_textFieldEmployees.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldEmployees.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldEmployees.gridx = 3;
		gbc_textFieldEmployees.gridy = 4;
		add(textFieldEmployees, gbc_textFieldEmployees);
		textFieldEmployees.setColumns(10);

		JRadioButton rdbtnEinmalProWoche = new JRadioButton("einmal pro Woche");
		rdbtnEinmalProWoche.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				spinner2.setEnabled(false);
			}
		});
		rdbtnEinmalProWoche.setSelected(true);
		buttonGroup_1.add(rdbtnEinmalProWoche);
		GridBagConstraints gbc_rdbtnEinmalProWoche = new GridBagConstraints();
		gbc_rdbtnEinmalProWoche.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnEinmalProWoche.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnEinmalProWoche.gridx = 0;
		gbc_rdbtnEinmalProWoche.gridy = 5;
		add(rdbtnEinmalProWoche, gbc_rdbtnEinmalProWoche);

		JLabel lbl1Termin = new JLabel("1. Termin:");
		GridBagConstraints gbc_lbl1Termin = new GridBagConstraints();
		gbc_lbl1Termin.anchor = GridBagConstraints.WEST;
		gbc_lbl1Termin.insets = new Insets(0, 0, 5, 5);
		gbc_lbl1Termin.gridx = 1;
		gbc_lbl1Termin.gridy = 5;
		add(lbl1Termin, gbc_lbl1Termin);

		spinner1 = new JSpinner();
		spinner1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Date d = (Date) spinner1.getModel().getValue();
				spinner2.setModel(new SpinnerDateModel(d, d, null,
						Calendar.DAY_OF_MONTH));
			}
		});
		spinner1.setModel(new SpinnerDateModel(dateNowLater,dateNow,null,
				Calendar.DAY_OF_YEAR));
		GridBagConstraints gbc_spinner1 = new GridBagConstraints();
		gbc_spinner1.anchor = GridBagConstraints.WEST;
		gbc_spinner1.insets = new Insets(0, 0, 5, 5);
		gbc_spinner1.gridwidth = 2;
		gbc_spinner1.gridx = 2;
		gbc_spinner1.gridy = 5;
		add(spinner1, gbc_spinner1);

		JRadioButton rdbtnZweimalProWoche = new JRadioButton(
				"zweimal pro Woche");
		rdbtnZweimalProWoche.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				spinner2.setEnabled(true);
				Date d = (Date) spinner1.getModel().getValue();
				spinner2.setModel(new SpinnerDateModel(d, d, null,
						Calendar.DAY_OF_MONTH));
			}
		});
		buttonGroup_1.add(rdbtnZweimalProWoche);
		GridBagConstraints gbc_rdbtnZweimalProWoche = new GridBagConstraints();
		gbc_rdbtnZweimalProWoche.fill = GridBagConstraints.HORIZONTAL;
		gbc_rdbtnZweimalProWoche.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnZweimalProWoche.gridx = 0;
		gbc_rdbtnZweimalProWoche.gridy = 6;
		add(rdbtnZweimalProWoche, gbc_rdbtnZweimalProWoche);

		JLabel lbl2Termin = new JLabel("2. Termin:");
		GridBagConstraints gbc_lbl2Termin = new GridBagConstraints();
		gbc_lbl2Termin.anchor = GridBagConstraints.WEST;
		gbc_lbl2Termin.insets = new Insets(0, 0, 5, 5);
		gbc_lbl2Termin.gridx = 1;
		gbc_lbl2Termin.gridy = 6;
		add(lbl2Termin, gbc_lbl2Termin);

		spinner2 = new JSpinner();
		spinner2.setEnabled(false);
		spinner2.setModel(new SpinnerDateModel(dateNowLater,dateNow,null,
				Calendar.DAY_OF_YEAR));
		GridBagConstraints gbc_spinner2 = new GridBagConstraints();
		gbc_spinner2.anchor = GridBagConstraints.WEST;
		gbc_spinner2.insets = new Insets(0, 0, 5, 5);
		gbc_spinner2.gridwidth = 2;
		gbc_spinner2.gridx = 2;
		gbc_spinner2.gridy = 6;
		add(spinner2, gbc_spinner2);

		JButton btnTerminePruefen = new JButton("Termine pruefen");
		btnTerminePruefen.setMnemonic('t');
		btnTerminePruefen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionevent) {

				Date first = (Date) spinner1.getValue();
				Date second = null;
				if (spinner2.isEnabled()) {
					second = (Date) spinner2.getValue();
				}
				Activity a = null;
				int index = listCourses.getSelectedIndex();
				if (index >= 0) {
					a = (Activity) listCourses.getModel().getElementAt(index);
				} else {
					lblstatus.setText("Fehler: keinen Kurs gewaehlt!");
					lblstatus.setForeground(Color.RED);
					ok = false;
					okButton.setEnabled(false);
					return;
				}
				int length = a.getDuration();
				Employee e = null;
				index = listEmployees.getSelectedIndex();
				if (index >= 0) {
					e = (Employee) listEmployees.getModel().getElementAt(index);
				} else {
					lblstatus.setText("Fehler: keinen Mitarbeiter gewaehlt!");
					lblstatus.setForeground(Color.RED);
					ok = false;
					okButton.setEnabled(false);
					return;
				}
				int minKapa = (Integer)spinnerMinkapa.getValue();
				c = oc.getAppointmentController().newCourse(first, second,
						length, e, a, minKapa);
				if (c != null) {
					lblstatus.setText("OK");
					lblstatus.setForeground(Color.GREEN);
					ok = true;
					okButton.setEnabled(true);
					return;
				} else {
					lblstatus.setText("Fehler: Kurs nicht moeglich!");
					lblstatus.setForeground(Color.RED);
					ok = false;
					okButton.setEnabled(false);
					return;
				}
			}
		});
		GridBagConstraints gbc_btnTerminePruefen = new GridBagConstraints();
		gbc_btnTerminePruefen.gridwidth = 2;
		gbc_btnTerminePruefen.anchor = GridBagConstraints.WEST;
		gbc_btnTerminePruefen.gridheight = 2;
		gbc_btnTerminePruefen.insets = new Insets(0, 0, 5, 5);
		gbc_btnTerminePruefen.gridx = 4;
		gbc_btnTerminePruefen.gridy = 5;
		add(btnTerminePruefen, gbc_btnTerminePruefen);
		
		JLabel lblMinimalkapazitt = new JLabel("Minimalkapazität:");
		GridBagConstraints gbc_lblMinimalkapazitt = new GridBagConstraints();
		gbc_lblMinimalkapazitt.insets = new Insets(0, 0, 5, 5);
		gbc_lblMinimalkapazitt.gridx = 0;
		gbc_lblMinimalkapazitt.gridy = 7;
		add(lblMinimalkapazitt, gbc_lblMinimalkapazitt);
		
		spinnerMinkapa = new JSpinner();
		spinnerMinkapa.setModel(new SpinnerNumberModel(new Integer(15), new Integer(1), null, new Integer(1)));
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner.insets = new Insets(0, 0, 5, 5);
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 7;
		add(spinnerMinkapa, gbc_spinner);

		lblstatus = new JLabel("");
		lblstatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblstatus.setForeground(Color.RED);
		GridBagConstraints gbc_lblstatus = new GridBagConstraints();
		gbc_lblstatus.insets = new Insets(0, 0, 0, 5);
		gbc_lblstatus.anchor = GridBagConstraints.NORTH;
		gbc_lblstatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblstatus.gridwidth = 6;
		gbc_lblstatus.gridx = 0;
		gbc_lblstatus.gridy = 8;
		add(lblstatus, gbc_lblstatus);

		init();
	}

	private void init() {
		if (oc != null && oc.getOffice() != null) {
			List<Activity> activities = oc.getOffice().getActivities();
			if (activities != null) {
				for (Activity a : activities) {
					if (a.getNumberOfSessions() > 1) {
						listCourses.addItem(a);
					}
				}
			}
		}

	}

	public boolean save() {
		if (ok && c != null && oc != null
				&& oc.getAppointmentController() != null) {
			// System.out.println("saveCourse...");
			Course tmp = c;
			boolean erfolg = true;
			while (tmp != null) {
				erfolg &= oc.getAppointmentController().saveCourse(tmp);
				tmp = tmp.getNextCourse();
			}
			return erfolg;
		} else {
			return false;
		}
	}
}
