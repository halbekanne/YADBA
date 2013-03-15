/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.appointment;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import model.Course;

import controller.OfficeController;

/**
 * 
 * @author Stefan Noll
 * 
 */
public class KursBeitretenDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final OfficeController oc;
	private JButton okButton;
	private JButton cancelButton;
	private final KursBeitretenPanel contentPanel;

	/**
	 * Create the dialog.
	 */
	public KursBeitretenDialog(OfficeController octrl) {
		oc = octrl;
		contentPanel = new KursBeitretenPanel(oc);
		setTitle("Kurs beitreten");
		setModal(true);
		setBounds(100, 100, 551, 379);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Speichern");
				okButton.setMnemonic('s');
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (contentPanel.save()) {
							dispose();
						} else {
							fehler();
						}
					}
				});
				contentPanel.setOkButton(okButton);
				okButton.setEnabled(false);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Abbrechen");
				cancelButton.setMnemonic('a');
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public KursBeitretenDialog (OfficeController o, Course c){
		this(o);
		contentPanel.selectCourse(c);
	}

	public void fehler() {
		JOptionPane.showMessageDialog(null,
				"Der Patient konnte nicht zum Kurs hinzugefuegt werden",
				"Fehlermeldung", JOptionPane.WARNING_MESSAGE);
	}

	@Override
	protected JRootPane createRootPane() {
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}

}
