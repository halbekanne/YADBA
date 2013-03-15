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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Contact;
import view.util.RegExpFormatter;
import view.util.TextFieldWithWarning;

/**
 * Ein Panel um Kontaktdaten anzuzeigen und zu bearbeiten.
 * @author Alex
 */
public class ContactPanel extends JPanel {

	/** default id damit Eclipse ruhig ist */
	private static final long serialVersionUID = 1L;
	
	protected final JLabel lblStreet;
	protected final JTextField textStreet;

	protected final JLabel lblPlz;
	protected final TextFieldWithWarning textPlz;

	protected final JLabel lblCity;
	protected final JTextField textCity;

	protected final JLabel lblPhone;
	protected final TextFieldWithWarning textPhone;

	protected final JLabel lblMobile;
	protected final TextFieldWithWarning textMobile;

	protected final JLabel lblEmail;
	protected final TextFieldWithWarning textEmail;

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
	public ContactPanel(Observer observer) {
		super();
		
		this.observer = observer;
		
		KeyListener keyListener = new InformObserverOnInput();
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {100, 80, 0};
		layout.columnWeights = new double[] {0.0, 0.1, 1.0};
		layout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0};
		setLayout(layout);
		
		addRowHeader("Straße:", 0);
	
		this.lblStreet = new JLabel();
		this.textStreet = new JTextField();
		GridBagConstraints gbcStrasse = new GridBagConstraints();
		gbcStrasse.insets = new Insets(0, 0, 5, 0);
		gbcStrasse.gridwidth = 2;
		gbcStrasse.fill = GridBagConstraints.HORIZONTAL;
		gbcStrasse.anchor = GridBagConstraints.NORTHWEST;
		gbcStrasse.gridx = 1;
		gbcStrasse.gridy = 0;
		add(this.lblStreet, gbcStrasse);
		add(this.textStreet, gbcStrasse);
	
		addRowHeader("Ort:", 1);
	
		this.lblPlz = new JLabel();
		this.textPlz = new TextFieldWithWarning(RegExpFormatter.PLZ_PATTERN);
		this.textPlz.addKeyListener(keyListener);
	
		GridBagConstraints gbcPlz = new GridBagConstraints();
		gbcPlz.fill = GridBagConstraints.HORIZONTAL;
		gbcPlz.anchor = GridBagConstraints.NORTHWEST;
		gbcPlz.insets = new Insets(0, 0, 5, 5);
		gbcPlz.gridx = 1;
		gbcPlz.gridy = 1;
		add(this.lblPlz, gbcPlz);
		add(this.textPlz, gbcPlz);
	
		this.lblCity = new JLabel();
		this.textCity = new JTextField();
		GridBagConstraints gbcStadt = new GridBagConstraints();
		gbcStadt.fill = GridBagConstraints.HORIZONTAL;
		gbcStadt.anchor = GridBagConstraints.NORTHWEST;
		gbcStadt.insets = new Insets(0, 0, 5, 0);
		gbcStadt.gridx = 2;
		gbcStadt.gridy = 1;
		add(this.lblCity, gbcStadt);
		add(this.textCity, gbcStadt);
	
		addRowHeader("Telefon:", 2);
	
		this.lblPhone = new JLabel();
		this.textPhone = new TextFieldWithWarning(
				RegExpFormatter.PHONE_PATTERN);
		this.textPhone.addKeyListener(keyListener);
		GridBagConstraints gbcTelefon = new GridBagConstraints();
		gbcTelefon.fill = GridBagConstraints.HORIZONTAL;
		gbcTelefon.anchor = GridBagConstraints.NORTHWEST;
		gbcTelefon.insets = new Insets(0, 0, 5, 0);
		gbcTelefon.gridwidth = 2;
		gbcTelefon.gridx = 1;
		gbcTelefon.gridy = 2;
		add(this.lblPhone, gbcTelefon);
		add(this.textPhone, gbcTelefon);
	
		addRowHeader("Handy:", 3);
	
		this.lblMobile = new JLabel();
		this.textMobile = new TextFieldWithWarning(RegExpFormatter.MOBILE_PATTERN);
		this.textMobile.addKeyListener(keyListener);
		GridBagConstraints gbcHandy = new GridBagConstraints();
		gbcHandy.fill = GridBagConstraints.HORIZONTAL;
		gbcHandy.anchor = GridBagConstraints.WEST;
		gbcHandy.insets = new Insets(0, 0, 5, 0);
		gbcHandy.gridwidth = 2;
		gbcHandy.gridx = 1;
		gbcHandy.gridy = 3;
		add(this.lblMobile, gbcHandy);
		add(this.textMobile, gbcHandy);
	
		addRowHeader("E-Mail:", 4);
	
		this.lblEmail = new JLabel();
		this.textEmail = new TextFieldWithWarning(RegExpFormatter.EMAIL_PATTERN);
		this.textEmail.addKeyListener(keyListener);
		GridBagConstraints gbcEmail = new GridBagConstraints();
		gbcEmail.fill = GridBagConstraints.HORIZONTAL;
		gbcEmail.anchor = GridBagConstraints.NORTHWEST;
		gbcEmail.insets = new Insets(0, 0, 5, 0);
		gbcEmail.gridwidth = 2;
		gbcEmail.gridx = 1;
		gbcEmail.gridy = 4;
		add(this.lblEmail, gbcEmail);
		add(this.textEmail, gbcEmail);
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
	 * @param contact Die Kontaktinformationen mit denen die Felder gef&uuml;lt werden.
	 */
	public void fillComponents(Contact contact) {
		String street;
		String plz;
		String city;
		String phone;
		String mobile;
		String email;
		
		if (contact == null) {
			street = "";
			plz = "";
			city = "";
			phone = "";
			mobile = "";
			email = "";
		} else {
			street = contact.getStreet();
			plz = contact.getZipCode();
			city = contact.getCity();
			phone = contact.getPhone();
			mobile = contact.getMobile();
			email = contact.getEmail();
		}
		
		lblStreet.setText(street);
		textStreet.setText(street);

		lblPlz.setText(plz);
		textPlz.setValue(plz);

		lblCity.setText(city);
		textCity.setText(city);

		lblPhone.setText(phone);
		textPhone.setValue(phone);

		lblMobile.setText(mobile);
		textMobile.setValue(mobile);

		lblEmail.setText(email);
		textEmail.setValue(email);
	}
	
	/**
	 * Setzt editModus auf editable und passt die Komponenten an.
	 * @param editable Bearbeiten-Modus ja oder nein
	 */
	public void setEditModus(boolean editable) {
		lblStreet.setVisible(!editable);
		textStreet.setVisible(editable);

		lblPlz.setVisible(!editable);
		textPlz.setVisible(editable);

		lblCity.setVisible(!editable);
		textCity.setVisible(editable);

		lblPhone.setVisible(!editable);
		textPhone.setVisible(editable);

		lblMobile.setVisible(!editable);
		textMobile.setVisible(editable);

		lblEmail.setVisible(!editable);
		textEmail.setVisible(editable);
	}

	/**
	 * Schaut ob alle Eingabefelder OK sind.
	 * @return true, wenn alle Eingaben OK sind.
	 */
	public boolean isAllInputValid() {
		return textPlz.hasValidInput() && textPhone.hasValidInput() && textMobile.hasValidInput() && textEmail.hasValidInput();
	}
	
	/**
	 * Liest die Daten aus den Felder aus und erstellet daraus ein Kontaktdaten-Objekt.
	 * @return Die Kontaktdaten, nicht gepr&uuml;ft.
	 */
	public Contact getContact() {
		return new Contact(textStreet.getText(),
				textPlz.getText(),
				textCity.getText(),
				textPhone.getText(),
				textMobile.getText(),
				textEmail.getText());
	}
}
