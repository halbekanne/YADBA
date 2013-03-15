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

import model.Employee;
import controller.OfficeController;

/**
 * 
 * @author Alex
 */
public class MitarbeiterVerwaltungsDialog extends AbstractVerwaltungsDialog {

	/**  */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Create the frame.
	 */
	public MitarbeiterVerwaltungsDialog(OfficeController officeControler) {
		super(officeControler);
		// general stuff
		setBounds(100, 100, 325, 447);
		setTitle("Mitarbeiter verwalten");
	}
	
	@Override
	protected TableModel getListModel() {
		return new AbstractTableModel() {
			/**  */
			private static final long serialVersionUID = 1L;
			
			private List<Employee> employees = officeCtrl.getOffice().getEmployees();
			
			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return employees.get(rowIndex);
				case 1:
					return employees.get(rowIndex).getLastName();
				case 2:
					return employees.get(rowIndex).getFirstName();
				default:
					return "";
				}
			}
			
			@Override
			public int getRowCount() {
				return employees.size();
			}
			
			@Override
			public int getColumnCount() {
				return 3;
			}
			
			@Override
			public String getColumnName(int column) {
				switch (column) {
				case 1:
					return "Nachname";
				case 2:
					return "Vorname";
				default:
					return "";
				}
			}
		};
	}
	
	@Override
	protected void newButtonActivated() {
		// nullpointerexception, weil mitarbeiter noch nicht vorhanden ist.
		MitarbeiterFormular formular = new MitarbeiterFormular(officeCtrl);
		formular.setLocationRelativeTo(this);
		formular.setVisible(true);
		
		((AbstractTableModel)table.getModel()).fireTableDataChanged();
	}

	@Override
	protected void editButtonActivated() {
		Employee empl = (Employee) getSelectedObject();
		MitarbeiterFormular formular = new MitarbeiterFormular(officeCtrl, empl);
		formular.setLocationRelativeTo(this);
		formular.setVisible(true);
		
		((AbstractTableModel)table.getModel()).fireTableDataChanged();
	}

}
