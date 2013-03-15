/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.util;

import java.awt.Color;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.JXFormattedTextField;
import org.jdesktop.swingx.prompt.BuddySupport;

/**
 * Ein Textfeld, das &uuml;ber regul&auml;re Ausdr&uuml;cke getestet werden kann
 * und ein '!' als Warnung anzeigt.
 * 
 * @author Alex
 */
public class TextFieldWithWarning extends JXFormattedTextField {

	/** default id damit Eclipse ruhig ist */
	private static final long serialVersionUID = 1L;

	// das !
	private final JLabel lblWarning;

	private final class WarningDocumentListener implements DocumentListener {
		@Override
		public void changedUpdate(DocumentEvent e) {
			doUpdate();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			doUpdate();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			doUpdate();
		}
	}

	/**
	 * Default constructor.
	 * 
	 * @param regexp
	 *            Der reguläre Ausdruck auf den der Inhalt geprüft wird.
	 */
	public TextFieldWithWarning(final String regexp) {
		this(regexp, true);
	}

	/**
	 * Default constructor.
	 * 
	 * @param regexp
	 *            Der reguläre Ausdruck auf den der Inhalt geprüft wird.
	 */
	public TextFieldWithWarning(final String regexp,
			final boolean allowEmptyInput) {
		this.lblWarning = new JLabel("!");
		addBuddy(lblWarning, BuddySupport.Position.RIGHT);
		this.lblWarning.setVisible(false);

		setFocusLostBehavior(JFormattedTextField.COMMIT);

		setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {
			@Override
			public AbstractFormatter getFormatter(JFormattedTextField ftf) {
				return new RegExpFormatter(regexp, allowEmptyInput);
			}
		});

		getDocument().addDocumentListener(new WarningDocumentListener());

		// Ausrufezeichen schon zu begin anzeigen
		doUpdate();
	}

	/**
	 * Das Ausrufezeichen anzeigen oder verstecken.
	 */
	private void doUpdate() {
		try {
			commitEdit();
			this.setBackground(Color.white);
			lblWarning.setVisible(false);
			lblWarning.setToolTipText("");
		} catch (ParseException e) {
			this.setBackground(new Color(208, 107, 100));
			lblWarning.setVisible(true);
			lblWarning.setToolTipText(e.getMessage());
		}
	}

	/**
	 * Abfrage ob der Inhalt den Erwartungen entspricht.
	 * 
	 * @return ture == ja.
	 */
	public boolean hasValidInput() {
		return !lblWarning.isVisible();
	}
}
