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
 * Terminklasse für Urlaubs- bzw. Fehlzeiten allgemein. Erbt von Appointment und hat domit einen Zeitraum und einen Mitarbeiter.
 */
public class Vacation extends Appointment {
	private static final long serialVersionUID = -7542264006370089831L;

	/**
	 * Konstruktor für Urlaubstermin mit allen Paramtern
	 * @param time Zeitintervall des Urlaubs
	 * @param employee Mitarbeiter, der in Zrlaub fährt
	 */
	public Vacation(TimeInterval time, Employee employee) {
		super(time, employee);
	}

}
