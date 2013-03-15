/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.firstrun;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;

import view.management.MitarbeiterFormular;
import view.management.OeffnungszeitenDialog;
import view.management.PraxisFormular;

import controller.OfficeController;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FirstrunDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final JButton btnPraxisdaten;
	private final JButton btnffnungszeiten;
	private final JButton btnPraxisleiter;
	private final JButton btnAbschlieen;
	private final JCheckBox chBxPraxisdaten;
	private final JCheckBox chBxffnungzeiten;
	private final JCheckBox chBxPraxisleiter;
	protected final OfficeController officeCtrl;

	/**
	 * Create the frame.
	 */
	public FirstrunDialog(OfficeController o) {
		setModal(true);
		officeCtrl = o;
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 237, 270);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel lblErstelleEineNeue = new JLabel("Erstelle eine neue Praxis:");

		btnPraxisdaten = new JButton("Praxisdaten");
		btnPraxisdaten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PraxisFormular form = new PraxisFormular(officeCtrl);
				form.setLocationRelativeTo(FirstrunDialog.this);
				form.setVisible(true);
				if (!officeCtrl.getOffice().getName().isEmpty()) {
					chBxPraxisdaten.setSelected(true);
					if (chBxffnungzeiten.isSelected()
							&& chBxPraxisdaten.isSelected()
							&& chBxPraxisleiter.isSelected())
						btnAbschlieen.setEnabled(true);
				}
			}
		});

		btnffnungszeiten = new JButton("Öffnungszeiten");
		btnffnungszeiten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OeffnungszeitenDialog form = new OeffnungszeitenDialog(
						officeCtrl);
				form.setLocationRelativeTo(FirstrunDialog.this);
				form.setVisible(true);
				boolean test = false;
				for (int i = 0; i < 7; i++) {
					if (officeCtrl.getOffice().getOpeningTime()[i] != null)
						test = true;
				}
				if (test) {
					chBxffnungzeiten.setSelected(true);
					if (chBxffnungzeiten.isSelected()
							&& chBxPraxisdaten.isSelected()
							&& chBxPraxisleiter.isSelected())
						btnAbschlieen.setEnabled(true);
				} else {
					chBxffnungzeiten.setSelected(false);
					btnAbschlieen.setEnabled(false);
				}
			}
		});

		btnPraxisleiter = new JButton("Praxisleiter");
		btnPraxisleiter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MitarbeiterFormular form = new MitarbeiterFormular(officeCtrl);
				form.setLocationRelativeTo(FirstrunDialog.this);
				form.setVisible(true);
				if (!officeCtrl.getOffice().getEmployees().isEmpty()) {
					chBxPraxisleiter.setSelected(true);
					if (chBxffnungzeiten.isSelected()
							&& chBxPraxisdaten.isSelected()
							&& chBxPraxisleiter.isSelected())
						btnAbschlieen.setEnabled(true);
				}
			}
		});

		btnAbschlieen = new JButton("Abschließen");
		btnAbschlieen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnAbschlieen.setToolTipText("Bitte erst alle Daten eingeben");
		btnAbschlieen.setEnabled(false);

		chBxffnungzeiten = new JCheckBox("");
		chBxffnungzeiten.setEnabled(false);

		chBxPraxisleiter = new JCheckBox("");
		chBxPraxisleiter.setEnabled(false);

		chBxPraxisdaten = new JCheckBox("");
		chBxPraxisdaten.setEnabled(false);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_contentPane.createSequentialGroup().addGap(7).addComponent(
						lblErstelleEineNeue, GroupLayout.PREFERRED_SIZE, 189,
						GroupLayout.PREFERRED_SIZE)).addGroup(
				gl_contentPane.createSequentialGroup().addGap(11).addComponent(
						btnPraxisdaten, GroupLayout.DEFAULT_SIZE, 177,
						Short.MAX_VALUE).addGap(8).addComponent(
						chBxPraxisdaten, GroupLayout.PREFERRED_SIZE, 28,
						GroupLayout.PREFERRED_SIZE)).addGroup(
				gl_contentPane.createSequentialGroup().addGap(11).addComponent(
						btnffnungszeiten, GroupLayout.DEFAULT_SIZE, 177,
						Short.MAX_VALUE).addGap(8).addComponent(
						chBxffnungzeiten, GroupLayout.PREFERRED_SIZE, 28,
						GroupLayout.PREFERRED_SIZE)).addGroup(
				gl_contentPane.createSequentialGroup().addGap(11).addComponent(
						btnPraxisleiter, GroupLayout.DEFAULT_SIZE, 177,
						Short.MAX_VALUE).addGap(8).addComponent(
						chBxPraxisleiter, GroupLayout.PREFERRED_SIZE, 28,
						GroupLayout.PREFERRED_SIZE)).addGroup(
				gl_contentPane.createSequentialGroup().addGap(11).addComponent(
						btnAbschlieen, GroupLayout.DEFAULT_SIZE, 196,
						Short.MAX_VALUE).addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_contentPane.createSequentialGroup().addGap(7).addComponent(
						lblErstelleEineNeue, GroupLayout.PREFERRED_SIZE, 39,
						GroupLayout.PREFERRED_SIZE).addGap(12).addGroup(
						gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnPraxisdaten).addComponent(
										chBxPraxisdaten,
										GroupLayout.PREFERRED_SIZE, 25,
										GroupLayout.PREFERRED_SIZE)).addGap(12)
						.addGroup(
								gl_contentPane.createParallelGroup(
										Alignment.LEADING).addComponent(
										btnffnungszeiten).addComponent(
										chBxffnungzeiten,
										GroupLayout.PREFERRED_SIZE, 25,
										GroupLayout.PREFERRED_SIZE)).addGap(12)
						.addGroup(
								gl_contentPane.createParallelGroup(
										Alignment.LEADING).addComponent(
										btnPraxisleiter).addComponent(
										chBxPraxisleiter,
										GroupLayout.PREFERRED_SIZE, 25,
										GroupLayout.PREFERRED_SIZE)).addGap(22)
						.addComponent(btnAbschlieen,
								GroupLayout.PREFERRED_SIZE, 41,
								GroupLayout.PREFERRED_SIZE).addContainerGap()));
		contentPane.setLayout(gl_contentPane);
	}
}
