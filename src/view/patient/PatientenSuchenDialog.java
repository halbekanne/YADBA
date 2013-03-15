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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import model.Patient;
import view.management.AbstractVerwaltungsDialog;
import controller.OfficeController;

/**
 * Der Dialog um einen Patienten zu suchen.
 * 
 * @author Alex
 */
public class PatientenSuchenDialog extends AbstractVerwaltungsDialog {

	/** default id damit Eclipse ruhig ist */
	private static final long serialVersionUID = 1L;

	// Die ganzen Swing(x)-Komponenten
	private final JTextField textSearch;
	private final JButton btnRemove;

	protected Action actRemove = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Löschen");
			putValue(MNEMONIC_KEY, KeyEvent.VK_L);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int result = JOptionPane.showConfirmDialog(null, "Wollen sie den Patienten wirklich löschen?", "Sind Sie sicher?", JOptionPane.OK_CANCEL_OPTION);
			if (result != JOptionPane.CANCEL_OPTION){
				Patient patient = (Patient) getSelectedObject();
				if (officeCtrl.getPatientController().removePatient(patient)) {
					((AbstractTableModel) table.getModel()).fireTableDataChanged();
				}
			}
			
		}
	};

	/**
	 * Create the dialog.
	 */
	public PatientenSuchenDialog(OfficeController officeController) {
		super(officeController);

		// general stuff
		setBounds(100, 100, 450, 300);
		setTitle("Patienten suchen");

		// content
		final JPanel searchPanel = new JPanel();
		this.contentPanel.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

		final JLabel lblSearch = new JLabel("Suchbegriff:");
		searchPanel.add(lblSearch);

		this.textSearch = new JTextField();
		this.textSearch
				.setToolTipText("<html>Geben Sie hier den Suchbegriff ein.<br />"
						+ "Ein Leerzeichen trennt unterschiedliche Begriffe.<br/>"
						+ "Ein '!' am Anfang eines Begeriffes negiert diesen.</html>");
		this.textSearch.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				doUpdate();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				doUpdate();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				doUpdate();
			}

			private void doUpdate() {
				try {
					String[] tookens = textSearch.getText().split(" ");
					List<RowFilter<TableModel, Object>> allFilters = new LinkedList<RowFilter<TableModel, Object>>();
					for (String currentToken : tookens) {
						int[] columnsToSearch = new int[] { 0, 1 };

						RowFilter<TableModel, Object> filter;
						if (currentToken.length() > 0
								&& currentToken.charAt(0) == '!') {
							filter = RowFilter.regexFilter("(?i)"
									+ Pattern.quote(currentToken.substring(1)),
									columnsToSearch);
							filter = RowFilter.notFilter(filter);
						} else {
							filter = RowFilter.regexFilter("(?i)"
									+ Pattern.quote(currentToken),
									columnsToSearch);
						}

						allFilters.add(filter);
					}
					RowSorter<? extends TableModel> sorter = table
							.getRowSorter();
					if (sorter instanceof TableRowSorter<?>) {
						((TableRowSorter<?>) sorter).setRowFilter(RowFilter
								.andFilter(allFilters));
					}
				} catch (java.util.regex.PatternSyntaxException e) {
					// If current expression doesn't parse, don't update.
				}
			}
		});
		searchPanel.add(this.textSearch);

		// btnNew wird zum Anzeigen umgewandelt
		actNew.putValue(Action.NAME, "Anzeigen");
		actNew.setEnabled(false);

		actRemove.setEnabled(false);
		popupMenu.add(actRemove);
		btnRemove = new JButton(actRemove);
		buttonsPanel.add(btnRemove);

	}

	@Override
	protected TableModel getListModel() {
		return new AbstractTableModel() {
			/** default id damit Eclipse ruhig ist */
			private static final long serialVersionUID = 1L;

			/** the data */
			List<Patient> patients = officeCtrl.getOffice().getPatients();

			@Override
			public int getColumnCount() {
				return 3;
			}

			@Override
			public int getRowCount() {
				return patients.size();
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

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return patients.get(rowIndex);
				case 1:
					return patients.get(rowIndex).getLastName();
				case 2:
					return patients.get(rowIndex).getFirstName();
				default:
					return "";
				}
			}
		};
	}

	@Override
	protected void newButtonActivated() {
		Patient patient = (Patient) getSelectedObject();
		PatientenAktenDialog formular = new PatientenAktenDialog(officeCtrl,
				patient, false);
		formular.setLocationRelativeTo(this);
		formular.setVisible(true);

		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}

	@Override
	protected void editButtonActivated() {
		Patient patient = (Patient) getSelectedObject();
		PatientenAktenDialog formular = new PatientenAktenDialog(officeCtrl,
				patient, true);
		formular.setLocationRelativeTo(this);
		formular.setVisible(true);

		((AbstractTableModel) table.getModel()).fireTableDataChanged();

	}

	@Override
	protected void enableComponentsOnSelection(boolean enable) {
		super.enableComponentsOnSelection(enable);
		actNew.setEnabled(enable);
		actRemove.setEnabled(enable);
	}
}
