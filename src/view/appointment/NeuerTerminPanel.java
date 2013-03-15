/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.appointment;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Activity;
import model.Appointment;
import model.CalendarUtil;
import model.Employee;
import model.Office;
import model.Patient;
import model.Treatment;
import model.Visit;
import view.util.FilteredJList;
import controller.OfficeController;

/**
 * 
 * @author Stefan Noll
 * 
 */

@SuppressWarnings("unchecked")
public class NeuerTerminPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final OfficeController oc;
	private Office o;

	private final JTextField textFieldPatienten;
	private final JTextField textFieldBehandlungen;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final JCheckBox chckbxHausbesuch;
	private final JSpinner spinnerFahrzeit;
	private final JSpinner spinnerStart;
	private final JSpinner spinnerEnde;
	private final JSpinner spinnerEntfernung;
	private final JCheckBox chckbxSelbstzahler;
	private final JRadioButton rdbtnAlleMitarbeiter;
	private final JSpinner spinnerZeitpuffer;
	private final JLabel lblPatient;
	private final JLabel lblBehandlung;
	private final FilteredJList listPatienten;
	private final FilteredJList listBehandlungen;
	private final JRadioButton rdbtnLiebling;
	private final JButton btnVorschlag;
	private final JList<Appointment> listVorschlaege;
	private DefaultListModel<Appointment> modelVorschlaege;
	private final JList<Appointment> listTermine;
	private DefaultListModel<Appointment> modelTermine;
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
	@SuppressWarnings("deprecation")
	public NeuerTerminPanel(OfficeController officeCtrl_) {
		oc = officeCtrl_;
		if (oc != null) {
			o = oc.getOffice();
		}

		Date dateNow = new Date();
		Date dateNowLater = new Date();
		if (dateNow.getMinutes() != 0) {
			dateNow.setMinutes(dateNow.getMinutes() - 1);
		} else {
			dateNowLater.setMinutes(dateNow.getMinutes() + 1);
		}
		Date dateNowLaterLater = CalendarUtil.increaseDay(dateNowLater, 1);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 80, 0, 0,
				0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0,
				0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 1.0, 0.0, 1.0 };
		setLayout(gridBagLayout);

		lblPatient = new JLabel("Patient");
		lblPatient.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblPatient = new GridBagConstraints();
		gbc_lblPatient.fill = GridBagConstraints.BOTH;
		gbc_lblPatient.insets = new Insets(0, 0, 5, 5);
		gbc_lblPatient.gridwidth = 5;
		gbc_lblPatient.gridx = 0;
		gbc_lblPatient.gridy = 0;
		add(lblPatient, gbc_lblPatient);

		lblBehandlung = new JLabel("Behandlung");
		lblBehandlung.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblBehandlung = new GridBagConstraints();
		gbc_lblBehandlung.fill = GridBagConstraints.BOTH;
		gbc_lblBehandlung.insets = new Insets(0, 0, 5, 0);
		gbc_lblBehandlung.gridwidth = 5;
		gbc_lblBehandlung.gridx = 5;
		gbc_lblBehandlung.gridy = 0;
		add(lblBehandlung, gbc_lblBehandlung);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);

		listPatienten = new FilteredJList();
		listPatienten.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPatienten.setCellRenderer(new DefaultListCellRenderer() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				Patient p = (Patient) value;
				setText(p.toString());
				return this;
			}
		});
		scrollPane.setViewportView(listPatienten);
		listPatienten.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				int index = listPatienten.getSelectedIndex();
				if (index >= 0) {
					Patient p = (Patient) listPatienten.getModel()
							.getElementAt(index);
					spinnerEntfernung.setValue(p.getDistance());
					if (p.getPreferredEmployee() != null) {
						rdbtnLiebling.setText(p.getPreferredEmployee()
								.toString());
						rdbtnLiebling.setEnabled(true);
					} else {
						rdbtnLiebling.setText("");
						rdbtnLiebling.setEnabled(false);
					}
				}
			}
		});

		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridheight = 2;
		gbc_scrollPane_1.gridwidth = 5;
		gbc_scrollPane_1.gridx = 5;
		gbc_scrollPane_1.gridy = 1;
		add(scrollPane_1, gbc_scrollPane_1);

		listBehandlungen = new FilteredJList();
		listBehandlungen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listBehandlungen.setCellRenderer(new DefaultListCellRenderer() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				Activity a = (Activity) value;
				setText(a.getName());
				return this;
			}
		});
		scrollPane_1.setViewportView(listBehandlungen);
		textFieldPatienten = listPatienten.getFilterField();
		GridBagConstraints gbc_textFieldPatienten = new GridBagConstraints();
		gbc_textFieldPatienten.gridwidth = 5;
		gbc_textFieldPatienten.anchor = GridBagConstraints.NORTH;
		gbc_textFieldPatienten.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPatienten.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPatienten.gridx = 0;
		gbc_textFieldPatienten.gridy = 3;
		add(textFieldPatienten, gbc_textFieldPatienten);
		textFieldPatienten.setColumns(10);

		textFieldBehandlungen = listBehandlungen.getFilterField();
		GridBagConstraints gbc_textFieldBehandlungen = new GridBagConstraints();
		gbc_textFieldBehandlungen.anchor = GridBagConstraints.NORTH;
		gbc_textFieldBehandlungen.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldBehandlungen.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldBehandlungen.gridwidth = 5;
		gbc_textFieldBehandlungen.gridx = 5;
		gbc_textFieldBehandlungen.gridy = 3;
		add(textFieldBehandlungen, gbc_textFieldBehandlungen);
		textFieldBehandlungen.setColumns(10);

		chckbxSelbstzahler = new JCheckBox("Selbstzahler");
		GridBagConstraints gbc_chckbxSelbstzahler = new GridBagConstraints();
		gbc_chckbxSelbstzahler.gridwidth = 4;
		gbc_chckbxSelbstzahler.fill = GridBagConstraints.BOTH;
		gbc_chckbxSelbstzahler.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxSelbstzahler.gridx = 0;
		gbc_chckbxSelbstzahler.gridy = 4;
		add(chckbxSelbstzahler, gbc_chckbxSelbstzahler);

		rdbtnAlleMitarbeiter = new JRadioButton("Alle Mitarbeiter");
		buttonGroup.add(rdbtnAlleMitarbeiter);
		rdbtnAlleMitarbeiter.setSelected(true);
		GridBagConstraints gbc_rdbtnAlleMitarbeiter = new GridBagConstraints();
		gbc_rdbtnAlleMitarbeiter.anchor = GridBagConstraints.WEST;
		gbc_rdbtnAlleMitarbeiter.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnAlleMitarbeiter.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnAlleMitarbeiter.gridx = 5;
		gbc_rdbtnAlleMitarbeiter.gridy = 4;
		add(rdbtnAlleMitarbeiter, gbc_rdbtnAlleMitarbeiter);

		rdbtnLiebling = new JRadioButton("");
		rdbtnLiebling.setEnabled(false);
		buttonGroup.add(rdbtnLiebling);
		GridBagConstraints gbc_rdbtnLiebling = new GridBagConstraints();
		gbc_rdbtnLiebling.gridwidth = 3;
		gbc_rdbtnLiebling.anchor = GridBagConstraints.WEST;
		gbc_rdbtnLiebling.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnLiebling.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnLiebling.gridx = 6;
		gbc_rdbtnLiebling.gridy = 4;
		add(rdbtnLiebling, gbc_rdbtnLiebling);

		JLabel lblTerminauswahl = new JLabel("Terminauswahl");
		lblTerminauswahl.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblTerminauswahl = new GridBagConstraints();
		gbc_lblTerminauswahl.anchor = GridBagConstraints.SOUTH;
		gbc_lblTerminauswahl.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTerminauswahl.insets = new Insets(0, 0, 5, 5);
		gbc_lblTerminauswahl.gridwidth = 5;
		gbc_lblTerminauswahl.gridx = 0;
		gbc_lblTerminauswahl.gridy = 5;
		add(lblTerminauswahl, gbc_lblTerminauswahl);

		btnVorschlag = new JButton("Vorschlaege");
		btnVorschlag.setMnemonic('v');
		btnVorschlag.setVerticalAlignment(SwingConstants.TOP);
		btnVorschlag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				modelVorschlaege.clear();

				Date dateStart = (Date) spinnerStart.getModel().getValue();
				Date dateEnde = (Date) spinnerEnde.getModel().getValue();
				@SuppressWarnings("unused")
				boolean selbstzahler = chckbxSelbstzahler.isSelected();
				boolean alleMitarbeiter = rdbtnAlleMitarbeiter.isSelected();
				boolean hausbesuch = chckbxHausbesuch.isSelected();
				int fahrtzeit = ((Integer) spinnerFahrzeit.getValue())
						.intValue();
				int entfernung = ((Integer) spinnerEntfernung.getValue())
						.intValue();
				int zeitpuffer = ((Integer) spinnerZeitpuffer.getValue())
						.intValue();
				Patient patient = null;
				int patientAt = listPatienten.getSelectedIndex();
				if (patientAt >= 0) {
					patient = (Patient) listPatienten.getModel().getElementAt(
							patientAt);
				} else {
					return;
				}
				Activity behandlung = null;
				int behandlungAt = listBehandlungen.getSelectedIndex();
				if (behandlungAt >= 0) {
					behandlung = (Activity) listBehandlungen.getModel()
							.getElementAt(behandlungAt);
				} else {
					return;
				}

				int length = behandlung.getDuration() + zeitpuffer;
				if (hausbesuch) {
					length += fahrtzeit;
				}
				Employee mitarbeiter = null;
				if (!alleMitarbeiter) {
					mitarbeiter = patient.getPreferredEmployee();
				}

				// System.out.println("Patient: " + patient);
				// System.out.println("Behandlung: " + behandlung);
				// System.out.println("Termin von: " + dateStart + " bis "
				// + dateEnde);
				// System.out.println("selbstzahler: " + selbstzahler);
				// System.out.println("alleMitarbeiter: " + alleMitarbeiter);
				// System.out.println("Fahrtzeit: " + fahrtzeit);
				// System.out.println("Entfernung: " + entfernung);
				// System.out.println("length: " + length);

				// System.out.println("Berechne Vorschlaege...");
				Appointment[] array = new Appointment[modelTermine.getSize()];
				modelTermine.copyInto(array);
				List <Treatment> vorgemerkt = new LinkedList<Treatment>();
				for (int i = 0; i< listTermine.getModel().getSize(); i++)
					vorgemerkt.add((Treatment) listTermine.getModel().getElementAt(i));
				List<Treatment> vorschlaege = oc.getAppointmentController()
						.proposeTreatments(dateStart, dateEnde, length,
								behandlung, patient, mitarbeiter, hausbesuch, vorgemerkt);
				
//				System.out.println("Vorschlaege fertig berechnet.");


				// if (vorschlaege == null) {
				// System.out.println("Anzahl Vorschlaege: " + 0);
				// } else {
				// System.out.println("Anzahl Vorschlaege: "
				// + vorschlaege.size());
				// }

				if (vorschlaege != null) {
					for (Treatment t : vorschlaege) {
						if (hausbesuch) {
							((Visit) t).setDriveDistance(entfernung * 10);
							((Visit) t).setDriveTime(fahrtzeit / 15);
						}
						modelVorschlaege.addElement(t);
					}
				}
			}
		});

		chckbxHausbesuch = new JCheckBox("Hausbesuch");
		chckbxHausbesuch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxHausbesuch.isSelected()) {
					spinnerFahrzeit.setEnabled(true);
					spinnerEntfernung.setEnabled(true);
				} else {
					spinnerFahrzeit.setEnabled(false);
					spinnerEntfernung.setEnabled(false);
				}
			}
		});
		GridBagConstraints gbc_chckbxHausbesuch = new GridBagConstraints();
		gbc_chckbxHausbesuch.anchor = GridBagConstraints.WEST;
		gbc_chckbxHausbesuch.fill = GridBagConstraints.VERTICAL;
		gbc_chckbxHausbesuch.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxHausbesuch.gridx = 5;
		gbc_chckbxHausbesuch.gridy = 5;
		add(chckbxHausbesuch, gbc_chckbxHausbesuch);

		JLabel lblFahrtzeit = new JLabel("Fahrtzeit");
		GridBagConstraints gbc_lblFahrtzeit = new GridBagConstraints();
		gbc_lblFahrtzeit.anchor = GridBagConstraints.WEST;
		gbc_lblFahrtzeit.insets = new Insets(0, 0, 5, 5);
		gbc_lblFahrtzeit.gridx = 6;
		gbc_lblFahrtzeit.gridy = 5;
		add(lblFahrtzeit, gbc_lblFahrtzeit);

		spinnerFahrzeit = new JSpinner();
		spinnerFahrzeit.setEditor(new JSpinner.DefaultEditor(spinnerFahrzeit));
		spinnerFahrzeit.setEnabled(false);
		spinnerFahrzeit.setModel(new SpinnerNumberModel(new Integer(0),
				new Integer(0), null, new Integer(15)));
		GridBagConstraints gbc_spinnerFahrzeit = new GridBagConstraints();
		gbc_spinnerFahrzeit.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerFahrzeit.anchor = GridBagConstraints.NORTH;
		gbc_spinnerFahrzeit.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerFahrzeit.gridx = 7;
		gbc_spinnerFahrzeit.gridy = 5;
		add(spinnerFahrzeit, gbc_spinnerFahrzeit);

		JLabel lblMin1 = new JLabel("min");
		GridBagConstraints gbc_lblMin1 = new GridBagConstraints();
		gbc_lblMin1.anchor = GridBagConstraints.WEST;
		gbc_lblMin1.insets = new Insets(0, 0, 5, 5);
		gbc_lblMin1.gridx = 8;
		gbc_lblMin1.gridy = 5;
		add(lblMin1, gbc_lblMin1);

		JLabel lblStart = new JLabel("Start:");
		GridBagConstraints gbc_lblStart = new GridBagConstraints();
		gbc_lblStart.anchor = GridBagConstraints.EAST;
		gbc_lblStart.insets = new Insets(0, 0, 5, 5);
		gbc_lblStart.gridx = 0;
		gbc_lblStart.gridy = 6;
		add(lblStart, gbc_lblStart);

		spinnerStart = new JSpinner();
		spinnerStart.setModel(new SpinnerDateModel(dateNowLater, dateNow, null,
				Calendar.MINUTE));
		spinnerStart.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Date d = (Date) spinnerStart.getModel().getValue();
				spinnerEnde.setModel(new SpinnerDateModel(d, d, null,
						Calendar.DAY_OF_MONTH));
			}
		});
		GridBagConstraints gbc_spinnerStart = new GridBagConstraints();
		gbc_spinnerStart.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerStart.gridwidth = 3;
		gbc_spinnerStart.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerStart.gridx = 1;
		gbc_spinnerStart.gridy = 6;
		add(spinnerStart, gbc_spinnerStart);

		JLabel lblEntfernung = new JLabel("Entfernung");
		GridBagConstraints gbc_lblEntfernung = new GridBagConstraints();
		gbc_lblEntfernung.insets = new Insets(0, 0, 5, 5);
		gbc_lblEntfernung.gridx = 6;
		gbc_lblEntfernung.gridy = 6;
		add(lblEntfernung, gbc_lblEntfernung);

		spinnerEntfernung = new JSpinner();
		spinnerEntfernung.setEnabled(false);
		spinnerEntfernung.setModel(new SpinnerNumberModel(new Integer(0),
				new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_spinnerEntfernung = new GridBagConstraints();
		gbc_spinnerEntfernung.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerEntfernung.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerEntfernung.gridx = 7;
		gbc_spinnerEntfernung.gridy = 6;
		add(spinnerEntfernung, gbc_spinnerEntfernung);
		GridBagConstraints gbc_btnVorschlag = new GridBagConstraints();
		gbc_btnVorschlag.gridheight = 2;
		gbc_btnVorschlag.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnVorschlag.gridwidth = 2;
		gbc_btnVorschlag.insets = new Insets(0, 0, 5, 5);
		gbc_btnVorschlag.gridx = 4;
		gbc_btnVorschlag.gridy = 6;
		add(btnVorschlag, gbc_btnVorschlag);

		JLabel lblKm = new JLabel("km");
		GridBagConstraints gbc_lblKm = new GridBagConstraints();
		gbc_lblKm.anchor = GridBagConstraints.WEST;
		gbc_lblKm.insets = new Insets(0, 0, 5, 5);
		gbc_lblKm.gridx = 8;
		gbc_lblKm.gridy = 6;
		add(lblKm, gbc_lblKm);

		JLabel lblEnde = new JLabel("Ende:");
		GridBagConstraints gbc_lblEnde = new GridBagConstraints();
		gbc_lblEnde.anchor = GridBagConstraints.EAST;
		gbc_lblEnde.insets = new Insets(0, 0, 5, 5);
		gbc_lblEnde.gridx = 0;
		gbc_lblEnde.gridy = 7;
		add(lblEnde, gbc_lblEnde);

		spinnerEnde = new JSpinner();
		spinnerEnde.setModel(new SpinnerDateModel(dateNowLaterLater, dateNow,
				null, Calendar.DAY_OF_MONTH));
		GridBagConstraints gbc_spinnerEnde = new GridBagConstraints();
		gbc_spinnerEnde.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerEnde.gridwidth = 3;
		gbc_spinnerEnde.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerEnde.gridx = 1;
		gbc_spinnerEnde.gridy = 7;
		add(spinnerEnde, gbc_spinnerEnde);

		JLabel lblZeitpuffer = new JLabel("Zeitpuffer");
		GridBagConstraints gbc_lblZeitpuffer = new GridBagConstraints();
		gbc_lblZeitpuffer.anchor = GridBagConstraints.EAST;
		gbc_lblZeitpuffer.insets = new Insets(0, 0, 5, 5);
		gbc_lblZeitpuffer.gridx = 6;
		gbc_lblZeitpuffer.gridy = 7;
		add(lblZeitpuffer, gbc_lblZeitpuffer);

		spinnerZeitpuffer = new JSpinner();
		spinnerZeitpuffer.setEditor(new JSpinner.DefaultEditor(
				spinnerZeitpuffer));
		spinnerZeitpuffer.setModel(new SpinnerNumberModel(new Integer(0),
				new Integer(0), null, new Integer(5)));
		GridBagConstraints gbc_spinnerZeitpuffer = new GridBagConstraints();
		gbc_spinnerZeitpuffer.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerZeitpuffer.anchor = GridBagConstraints.NORTH;
		gbc_spinnerZeitpuffer.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerZeitpuffer.gridx = 7;
		gbc_spinnerZeitpuffer.gridy = 7;
		add(spinnerZeitpuffer, gbc_spinnerZeitpuffer);

		JLabel lblMin2 = new JLabel("min");
		GridBagConstraints gbc_lblMin2 = new GridBagConstraints();
		gbc_lblMin2.anchor = GridBagConstraints.WEST;
		gbc_lblMin2.insets = new Insets(0, 0, 5, 5);
		gbc_lblMin2.gridx = 8;
		gbc_lblMin2.gridy = 7;
		add(lblMin2, gbc_lblMin2);

		JScrollPane scrollPane_2 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_2 = new GridBagConstraints();
		gbc_scrollPane_2.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_2.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_2.gridwidth = 10;
		gbc_scrollPane_2.gridx = 0;
		gbc_scrollPane_2.gridy = 8;
		add(scrollPane_2, gbc_scrollPane_2);

		listVorschlaege = new JList<Appointment>();
		listVorschlaege.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_2.setViewportView(listVorschlaege);
		listVorschlaege.setCellRenderer(new DefaultListCellRenderer() {
			/** */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				Treatment t = (Treatment) value;
				setText(CalendarUtil.dateTimeToString(t.getTime().getBegin())
						+ " " + t.getEmployee().getFirstName() + ","
						+ t.getEmployee().getLastName() + " "
						+ t.getRoom().getName());
				return this;
			}
		});

		JButton btnHinzu = new JButton("Hinzufuegen");
		btnHinzu.setMnemonic('h');
		btnHinzu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = listVorschlaege.getSelectedIndex();
				if (index >= 0) {
					modelTermine.addElement(listVorschlaege.getSelectedValue());
					modelVorschlaege.clear();

					// Speichern aktivieren
					if (modelTermine.size() > 0) {
						okButton.setEnabled(true);
					}
				}
			}
		});
		GridBagConstraints gbc_btnHinzu = new GridBagConstraints();
		gbc_btnHinzu.gridwidth = 2;
		gbc_btnHinzu.anchor = GridBagConstraints.SOUTH;
		gbc_btnHinzu.insets = new Insets(0, 0, 5, 5);
		gbc_btnHinzu.gridx = 0;
		gbc_btnHinzu.gridy = 9;
		add(btnHinzu, gbc_btnHinzu);

		JButton btnEntfernen = new JButton("Entfernen");
		btnEntfernen.setMnemonic('e');
		btnEntfernen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = listTermine.getSelectedIndex();
				if (index >= 0) {
					modelTermine.remove(index);
				}

				// Speichern blockieren
				if (modelTermine.size() < 1) {
					okButton.setEnabled(false);
				}
			}
		});
		GridBagConstraints gbc_btnEntfernen = new GridBagConstraints();
		gbc_btnEntfernen.gridwidth = 2;
		gbc_btnEntfernen.insets = new Insets(0, 0, 5, 5);
		gbc_btnEntfernen.gridx = 2;
		gbc_btnEntfernen.gridy = 9;
		add(btnEntfernen, gbc_btnEntfernen);

		JLabel lblMerkliste = new JLabel("Liste bisher vorgemerkter Termine");
		lblMerkliste.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblMerkliste = new GridBagConstraints();
		gbc_lblMerkliste.gridwidth = 3;
		gbc_lblMerkliste.anchor = GridBagConstraints.SOUTH;
		gbc_lblMerkliste.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblMerkliste.insets = new Insets(0, 0, 5, 5);
		gbc_lblMerkliste.gridx = 5;
		gbc_lblMerkliste.gridy = 9;
		add(lblMerkliste, gbc_lblMerkliste);

		JScrollPane scrollPane_3 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_3 = new GridBagConstraints();
		gbc_scrollPane_3.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_3.gridwidth = 10;
		gbc_scrollPane_3.gridx = 0;
		gbc_scrollPane_3.gridy = 10;
		add(scrollPane_3, gbc_scrollPane_3);

		listTermine = new JList<Appointment>();
		listTermine.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_3.setViewportView(listTermine);
		listTermine.setCellRenderer(new DefaultListCellRenderer() {
			/**  */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				Treatment t = (Treatment) value;
				setText(CalendarUtil.dateTimeToString(t.getTime().getBegin())
						+ " " + t.getEmployee().getFirstName() + ","
						+ t.getEmployee().getLastName() + " "
						+ t.getRoom().getName());
				return this;
			}
		});

		init();
	}

	private void init() {
		if (oc != null && oc.getOffice() != null) {

			List<Patient> patients = o.getPatients();
			if (patients != null) {
				for (Patient p : patients) {
					listPatienten.addItem(p);
				}
			}

			List<Activity> activities = o.getActivities();
			if (activities != null) {
				for (Activity a : activities) {
					if (a.getNumberOfSessions() == 1) {
						listBehandlungen.addItem(a);
					}
				}
			}
		}

		modelVorschlaege = new DefaultListModel<Appointment>();
		listVorschlaege.setModel(modelVorschlaege);

		modelTermine = new DefaultListModel<Appointment>();
		listTermine.setModel(modelTermine);
	}

	public boolean save() {

		if (modelTermine.size() < 1 || oc == null
				|| oc.getAppointmentController() == null) {
			return false;
		}

		for (int i = 0; i < modelTermine.size(); ++i) {
			Treatment t = (Treatment) modelTermine.get(i);
			oc.getAppointmentController().saveTreatment(t);
		}

		return true;
	}
}
