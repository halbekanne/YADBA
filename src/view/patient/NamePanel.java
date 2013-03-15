/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.patient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import view.util.TextFieldWithWarning;

/**
 * Ein Panel um Anrede, Vorname & Nachname anzuzeigen und zu bearbeiten.
 * @author Alex
 */
public class NamePanel extends JPanel {

	/** default id damit Eclipse ruhig ist */
	private static final long serialVersionUID = 1L;
	
	protected final JLabel lblSalutation;
	protected final JComboBox<String> cmbTitle;

	protected final JLabel lblFirstName;
	protected final TextFieldWithWarning textFirstName;

	protected final JLabel lblLastName;
	protected final TextFieldWithWarning textLastName;

	private Observer observer;
	
	protected final class InformObserverOnInput implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			observer.update(null, "checkButtons");
		}

		@Override
		public void keyReleased(KeyEvent e) {
			observer.update(null, "checkButtons");
		}

		@Override
		public void keyPressed(KeyEvent e) {
			observer.update(null, "checkButtons");
		}
	}
	
	/**
	 * Erstellt einen neues Panel.
	 * @param buttonUpdate Ein Keylistener der die Felder &uuml;berwacht.
	 */
	public NamePanel(Observer observer) {
		super();
		
		this.observer = observer;
		
		KeyListener keyListener = new InformObserverOnInput();

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {100, 0};
		layout.columnWeights = new double[] {0.0, 1.0};
		setLayout(layout);
		
		addRowHeader("Anrede:", 0);
	
		this.lblSalutation = new JLabel();
		this.cmbTitle = new JComboBox<String>(new String[] { "Frau", "Herr",
				"Dr.", "Prof." });
		GridBagConstraints gbcAnrede = new GridBagConstraints();
		gbcAnrede.gridwidth = 2;
		gbcAnrede.fill = GridBagConstraints.HORIZONTAL;
		gbcAnrede.anchor = GridBagConstraints.NORTHWEST;
		gbcAnrede.insets = new Insets(0, 0, 5, 0);
		gbcAnrede.gridx = 1;
		gbcAnrede.gridy = 0;
		add(this.lblSalutation, gbcAnrede);
		add(this.cmbTitle, gbcAnrede);
	
		addRowHeader("Vorname:", 1);
	
		this.lblFirstName = new JLabel();
		this.textFirstName = new TextFieldWithWarning("", false);
		this.textFirstName.addKeyListener(keyListener);
		GridBagConstraints gbcVorname = new GridBagConstraints();
		gbcVorname.gridwidth = 2;
		gbcVorname.fill = GridBagConstraints.HORIZONTAL;
		gbcVorname.anchor = GridBagConstraints.NORTHWEST;
		gbcVorname.insets = new Insets(0, 0, 5, 0);
		gbcVorname.gridx = 1;
		gbcVorname.gridy = 1;
		add(this.lblFirstName, gbcVorname);
		add(this.textFirstName, gbcVorname);
	
		addRowHeader("Nachname:", 2);
	
		this.lblLastName = new JLabel();
		this.textLastName = new TextFieldWithWarning("", false);
		this.textLastName.addKeyListener(keyListener);
		GridBagConstraints gbcNachname = new GridBagConstraints();
		gbcNachname.gridwidth = 2;
		gbcNachname.fill = GridBagConstraints.HORIZONTAL;
		gbcNachname.anchor = GridBagConstraints.NORTHWEST;
		gbcNachname.insets = new Insets(0, 0, 5, 0);
		gbcNachname.gridx = 1;
		gbcNachname.gridy = 2;
		gbcNachname.weighty = 1.0;
		add(this.lblLastName, gbcNachname);
		add(this.textLastName, gbcNachname);
	}
	
	/**
	 * Fügt in die 1. Spalte des JPanel mit GridBagLayout ein Label mit dem Text
	 * in die row-te Zeile.
	 * 
	 * @param panel
	 *            Das Panel in das das Label kommt.
	 * @param text
	 *            Der Text des Labels.
	 * @param row
	 *            Die gewünschte Zeile.
	 */
	private void addRowHeader(String text, int row) {
		final JLabel label = new JLabel(text);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = row;
		
		add(label, gbc);
	}
	
	/**
	 * Füllt die Labels und Felder mit den Daten der Person.
	 * @param title Die Anrede der Person.
	 * @param firstName Der Vorname der Person.
	 * @param lastName Der Nachname der Person.
	 */
	public void fillComponents(String title, String firstName, String lastName) {
		lblSalutation.setText(title);
		cmbTitle.setSelectedItem(title);

		lblFirstName.setText(firstName);
		textFirstName.setText(firstName);

		lblLastName.setText(lastName);
		textLastName.setText(lastName);
	}
	
	/**
	 * Setzt editModus auf editable und passt die Komponenten an.
	 * @param editable Bearbeiten-Modus ja oder nein
	 */
	public void setEditModus(boolean editable) {
		lblSalutation.setVisible(!editable);
		cmbTitle.setVisible(editable);
		
		lblLastName.setVisible(!editable);
		textLastName.setVisible(editable);

		lblFirstName.setVisible(!editable);
		textFirstName.setVisible(editable);
	}

	/**
	 * Schaut ob alle Eingabefelder OK sind.
	 * @return true, wenn alle Eingaben OK sind.
	 */
	public boolean isAllInputValid() {
		return textFirstName.hasValidInput() && textLastName.hasValidInput();
	}

	/**
	 * Gibt die ausgew&auml;hlte Anrede zur&uuml;ck.
	 * @return Die Anrede.
	 */
	public String getTitle() {
		return (String) cmbTitle.getSelectedItem();
	}
	
	/**
	 * Gibt den eingegeben Vornamen zur&uuml;ck.
	 * @return Der Vorname (kann leer sein).
	 */
	public String getFirstName() {
		return textFirstName.getText();
	}
	
	/**
	 * Gibt den eingegeben Nachnamen zur&uuml;ck.
	 * @return Der Nachname (kann leer sein).
	 */
	public String getLastName() {
		return textLastName.getText();
	}
}
