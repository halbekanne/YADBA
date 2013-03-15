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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import model.Contact;
import model.Person;
import view.patient.ContactPanel;
import view.patient.NamePanel;
import controller.OfficeController;

public abstract class PersonenFormular extends JDialog implements Observer {

	/** default id damit Eclipse ruhig ist */
	private static final long serialVersionUID = 1L;

	/** der OfficeControler */
	protected final OfficeController officeCtrl;

	/**
	 * true == Textfelder und Speicherbutton zum editiern anstatt nur anzeigen
	 * von Labels.
	 */
	protected boolean editModus;

	/** die Person oder null */
	protected Person person;

	// Die ganzen Swing(x)-Komponenten
	protected final JLabel lblHeader;

	protected final JPanel contentPanel = new JPanel();

	protected final JButton btnEdit;

	protected final JButton btnSave;

	protected final JButton btnClose;

	protected final NamePanel namePanel;

	protected final ContactPanel contactPanel;

	public PersonenFormular(OfficeController officeController) {
		this(officeController, null);
	}

	public PersonenFormular(OfficeController officeController, Person person) {
		this.officeCtrl = officeController;
		this.person = person;

		// Allg
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());

		// Ueberschrift
		final JPanel headerpanel = new JPanel();
		getContentPane().add(headerpanel, BorderLayout.NORTH);

		lblHeader = new JLabel();
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 14));
		headerpanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
		headerpanel.add(lblHeader);

		// Inhalt
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);

		GridBagLayout contentLayout = new GridBagLayout();
		contentLayout.columnWeights = new double[] { 1.0 };
		contentLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
		this.contentPanel.setLayout(contentLayout);

		namePanel = new NamePanel(this);
		GridBagConstraints gbcName = new GridBagConstraints();
		gbcName.fill = GridBagConstraints.HORIZONTAL;
		gbcName.anchor = GridBagConstraints.NORTHWEST;
		gbcName.insets = new Insets(0, 0, 5, 0);
		gbcName.gridx = 0;
		gbcName.gridy = 0;
		contentPanel.add(namePanel, gbcName);

		JSeparator separator = new JSeparator();
		GridBagConstraints gbcSperator1 = new GridBagConstraints();
		gbcSperator1.fill = GridBagConstraints.HORIZONTAL;
		gbcSperator1.anchor = GridBagConstraints.NORTHWEST;
		gbcSperator1.insets = new Insets(0, 0, 5, 0);
		gbcSperator1.gridx = 0;
		gbcSperator1.gridy = 1;
		contentPanel.add(separator, gbcSperator1);

		contactPanel = new ContactPanel(this);
		GridBagConstraints gbcContact = new GridBagConstraints();
		gbcContact.fill = GridBagConstraints.HORIZONTAL;
		gbcContact.anchor = GridBagConstraints.NORTHWEST;
		gbcContact.insets = new Insets(0, 0, 5, 0);
		gbcContact.gridx = 0;
		gbcContact.gridy = 2;
		contentPanel.add(contactPanel, gbcContact);

		// Buttons
		JPanel buttonPanel = new JPanel();
		// buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		this.btnEdit = new JButton("Bearbeiten");
		this.btnEdit.setMnemonic('e');
		this.btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setEditModus(true);
				rootPane.setDefaultButton(btnSave);
			}
		});
		buttonPanel.add(btnEdit);

		buttonPanel.add(Box.createHorizontalGlue());

		this.btnSave = new JButton("Speichern");
		this.btnSave.setMnemonic('s');
		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// save
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
		rootPane.setDefaultButton(btnSave);
		buttonPanel.add(this.btnSave);

		btnClose = new JButton("Schließen");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		buttonPanel.add(btnClose);
	}

	/**
	 * Schaut ob alle Eingabefelder OK sind.
	 * 
	 * @return true, wenn alle Eingaben OK sind.
	 */
	protected boolean isAllInputValid() {
		return namePanel.isAllInputValid() && contactPanel.isAllInputValid();
	}

	/**
	 * Nimmt sich den aktuellen Inhalt der Felder, erzeugt einen Patienten und
	 * erstellt/speichert ihn.
	 */
	protected abstract boolean saveInput();

	/**
	 * Füllt die Labels und Felder mit den Daten der Person.
	 */
	protected void fillComponents() {
		String title;
		String vorname;
		String lastname;
		Contact contact;

		if (person == null) {
			title = "";
			vorname = "";
			lastname = "";
			contact = null;
		} else {
			title = person.getTitle();
			vorname = person.getFirstName();
			lastname = person.getLastName();

			contact = person.getContact();
		}

		namePanel.fillComponents(title, vorname, lastname);
		contactPanel.fillComponents(contact);
	}

	/**
	 * Setzt editModus auf editable und passt die Komponenten an.
	 * 
	 * @param editable
	 *            Bearbeiten-Modus ja oder nein
	 */
	public void setEditModus(boolean editable) {
		editModus = editable;

		if (editModus) {
			btnClose.setText("Abbrechen");
			btnClose.setMnemonic('a');
		} else {
			btnClose.setText("Schließen");
			btnClose.setMnemonic('c');
		}

		btnEdit.setVisible(!editable);

		btnSave.setVisible(editable);
		btnSave.setEnabled(isAllInputValid());

		namePanel.setEditModus(editable);
		contactPanel.setEditModus(editable);

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
		if ("checkButtons".equals(arg)) {
			btnSave.setEnabled(isAllInputValid());
		}
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);

		lblHeader.setText(title);
	}
}
