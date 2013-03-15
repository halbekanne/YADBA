/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.management;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.CalendarUtil;
import model.TimeInterval;
import model.TimeOfDayInterval;
import controller.OfficeController;

/**
 * 
 * @author 	Michael Backhaus
 *
 */
public class OeffnungszeitenDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JSpinner spinnerMontagbis;
	private JSpinner spinnerMontagvon;
	private JSpinner spinnerDienstagvon;
	private JSpinner spinnerDienstagbis;
	private JSpinner spinnerMittwochvon;
	private JSpinner spinnerMittwochbis;
	private JSpinner spinnerDonnerstagvon;
	private JSpinner spinnerDonnerstagbis;
	private JSpinner spinnerFreitagvon;
	private JSpinner spinnerFreitagbis;
	private JSpinner spinnerSamstagvon;
	private JSpinner spinnerSamstagbis;
	private JSpinner spinnerSonntagvon;
	private JSpinner spinnerSonntagbis;
	private OfficeController officeController;
	
	/**
	 * Create the application.
	 * @param officeCtrl 
	 */
	public OeffnungszeitenDialog(OfficeController officeCtrl) {
		getContentPane().setEnabled(false);
		officeController = officeCtrl;
		initialize();
	}



	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setTitle("Öffnungszeiten");
		this.setResizable(false);
		this.setModal(true);
		this.setBounds(100, 100, 275, 302);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);
		String[] zeiten = {"00:00", "00:05", "00:10", "00:15", "00:20", "00:25", "00:30", "00:35", "00:40", "00:45", "00:50", "00:55", "01:00", "01:05", "01:10", "01:15", "01:20", "01:25", "01:30", "01:35", "01:40", "01:45", "01:50", "01:55", "02:00", "02:05", "02:10", "02:15", "02:20", "02:25", "02:30", "02:35", "02:40", "02:45", "02:50", "02:55", "03:00", "03:05", "03:10", "03:15", "03:20", "03:25", "03:30", "03:35", "03:40", "03:45", "03:50", "03:55", "04:00", "04:05", "04:10", "04:15", "04:20", "04:25", "04:30", "04:35", "04:40", "04:45", "04:50", "04:55", "05:00", "05:05", "05:10", "05:15", "05:20", "05:25", "05:30", "05:35", "05:40", "05:45", "05:50", "05:55", "06:00", "06:05", "06:10", "06:15", "06:20", "06:25", "06:30", "06:35", "06:40", "06:45", "06:50", "06:55", "07:00", "07:05", "07:10", "07:15", "07:20", "07:25", "07:30", "07:35", "07:40", "07:45", "07:50", "07:55", "08:00", "08:05", "08:10", "08:15", "08:20", "08:25", "08:30", "08:35", "08:40", "08:45", "08:50", "08:55", "09:00", "09:05", "09:10", "09:15", "09:20", "09:25", "09:30", "09:35", "09:40", "09:45", "09:50", "09:55", "10:00", "10:05", "10:10", "10:15", "10:20", "10:25", "10:30", "10:35", "10:40", "10:45", "10:50", "10:55", "11:00", "11:05", "11:10", "11:15", "11:20", "11:25", "11:30", "11:35", "11:40", "11:45", "11:50", "11:55", "12:00", "12:05", "12:10", "12:15", "12:20", "12:25", "12:30", "12:35", "12:40", "12:45", "12:50", "12:55", "13:00", "13:05", "13:10", "13:15", "13:20", "13:25", "13:30", "13:35", "13:40", "13:45", "13:50", "13:55", "14:00", "14:05", "14:10", "14:15", "14:20", "14:25", "14:30", "14:35", "14:40", "14:45", "14:50", "14:55", "15:00", "15:05", "15:10", "15:15", "15:20", "15:25", "15:30", "15:35", "15:40", "15:45", "15:50", "15:55", "16:00", "16:05", "16:10", "16:15", "16:20", "16:25", "16:30", "16:35", "16:40", "16:45", "16:50", "16:55", "17:00", "17:05", "17:10", "17:15", "17:20", "17:25", "17:30", "17:35", "17:40", "17:45", "17:50", "17:55", "18:00", "18:05", "18:10", "18:15", "18:20", "18:25", "18:30", "18:35", "18:40", "18:45", "18:50", "18:55", "19:00", "19:05", "19:10", "19:15", "19:20", "19:25", "19:30", "19:35", "19:40", "19:45", "19:50", "19:55", "20:00", "20:05", "20:10", "20:15", "20:20", "20:25", "20:30", "20:35", "20:40", "20:45", "20:50", "20:55", "21:00", "21:05", "21:10", "21:15", "21:20", "21:25", "21:30", "21:35", "21:40", "21:45", "21:50", "21:55", "22:00", "22:05", "22:10", "22:15", "22:20", "22:25", "22:30", "22:35", "22:40", "22:45", "22:50", "22:55", "23:00", "23:05", "23:10", "23:15", "23:20", "23:25", "23:30", "23:35", "23:40", "23:45", "23:50", "23:55"};
		SpinnerListModel mo1model = new SpinnerListModel(zeiten);
		SpinnerListModel mo2model = new SpinnerListModel(zeiten);
		SpinnerListModel di1model = new SpinnerListModel(zeiten);
		SpinnerListModel di2model = new SpinnerListModel(zeiten);
		SpinnerListModel mi1model = new SpinnerListModel(zeiten);
		SpinnerListModel mi2model = new SpinnerListModel(zeiten);
		SpinnerListModel do1model = new SpinnerListModel(zeiten);
		SpinnerListModel do2model = new SpinnerListModel(zeiten);
		SpinnerListModel fr1model = new SpinnerListModel(zeiten);
		SpinnerListModel fr2model = new SpinnerListModel(zeiten);
		SpinnerListModel sa1model = new SpinnerListModel(zeiten);
		SpinnerListModel sa2model = new SpinnerListModel(zeiten);
		SpinnerListModel so1model = new SpinnerListModel(zeiten);
		SpinnerListModel so2model = new SpinnerListModel(zeiten);
		
		spinnerMontagvon = new JSpinner();
		spinnerMontagvon.setEnabled(false);
		spinnerMontagvon.setModel(mo1model);
		spinnerMontagvon.setBounds(131, 39, 57, 20);
		getContentPane().add(spinnerMontagvon);
		
		spinnerMontagbis = new JSpinner();
		spinnerMontagbis.setEnabled(false);
		spinnerMontagbis.setModel(mo2model);
		spinnerMontagbis.setBounds(203, 39, 57, 20);
		getContentPane().add(spinnerMontagbis);
		

		spinnerDienstagvon = new JSpinner();
		spinnerDienstagvon.setEnabled(false);
		spinnerDienstagvon.setModel(di1model);
		spinnerDienstagvon.setBounds(131, 66, 57, 20);
		getContentPane().add(spinnerDienstagvon);
		
		spinnerDienstagbis = new JSpinner();
		spinnerDienstagbis.setEnabled(false);
		spinnerDienstagbis.setModel(di2model);
		spinnerDienstagbis.setBounds(203, 66, 57, 20);
		getContentPane().add(spinnerDienstagbis);

		spinnerMittwochvon = new JSpinner();
		spinnerMittwochvon.setEnabled(false);
		spinnerMittwochvon.setModel(mi1model);
		spinnerMittwochvon.setBounds(131, 93, 57, 20);
		getContentPane().add(spinnerMittwochvon);
		
		spinnerMittwochbis = new JSpinner();
		spinnerMittwochbis.setEnabled(false);
		spinnerMittwochbis.setModel(mi2model);
		spinnerMittwochbis.setBounds(203, 93, 57, 20);
		getContentPane().add(spinnerMittwochbis);
		
		spinnerDonnerstagvon = new JSpinner();
		spinnerDonnerstagvon.setEnabled(false);
		spinnerDonnerstagvon.setModel(do1model);
		spinnerDonnerstagvon.setBounds(131, 120, 57, 20);
		getContentPane().add(spinnerDonnerstagvon);
		
		spinnerDonnerstagbis = new JSpinner();
		spinnerDonnerstagbis.setEnabled(false);
		spinnerDonnerstagbis.setModel(do2model);
		spinnerDonnerstagbis.setBounds(203, 120, 57, 20);
		getContentPane().add(spinnerDonnerstagbis);
		
		spinnerFreitagvon = new JSpinner();
		spinnerFreitagvon.setEnabled(false);
		spinnerFreitagvon.setModel(fr1model);
		spinnerFreitagvon.setBounds(131, 147, 57, 20);
		getContentPane().add(spinnerFreitagvon);
		
		spinnerFreitagbis = new JSpinner();
		spinnerFreitagbis.setEnabled(false);
		spinnerFreitagbis.setModel(fr2model);
		spinnerFreitagbis.setBounds(203, 147, 57, 20);
		getContentPane().add(spinnerFreitagbis);
		
		spinnerSamstagvon = new JSpinner();
		spinnerSamstagvon.setEnabled(false);
		spinnerSamstagvon.setModel(sa1model);
		spinnerSamstagvon.setBounds(131, 178, 57, 20);
		getContentPane().add(spinnerSamstagvon);
		
		spinnerSamstagbis = new JSpinner();
		spinnerSamstagbis.setEnabled(false);
		spinnerSamstagbis.setModel(sa2model);
		spinnerSamstagbis.setBounds(203, 178, 57, 20);
		getContentPane().add(spinnerSamstagbis);
		
		spinnerSonntagvon = new JSpinner();
		spinnerSonntagvon.setEnabled(false);
		spinnerSonntagvon.setModel(so1model);
		spinnerSonntagvon.setBounds(131, 209, 57, 20);
		getContentPane().add(spinnerSonntagvon);
		
		spinnerSonntagbis = new JSpinner();
		spinnerSonntagbis.setEnabled(false);
		spinnerSonntagbis.setModel(so2model);
		spinnerSonntagbis.setBounds(203, 209, 57, 20);
		getContentPane().add(spinnerSonntagbis);
		
		final JLabel lblVon = new JLabel("von:");
		lblVon.setBounds(143, 12, 37, 15);
		this.getContentPane().add(lblVon);
		
		JLabel lblBis = new JLabel("bis:");
		lblBis.setBounds(219, 12, 37, 15);
		this.getContentPane().add(lblBis);
		
	
		/*
		bisherige Oeffnungszeiten auslesen
		*/
		final TimeOfDayInterval[] oeffnungszeiten = officeController.getOffice().getOpeningTime().clone();

		final JButton btnSpeichern = new JButton("Speichern");
		btnSpeichern.setMnemonic('s');
		
		/*
		 * Versucht neue Oeffnungszeiten zu speichern
		 */
		final JCheckBox chckbxMontag = new JCheckBox("Montag");
		chckbxMontag.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox)e.getSource()).isSelected();
				spinnerMontagvon.setEnabled(selected);
				spinnerMontagbis.setEnabled(selected);
			}
		});
		chckbxMontag.setBounds(8, 37, 100, 23);
		this.getContentPane().add(chckbxMontag);
		
		final JCheckBox chckbxDienstag = new JCheckBox("Dienstag");
		chckbxDienstag.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox)e.getSource()).isSelected();
				spinnerDienstagvon.setEnabled(selected);
				spinnerDienstagbis.setEnabled(selected);
			}
		});
		chckbxDienstag.setBounds(8, 64, 95, 23);
		this.getContentPane().add(chckbxDienstag);
		
		final JCheckBox chckbxMittwoch = new JCheckBox("Mittwoch");
		chckbxMittwoch.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox)e.getSource()).isSelected();
				spinnerMittwochvon.setEnabled(selected);
				spinnerMittwochbis.setEnabled(selected);
			}
		});
		chckbxMittwoch.setBounds(8, 91, 100, 23);
		this.getContentPane().add(chckbxMittwoch);
		
		final JCheckBox chckbxDonnerstag = new JCheckBox("Donnerstag");
		chckbxDonnerstag.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox)e.getSource()).isSelected();
				spinnerDonnerstagvon.setEnabled(selected);
				spinnerDonnerstagbis.setEnabled(selected);
			}
		});
		chckbxDonnerstag.setBounds(8, 118, 109, 23);
		this.getContentPane().add(chckbxDonnerstag);
		
		final JCheckBox chckbxFreitag = new JCheckBox("Freitag");
		chckbxFreitag.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox)e.getSource()).isSelected();
				spinnerFreitagvon.setEnabled(selected);
				spinnerFreitagbis.setEnabled(selected);
			}
		});
		chckbxFreitag.setBounds(8, 145, 95, 23);
		this.getContentPane().add(chckbxFreitag);
		
		final JCheckBox chckbxSamstag = new JCheckBox("Samstag");
		chckbxSamstag.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox)e.getSource()).isSelected();
				spinnerSamstagvon.setEnabled(selected);
				spinnerSamstagbis.setEnabled(selected);
			}
		});

		chckbxSamstag.setBounds(8, 176, 95, 23);
		this.getContentPane().add(chckbxSamstag);
		
		final JCheckBox chckbxSonntag = new JCheckBox("Sonntag");
		chckbxSonntag.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox)e.getSource()).isSelected();
				spinnerSonntagvon.setEnabled(selected);
				spinnerSonntagbis.setEnabled(selected);
			}
		});
		chckbxSonntag.setBounds(8, 207, 95, 23);
		this.getContentPane().add(chckbxSonntag);
		
		//Daten aus Steuerelementen holen und versuchen zu speichern
		
		
		
		btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxMontag.isSelected()){
					oeffnungszeiten[0] = new TimeOfDayInterval(new TimeInterval(
							CalendarUtil.getDateByHoursMinutesString(spinnerMontagvon.getValue().toString()),
							CalendarUtil.getDateByHoursMinutesString(spinnerMontagbis.getValue().toString())));	
				}else{
					oeffnungszeiten[0] = null;
				}
				
				if (chckbxDienstag.isSelected()){
					oeffnungszeiten[1] = new TimeOfDayInterval(new TimeInterval(
							CalendarUtil.getDateByHoursMinutesString(spinnerDienstagvon.getValue().toString()),
							CalendarUtil.getDateByHoursMinutesString(spinnerDienstagbis.getValue().toString())));					
				}else{
					oeffnungszeiten[1] = null;
				}
				
				if (chckbxMittwoch.isSelected()){
					oeffnungszeiten[2] = new TimeOfDayInterval(new TimeInterval(
							CalendarUtil.getDateByHoursMinutesString(spinnerMittwochvon.getValue().toString()),
							CalendarUtil.getDateByHoursMinutesString(spinnerMittwochbis.getValue().toString())));
				}else{
					oeffnungszeiten[2] = null; 
				}
				
				if (chckbxDonnerstag.isSelected()){
					oeffnungszeiten[3] = new TimeOfDayInterval(new TimeInterval(
							CalendarUtil.getDateByHoursMinutesString(spinnerDonnerstagvon.getValue().toString()),
							CalendarUtil.getDateByHoursMinutesString(spinnerDonnerstagbis.getValue().toString())));
				}else{
					oeffnungszeiten[3] = null;
				}
				
				if (chckbxFreitag.isSelected()){
					oeffnungszeiten[4] = new TimeOfDayInterval(new TimeInterval(
						CalendarUtil.getDateByHoursMinutesString(spinnerFreitagvon.getValue().toString()),
						CalendarUtil.getDateByHoursMinutesString(spinnerFreitagbis.getValue().toString())));
				}else{
					oeffnungszeiten[4] = null;
				}
				
				if (chckbxSamstag.isSelected()){
					oeffnungszeiten[5] = new TimeOfDayInterval(new TimeInterval(
							CalendarUtil.getDateByHoursMinutesString(spinnerSamstagvon.getValue().toString()),
							CalendarUtil.getDateByHoursMinutesString(spinnerSamstagbis.getValue().toString())));
				}else{
					oeffnungszeiten[5] = null;
				}
				
				if (chckbxSonntag.isSelected()){
					oeffnungszeiten[6] = new TimeOfDayInterval(new TimeInterval(
							CalendarUtil.getDateByHoursMinutesString(spinnerSonntagvon.getValue().toString()),
							CalendarUtil.getDateByHoursMinutesString(spinnerSonntagbis.getValue().toString())));
				}else{
					oeffnungszeiten[6] = null;
				}
				//wenn die Oeffnungszeiten nicht gesetzt werden konnten		
				if (!officeController.setOpeningTime(oeffnungszeiten)) {
					//Fehler ausgeben
					JOptionPane.showMessageDialog(null,  "Konnte Öffnungszeiten nicht setzen!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
				} else {
					//Sonst schliessen
					dispose();
				}
			}
		});
		btnSpeichern.setBounds(8, 240, 121, 25);
		this.getContentPane().add(btnSpeichern);
		
		
		//Falls keine bisherigen Oeffnungszeiten vorhanden
		if (oeffnungszeiten[0] == null){
			//Standardwete verwenden
			mo1model.setValue("08:00");
			mo2model.setValue("16:00");
			chckbxMontag.setSelected(false);
		}else {
			chckbxMontag.setSelected(true);
			mo1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[0].getBegin()));
			mo2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[0].getEnd()));
		}
		
		if (oeffnungszeiten[1] == null){
			di1model.setValue("08:00");
			di2model.setValue("16:00");
			chckbxDienstag.setSelected(false);
		}else {
			chckbxDienstag.setSelected(true);
			di1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[1].getBegin()));
			di2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[1].getEnd()));
		}

		if (oeffnungszeiten[2] == null){
			mi1model.setValue("08:00");
			mi2model.setValue("16:00");
			chckbxMittwoch.setSelected(false);
		}else {
			chckbxMittwoch.setSelected(true);
			mi1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[2].getBegin()));
			mi2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[2].getEnd()));
		}
		
		if (oeffnungszeiten[3] == null){
			do1model.setValue("08:00");
			do2model.setValue("16:00");
			chckbxDonnerstag.setSelected(false);
		}else {
			chckbxDonnerstag.setSelected(true);
			do1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[3].getBegin()));
			do2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[3].getEnd()));
		}
		
		if (oeffnungszeiten[4] == null){
			fr1model.setValue("08:00");
			fr2model.setValue("16:00");
			chckbxFreitag.setSelected(false);
		}else {
			chckbxFreitag.setSelected(true);
			fr1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[4].getBegin()));
			fr2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[4].getEnd()));
		}
		
		if (oeffnungszeiten[5] == null){
			sa1model.setValue("08:00");
			sa2model.setValue("16:00");
			chckbxSamstag.setSelected(false);
		}else {
			chckbxSamstag.setSelected(true);
			sa1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[5].getBegin()));
			sa2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[5].getEnd()));
		}
		
		if (oeffnungszeiten[6] == null){
			so1model.setValue("08:00");
			so2model.setValue("16:00");
			chckbxSonntag.setSelected(false);
		}else {
			chckbxSonntag.setSelected(true);
			so1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[6].getBegin()));
			so2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[6].getEnd()));
		}
		
		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.setMnemonic('a');
		btnAbbrechen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnAbbrechen.setBounds(139, 241, 121, 25);
		getContentPane().add(btnAbbrechen);
		
		spinnerMontagvon.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// Wenn begin nicht vor end liegt
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerMontagvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerMontagbis.getValue().toString()))) {
					//vorheriges datum ist nicht null
					if (spinnerMontagbis.getPreviousValue()!= null){
						spinnerMontagvon.setValue(spinnerMontagbis.getPreviousValue());
						//Fehler ausgeben
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
		spinnerMontagbis.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerMontagvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerMontagbis.getValue().toString()))) {
					if (spinnerMontagvon.getNextValue()!=null){
						spinnerMontagbis.setValue(spinnerMontagvon.getNextValue());
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
		spinnerDienstagvon.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerDienstagvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerDienstagbis.getValue().toString()))) {
					if (spinnerDienstagbis.getPreviousValue()!=null){
						spinnerDienstagvon.setValue(spinnerDienstagbis.getPreviousValue());
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
		spinnerDienstagbis.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerDienstagvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerDienstagbis.getValue().toString()))) {
					if(spinnerDienstagvon.getNextValue()!= null){
						spinnerDienstagbis.setValue(spinnerDienstagvon.getNextValue());
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
		spinnerMittwochvon.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerMittwochvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerMittwochbis.getValue().toString()))) {
					if(spinnerMittwochbis.getPreviousValue()!=null){
						spinnerMittwochvon.setValue(spinnerMittwochbis.getPreviousValue());
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
		spinnerMittwochbis.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerMittwochvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerMittwochbis.getValue().toString()))) {
					if(spinnerMittwochvon.getNextValue()!=null){
						spinnerMittwochbis.setValue(spinnerMittwochvon.getNextValue());
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
		spinnerDonnerstagvon.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerDonnerstagvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerDonnerstagbis.getValue().toString()))) {
					if (spinnerDonnerstagbis.getPreviousValue()!=null){
						spinnerDonnerstagvon.setValue(spinnerDonnerstagbis.getPreviousValue());
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
		spinnerDonnerstagbis.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerDonnerstagvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerDonnerstagbis.getValue().toString()))) {
					if(spinnerDonnerstagvon.getNextValue()!=null){
						spinnerDonnerstagbis.setValue(spinnerDonnerstagvon.getNextValue());
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
		spinnerFreitagvon.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerFreitagvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerFreitagbis.getValue().toString()))) {
					if(spinnerFreitagbis.getPreviousValue()!=null){
						spinnerFreitagvon.setValue(spinnerFreitagbis.getPreviousValue());
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
		spinnerFreitagbis.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerFreitagvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerFreitagbis.getValue().toString()))) {
					if(spinnerFreitagbis.getPreviousValue()!=null){
						spinnerFreitagbis.setValue(spinnerFreitagvon.getNextValue());
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
		spinnerSamstagvon.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerSamstagvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerSamstagbis.getValue().toString()))) {
					if (spinnerSamstagbis.getPreviousValue()!=null){
						spinnerSamstagvon.setValue(spinnerSamstagbis.getPreviousValue());
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
		spinnerSamstagbis.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerSamstagvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerSamstagbis.getValue().toString()))) {
					if(spinnerSamstagvon.getNextValue()!=null){
						spinnerSamstagbis.setValue(spinnerSamstagvon.getNextValue());
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
		spinnerSonntagvon.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!CalendarUtil.getDateByHoursMinutesString(spinnerSonntagvon.getValue().toString()).before(CalendarUtil.getDateByHoursMinutesString(spinnerSonntagbis.getValue().toString()))) {
					if (spinnerSonntagbis.getPreviousValue()!=null){
						spinnerSonntagvon.setValue(spinnerSonntagbis.getPreviousValue());
						JOptionPane.showMessageDialog(null, "Die Öffnung muss früher stattfinden als die Schliessung!", "Fehler!", JOptionPane.OK_CANCEL_OPTION);
					}
				}
			}
		});
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
