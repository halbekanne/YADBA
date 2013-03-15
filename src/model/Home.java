/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package model;

import java.util.List;

/**
 * Klasse für den "Raum", in dem Hausbesuche stattfinden. Ist immer frei.
 */
public class Home extends Room {
	private static final long serialVersionUID = -4392749436863036750L;

	/**
	 * Konstruktor für Haus. Name wird fest auf "Haus", Kapazität auf 1 gesetzt.
	 * @param activity Die in dem Haus möglichen Behandlungen.
	 */
	public Home(List<Activity> activity) {
		super("Hausbesuch", 1, activity);
	}

	
	@Override
	public boolean isAvailable(TimeInterval t) {
		return true;
	}

}
