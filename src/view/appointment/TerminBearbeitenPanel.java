/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.appointment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DateFormatter;

import model.Activity;
import model.Appointment;
import model.CalendarUtil;
import model.Course;
import model.Employee;
import model.Home;
import model.Patient;
import model.PracticalAppointment;
import model.Room;
import model.TimeInterval;
import model.Treatment;
import model.Vacation;
import model.Visit;
import view.MainFrame;
import view.patient.PatientenAktenDialog;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controller.OfficeController;

/**
 * Dialog um Termine zu bearbeiten.
 * 
 * @author Benjamin Kramer
 * 
 */
public class TerminBearbeitenPanel extends JPanel {
	private static final long serialVersionUID = 5135284330837006914L;
	private static final DateFormat dformat = DateFormat.getDateTimeInstance(
			DateFormat.MEDIUM, DateFormat.SHORT);

	private final JComboBox<String> comboRaum;
	private final JCheckBox chkSelbstzahler;
	private final JCheckBox chkHausbesuch;
	private final JSpinner spinnerZeitpuffer;
	private final JSpinner spinnerFahrzeit;
	private final JSpinner spinnerEntfernung;
	private final JLabel lblPatientName;
	private final JLabel lblBehandlungName;
	private final JLabel lblPatient;
	private final JComboBox<String> comboMitarbeiter;
	private final JFormattedTextField txtStart;
	private final JFormattedTextField txtEnd;
	private final JButton btnEdit;
	private final JButton btnDelete;
	private final JButton btnClose;
	private final JList<Patient> listPatient;

	private final OfficeController officeCtrl;
	private final MainFrame theMainFrame;
	private PracticalAppointment lastTreatment;
	private boolean course;
	private Patient patient;
	private JButton btnHinzufgen;

	/**
	 * Create the panel.
	 * 
	 * @param mainFrame
	 *            Das Hauptfenster, welches vielleicht geupdatet werden muss.
	 */
	public TerminBearbeitenPanel(MainFrame mainFrame,
			final OfficeController officeCtrl) {
		this.officeCtrl = officeCtrl;
		this.theMainFrame = mainFrame;
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("60dlu:grow"),
				ColumnSpec.decode("left:min"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));

		listPatient = new JList<Patient>();
		// listPatient.setModel(new AbstractListModel() {
		// String[] values = new String[] {"P 1", "P 2", "P 3"};
		// public int getSize() {
		// return values.length;
		// }
		// public Object getElementAt(int index) {
		// return values[index];
		// }
		// });
		listPatient.setModel(new PatientModel());
		listPatient.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (listPatient.getSelectedValue() == null) {
					return;
				}
				PatientenAktenDialog dialog = new PatientenAktenDialog(
						officeCtrl, listPatient.getSelectedValue(), false);
				dialog.setLocationRelativeTo(theMainFrame);
				dialog.setVisible(true);
				theMainFrame.updateMainFrame();
			}
		});
		listPatient.setVisible(false);
		add(listPatient, "4, 4, fill, fill");

		lblPatient = new JLabel("Patient(en):");
		lblPatient.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblPatient, "2, 4");

		lblPatientName = new JLabel();
		lblPatientName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (patient != null) {
					PatientenAktenDialog dialog = new PatientenAktenDialog(
							TerminBearbeitenPanel.this.officeCtrl, patient,
							false);
					dialog.setLocationRelativeTo(theMainFrame);
					dialog.setVisible(true);
					theMainFrame.updateMainFrame();
				}
			}
		});
		add(lblPatientName, "4, 4");
		
		btnHinzufgen = new JButton("Hinzufügen...");
		btnHinzufgen.setVisible(false);
		btnHinzufgen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (lastTreatment instanceof Course){
					KursBeitretenDialog dialog = new KursBeitretenDialog(officeCtrl, (Course) lastTreatment);
					dialog.setLocationRelativeTo(theMainFrame);
					dialog.setVisible(true);
					theMainFrame.updateMainFrame();
				}
			}
		});
		add(btnHinzufgen, "4, 6");

		JLabel lblBehandlung = new JLabel("Behandlung:");
		add(lblBehandlung, "2, 8, right, default");

		lblBehandlungName = new JLabel("Yoga");
		add(lblBehandlungName, "4, 8, 2, 1");

		JLabel lblMitarbeiter = new JLabel("Mitarbeiter:");
		add(lblMitarbeiter, "2, 10, right, default");

		comboMitarbeiter = new JComboBox<String>();
		comboMitarbeiter.setModel(new DefaultComboBoxModel<String>(
				new String[] { "Urlaub, Sabine" }));
		comboMitarbeiter.setEditable(true);
		add(comboMitarbeiter, "4, 10, fill, default");

		JLabel lblStart = new JLabel("Start:");
		add(lblStart, "2, 12, right, default");

		txtStart = new JFormattedTextField(new DateFormatter(dformat));
		txtStart.getDocument().addDocumentListener(new DocumentListener() {
			private void update(DocumentEvent e) {
				if (lastTreatment == null) {
					return;
				}

				long dur = lastTreatment.getTime().getDurationInMinutes();
				Date cur;
				try {
					cur = dformat.parse(txtStart.getText());
				} catch (ParseException e1) {
					return;
				}
				txtEnd.setText(dformat.format(CalendarUtil.addMinutes(cur,(int) dur)));

				updateCombos();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				update(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				update(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				update(e);
			}
		});
		txtStart.setText("23.05.2013 05:55");
		add(txtStart, "4, 12, fill, default");

		JLabel lblEnde = new JLabel("Ende:");
		add(lblEnde, "2, 14, right, default");

		txtEnd = new JFormattedTextField(new DateFormatter(dformat));
		txtEnd.setEditable(false);
		txtEnd.setText("23.05.2013 23:23");
		add(txtEnd, "4, 14, fill, default");

		chkSelbstzahler = new JCheckBox("Selbstzahler");
		add(chkSelbstzahler, "4, 16");

		chkHausbesuch = new JCheckBox("Hausbesuch");
		chkHausbesuch.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setHausbesuch(chkHausbesuch.isSelected());
			}
		});
		add(chkHausbesuch, "4, 18");

		JLabel lblFahrzeit = new JLabel("Fahrzeit:");
		add(lblFahrzeit, "2, 20, right, default");

		spinnerFahrzeit = new JSpinner();
		spinnerFahrzeit.setEnabled(false);
		spinnerFahrzeit.setModel(new SpinnerNumberModel(0, 0, null, 15));
		spinnerFahrzeit.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateEnd();
			}
		});
		add(spinnerFahrzeit, "4, 20");

		JLabel lblMin = new JLabel("min");
		add(lblMin, "5, 20");

		JLabel lblEntfernung = new JLabel("Entfernung:");
		add(lblEntfernung, "2, 22, right, default");

		spinnerEntfernung = new JSpinner();
		spinnerEntfernung.setEnabled(false);
		spinnerEntfernung.setModel(new SpinnerNumberModel(0, 0, null, 1));
		add(spinnerEntfernung, "4, 22");

		JLabel lblKm = new JLabel("km");
		add(lblKm, "5, 22");

		JLabel lblZeitpuffer = new JLabel("Zeitpuffer:");
		add(lblZeitpuffer, "2, 24, right, default");

		spinnerZeitpuffer = new JSpinner();
		spinnerZeitpuffer.setModel(new SpinnerNumberModel(0, 0, null, 5));
		spinnerZeitpuffer.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateEnd();
			}
		});
		add(spinnerZeitpuffer, "4, 24");

		JLabel label = new JLabel("min");
		add(label, "5, 24");

		JLabel lblRaum = new JLabel("Raum:");
		add(lblRaum, "2, 26, right, default");

		comboRaum = new JComboBox<String>();
		comboRaum.setModel(new DefaultComboBoxModel<String>(new String[] {
				"E23", "K10" }));
		comboRaum.setEditable(true);
		add(comboRaum, "4, 26, fill, default");

		btnEdit = new JButton("Speichern");
		btnEdit.setMnemonic('s');
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAppointment();
			}
		});
		add(btnEdit, "4, 28");

		btnDelete = new JButton("Löschen");
		btnDelete.setMnemonic('l');
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteAppointment();
			}
		});
		add(btnDelete, "2, 30");

		btnClose = new JButton("Abschließen");
		btnClose.setMnemonic('b');
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeAppointment();
			}
		});
		add(btnClose, "4, 30");

		setEnable(false);
	}

	private class RaumModel extends AbstractListModel<String> implements
			ComboBoxModel<String> {
		private static final long serialVersionUID = 1L;
		private final List<Room> rooms;
		private Room curRoom;

		public RaumModel(List<Room> rooms, Room room) {
			this.rooms = rooms;
			this.curRoom = room;
		}

		public Room getSelectedRoom() {
			return curRoom;
		}

		@Override
		public String getSelectedItem() {
			return curRoom != null ? curRoom.getName() : null;
		}

		@Override
		public void setSelectedItem(Object name) {
			curRoom = null;
			for (Room r : rooms) {
				if (r.getName().equals(name)) {
					curRoom = r;
				}
			}
			if (lastTreatment.getRoom().getName().equals(name)) {
				curRoom = lastTreatment.getRoom();
			}
		}

		@Override
		public String getElementAt(int index) {
			if (lastTreatment == null) {
				return null;
			}
			if (index == 0) {
				return lastTreatment.getRoom().getName();
			}
			return rooms.get(index - 1).getName();
		}

		@Override
		public int getSize() {
			return rooms.size() + 1;
		}

	}

	private class MitarbeiterModel extends AbstractListModel<String> implements
			ComboBoxModel<String> {
		private static final long serialVersionUID = 1L;
		private final List<Employee> employees;
		private Employee curEmployee;

		public MitarbeiterModel(List<Employee> employees, Employee employee) {
			this.employees = employees;
			this.curEmployee = employee;
		}

		public Employee getSelectedEmployee() {
			return curEmployee;
		}

		@Override
		public String getSelectedItem() {
			return curEmployee != null ? curEmployee.toString() : null;
		}

		@Override
		public void setSelectedItem(Object name) {
			curEmployee = null;
			for (Employee e : employees) {
				if (e.toString().equals(name)) {
					curEmployee = e;
				}
			}
			if (lastTreatment.getEmployee().toString().equals(name)) {
				curEmployee = lastTreatment.getEmployee();
			}
		}

		@Override
		public String getElementAt(int index) {
			if (lastTreatment == null) {
				return null;
			}
			if (index == 0) {
				return lastTreatment.getEmployee().toString();
			}
			return employees.get(index - 1).toString();
		}

		@Override
		public int getSize() {
			return employees.size() + 1;
		}
	}

	private class PatientModel extends AbstractListModel<Patient> {
		private static final long serialVersionUID = 1L;
		LinkedList<Patient> patienten;

		public PatientModel() {
			patienten = loadPatients();
		}

		@Override
		public int getSize() {
			if (patienten == null) {
				return 0;
			} else {
				return patienten.size();
			}
		}

		@Override
		public Patient getElementAt(int index) {
			if (patienten == null) {
				return null;
			} else {
				return patienten.get(index);
			}
		}
	}

	/**
	 * Checkt den Spinner f&uuml;r den Endzeitpunkt.
	 */
	private void updateEnd() {
		Date begin;
		try {
			begin = dformat.parse(txtStart.getText());
		} catch (ParseException e) {
			return;
		}
		int dur = lastTreatment.getActivity().getDuration();
		if (spinnerFahrzeit.getValue() instanceof Integer) {
			dur += (Integer) spinnerFahrzeit.getValue();
		}
		if (spinnerZeitpuffer.getValue() instanceof Integer) {
			dur += (Integer) spinnerZeitpuffer.getValue();
		}

		Date end = CalendarUtil.addMinutes(begin, dur);
		txtEnd.setText(dformat.format(end));
	}

	/**
	 * (De-)aktiviert alle Elemente und tr&auml;gt beim Deaktivieren
	 * Standardwerte ein.
	 * 
	 * @param enabled
	 *            true == Elemente werden aktiviert.
	 */
	private void setEnable(boolean enabled) {
		lblPatientName.setText("-");
		lblBehandlungName.setText("-");
		txtStart.setText("");
		txtEnd.setText("");
		comboMitarbeiter.setModel(new MitarbeiterModel(
				new ArrayList<Employee>(), null));
		comboRaum.setModel(new RaumModel(new ArrayList<Room>(), null));

		txtStart.setEnabled(enabled);
		txtEnd.setEnabled(enabled);
		comboMitarbeiter.setEnabled(enabled);

		chkSelbstzahler.setEnabled(enabled);
		chkHausbesuch.setEnabled(enabled);

		comboRaum.setEnabled(enabled);
		spinnerFahrzeit.setEnabled(enabled);
		spinnerEntfernung.setEnabled(enabled);
		spinnerZeitpuffer.setEnabled(enabled);

		btnEdit.setEnabled(enabled);
		btnDelete.setEnabled(enabled);
		btnClose.setEnabled(enabled);

		// Standardwerte eintragen
		if (!enabled) {
			btnHinzufgen.setVisible(false);
			listPatient.setVisible(false);
			lblPatient.setText("Patient(en):");
			patient = null;
			spinnerZeitpuffer.setValue(0);
			chkSelbstzahler.setSelected(false);
			chkHausbesuch.setSelected(false);
		}
	}

	/**
	 * Setzt einen Termin f&uuml;r das Panel der direkt angezeigt wird.
	 * 
	 * @param ap
	 *            Der Termin, welcher angezeigt werden soll.
	 */
	public void setAppointment(Appointment ap) {
		setEnable(false);
		if (ap == null) {
			return;
		}

		if (ap instanceof Course) {
			lastTreatment = (Course) ap;
			patient = null;
			course = true;
			lblPatientName.setVisible(false);
			listPatient.setVisible(true);
			listPatient.setModel(new PatientModel());
			spinnerZeitpuffer.setEnabled(false);
			btnHinzufgen.setVisible(true);
		}

		if (ap instanceof Vacation) {
			patient = null;
			lblPatient.setText("Mitarbeiter:");
			lblPatientName.setVisible(true);
			listPatient.setVisible(false);
			lblPatientName.setVisible(true);
			lblPatientName.setText(ap.getEmployee().toString());
			lblBehandlungName.setText("Urlaub");
			txtStart.setText(dformat.format(ap.getTime().getBegin()));
			txtEnd.setText(dformat.format(ap.getTime().getEnd()));
			comboMitarbeiter.setModel(new MitarbeiterModel(
					new ArrayList<Employee>(), null));
			comboRaum.setModel(new RaumModel(new ArrayList<Room>(), null));
			return;
		}
		lblPatient.setText("Patient(en):");
		PracticalAppointment t = (PracticalAppointment) ap;

		if (t.isOpen()) {
			setEnable(true);
		}

		if (ap instanceof Treatment) {
			course = false;
			this.lastTreatment = (Treatment) ap;
			Treatment tr = (Treatment) ap;
			patient = tr.getPatient();
			listPatient.setVisible(false);
			btnHinzufgen.setVisible(false);
			lblPatientName.setText("<html><font color=#0000ff><u>"
					+ patient.toString() + "</u></font></html>");
			lblPatientName.setVisible(true);
			chkSelbstzahler.setSelected(tr.isSelfPaid() || patient.isPrivat());
			chkSelbstzahler.setEnabled(t.isOpen() && !patient.isPrivat());
			spinnerZeitpuffer.setEnabled(t.isOpen());
		}

		lblBehandlungName.setText(t.getActivity().getName());

		comboMitarbeiter.setModel(new MitarbeiterModel(officeCtrl
				.getEmployeeController().getAvailableEmployees(t.getTime(),
						t.getActivity()), t.getEmployee()));

		int zeit = t.getActivity().getDuration();

		if (t instanceof Visit) {
			setHausbesuch(true);
			Visit v = (Visit) t;
			spinnerFahrzeit.setValue(v.getDriveTimeInMinutes());
			spinnerEntfernung.setValue(v.getDriveDistance() / 10);
			zeit += v.getDriveTimeInMinutes();
		} else {
			setHausbesuch(false);
			comboRaum.setEnabled(t.isOpen());
			comboRaum.setModel(new RaumModel(officeCtrl.getRoomController()
					.getAvailableRooms(t.getTime(), t.getActivity()), t
					.getRoom()));
		}
		
		if (!course) {
			spinnerZeitpuffer.setValue(t.getTime().getDurationInMinutes()
					- zeit);
		}
		
		txtStart.setText(dformat.format(ap.getTime().getBegin()));
		txtEnd.setText(dformat.format(ap.getTime().getEnd()));

		btnEdit.setEnabled(t.isOpen() && !(t instanceof Course));
		btnDelete.setEnabled(t.isOpen() && !(t instanceof Course));
		btnClose.setEnabled(t.isOpen());
	}

	/**
	 * Aktion die ausgef&uuml;rt wird, wenn der Termin bearbeitet wurde.
	 */
	private void saveAppointment() {
		if (lastTreatment == null || !(lastTreatment instanceof Treatment)) {
			return;
		}

		Treatment newAp;

		TimeInterval parsedTimes;
		try {
			parsedTimes = new TimeInterval(dformat.parse(txtStart.getText()),
					dformat.parse(txtEnd.getText()));
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "Konnte Zeiten nicht lesen!");
			return;
		}

		Employee newEmployee = ((MitarbeiterModel) comboMitarbeiter.getModel())
				.getSelectedEmployee();
		Room newRoom = ((RaumModel) comboRaum.getModel()).getSelectedRoom();

		int driveTime = ((Integer) spinnerFahrzeit.getValue()) / 15;
		int driveDistance = ((Integer) spinnerEntfernung.getValue()) * 10;

		Treatment t = (Treatment) lastTreatment;
		if (chkHausbesuch.isSelected()) {
			List<Activity> allActs = officeCtrl.getOffice().getActivities();
			newAp = new Visit(parsedTimes, newEmployee, new Home(allActs), t
					.getActivity(), t.getPatient(), chkSelbstzahler
					.isSelected(), driveTime, driveDistance);
		} else {
			newAp = new Treatment(parsedTimes, newEmployee, newRoom, t
					.getActivity(), t.getPatient(), chkSelbstzahler
					.isSelected());
		}

		if (!officeCtrl.getAppointmentController().editTreatment(
				(Treatment) lastTreatment, newAp)) {
			if (!lastTreatment.isOpen()) {
				JOptionPane
						.showMessageDialog(this,
								"Konnte Termin nicht speichern! Termin ist bereits geschlossen.");
			} else {
				JOptionPane
						.showMessageDialog(this,
								"Konnte Termin nicht speichern! Überprüfen Sie Ihre Eingaben.");
			}
		} else {
			setAppointment(newAp);
			theMainFrame.updateMainFrame();
		}
	}

	/**
	 * Passt die anderen Felder (Raum, Fahrzeit, Entfernung) an, wenn die
	 * Checkbox f&uuml;r Hausbesuche (de-)aktivert wird.
	 * 
	 * @param isHausbesuch
	 *            Ist es ein Hausbesuch?
	 */
	private void setHausbesuch(boolean isHausbesuch) {
		chkHausbesuch.setSelected(isHausbesuch);
		comboRaum.setEnabled(!isHausbesuch);
		spinnerFahrzeit.setEnabled(isHausbesuch);
		spinnerEntfernung.setEnabled(isHausbesuch);

		if (!isHausbesuch) {
			spinnerFahrzeit.setValue(0);
			spinnerEntfernung.setValue(0);
			if (lastTreatment != null) {
				Room r = (comboRaum.getModel() instanceof RaumModel) ? ((RaumModel) comboRaum
						.getModel()).getSelectedRoom()
						: null;
				comboRaum.setModel(new RaumModel(officeCtrl.getRoomController()
						.getAvailableRooms(lastTreatment.getTime(),
								lastTreatment.getActivity()), r));
			}
		}
	}

	private void updateCombos() {
		TimeInterval parsedTimes;
		try {
			parsedTimes = new TimeInterval(dformat.parse(txtStart.getText()),
					dformat.parse(txtEnd.getText()));
		} catch (ParseException e) {
			return;
		}
		comboMitarbeiter.setModel(new MitarbeiterModel(officeCtrl
				.getEmployeeController().getAvailableEmployees(parsedTimes,
						lastTreatment.getActivity()), lastTreatment
				.getEmployee()));

		if (!(lastTreatment instanceof Visit)) {
			comboRaum.setModel(new RaumModel(
					officeCtrl.getRoomController().getAvailableRooms(
							parsedTimes, lastTreatment.getActivity()),
					lastTreatment.getRoom()));
		}
	}

	/**
	 * Aktion die ausgef&uuml;hrt wird, wenn ein Termin gel&ouml;scht wird.
	 */
	private void deleteAppointment() {
		if (lastTreatment == null || !(lastTreatment instanceof Treatment)) {
			return;
		}

		if (JOptionPane.showConfirmDialog(this, "Termin wirklich löschen?",
				"Termin", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			if (!officeCtrl.getAppointmentController().removeTreatment(
					(Treatment) lastTreatment)) {
				JOptionPane.showMessageDialog(this,
						"Konnte Termin nicht löschen!");
			} else {
				this.lastTreatment = null;
				setEnable(false);
				theMainFrame.updateMainFrame();
			}
		}
	}

	/**
	 * Aktion die ausgef&uuml;hrt wird, wenn ein Termin abgeschlossen wird.
	 */
	private void closeAppointment() {
		if (lastTreatment == null) {
			return;
		}

		if (JOptionPane.showConfirmDialog(this, "Termin wirklich schließen?",
				"Termin", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			if (!officeCtrl.getAppointmentController().close(lastTreatment)) {
				JOptionPane.showMessageDialog(this,
						"Konnte Termin nicht schließen!");
			} else {
				theMainFrame.updateMainFrame();
			}

			setAppointment(lastTreatment);
		}
	}

	/**
	 * Gibt die Liste der Patienten zur&uuml;ck die einen Kurs besuchen.
	 * 
	 * @return Die Liste der Patienten zum Kurs oder null, wenn es kein Kurs
	 *         ist.
	 */
	private LinkedList<Patient> loadPatients() {
		if (!course) {
			return null;
		}
		return (LinkedList<Patient>) ((Course) lastTreatment).getPatients();
	}

}
