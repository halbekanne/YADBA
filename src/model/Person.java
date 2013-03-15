/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package model;

import java.io.Serializable;

/**
 * Oberklasse für Personen. Enthält Vorname, Nachname, Anrede und Kontaktdaten
 */
public class Person implements Serializable {
	private static final long serialVersionUID = 7930774279068548355L;

	/**
	 * Kontaktdaten der Person
	 */
	protected Contact contact;

	/**
	 * Vorname der Person
	 */
	protected String firstName;

	/**
	 * Nachname der Person
	 */
	protected String lastName;

	/**
	 * Anrede
	 */
	protected String title;
	
	/**
	 * @param contact
	 * @param firstName
	 * @param lastName
	 * @param title
	 */
	public Person(Contact contact, String firstName, String lastName,
			String title) {
		super();
		this.contact = contact;
		this.firstName = firstName;
		this.lastName = lastName;
		this.title = title;
	}

	/**
	 * Kontaktdaten
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * Setze die Kontaktdaten.
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * Vorname
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setze Vornamen.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Nachname
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setze den Nachnamen.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gibe den Namen in der Form "Nachname, Vorname" als String zurück.
	 */
	@Override
	public String toString() {
		return lastName+", "+firstName;
	}
	
	/**
	 * Anrede
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setze die Anrede.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Testet, ob die Person formal korrekt ist, d. h. alle Referenzen != null sind und der Name nicht leer ist
	 * @return true, wenn alles in Ordnung ist, sonst false
	 */
	public boolean validPerson(){
		return firstName!=null&&lastName!=null&&!firstName.isEmpty()&&!lastName.isEmpty()&&contact!=null;
	}


}
