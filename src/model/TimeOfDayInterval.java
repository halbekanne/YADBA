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
import java.util.Date;

/**
 * Ein Zeitintervall, das nur einen Bereich eines Tages abbildet.
 * 
 * Wird verwendet, um sich wiederholende Ereignisse abzubilden.
 */
public class TimeOfDayInterval implements Serializable {
	private static final long serialVersionUID = 8419172883100738099L;
	private TimeInterval t;

	/**
	 * Konstuiert neues TimeOfDayInterval aus einem TimeInterval
	 * Dazu werden die Daten auf ein Datum normalisiert, sodass nur noch Uhrzeiten verglichen werden
	 * @param t Ein TimeInterval bei dem die Uhrzeit des Startdatums vor der Uhrzeit des Enddatums liegt.
	 */
	public TimeOfDayInterval(TimeInterval t){
		this(t.getBegin(), t.getEnd());
	}
	
	/**
	 * Konstuiert neues TimeOfDayInterval aus einem Start- und einem Enddatumsobjekt 
	 * Dazu werden die Daten auf ein Datum normalisiert, sodass nur noch Uhrzeiten verglichen werden
	 * @param start Ein Datumsobjekt
	 * @param end Ein Datumsobjekt, dessen Uhrzeit hinter der von start liegt.
	 */
	public TimeOfDayInterval(Date start, Date end){
		start = CalendarUtil.standardizeDate(start);
		end = CalendarUtil.standardizeDate(end);
		try{
			this.t = new TimeInterval(start, end);
		}catch (IllegalArgumentException e){
			throw new IllegalArgumentException("Die Enduhrzeit eines TimeOfDayInterval muss vor der Startuhrzeit liegen!");
		}
	}

	/**
	 * Testet, ob die Uhrzeit eines gegebenen Dates innerhalb des Intervals liegt. 
	 * @param d Date, dessen Uhrzeit geprüft wird. Das Datum wird ignoriert!
	 * @return true, falls die Uhrzeit von d innerhalb des Intervals liegt
	 */
	public boolean contains(Date d) {
		d = CalendarUtil.standardizeDate(d);
		return t.contains(d);
	}

	/**
	 * Testet, ob die das übergebene Uhrzeitintervall innerhalbe dieses Uhrzeitintervalls liegt
	 * @param interval Ein TimeOfDayInterval
	 * @return true, falls interval innerhalb dieses Objekts liegt, sonst false
	 */
	public boolean contains(TimeOfDayInterval interval){
		return this.t.contains(interval.t);
	}

	/**
	 * Gibt die Anfangsuhrzeit als Date zurück. Das Datum ist normalisiert und hat keinerlei Bedeutung!
	 * @return Anfangsuhrzeit
	 */
	public Date getBegin(){
		return t.getBegin();
	}

	/**
	 * Gibt die Enduhrzeit als Date zurück. Das Datum ist normalisiert und hat keinerlei Bedeutung!
	 * @return Enduhrzeit
	 */
	public Date getEnd(){
		return t.getEnd();
	}

}
