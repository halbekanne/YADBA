/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.management;

import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import model.Activity;
import controller.OfficeController;

/**
 * Ein Dialog der alle Behandlungen anzeigt.
 * 
 * @author Alex
 */
public class BehandlungsVerwaltungsDialog extends AbstractVerwaltungsDialog {

	/**  */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the frame.
	 */
	public BehandlungsVerwaltungsDialog(OfficeController officeControler) {
		super(officeControler);
		// general stuff
		setBounds(100, 100, 325, 447);
		setTitle("Behandlugen verwalten");

		// Behandlungen werden noch nicht bearbeitet
		btnEdit.setVisible(false);
		popupMenu.removeAll();
		popupMenu.add(actNew);
	}

	@Override
	protected TableModel getListModel() {
		return new AbstractTableModel() {
			/**  */
			private static final long serialVersionUID = 1L;

			private final List<Activity> activities = officeCtrl.getOffice()
					.getActivities();

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return activities.get(rowIndex);
				case 1:
					return activities.get(rowIndex).getName();
				case 2:
					return activities.get(rowIndex).getNumberOfSessions();
				case 3:
					return activities.get(rowIndex).getDuration();
				case 4:
					return String.format("%.2f", activities.get(rowIndex)
							.getPrice() / 100.0);
				default:
					return "";
				}
			}

			@Override
			public int getRowCount() {
				return activities.size();
			}

			@Override
			public int getColumnCount() {
				return 5;
			}

			@Override
			public String getColumnName(int column) {
				switch (column) {
				case 1:
					return "Name";
				case 2:
					return "Anzahl";
				case 3:
					return "Dauer";
				case 4:
					return "Preis (€)";
				default:
					return "";
				}
			}
		};
	}

	@Override
	protected void newButtonActivated() {
		BehandlungFormular formular = new BehandlungFormular(officeCtrl);
		formular.setLocationRelativeTo(this);
		formular.setVisible(true);

		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}

	@Override
	protected void editButtonActivated() {
		Activity activity = (Activity) getSelectedObject();
		BehandlungFormular formular = new BehandlungFormular(officeCtrl,
				activity);
		formular.setLocationRelativeTo(this);
		formular.setVisible(true);

		((AbstractTableModel) table.getModel()).fireTableDataChanged();

	}
}
