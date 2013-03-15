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

import model.Material;
import controller.OfficeController;

/**
 * 
 * @author Alex
 */
public class MaterialVerwaltungsDialog extends AbstractVerwaltungsDialog {

	/**  */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the frame.
	 */
	public MaterialVerwaltungsDialog(OfficeController officeControler) {
		super(officeControler);
		// general stuff
		setBounds(100, 100, 325, 447);
		setTitle("Material verwalten");

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

			private final List<Material> materials = officeCtrl.getOffice()
					.getMaterials();

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return materials.get(rowIndex);
				case 1:
					return materials.get(rowIndex).getName();
				case 2:
					return String.format("%.2f", materials.get(rowIndex)
							.getPrice() / 100.0);
				default:
					return "";
				}
			}

			@Override
			public int getRowCount() {
				return materials.size();
			}

			@Override
			public int getColumnCount() {
				return 3;
			}

			@Override
			public String getColumnName(int column) {
				switch (column) {
				case 1:
					return "Material";
				case 2:
					return "Preis (€)";
				default:
					return "";
				}
			}
		};
	}

	@Override
	protected void newButtonActivated() {
		MaterialFormular formular = new MaterialFormular(officeCtrl);
		formular.setLocationRelativeTo(this);
		formular.setVisible(true);

		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}

	@Override
	protected void editButtonActivated() {
		Material material = (Material) getSelectedObject();
		MaterialFormular formular = new MaterialFormular(officeCtrl, material);
		formular.setLocationRelativeTo(this);
		formular.setVisible(true);

		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}

}
