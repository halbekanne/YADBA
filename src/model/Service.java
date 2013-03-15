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
 * Abstrakte Oberklasse für Activity (Behandlung) und Material. Enthält Namen und Preis.
 */
public abstract class Service implements Serializable {
	private static final long serialVersionUID = -6758755346671646282L;

	/**
	 * Der Preis der Leistung in Cent
	 */
	private int price;

	/**
	 * Der Name der Leistung
	 */
	private String name;
	
	public Service(String name, int price) {
		this.name = name;
		this.price = price;
	}

	/**
	 * Der Preis der Leistung.
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Setze den Preis der Leistung.
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * Der Name der Leistung.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setze den Namen der Leistung.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Testet, ob die Leistung formal in Ordnung ist, d.h. alle nötigen Werte enthalten sind
	 * @return true, falls alles OK ist, sonst false
	 */
	public boolean validService(){
		return name != null && !name.isEmpty() && price >= 0;
	}
	
	@Override
	public String toString(){
		return name;
	}

}
