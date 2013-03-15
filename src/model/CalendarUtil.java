/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Diverse Hilfsfunktionen zum Umgang mit Zeiten.
 */
public class CalendarUtil {

	private static Calendar c1 = Calendar.getInstance();
	private static Calendar c2 = Calendar.getInstance();
	private static Date DefaultDate = new Date(0);

	// Instanziierung verhindern.
	private CalendarUtil() {}	

	/**
	 * Gibt die gleiche Zeit am nächsten Tag zurück.
	 * @param t Der Zeitpunkt.
	 * @return Gleiche Zeit, Tag um eins erhöht.
	 */
	public static Date getNextDay(Date t) {
		c1.setTime(t); 
		c1.add(Calendar.DATE, 1);
		t = c1.getTime();
		return t;
	}
	
	/**
	 * Addiert <b>minuten</b> Minuten auf den Zeitpunkt.
	 * @param t Der Zeitpunkt.
	 * @param minutes Anzahl an Minuten.
	 * @return Das neue Datum, oder das alte Datum wenn minuten gleich 0.
	 */
	public static Date addMinutes(Date t, int minutes) {
		if (minutes == 0)
			return t;
		c1.setTime(t); 
		c1.add(Calendar.MINUTE, minutes);
		t = c1.getTime();
		return t;
	}
	
	/**
	 * Gibt zu einem gegebenen Date den nächsten Zeitpunkt im 5-Minuten-Raster zurück
	 * @param t Ein Datum
	 * @return Der nächste Zeitpunkt im 5-Minuten-Raster
	 */
	public static Date getNextSlot(Date t){
		c1.setTime(t);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		int minutes = c1.get(Calendar.MINUTE);
		if (minutes%5 != 0)
			c1.add(Calendar.MINUTE, (5 - minutes%5));
		return c1.getTime();
	}
	
	/**
	 * Gibt zu einem gegebenen Date den letzten Zeitpunkt im 5-Minuten-Raster zurück
	 * @param t Ein Datum
	 * @return Der letzten Zeitpunkt im 5-Minuten-Raster
	 */
	public static Date getPrevSlot(Date t){
		c1.setTime(t);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		int minutes = c1.get(Calendar.MINUTE);
		if (minutes%5 != 0)
			c1.add(Calendar.MINUTE, (-1) * (minutes%5));
		return c1.getTime();
	}
	
	

	/**
	 * Gibt den Wochentag zurück.
	 * 
	 * Montag = 0, Dienstag = 1, ..., Sonntag = 6
	 * 
	 * @param t Der Zeitpunkt.
	 * @return Wochentag als Zahl.
	 */
	public static int getDayOfWeek(Date t) {
		c1.setTime(t);
		c1.add(Calendar.DAY_OF_MONTH, -1);
		return (c1.get(Calendar.DAY_OF_WEEK)-1)%7; // Calendar fängt mit Sonntag=1 an, wir mit Montag=0
	}

	/**
	 * Prüft, ob zwei Zeitpunkte im selben Tag liegen.
	 * @return true, wenn das Datum identisch ist.
	 */
	public static boolean sameDay(Date t1, Date t2) {
		c1.setTime(t1);
		c2.setTime(t2);
		return (c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR)) 
		&& (c1.get(Calendar.MONTH)==c2.get(Calendar.MONTH))
		&& (c1.get(Calendar.DAY_OF_MONTH)==c2.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Kombiniert das Datum <b>date</b> mit der Zeit aus <b>time</b> zu einem neuen Date.
	 * @param date Date aus dem das Datum übernommen wird.
	 * @param time Date aus dem die Zeit übernommen wird.
	 * @return Das kombinierte Date.
	 */
	public static Date combineDateTime(Date date, Date time) {
		c1.setTime(date);

		c2.setTime(time);

		Calendar result = Calendar.getInstance();
		result.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DATE), 
				c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE), c2.get(Calendar.SECOND));
		result.set(Calendar.MILLISECOND, 0); // Setze Millisekunden zurück.
		return result.getTime();
	}

	/**
	 * Gibt die Zeit aus dem Date <b>t</b> in der Form <i>hh:mm</i> zurück.
	 */
	public static String getHourMinuteString(Date t) {
		c1.setTime(t);
		String hour = c1.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + c1.get(Calendar.HOUR_OF_DAY) : Integer.toString(c1.get(Calendar.HOUR_OF_DAY));
		String minute = c1.get(Calendar.MINUTE) < 10 ? "0" + c1.get(Calendar.MINUTE) : Integer.toString(c1.get(Calendar.MINUTE));
		return hour + ":" + minute;
	}

	/**
	 * Gibt ein Datum zurück, wo nur Stunden und Minuten signifikant sind.
	 */
	public static Date getDefaultDate(int hours, int minutes) {
		c1.setTime(DefaultDate);
		c1.set(Calendar.HOUR_OF_DAY, hours);
		c1.set(Calendar.MINUTE, minutes);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		return c1.getTime();
	}

	/**
	 * Parst eine Zeitangabe der Form <i>hh:mm</i> in ein Date.
	 * @return Das Date, wenn das Parsen erfolgreich ist, sonst null.
	 */
	public static Date getDateByHoursMinutesString(String hoursMinutes) {
		String[] timeParts = hoursMinutes.split(":");
		if (timeParts.length == 2) {
			int hours = Integer.parseInt(timeParts[0]);
			int minutes = Integer.parseInt(timeParts[1]);
			return getDefaultDate(hours, minutes);
		}
		return null;
	}

	/**
	 * Entfernt Datumsanteile aus <b>t</b>, nur Stunde und Minute bleiben.
	 */
	public static Date standardizeDate(Date t) {
		c2.setTime(t);
		return getDefaultDate(c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE));
	}
	
	/**
	 * Entfernt Datumsanteile aus <b>t</b>, nur Stunde und Minute bleiben.
	 */
	public static Date standardizeTime(Date t) {
		c1.setTime(t);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		return c1.getTime();
	}
	
	/**
	 * Gibt ein Zeitinterval für einen Tag zurück, entsprechend des übergebenen Date Parameters.
	 * @param t
	 * @return
	 */
	public static TimeInterval createDayTimeInterval(Date t) {
		c1.setTime(t);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		TimeInterval timeInt = new TimeInterval(c1.getTime(), 24*60);
		return timeInt;
	}

	/**
	 * Erhöht den Tag des Datums um den festgelegten Betrag und gibt das Datum zurück.
	 * @param t
	 * @return
	 */
	public static Date increaseDay(Date t, int days) {
		c1.setTime(t);
		c1.add(Calendar.DAY_OF_MONTH, days);
		return c1.getTime();
	}
	
	public static String dateToString(Date d){
		c1.setTime(d);
		Date date = c1.getTime();
		String strDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		strDate = sdf.format(date);
		return strDate;
	}
	
	public static String dateTimeToString(Date d){
		c1.setTime(d);
		Date date = c1.getTime();
		String strDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		strDate = sdf.format(date);
		return strDate;
	}
	
	public static String dateTimeToStringNormed(Date d){
		c1.setTime(d);
		Date date = c1.getTime();
		String strDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
		strDate = sdf.format(date);
		return strDate;
	}
	
	public static int getMinutesInDay(Date d) {
		c1.setTime(d);
		return c1.get(Calendar.HOUR_OF_DAY) * 60 + c1.get(Calendar.MINUTE);
	}
}
