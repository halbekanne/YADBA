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
 * Rang eines Mitarbeiters.
 */
public enum Rank implements Serializable {
	/** Chefin */
	BOSS("Chef"),
	/** Stellvertreter der Chefin */
	REPLACEMENT("Stellvertreter"),
	/** Normaler Mitarbeiter */
	EMPLOYEE("Mitarbeiter");
	
	/** der Name der zum Anzeigen verwendet wird. */
	private String viewName;

	/**
	 * Erstellt den Eintrag.
	 * @param viewString Der zu verwendende Anzeigenname.
	 */
	private Rank(String viewString) {
		this.viewName = viewString;
	}
	
	/**
	 * Gibt den Anzeigenname zur&uuml;ck.
	 * @return Der Name der zum Anzeigen verwendet werden sollte.
	 */
	public String getViewName() {
		return viewName;
	}
}
