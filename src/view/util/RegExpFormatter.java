/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.util;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.DefaultFormatter;

/**
 * Formatter für FormattedTexFields der mit Regulärenausrücken arbeitet.
 * 
 * @author Alex
 */
public class RegExpFormatter extends DefaultFormatter {

	/** default id damit Eclipse ruhig ist. */
	private static final long serialVersionUID = 1L;

	/** DAS Muster. */
	private final Pattern muster;

	/** sind leere Inputs OK */
	private final boolean allowEmpty;

	/** Regulärerausdruck für PLZs. */
	public static final String PLZ_PATTERN = "^\\d{5}$";

	/** Regulärerausdruck für Telfonnr. */
	public static final String PHONE_PATTERN = "^[0-9\\+\\/\\(\\) \\-]{5,30}$";

	/** Regulärerausdruck für Handynr. */
	public static final String MOBILE_PATTERN = "^[0-9\\+\\/\\(\\) \\-]{5,30}$";

	/** Regulärerausdruck für E-Mails */
	public static final String EMAIL_PATTERN = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";

	/**
	 * Erstellt ein Objekt mit dem Ausdruck und erlaubten leeren Input.
	 * 
	 * @param regexp
	 *            Der reguläre Ausdruck. Ist der leer, wird nicht weiter
	 *            &uuml;berpr&uuml;ft.
	 */
	public RegExpFormatter(String regexp) {
		this(regexp, true);
	}

	/**
	 * Erstllet ein Objekt mit dem Ausdruck und erlaubt/verbietet leeren Input.
	 * 
	 * @param regexp
	 *            Der reguläre Ausdruck. Ist der leer, wird nicht weiter
	 *            &uuml;berpr&uuml;ft.
	 * @param allowEmptyInput
	 *            true == leerer Input wird akzeptiert.
	 */
	public RegExpFormatter(String regexp, boolean allowEmptyInput) {
		this.muster = Pattern.compile(regexp);
		this.allowEmpty = allowEmptyInput;
		setOverwriteMode(false);
	}

	@Override
	public Object stringToValue(String input) throws ParseException {
		input = input.trim();

		// ist der Input leer und sollte es nicht sein?
		if (!allowEmpty && input.isEmpty()) {
			throw new ParseException(
					"Eine leerere Eingabe ist nicht zugelassen!", 0);
		}

		// leeres Muster -> Input OK, leerer Input ist hier erlaubt -> Input OK
		if (muster.pattern().isEmpty() || input.isEmpty()) {
			return input;
		}

		// das eigentliche testen
		Matcher matcher = muster.matcher(input);
		if (matcher.matches()) {
			return input;
		} else {
			throw new ParseException("Ihre Eingabe ist nicht gültig!", 0);
		}
	}

}
