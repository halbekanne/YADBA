/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.management;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;

import model.Appointment;
import model.CalendarUtil;
import model.Employee;
import model.TimeInterval;
import model.Vacation;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import controller.OfficeController;

public class FehlzeitenDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private final OfficeController officeCtrl;

	private final Employee employee;

	private JSpinner spinnerStart;
	private JSpinner spinnerEnd;

	private final JList<Vacation> list;

	/**
	 * Create the dialog.
	 */
	public FehlzeitenDialog(OfficeController o, Employee e) {
		this.officeCtrl = o;
		this.employee = e;
		setBounds(100, 100, 362, 450);
		this.setModal(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] { FormFactory.UNRELATED_GAP_COLSPEC,
						ColumnSpec.decode("166px"),
						FormFactory.UNRELATED_GAP_COLSPEC,
						ColumnSpec.decode("149px"), }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.UNRELATED_GAP_ROWSPEC,
						RowSpec.decode("207px"),
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.UNRELATED_GAP_ROWSPEC,
						RowSpec.decode("20px"),
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));

		JLabel lblFehlzeitenHerrMustermann = new JLabel("Fehlzeiten von "
				+ employee.getFirstName() + " " + employee.getLastName());
		contentPanel.add(lblFehlzeitenHerrMustermann, "2, 2");

		this.list = new JList<Vacation>();
		list.setModel(new DefaultListModel<Vacation>() {

			private static final long serialVersionUID = 1L;
			LinkedList<Vacation> fehlzeiten;

			@Override
			public int getSize() {
				loadFehlzeiten();
				return fehlzeiten.size();
			}

			@Override
			public Vacation getElementAt(int index) {
				loadFehlzeiten();
				return fehlzeiten.get(index);
			}

			public void loadFehlzeiten() {
				fehlzeiten = new LinkedList<Vacation>();
				for (Appointment a : employee.getAppointments()) {
					if (a instanceof Vacation) {
						fehlzeiten.add((Vacation) a);
					}
				}
				this.fireContentsChanged(new String("Trololol"), 0,
						fehlzeiten.size());
			}
		});
		contentPanel.add(list, "1, 4, 4, 1, fill, fill");

		JButton btnFehlzeitLschen = new JButton("Fehlzeit löschen");
		btnFehlzeitLschen.setMnemonic('l');
		btnFehlzeitLschen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() < 0) {
					return;
				}
				Vacation v = list.getModel().getElementAt(
						list.getSelectedIndex());
				officeCtrl.getAppointmentController().removeVacation(v);
				list.getModel().getSize();
			}
		});
		contentPanel.add(btnFehlzeitLschen, "4, 6");

		JSeparator separator = new JSeparator();
		contentPanel.add(separator, "2, 8, 3, 1");

		JLabel lblStartzeitpunkt = new JLabel("Startzeitpunkt");
		contentPanel.add(lblStartzeitpunkt, "2, 10");

		spinnerStart = new JSpinner();
		spinnerStart.setModel(new SpinnerDateModel(Calendar.getInstance()
				.getTime(), null, null, Calendar.MINUTE));
		contentPanel.add(spinnerStart, "4, 10, fill, top");

		JLabel lblEndzeitpunkt = new JLabel("Endzeitpunkt");
		contentPanel.add(lblEndzeitpunkt, "2, 12");

		spinnerEnd = new JSpinner();
		spinnerEnd.setModel(new SpinnerDateModel(CalendarUtil
				.getNextDay(Calendar.getInstance().getTime()), null, null,
				Calendar.MINUTE));
		contentPanel.add(spinnerEnd, "4, 12, fill, top");

		JButton btnHinzufgen = new JButton("Hinzuf\u00FCgen");
		btnHinzufgen.setMnemonic('h');
		contentPanel.add(btnHinzufgen, "4, 14");
		btnHinzufgen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Vacation v = getVacation();
				if (officeCtrl.getAppointmentController().saveVacation(v)) {
					list.getModel().getSize(); // getSize läd die Liste neu, Bitches!
				} else {
					JOptionPane
							.showMessageDialog(
									null,
									"Konnte die neuen Fehlzeiten nicht speichern, überprüfen Sie bitte die Eingaben!",
									"Fehler beim Speichern",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Schließen");
				cancelButton.setMnemonic('c');
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	private Vacation getVacation() {
		Date start = (Date) spinnerStart.getValue();
		Date end = (Date) spinnerEnd.getValue();
		if (!start.before(end)) {
			return null;
		}
		return new Vacation(new TimeInterval(start, end), employee);
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
