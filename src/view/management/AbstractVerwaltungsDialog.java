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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import controller.OfficeController;

/**
 * Die Oberklasse für diverse Verwalugnsdialog mit einer Tabelle und Buttons zum
 * erstellen und bearbeiten.
 * 
 * @author Alex
 */
public abstract class AbstractVerwaltungsDialog extends JDialog {
	/**  */
	private static final long serialVersionUID = 1L;

	/** der OfficeControler */
	protected final OfficeController officeCtrl;

	// die Komponenten
	protected final JPanel contentPanel;
	protected final JPanel buttonsPanel;
	protected final JLabel lblHeader;
	protected final JButton btnClose;
	protected final JButton btnNew;
	protected final JButton btnEdit;
	protected final JTable table;
	protected final JPanel bottomPanel;
	protected final JPopupMenu popupMenu;

	protected Action actNew = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Neu");
			putValue(MNEMONIC_KEY, KeyEvent.VK_N);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			newButtonActivated();
		}
	};

	protected Action actEdit = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Bearbeiten");
			putValue(MNEMONIC_KEY, KeyEvent.VK_E);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			editButtonActivated();
		}
	};

	public AbstractVerwaltungsDialog(OfficeController officeControler) {
		// general stuff
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		setModal(true);

		officeCtrl = officeControler;

		// header
		final JPanel headerPanel = new JPanel();
		getContentPane().add(headerPanel, BorderLayout.NORTH);

		lblHeader = new JLabel();
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 14));
		headerPanel.add(lblHeader);

		// content
		this.contentPanel = new JPanel();
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 5));

		popupMenu = new JPopupMenu();
		popupMenu.add(actNew);
		popupMenu.add(actEdit);

		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		table = new JTable(getListModel());
		table.removeColumn(table.getColumnModel().getColumn(0));
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();
		sorter.setModel(table.getModel());
		table.setRowSorter(sorter);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setComponentPopupMenu(popupMenu);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent event) {
						boolean isSomethingSelected = table
								.getSelectedRowCount() > 0;
						enableComponentsOnSelection(isSomethingSelected);
					}
				});
		scrollPane.setViewportView(table);

		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		contentPanel.add(buttonsPanel, BorderLayout.SOUTH);

		btnNew = new JButton(actNew);
		buttonsPanel.add(btnNew);

		btnEdit = new JButton(actEdit);
		actEdit.setEnabled(false);
		buttonsPanel.add(btnEdit);

		bottomPanel = new JPanel();
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		btnClose = new JButton("Schließen");
		btnClose.setMnemonic('c');
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		rootPane.setDefaultButton(btnClose);
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		bottomPanel.add(btnClose);
	}

	/**
	 * Gibt das Model für die Tabelle zurück.
	 * 
	 * @return Das Model für die Tabelle.
	 */
	protected abstract TableModel getListModel();

	/**
	 * Wird aufgerufen, wenn der Neu-Knopf aktiviert wurde.
	 */
	protected abstract void newButtonActivated();

	/**
	 * Wird aufgerufen, wenn der Bearbeiten-Knopf aktiviert wurde.
	 */
	protected abstract void editButtonActivated();

	/**
	 * Wird genutzt um Komponenten zu aktivieren, wenn in der Tablle etwas
	 * slected wurde.
	 * 
	 * @param enable
	 *            true, wenn es eine Auswahl gibt.
	 */
	protected void enableComponentsOnSelection(boolean enable) {
		actEdit.setEnabled(enable);
	}

	/**
	 * Gibt das ausgew&auml;hlte Object in der 0. Splate zur&uuml;ck.
	 * 
	 * @return Das ausgew&auml;hlte Object oder null.
	 */
	protected Object getSelectedObject() {
		if (table.getSelectedRowCount() == 0) {
			return null;
		} else {
			return table.getModel().getValueAt(
					table.getRowSorter().convertRowIndexToModel(
							table.getSelectedRow()), 0);
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

	@Override
	public void setTitle(String title) {
		super.setTitle(title);

		lblHeader.setText(title);
	}
}
