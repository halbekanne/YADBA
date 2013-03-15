/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package model;

/**
 * Material, dass an Patienten verkauft werden kann. Erbt von Service und hat somit Preis und Namen.
 */
public class Material extends Service {
	private static final long serialVersionUID = -7025100382918217565L;

	/**
	 * Erzeugt neues Material mit allen Parametern
	 * @param name Der Name des Materials
	 * @param price Der Preis des Materials
	 */
	public Material(String name, int price){
		super(name, price);
	}
}
