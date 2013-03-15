/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.patient;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import model.Material;
import model.Patient;
import controller.OfficeController;

public class PatientenMaterialDialog extends JDialog {

	private final class MaterialModel extends AbstractTableModel {
		/** */
		private static final long serialVersionUID = 1L;

		List<Material> materials;

		public MaterialModel(Patient pat) {
			materials = pat.getMaterial();
		}

		@Override
		public String getColumnName(int column) {
			switch (column) {
			case 0:
				return "Bezeichnung";
			case 1:
				return "Preis (€):";
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
			return 2;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return materials.get(rowIndex).getName();
			case 1:
				return String.format("%.2f",
						materials.get(rowIndex).getPrice() / 100.0);
			default:
				return "";
			}
		}
	}

	/** */
	private static final long serialVersionUID = 1L;

	/** DER OfficeControler */
	private final OfficeController officeCtrl;

	/** halt der Patient oder null */
	private final Patient pat;

	// die Komponenten
	private final JPanel contentPanel;
	private final JLabel lblMaterial;
	private final JScrollPane scrollPane;
	private final JTable table;
	private final JButton btnRemove;

	/**
	 * Create the dialog.
	 */
	public PatientenMaterialDialog(OfficeController officeController,
			Patient patient) {
		if (patient == null) {
			throw new IllegalArgumentException(
					"Material kann nur zu einem Patienten hinzugefügt werden");
		}

		this.officeCtrl = officeController;
		this.pat = patient;

		setBounds(0, 0, 550, 350);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());

		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(headerPanel, BorderLayout.NORTH);

		this.lblMaterial = new JLabel(
				"<html><font size=+1><u>Material</u></font></html>");
		headerPanel.add(lblMaterial);

		this.contentPanel = new JPanel();
		this.contentPanel.setLayout(new BoxLayout(contentPanel,
				BoxLayout.Y_AXIS));
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);

		this.scrollPane = new JScrollPane();
		this.contentPanel.add(this.scrollPane);

		final MaterialModel tableModel = new MaterialModel(pat);
		this.table = new JTable(tableModel);
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent event) {
						boolean isSomethingSelected = table
								.getSelectedRowCount() > 0;
						btnRemove.setEnabled(isSomethingSelected);
					}
				});
		this.scrollPane.setViewportView(this.table);

		JPanel editPanel = new JPanel();
		editPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		contentPanel.add(editPanel);

		DefaultComboBoxModel<Material> materialModel = new DefaultComboBoxModel<Material>(
				officeCtrl.getOffice().getMaterials().toArray(
						new Material[officeCtrl.getOffice().getMaterials()
								.size()]));
		final JComboBox<Material> cmbMaterial = new JComboBox<Material>(
				materialModel);
		editPanel.add(cmbMaterial);

		JButton btnAdd = new JButton("Hinzufügen");
		btnAdd.setMnemonic('h');
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cmbMaterial.getSelectedIndex() != -1) {
					Material mat = (Material) cmbMaterial.getSelectedItem();
					// tableModel.materials.add(mat);

					officeCtrl.getPatientController().addMaterial(pat, mat);

					tableModel.fireTableDataChanged();
				}
			}
		});
		editPanel.add(btnAdd);

		btnRemove = new JButton("Löschen");
		btnRemove.setMnemonic('l');
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRowCount() > 0) {
					Material mat = tableModel.materials.get(table
							.getSelectedRow());
					officeCtrl.getPatientController().removeMaterial(pat, mat);
					tableModel.fireTableDataChanged();
				}
			}
		});
		editPanel.add(btnRemove);

		// buttons
		final JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		final JButton closeButton = new JButton("Schließen");
		closeButton.setMnemonic('c');
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		buttonPane.add(closeButton);
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
