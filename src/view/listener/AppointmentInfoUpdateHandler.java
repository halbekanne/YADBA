/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
/**
 * 
 */
package view.listener;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Appointment;
import view.appointment.TerminBearbeitenPanel;

/**
 * @author Dominik Halfkann
 * 
 */
public class AppointmentInfoUpdateHandler implements ListSelectionListener {

	private TerminBearbeitenPanel terminBearbeitenPanel;
	private JTable calendarTable;

	public AppointmentInfoUpdateHandler(JTable calendarTable, TerminBearbeitenPanel terminBearbeitenPanel) {
		this.terminBearbeitenPanel = terminBearbeitenPanel;
		this.calendarTable = calendarTable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */
	@Override
	public void valueChanged(final ListSelectionEvent e) {
		@SuppressWarnings("unused")
		int index = e.getFirstIndex();
		if (calendarTable.getSelectedRow() >= 0 && calendarTable.getSelectedColumn() >= 0) {
			Object o = calendarTable.getModel().getValueAt(calendarTable.getSelectedRow(),
					calendarTable.getSelectedColumn());
			if (o instanceof Appointment) {
				Appointment app = (Appointment) o;
				// Termin ans Panel übergeben.
				terminBearbeitenPanel.setAppointment(app);
			} else {
				terminBearbeitenPanel.setAppointment(null);
			}
		} else {
			terminBearbeitenPanel.setAppointment(null);
		}

	}

}
