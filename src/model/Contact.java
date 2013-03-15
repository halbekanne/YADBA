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
 * Klasse für Kontaktdaten einer Person mit Anschrift, Festnetznummer, Mobilnummer und E-Mail Adresse
 */
public class Contact implements Serializable {
	private static final long serialVersionUID = 3071309617304362760L;

	/**
	 * Straße
	 */
	private String street;

	/**
	 * Postleitzahl
	 */
	private String zipCode;

	/**
	 * Stadt
	 */
	private String city;

	/**
	 * Festnetznummer
	 */
	private String phone;

	/**
	 * Mobiltelefonnummer
	 */
	private String mobile;

	/**
	 * E-Mail Adresse
	 */
	private String email;

	/**
	 * @return Die Straße
	 */
	
	/**
	 * Konstruktor für Kontaktdaten mit allen Parametern
	 * @param street Straße
	 * @param zip Postleitzahl
	 * @param city Stadt
	 * @param phone Festnetznummer
	 * @param mobile Mobiltelefonnummer
	 * @param email E-Mail Adresse
	 */
	public Contact(String street, String zip, String city, String phone,
			String mobile, String email) {
		this.street = street;
		this.zipCode = zip;
		this.city = city;
		this.phone = phone;
		this.mobile = mobile;
		this.email = email;
	}
	
	/**
	 * Straße.
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * Setze die Straße.
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * Postleitzahl.
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * Setze die Postleitzahl.
	 */
	public void setZipCode(String zip) {
		this.zipCode = zip;
	}

	/**
	 * Stadt.
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Setze die Stadt.
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Festnetznummer.
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Setze die Festnetznummer.
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Mobiltelefonnummer.
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * Setze die Mobiltelefonnummer.
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * E-Mail Adresse.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setze die E-Mail Adresse.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}
