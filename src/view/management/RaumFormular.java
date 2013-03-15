/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.management;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import model.Activity;
import model.Room;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controller.OfficeController;

public class RaumFormular extends JDialog {

	/** default id damit Eclipse ruhig ist */
	private static final long serialVersionUID = 1L;

	/** der OfficeControler */
	private final OfficeController officeCtrl;

	/** der Raum der bearbeitet wird oder null */
	private final Room room;

	// die Komponenten
	private final JPanel contentPanel = new JPanel();
	private final JTextField textName;
	private final JSpinner spinnerCapacity;
	private final JTable table;

	/**
	 * Create the dialog.
	 * 
	 * @param officeController
	 *            DER officeController
	 */
	public RaumFormular(OfficeController officeController) {
		this(officeController, null);
	}

	/**
	 * Create the dialog.
	 * 
	 * @param officeController
	 *            DER officeController
	 * @param r
	 *            Ein Raum der bearbeitet werden soll oder null.
	 */
	public RaumFormular(OfficeController officeController, Room r) {
		this.officeCtrl = officeController;
		this.room = r;

		setModal(true);
		setBounds(100, 100, 327, 335);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, }));
		{
			JLabel lblBezeichnung = new JLabel("Bezeichnung:");
			lblBezeichnung.setHorizontalAlignment(SwingConstants.LEFT);
			contentPanel.add(lblBezeichnung, "2, 2, left, top");
		}
		{
			textName = new JTextField();
			contentPanel.add(textName, "4, 2, fill, top");
			textName.setColumns(10);
		}
		{
			JLabel lblKapazitt = new JLabel("Kapazität:");
			contentPanel.add(lblKapazitt, "2, 4, left, top");
		}
		{
			spinnerCapacity = new JSpinner();
			spinnerCapacity.setModel(new SpinnerNumberModel(new Integer(1),
					new Integer(1), null, new Integer(1)));
			contentPanel.add(spinnerCapacity, "4, 4, default, top");
		}
		{
			{
				JScrollPane scrollPane = new JScrollPane();
				contentPanel.add(scrollPane, "2, 6, 3, 1, fill, fill");
				table = new JTable();
				scrollPane.setViewportView(table);
				table.setModel(new AbstractTableModel() {
					/** default id damit Eclipse ruhig ist */
					private static final long serialVersionUID = 1L;

					private final List<Activity> activities = officeCtrl
							.getOffice().getActivities();
					private List<Activity> qualified;

					@Override
					public Object getValueAt(int rowIndex, int columnIndex) {
						if (qualified == null) {
							qualified = new LinkedList<Activity>();
							if (room != null) {
								qualified.addAll(room.getActivity());
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
							return "durchführbar";
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
				table.getColumnModel().getColumn(1).setPreferredWidth(60);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Speichern");
				okButton.setMnemonic('s');
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (saveInput()) {

							dispose();
						} else {
							JOptionPane
									.showMessageDialog(
											null,
											"Konnte nicht speichern, überprüfen Sie bitte die Eingaben!",
											"Fehler beim Speichern",
											JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Abbrechen");
				cancelButton.setMnemonic('a');
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}

		fillComponents();
	}

	/**
	 * Füllt die Labels und Felder mit den Daten der Person.
	 * */
	private void fillComponents() {
		String name;
		int capacity;

		if (room == null) {
			name = "";
			capacity = 1;
		} else {
			name = room.getName();
			capacity = room.getCapacity();
		}

		textName.setText(name);
		spinnerCapacity.setValue(capacity);
	}

	private boolean saveInput() {
		List<Activity> activities = new LinkedList<Activity>();
		for (int i = 0; i < table.getRowCount(); ++i) {
			if ((Boolean) table.getValueAt(i, 1)) {
				activities.add((Activity) officeCtrl.getServiceController()
						.getServiceByName((String) table.getValueAt(i, 0)));
			}
		}

		Room newRoom = new Room(textName.getText(),
				((SpinnerNumberModel) spinnerCapacity.getModel()).getNumber()
						.intValue(), activities);

		if (room == null) {
			return officeCtrl.getRoomController().saveRoom(newRoom);
		} else {
			return officeCtrl.getRoomController().editRoom(room, newRoom);
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
