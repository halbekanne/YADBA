/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.management;

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

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import model.Activity;
import model.Employee;
import model.Rank;
import model.TimeOfDayInterval;
import controller.OfficeController;

/**
 * Ein Panel um Mitarbeiterdaten anzuzeigen und zu bearbeiten.
 * 
 * @author Alex
 */
public class EmployeeDataPanel extends JPanel {

	/** default id damit Eclipse ruhig ist */
	private static final long serialVersionUID = 1L;

	/** der OfficeControler */
	protected final OfficeController officeCtrl;
	protected final Employee employee;

	// die Komponenten
	private final JTable table;
	private final JComboBox<Rank> cbxRank;
	private final JPasswordField passwordField;
	private final JPasswordField passwordField_1;
	private final JButton btnWorkingTimes;
	private final JButton btnFehlzeiten;

	/**
	 * Erstellt einen neues Panel.
	 * 
	 * @param officeController
	 *            DER OfficeControler.
	 */
	public EmployeeDataPanel(OfficeController officeController, Employee e) {
		this.officeCtrl = officeController;
		this.employee = e;

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 125, 0 };
		layout.columnWeights = new double[] { 0.0, 1.0 };
		layout.rowHeights = new int[] { 30, 30, 30, 30, 0, 100 };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		setLayout(layout);

		JLabel lblPosition = new JLabel("Position");
		GridBagConstraints gbcLblPosition = new GridBagConstraints();
		gbcLblPosition.fill = GridBagConstraints.HORIZONTAL;
		gbcLblPosition.anchor = GridBagConstraints.NORTHWEST;
		gbcLblPosition.insets = new Insets(0, 0, 5, 5);
		gbcLblPosition.gridx = 0;
		gbcLblPosition.gridy = 0;
		add(lblPosition, gbcLblPosition);

		cbxRank = new JComboBox<Rank>();
		cbxRank.setModel(new DefaultComboBoxModel<Rank>(Rank.values()));
		cbxRank.setRenderer(new DefaultListCellRenderer() {
			/**   */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(
						list, value, index, isSelected, cellHasFocus);
				label.setText(((Rank) value).getViewName());
				return label;
			}

		});
		GridBagConstraints gbcPosition = new GridBagConstraints();
		gbcPosition.fill = GridBagConstraints.HORIZONTAL;
		gbcPosition.anchor = GridBagConstraints.NORTHWEST;
		gbcPosition.insets = new Insets(0, 0, 5, 0);
		gbcPosition.gridx = 1;
		gbcPosition.gridy = 0;
		add(cbxRank, gbcPosition);

		JLabel lblPasswort = new JLabel("Passwort");
		GridBagConstraints gbcLblPassword = new GridBagConstraints();
		gbcLblPassword.fill = GridBagConstraints.HORIZONTAL;
		gbcLblPassword.anchor = GridBagConstraints.NORTHWEST;
		gbcLblPassword.insets = new Insets(0, 0, 5, 5);
		gbcLblPassword.gridx = 0;
		gbcLblPassword.gridy = 1;
		add(lblPasswort, gbcLblPassword);

		passwordField = new JPasswordField();
		GridBagConstraints gbcPassword = new GridBagConstraints();
		gbcPassword.fill = GridBagConstraints.HORIZONTAL;
		gbcPassword.anchor = GridBagConstraints.NORTHWEST;
		gbcPassword.insets = new Insets(0, 0, 5, 0);
		gbcPassword.gridx = 1;
		gbcPassword.gridy = 1;
		add(passwordField, gbcPassword);

		JLabel lblPasswortwiederholen = new JLabel("Wiederholung:");
		GridBagConstraints gbcLblPasswordRe = new GridBagConstraints();
		gbcLblPasswordRe.fill = GridBagConstraints.HORIZONTAL;
		gbcLblPasswordRe.anchor = GridBagConstraints.NORTHWEST;
		gbcLblPasswordRe.insets = new Insets(0, 0, 5, 0);
		gbcLblPasswordRe.gridwidth = 2;
		gbcLblPasswordRe.gridx = 0;
		gbcLblPasswordRe.gridy = 2;
		add(lblPasswortwiederholen, gbcLblPasswordRe);

		passwordField_1 = new JPasswordField();
		GridBagConstraints gbcPasswordRe = new GridBagConstraints();
		gbcPasswordRe.fill = GridBagConstraints.HORIZONTAL;
		gbcPasswordRe.anchor = GridBagConstraints.NORTHWEST;
		gbcPasswordRe.insets = new Insets(0, 0, 5, 0);
		gbcPasswordRe.gridx = 1;
		gbcPasswordRe.gridy = 2;
		add(passwordField_1, gbcPasswordRe);

		btnWorkingTimes = new JButton("Arbeitszeiten");
		btnWorkingTimes.setMnemonic('z');
		btnWorkingTimes.setEnabled(employee != null);
		btnWorkingTimes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (employee != null) {
					ArbeitszeitenDialog dialog = new ArbeitszeitenDialog(
							officeCtrl, employee);
					dialog.setLocationRelativeTo(EmployeeDataPanel.this);
					dialog.setVisible(true);
				} else {
					JOptionPane
							.showMessageDialog(
									null,
									"Arbeitszeiten können erst nach Abspeichern der Mitarbeiters bearbeitet werden!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
				}
			}
		});
		GridBagConstraints gbcWorkingTimes = new GridBagConstraints();
		gbcWorkingTimes.fill = GridBagConstraints.HORIZONTAL;
		gbcWorkingTimes.anchor = GridBagConstraints.NORTHWEST;
		gbcWorkingTimes.insets = new Insets(0, 0, 5, 0);
		gbcWorkingTimes.gridx = 1;
		gbcWorkingTimes.gridy = 3;
		add(btnWorkingTimes, gbcWorkingTimes);

		btnFehlzeiten = new JButton("Fehlzeiten");
		btnFehlzeiten.setMnemonic('f');
		btnFehlzeiten.setEnabled(employee != null);
		btnFehlzeiten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (employee != null) {
					FehlzeitenDialog fehlzeitenDialog = new FehlzeitenDialog(
							officeCtrl, employee);
					fehlzeitenDialog
							.setLocationRelativeTo(EmployeeDataPanel.this);
					fehlzeitenDialog.setVisible(true);
				} else {
					JOptionPane
							.showMessageDialog(
									null,
									"Fehlzeiten können erst nach Abspeichern der Mitarbeiters bearbeitet werden!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
				}
			}
		});
		GridBagConstraints gbc_btnFehlzeiten = new GridBagConstraints();
		gbc_btnFehlzeiten.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnFehlzeiten.insets = new Insets(0, 0, 5, 0);
		gbc_btnFehlzeiten.gridx = 1;
		gbc_btnFehlzeiten.gridy = 4;
		add(btnFehlzeiten, gbc_btnFehlzeiten);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbcAxctiviteis = new GridBagConstraints();
		gbcAxctiviteis.fill = GridBagConstraints.BOTH;
		gbcAxctiviteis.anchor = GridBagConstraints.NORTHWEST;
		gbcAxctiviteis.gridwidth = 2;
		gbcAxctiviteis.gridx = 0;
		gbcAxctiviteis.gridy = 5;
		add(scrollPane, gbcAxctiviteis);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new AbstractTableModel() {
			/** default id damit Eclipse ruhig ist */
			private static final long serialVersionUID = 1L;

			private final List<Activity> activities = officeCtrl.getOffice()
					.getActivities();
			private List<Activity> qualified;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (qualified == null) {
					qualified = new LinkedList<Activity>();
					if (employee != null) {
						qualified.addAll(employee.getActivities());
					}
				}
				Activity activity = activities.get(rowIndex);

				switch (columnIndex) {
				case 0:
					return activity.getName();
				case 1:
					return qualified.contains(activity);
				default:
					return "";
				}
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				switch (columnIndex) {
				case 0:
					return String.class;
				case 1:
					return Boolean.class;
				default:
					return null;
				}
			}

			@Override
			public int getRowCount() {
				return activities.size();
			}

			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public String getColumnName(int column) {
				switch (column) {
				case 0:
					return "Leistung";
				case 1:
					return "qualifiziert";
				default:
					return "";
				}
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}

			@Override
			public void setValueAt(Object obj, int row, int column) {
				if (!isCellEditable(row, column)) {
					return;
				}
				if ((Boolean) obj) {
					qualified.add(activities.get(row));
				} else {
					qualified.remove(activities.get(row));
				}
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.getColumnModel().getColumn(0).setMinWidth(40);
		table.getColumnModel().getColumn(1).setPreferredWidth(70);
	}

	/**
	 * Füllt die Labels und Felder mit den Daten des Mitarbeiters.
	 * */
	public void fillComponents(Rank position) {
		cbxRank.setSelectedItem(position);
	}

	/**
	 * Schaut ob alle Eingabefelder OK sind.
	 * 
	 * @return true, wenn alle Eingaben OK sind.
	 */
	public boolean isAllInputValid() {
		if (getPassword() == null) {
			return false;
		}
		return true;
	}

	/**
	 * Setzt editModus auf editable und passt die Komponenten an.
	 * 
	 * @param editable
	 *            Bearbeiten-Modus ja oder nein
	 */
	public void setEditModus(boolean editable) {
		// Mitarbeiter werden nur bearbeitet, also kein umschalten.
	}

	/**
	 * Gibt die Position des Mitarbeiters zur&uuml;ck.
	 * 
	 * @return Die Position.
	 */
	public Rank getRank() {
		return (Rank) cbxRank.getSelectedItem();
	}

	/**
	 * Gibt das Passwort des Mitarbeiters zur&uuml;ck oder null, falls die
	 * eingegebenen Passwörter nicht identisch sind.
	 * 
	 * @return Das Passwort.
	 */
	public String getPassword() {
		String password = new String(passwordField.getPassword());
		if (password.compareTo(new String(passwordField_1.getPassword())) == 0) {
			return new String(passwordField.getPassword());
		}
		return null;
	}

	/**
	 * Gibt die Arbeitszeiten desMitarbeiters zur&uuml;ck.
	 * 
	 * @return Ein Array von 7 TimeOfDayInterval, die die Arbeitzeit enthalten.
	 */
	public TimeOfDayInterval[] getWorkingTimes() {
		// TODO
		Calendar cal = Calendar.getInstance();
		cal.set(2013, 1, 1, 9, 15);
		Date begin = cal.getTime();
		cal.set(2013, 1, 1, 16, 00);
		Date end = cal.getTime();

		TimeOfDayInterval[] workingTime = new TimeOfDayInterval[7];
		for (int i = 0; i != 7; ++i) {
			workingTime[i] = new TimeOfDayInterval(begin, end);
		}
		return workingTime;
	}

	/**
	 * Gibt eine Liste der Behandlungen zur&uuml;ck, die der Mitarbeiter machen
	 * kann.
	 * 
	 * @return Eine List von Behandlungen, die der Mitarbeiter kann.
	 */
	public List<Activity> getActivities() {
		LinkedList<Activity> result = new LinkedList<Activity>();
		for (int row = 0; row < table.getRowCount(); row++) {
			Activity act = (Activity) officeCtrl.getServiceController()
					.getServiceByName((String) table.getValueAt(row, 0));
			if ((Boolean) table.getValueAt(row, 1)) {
				result.add(act);
			}
		}
		return result;
	}

}
