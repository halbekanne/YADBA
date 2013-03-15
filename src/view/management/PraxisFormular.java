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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import view.patient.ContactPanel;
import controller.OfficeController;

public class PraxisFormular extends JDialog implements Observer {

	/**  */
	private static final long serialVersionUID = 1L;

	/** der OfficeControler */
	protected final OfficeController officeCtrl;

	// die Komponenten
	private final JButton btnSave;
	private final ContactPanel contactPanel;
	private final JTextField textName;
	private final JTextField textTaxNo;

	public PraxisFormular(OfficeController officeController) {
		this.officeCtrl = officeController;

		setBounds(0, 0, 400, 300);
		setModal(true);

		getContentPane().setLayout(new BorderLayout());

		// header
		final JPanel headerPanel = new JPanel();
		getContentPane().add(headerPanel, BorderLayout.NORTH);

		final JLabel lblHeader = new JLabel("Praxisdaten");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 14));
		headerPanel.add(lblHeader);

		// content
		final JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout contentLayout = new GridBagLayout();
		contentLayout.columnWeights = new double[] { 1.0 };
		contentLayout.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		contentPanel.setLayout(contentLayout);
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		final JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel.gridy = 0;
		gbc_panel.gridx = 0;
		contentPanel.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 100, 0 };
		gbl_panel.rowHeights = new int[] { 30, 30 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0 };
		gbl_panel.rowWeights = new double[] { 0.0, 1.0 };
		panel.setLayout(gbl_panel);

		final JLabel lblPraxisname = new JLabel("Praxisname:");
		GridBagConstraints gbc_lblPraxisname = new GridBagConstraints();
		gbc_lblPraxisname.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblPraxisname.insets = new Insets(0, 0, 5, 5);
		gbc_lblPraxisname.gridx = 0;
		gbc_lblPraxisname.gridy = 0;
		panel.add(lblPraxisname, gbc_lblPraxisname);

		this.textName = new JTextField();
		GridBagConstraints gbc_textName = new GridBagConstraints();
		gbc_textName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textName.anchor = GridBagConstraints.NORTH;
		gbc_textName.insets = new Insets(0, 0, 5, 0);
		gbc_textName.gridx = 1;
		gbc_textName.gridy = 0;
		panel.add(this.textName, gbc_textName);

		final JLabel lblSteuernummer = new JLabel("Steuernummer:");
		GridBagConstraints gbc_lblSteuernummer = new GridBagConstraints();
		gbc_lblSteuernummer.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblSteuernummer.insets = new Insets(0, 0, 5, 5);
		gbc_lblSteuernummer.gridx = 0;
		gbc_lblSteuernummer.gridy = 1;
		panel.add(lblSteuernummer, gbc_lblSteuernummer);

		this.textTaxNo = new JTextField();
		GridBagConstraints gbc_textTaxNo = new GridBagConstraints();
		gbc_textTaxNo.fill = GridBagConstraints.HORIZONTAL;
		gbc_textTaxNo.anchor = GridBagConstraints.NORTH;
		gbc_textTaxNo.gridx = 1;
		gbc_textTaxNo.gridy = 1;
		panel.add(this.textTaxNo, gbc_textTaxNo);

		final JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.anchor = GridBagConstraints.NORTHWEST;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		contentPanel.add(separator, gbc_separator);

		contactPanel = new ContactPanel(this);
		contactPanel.setEditModus(true);
		GridBagConstraints gbc_contactPanel = new GridBagConstraints();
		gbc_contactPanel.anchor = GridBagConstraints.NORTHWEST;
		gbc_contactPanel.fill = GridBagConstraints.BOTH;
		gbc_contactPanel.gridy = 2;
		gbc_contactPanel.gridx = 0;
		contentPanel.add(contactPanel, gbc_contactPanel);

		// buttons
		final JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		btnSave = new JButton("Speichern");
		btnSave.setMnemonic('s');
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (officeCtrl.editOffice(textName.getText(), textTaxNo
						.getText(), contactPanel.getContact())) {
					dispose();
				} else {
					JOptionPane.showMessageDialog(null,
							"Konnte Kontaktinformationen nicht ändern!",
							"Fehler bei Kontaktinformationen änderung",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		bottomPanel.add(btnSave);
		getRootPane().setDefaultButton(btnSave);

		final JButton btnCancel = new JButton("Abbrechen");
		btnCancel.setMnemonic('a');
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		bottomPanel.add(btnCancel);

		fillComponents();
	}

	/**
	 * Füllt die Labels und Felder mit den Daten der Person.
	 */
	private void fillComponents() {
		textName.setText(officeCtrl.getOffice().getName());
		textTaxNo.setText(officeCtrl.getOffice().getTaxNo());
		contactPanel.fillComponents(officeCtrl.getOffice().getContact());
	}

	/**
	 * Schaut ob alle Eingabefelder OK sind.
	 * 
	 * @return true, wenn alle Eingaben OK sind.
	 */
	public boolean isAllInputValid() {
		return contactPanel.isAllInputValid();
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
	public void update(Observable o, Object arg) {
		btnSave.setEnabled(isAllInputValid());
	}

}
