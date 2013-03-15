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
import java.util.Calendar;
import java.util.Date;

/** 
 * Represents an interval of two points in time. 
 * @author Benjamin Kramer
 */
public class TimeInterval implements Serializable {
	private static final long serialVersionUID = 4455675367205376771L;

	private Date begin;
	private Date end;

	/**
	 * Constructs a new TimeInterval [begin, end).
	 * @throws IllegalArgumentException if end is before begin.
	 */
	public TimeInterval(Date begin, Date end) {
		if (end.before(begin))
			throw new IllegalArgumentException("Invalid interval");
		this.begin = begin;
		this.end = end;
	}

	/**
	 * Constructs a new TimeInterval [begin, begin+minutes]
	 * @param minutes a number of minutes, determining the length of this interval. 
	 * @throws IllegalArgumentException if begin+minutes is before begin.
	 */
	public TimeInterval(Date begin, int minutes) {
		if (minutes < 0)
			throw new IllegalArgumentException("Invalid interval");
		this.begin = begin;
		this.end = CalendarUtil.addMinutes(begin, minutes);
	}

	/**
	 * Get the first time stamp in this interval.
	 */
	public Date getBegin() {
		return begin;
	}

	/**
	 * Get the last time stamp in this interval.
	 */
	public Date getEnd() {
		return end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((begin == null) ? 0 : begin.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeInterval other = (TimeInterval) obj;
		if (begin == null) {
			if (other.begin != null)
				return false;
		} else if (!begin.equals(other.begin))
			return false;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + begin + ", " + end + ")";
	}

	/**
	 * Get the length of this interval in minutes.
	 */
	public long getDurationInMinutes() {
		return (end.getTime() - begin.getTime()) / (1000*60);
	}

	/**
	 * Checks whether <b>d</b> is contained within this interval.
	 */
	public boolean contains(Date d) {
		return !begin.after(d) && !end.before(d);
	}
	
	/**
	 * Testet, ob ein übergebenes Zeitintervall in diesem Zeitintervall enthalten ist
	 * @param t Das übergebene Zeitintercall
	 * @return true, falls t in diesem Zeitintervall enthalten ist
	 */
	public boolean contains(TimeInterval t){
		return this.contains(t.begin) && this.contains(t.end);
	}

	/**
	 * Checks whether the interval <b>t</b> overlaps with this interval.
	 */
	public boolean overlapsWith(TimeInterval t) {
		assert !begin.after(end) && !t.begin.after(t.end);
		// end > t.begin && begin < t.end
		return end.after(t.begin) && begin.before(t.end);
	}
	
	/**
	 * Vergleicht das Zeitintervall mit der aktuellen Systemzeit
	 * @return 1, falls das Intervall in der Zukunft liegt, 0, falls es gerade stattfindet, -1, falls es in der Vergangenheit liegt
	 */
	public int compareToNow(){
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		if (now.before(begin)){ //Zeitintervall liegt in der Zukunft
			return 1;
		} else if (this.contains(now)){ //Zeitintervall liegt in der Gegenwart
			return 0;
		} else { // Zeitintervall liegt in der Vergangenheit
			return -1;
		}
	}
}
