/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.export;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import model.Appointment;
import model.Course;
import model.Employee;
import model.Patient;
import model.Person;
import model.PracticalAppointment;
import model.Room;
import model.TimeInterval;
import model.Treatment;
import model.Vacation;
import controller.OfficeController;

/**
 * 
 * @author Fabian König
 * 
 */

@SuppressWarnings("unchecked")
public class KalenderExportierenDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table_dates;
	private JComboBox comboBox;
	private ButtonGroup chooseType;
	private JRadioButton rdbtnPatient, rdbtnMitarbeiter, rdbtnRaum;
	private DefaultComboBoxModel<Object> comboBoxModel;
	private JSpinner DateStart;
	private JSpinner DateEnd;
	private OfficeController officeCtrl;
	private JButton btnIcalendar;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */

	public KalenderExportierenDialog(final OfficeController ofC) {
		setModal(true);
		officeCtrl = ofC;
		setTitle("Kalender exportieren");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		chooseType = new ButtonGroup();

		rdbtnMitarbeiter = new JRadioButton("Mitarbeiter");
		rdbtnMitarbeiter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changedType(officeCtrl.getOffice().getEmployees());
			}
		});
		chooseType.add(rdbtnMitarbeiter);

		rdbtnPatient = new JRadioButton("Patient");
		rdbtnPatient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changedType(officeCtrl.getOffice().getPatients());
			}
		});
		chooseType.add(rdbtnPatient);

		rdbtnRaum = new JRadioButton("Raum");
		rdbtnRaum.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changedType(officeCtrl.getOffice().getRooms());
			}
		});
		chooseType.add(rdbtnRaum);

		comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					changedContent();
				}
			}
		});
		comboBoxModel = new DefaultComboBoxModel<Object>();
		comboBox.setModel(comboBoxModel);
		comboBox.setRenderer(new DefaultListCellRenderer() {
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
				if (value instanceof Room) {
					Room r = (Room) value;
					setText(r.getName());
				} else if (value instanceof Person) {
					Person p = (Person) value;
					setText(p.getLastName() + "," + p.getFirstName());
				} else {
					setText("Keine Objekte in der Auswahl");
				}
				return this;
			}
		});

		JPanel panel_top_date_start = new JPanel();
		panel_top_date_start.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblVon = new JLabel("Von:");
		panel_top_date_start.add(lblVon);

		DateStart = new JSpinner();
		DateStart.setModel(new SpinnerDateModel(new Date(), null, null,
				Calendar.DAY_OF_YEAR));
		DateStart.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Date dStart = (Date) DateStart.getValue();
				Date dEnd = (Date) DateEnd.getValue();
				if (dStart.after(dEnd)) {
					JOptionPane.showMessageDialog(null,
							"Startzeitpunkt muss vor Endzeitpunkt liegen",
							"Error", JOptionPane.OK_OPTION);
					DateStart.setValue(DateStart.getPreviousValue());
				} else {
					changedContent();
				}

			}
		});
		panel_top_date_start.add(DateStart);

		JPanel panel_top_date_end = new JPanel();
		panel_top_date_end.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblBis = new JLabel("Bis:");
		panel_top_date_end.add(lblBis);

		DateEnd = new JSpinner();
		DateEnd.setModel(new SpinnerDateModel(new Date(), null, null,
				Calendar.DAY_OF_YEAR));
		DateEnd.setValue(DateEnd.getNextValue());
		DateEnd.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Date dStart = (Date) DateStart.getValue();
				Date dEnd = (Date) DateEnd.getValue();
				if (dEnd.before(dStart)) {
					JOptionPane.showMessageDialog(null,
							"Endzeitpunkt muss nach Startzeitpunkt liegen",
							"Error", JOptionPane.OK_OPTION);
					DateEnd.setValue(DateEnd.getNextValue());
				} else {
					changedContent();
				}

			}
		});
		panel_top_date_end.add(DateEnd);

		JLabel lblExportieren = new JLabel("Exportieren als: ");

		btnIcalendar = new JButton("iCalendar");
		btnIcalendar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = comboBox.getSelectedItem();
				TimeInterval t = new TimeInterval((Date) DateStart.getValue(),
						(Date) DateEnd.getValue());
				String sExport;
				if (obj instanceof Employee) {
					sExport = officeCtrl.getIOController().exportCalendar(
							(Employee) obj, t);
				} else if (obj instanceof Patient) {
					sExport = officeCtrl.getIOController().exportCalendar(
							(Patient) obj, t);
				} else if (obj instanceof Room) {
					sExport = officeCtrl.getIOController().exportCalendar(
							(Room) obj, t);
				} else {
					sExport = "";
				}
				if (sExport == "") {
					JOptionPane.showMessageDialog(getParent(),
							"Konnte Datei nicht speichern!");
				} else {
					JFileChooser fc = new JFileChooser();
					int returnVal = fc.showSaveDialog(getParent());
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						String sFile = file.getAbsolutePath();
						if (!sFile.endsWith(".ics")) {
							sFile += ".ics";
						}
						if (!officeCtrl.getIOController().saveStringToFile(
								sExport, sFile)) {
							JOptionPane.showMessageDialog(getParent(),
									"Konnte Datei nicht speichern!");
						}
					}
				}

			}
		});

		// JButton btnPdf = new JButton("PDF");

		JScrollPane scrollPane = new JScrollPane();

		JButton btnClose = new JButton("Schließen");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGap(3)
										.addComponent(rdbtnMitarbeiter)
										.addGap(4)
										.addComponent(rdbtnPatient)
										.addGap(4)
										.addComponent(rdbtnRaum)
										.addGap(8)
										.addComponent(comboBox, 0, 409,
												Short.MAX_VALUE).addGap(8))
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGap(3)
										.addComponent(lblExportieren,
												GroupLayout.PREFERRED_SIZE,
												123, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(btnIcalendar,
												GroupLayout.PREFERRED_SIZE,
												105, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED,
												336, Short.MAX_VALUE)
										.addComponent(btnClose))
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGap(13)
										.addComponent(panel_top_date_start,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addComponent(panel_top_date_end,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap(331, Short.MAX_VALUE))
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(scrollPane,
												GroupLayout.DEFAULT_SIZE, 658,
												Short.MAX_VALUE)
										.addContainerGap()));
		gl_contentPane
				.setVerticalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGap(2)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGap(1)
																		.addComponent(
																				rdbtnMitarbeiter))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGap(1)
																		.addComponent(
																				rdbtnPatient))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGap(1)
																		.addComponent(
																				rdbtnRaum))
														.addComponent(
																comboBox,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																panel_top_date_start,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																panel_top_date_end,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addComponent(scrollPane,
												GroupLayout.DEFAULT_SIZE, 231,
												Short.MAX_VALUE)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING,
																false)
														.addComponent(
																btnClose,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																lblExportieren,
																GroupLayout.DEFAULT_SIZE,
																35,
																Short.MAX_VALUE)
														.addComponent(
																btnIcalendar,
																GroupLayout.DEFAULT_SIZE,
																35,
																Short.MAX_VALUE))
										.addGap(4)));

		table_dates = new JTable();
		scrollPane.setViewportView(table_dates);
		table_dates.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_dates.setEnabled(false);
		table_dates.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Start", "Ende", "Patient", "Behandlung",
						"Mitarbeiter", "Raum" }));
		table_dates.getColumnModel().getColumn(0).setPreferredWidth(150);
		table_dates.getColumnModel().getColumn(1).setPreferredWidth(150);
		table_dates.getColumnModel().getColumn(3).setPreferredWidth(100);
		table_dates.getColumnModel().getColumn(3).setMinWidth(100);
		table_dates.getColumnModel().getColumn(4).setPreferredWidth(100);
		table_dates.getColumnModel().getColumn(4).setMinWidth(100);

		contentPane.setLayout(gl_contentPane);
	}

	/**
	 * Updatet die in der ComboBox angezeigte Liste auf die Elemente des neu
	 * gewählten Types
	 * 
	 * @param list
	 *            Die neuen Elemente
	 */
	protected void changedType(List list) {
		comboBoxModel.removeAllElements();
		for (Object o : list) {
			comboBoxModel.addElement(o);
		}
	}

	/**
	 * Wird aufgerufen, wenn ein neues Element gewählt wurde, erzeugt die Liste
	 * hinzugehoeriger Termine
	 * 
	 * @return true, falls für das Element ein (gegebenenfalls auch leerer)
	 *         Kalender generiert werden konnte
	 */
	protected boolean changedContent() {
		Object obj = comboBox.getSelectedItem();
		TimeInterval t = new TimeInterval((Date) DateStart.getValue(),
				(Date) DateEnd.getValue());
		if (obj instanceof Employee) {
			Employee e = (Employee) obj;
			List<Appointment> list = officeCtrl.getEmployeeController()
					.getAppointmentsInTime(e, t);
			loopOverAppointments(list);
		} else if (obj instanceof Patient) {
			Patient p = (Patient) obj;
			List<PracticalAppointment> list = officeCtrl.getPatientController()
					.getPracticalAppointmentsInTime(p, t);
			loopOverAppointments(list);
		} else if (obj instanceof Room) {
			Room r = (Room) obj;
			List<PracticalAppointment> list = officeCtrl.getRoomController()
					.getPracticalAppoinmentsInTime(r, t);
			loopOverAppointments(list);
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Bekommt die Terminliste von changedContext und baut daraus einen
	 * (gegebenenfalls auch leeren) Kalender
	 * 
	 * @param list
	 *            Die liste der Termine des in der GUI ausgeählten Elementes
	 * @return true, falls für das Element ein (gegebenenfalls auch leerer)
	 *         Kalender generiert werden konnte
	 */
	private boolean loopOverAppointments(List<? extends Appointment> list) {
		DefaultTableModel defaultModel = (DefaultTableModel) table_dates
				.getModel();
		defaultModel.setRowCount(0);
		defaultModel.setRowCount(list.size());
		if (list.size() == 0) {
			btnIcalendar.setEnabled(false);
		} else {
			btnIcalendar.setEnabled(true);
			for (int row = 0; row < list.size(); row++) {
				Appointment ap = list.get(row);
				defaultModel.setValueAt(ap.getTime().getBegin(), row, 0);
				defaultModel.setValueAt(ap.getTime().getEnd(), row, 1);
				if (ap instanceof Vacation) {
					defaultModel.setValueAt("Fehlzeit", row, 2);
					defaultModel.setValueAt("", row, 3);
					defaultModel.setValueAt("", row, 4);
					defaultModel.setValueAt("", row, 5);
				} else if (ap instanceof PracticalAppointment) {
					PracticalAppointment pa = (PracticalAppointment) ap;
					if (pa instanceof Course) {
						defaultModel.setValueAt("Kurs", row, 2);
					} else if (pa instanceof Treatment) {
						Treatment tr = (Treatment) pa;
						defaultModel
								.setValueAt(
										tr.getPatient().getLastName()
												+ ", "
												+ tr.getPatient()
														.getFirstName(), row, 2);
					} else {
						return false;
					}
					defaultModel.setValueAt(pa.getActivity().getName(), row, 3);
					defaultModel.setValueAt(pa.getEmployee().getLastName()
							+ ", " + pa.getEmployee().getFirstName(), row, 4);
					defaultModel.setValueAt(pa.getRoom().getName(), row, 5);
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Hilfsmethode, um die Voreinstellungen der GUI einrichten zu können
	 * 
	 * @param ob
	 *            Das in der ComboBox zu wählende Objekt
	 * @param t
	 *            Das in den JSpinners einzustellende Zeitintervall
	 * @return
	 */
	public boolean changePreset(Object ob, TimeInterval t) {
		if (ob == null) {
			return false;
		}
		if (t == null) {
			Date tStart = new Date();
			Date tEnd = new Date(System.currentTimeMillis()
					+ (1000 * 60 * 60 * 24 * 7));
			t = new TimeInterval(tStart, tEnd);
		}
		DateStart.setModel(new SpinnerDateModel(t.getBegin(), null, null,
				Calendar.DAY_OF_YEAR));
		DateEnd.setModel(new SpinnerDateModel(t.getEnd(), null, null,
				Calendar.DAY_OF_YEAR));
		if (ob instanceof Employee) {
			rdbtnMitarbeiter.setSelected(true);
			rdbtnPatient.setSelected(false);
			rdbtnRaum.setSelected(false);
			changedType(officeCtrl.getOffice().getEmployees());
			comboBox.setSelectedItem(ob);
			changedContent();
			return true;
		} else if (ob instanceof Patient) {
			rdbtnMitarbeiter.setSelected(false);
			rdbtnPatient.setSelected(true);
			rdbtnRaum.setSelected(false);
			changedType(officeCtrl.getOffice().getPatients());
			comboBox.setSelectedItem(ob);
			changedContent();
			return true;
		} else if (ob instanceof Room) {
			rdbtnMitarbeiter.setSelected(false);
			rdbtnPatient.setSelected(false);
			rdbtnRaum.setSelected(true);
			changedType(officeCtrl.getOffice().getRooms());
			comboBox.setSelectedItem(ob);
			changedContent();
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected JRootPane createRootPane() {
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
}
