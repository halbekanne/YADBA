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

import model.Room;
import controller.OfficeController;

/**
 * Zeigt alle Räume an und bietet Optionen zum Verwalten.
 * @author Karl & Alex
 */
public class RaumverwaltungsDialog extends AbstractVerwaltungsDialog {

	/**  */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create the frame.
	 */
	public RaumverwaltungsDialog(OfficeController officeControler) {
		super(officeControler);
		// general stuff
		setBounds(100, 100, 325, 447);
		setTitle("Räume verwalten");
	}

	@Override
	protected TableModel getListModel() {
		return new AbstractTableModel() {
			/**  */
			private static final long serialVersionUID = 1L;
			
			private List<Room> rooms = officeCtrl.getOffice().getRooms();
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return rooms.get(rowIndex);
				case 1:
					return rooms.get(rowIndex).getName();
				default:
					return "";
				}
			}
			
			@Override
			public int getRowCount() {
				return rooms.size();
			}
			
			@Override
			public int getColumnCount() {
				return 2;
			}
			
			@Override
			public String getColumnName(int column) {
				switch (column) {
				case 1:
					return "Raum";
				default:
					return "";
				}
			}
		};
	}
	
	@Override
	protected void newButtonActivated() {
		RaumFormular formular = new RaumFormular(officeCtrl);
		formular.setLocationRelativeTo(this);
		formular.setVisible(true);

		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}

	@Override
	protected void editButtonActivated() {
		Room room = (Room) getSelectedObject();
		RaumFormular formular = new RaumFormular(officeCtrl, room);
		formular.setLocationRelativeTo(this);
		formular.setVisible(true);
		
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}
}
