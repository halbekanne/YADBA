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
import model.Employee;
import model.TimeInterval;
import model.TimeOfDayInterval;
import controller.EmployeeController;
import controller.OfficeController;

/**
 * 
 * @author Michael Backhaus
 * 
 */
public class ArbeitszeitenDialog extends JDialog {

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
	private final OfficeController officeController;
	private final EmployeeController employeeController;
	private final Employee employee;

	/**
	 * Create the application.
	 * 
	 * @param officeCtrl
	 */
	public ArbeitszeitenDialog(OfficeController officeCtrl, Employee empl) {
		getContentPane().setEnabled(false);
		officeController = officeCtrl;
		employeeController = officeCtrl.getEmployeeController();
		this.employee = empl;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setTitle("Arbeitszeiten");
		this.setResizable(false);
		this.setModal(true);
		this.setBounds(100, 100, 330, 330);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);
		String[] zeiten = { "00:00", "00:05", "00:10", "00:15", "00:20",
				"00:25", "00:30", "00:35", "00:40", "00:45", "00:50", "00:55",
				"01:00", "01:05", "01:10", "01:15", "01:20", "01:25", "01:30",
				"01:35", "01:40", "01:45", "01:50", "01:55", "02:00", "02:05",
				"02:10", "02:15", "02:20", "02:25", "02:30", "02:35", "02:40",
				"02:45", "02:50", "02:55", "03:00", "03:05", "03:10", "03:15",
				"03:20", "03:25", "03:30", "03:35", "03:40", "03:45", "03:50",
				"03:55", "04:00", "04:05", "04:10", "04:15", "04:20", "04:25",
				"04:30", "04:35", "04:40", "04:45", "04:50", "04:55", "05:00",
				"05:05", "05:10", "05:15", "05:20", "05:25", "05:30", "05:35",
				"05:40", "05:45", "05:50", "05:55", "06:00", "06:05", "06:10",
				"06:15", "06:20", "06:25", "06:30", "06:35", "06:40", "06:45",
				"06:50", "06:55", "07:00", "07:05", "07:10", "07:15", "07:20",
				"07:25", "07:30", "07:35", "07:40", "07:45", "07:50", "07:55",
				"08:00", "08:05", "08:10", "08:15", "08:20", "08:25", "08:30",
				"08:35", "08:40", "08:45", "08:50", "08:55", "09:00", "09:05",
				"09:10", "09:15", "09:20", "09:25", "09:30", "09:35", "09:40",
				"09:45", "09:50", "09:55", "10:00", "10:05", "10:10", "10:15",
				"10:20", "10:25", "10:30", "10:35", "10:40", "10:45", "10:50",
				"10:55", "11:00", "11:05", "11:10", "11:15", "11:20", "11:25",
				"11:30", "11:35", "11:40", "11:45", "11:50", "11:55", "12:00",
				"12:05", "12:10", "12:15", "12:20", "12:25", "12:30", "12:35",
				"12:40", "12:45", "12:50", "12:55", "13:00", "13:05", "13:10",
				"13:15", "13:20", "13:25", "13:30", "13:35", "13:40", "13:45",
				"13:50", "13:55", "14:00", "14:05", "14:10", "14:15", "14:20",
				"14:25", "14:30", "14:35", "14:40", "14:45", "14:50", "14:55",
				"15:00", "15:05", "15:10", "15:15", "15:20", "15:25", "15:30",
				"15:35", "15:40", "15:45", "15:50", "15:55", "16:00", "16:05",
				"16:10", "16:15", "16:20", "16:25", "16:30", "16:35", "16:40",
				"16:45", "16:50", "16:55", "17:00", "17:05", "17:10", "17:15",
				"17:20", "17:25", "17:30", "17:35", "17:40", "17:45", "17:50",
				"17:55", "18:00", "18:05", "18:10", "18:15", "18:20", "18:25",
				"18:30", "18:35", "18:40", "18:45", "18:50", "18:55", "19:00",
				"19:05", "19:10", "19:15", "19:20", "19:25", "19:30", "19:35",
				"19:40", "19:45", "19:50", "19:55", "20:00", "20:05", "20:10",
				"20:15", "20:20", "20:25", "20:30", "20:35", "20:40", "20:45",
				"20:50", "20:55", "21:00", "21:05", "21:10", "21:15", "21:20",
				"21:25", "21:30", "21:35", "21:40", "21:45", "21:50", "21:55",
				"22:00", "22:05", "22:10", "22:15", "22:20", "22:25", "22:30",
				"22:35", "22:40", "22:45", "22:50", "22:55", "23:00", "23:05",
				"23:10", "23:15", "23:20", "23:25", "23:30", "23:35", "23:40",
				"23:45", "23:50", "23:55" };
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


		// Oeffnungszeiten zum Abgleich laden

		final TimeOfDayInterval[] oeffnungszeiten = officeController
				.getOffice().getOpeningTime();
		
		
		
		
		spinnerMontagvon = new JSpinner();
		spinnerMontagvon.setEnabled(false);
		spinnerMontagvon.setModel(mo1model);
		spinnerMontagvon.setBounds(181, 62, 57, 20);
		getContentPane().add(spinnerMontagvon);

		spinnerMontagbis = new JSpinner();
		spinnerMontagbis.setEnabled(false);
		spinnerMontagbis.setModel(mo2model);
		spinnerMontagbis.setBounds(253, 62, 57, 20);
		getContentPane().add(spinnerMontagbis);

		spinnerDienstagvon = new JSpinner();
		spinnerDienstagvon.setEnabled(false);
		spinnerDienstagvon.setModel(di1model);
		spinnerDienstagvon.setBounds(181, 89, 57, 20);
		getContentPane().add(spinnerDienstagvon);

		spinnerDienstagbis = new JSpinner();
		spinnerDienstagbis.setEnabled(false);
		spinnerDienstagbis.setModel(di2model);
		spinnerDienstagbis.setBounds(253, 89, 57, 20);
		getContentPane().add(spinnerDienstagbis);

		spinnerMittwochvon = new JSpinner();
		spinnerMittwochvon.setEnabled(false);
		spinnerMittwochvon.setModel(mi1model);
		spinnerMittwochvon.setBounds(181, 116, 57, 20);
		getContentPane().add(spinnerMittwochvon);

		spinnerMittwochbis = new JSpinner();
		spinnerMittwochbis.setEnabled(false);
		spinnerMittwochbis.setModel(mi2model);
		spinnerMittwochbis.setBounds(253, 116, 57, 20);
		getContentPane().add(spinnerMittwochbis);

		spinnerDonnerstagvon = new JSpinner();
		spinnerDonnerstagvon.setEnabled(false);
		spinnerDonnerstagvon.setModel(do1model);
		spinnerDonnerstagvon.setBounds(181, 143, 57, 20);
		getContentPane().add(spinnerDonnerstagvon);

		spinnerDonnerstagbis = new JSpinner();
		spinnerDonnerstagbis.setEnabled(false);
		spinnerDonnerstagbis.setModel(do2model);
		spinnerDonnerstagbis.setBounds(253, 143, 57, 20);
		getContentPane().add(spinnerDonnerstagbis);

		spinnerFreitagvon = new JSpinner();
		spinnerFreitagvon.setEnabled(false);
		spinnerFreitagvon.setModel(fr1model);
		spinnerFreitagvon.setBounds(181, 170, 57, 20);
		getContentPane().add(spinnerFreitagvon);

		spinnerFreitagbis = new JSpinner();
		spinnerFreitagbis.setEnabled(false);
		spinnerFreitagbis.setModel(fr2model);
		spinnerFreitagbis.setBounds(253, 170, 57, 20);
		getContentPane().add(spinnerFreitagbis);

		spinnerSamstagvon = new JSpinner();
		spinnerSamstagvon.setEnabled(false);
		spinnerSamstagvon.setModel(sa1model);
		spinnerSamstagvon.setBounds(181, 201, 57, 20);
		getContentPane().add(spinnerSamstagvon);

		spinnerSamstagbis = new JSpinner();
		spinnerSamstagbis.setEnabled(false);
		spinnerSamstagbis.setModel(sa2model);
		spinnerSamstagbis.setBounds(253, 201, 57, 20);
		getContentPane().add(spinnerSamstagbis);

		spinnerSonntagvon = new JSpinner();
		spinnerSonntagvon.setEnabled(false);
		spinnerSonntagvon.setModel(so1model);
		spinnerSonntagvon.setBounds(181, 232, 57, 20);
		getContentPane().add(spinnerSonntagvon);

		spinnerSonntagbis = new JSpinner();
		spinnerSonntagbis.setEnabled(false);
		spinnerSonntagbis.setModel(so2model);
		spinnerSonntagbis.setBounds(253, 232, 57, 20);
		getContentPane().add(spinnerSonntagbis);

		final JLabel lblVon = new JLabel("von:");
		lblVon.setBounds(193, 35, 37, 15);
		this.getContentPane().add(lblVon);

		JLabel lblBis = new JLabel("bis:");
		lblBis.setBounds(269, 35, 37, 15);
		this.getContentPane().add(lblBis);

		
		final JCheckBox chckbxMontag = new JCheckBox("Montag");
		chckbxMontag.setEnabled(false);
		if (oeffnungszeiten[0]!=null){chckbxMontag.setEnabled(true);}
		chckbxMontag.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox) e.getSource()).isSelected();
				if (oeffnungszeiten[0]!=null){
					spinnerMontagvon.setEnabled(selected);
					spinnerMontagbis.setEnabled(selected);
					chckbxMontag.setEnabled(true);
				}else{
					chckbxMontag.setEnabled(false);
				}
			}
		});
		chckbxMontag.setBounds(12, 61, 100, 23);
		this.getContentPane().add(chckbxMontag);

		final JCheckBox chckbxDienstag = new JCheckBox("Dienstag");
		chckbxDienstag.setEnabled(false);
		if (oeffnungszeiten[1]!=null){chckbxDienstag.setEnabled(true);}
		chckbxDienstag.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox) e.getSource()).isSelected();
				if (oeffnungszeiten[1]!=null){
					spinnerDienstagvon.setEnabled(selected);
					spinnerDienstagbis.setEnabled(selected);
					chckbxDienstag.setEnabled(true);
				}else{
					chckbxDienstag.setEnabled(false);
				}
			}
		});
		chckbxDienstag.setBounds(12, 88, 95, 23);
		this.getContentPane().add(chckbxDienstag);

		final JCheckBox chckbxMittwoch = new JCheckBox("Mittwoch");
		chckbxMittwoch.setEnabled(false);
		if (oeffnungszeiten[2]!=null){chckbxMittwoch.setEnabled(true);}
		chckbxMittwoch.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox) e.getSource()).isSelected();
				if (oeffnungszeiten[2]!=null){
					spinnerMittwochvon.setEnabled(selected);
					spinnerMittwochbis.setEnabled(selected);
					chckbxMittwoch.setEnabled(true);
				}else{
					chckbxMittwoch.setEnabled(false);
				}
			}
		});
		chckbxMittwoch.setBounds(12, 115, 100, 23);
		this.getContentPane().add(chckbxMittwoch);

		final JCheckBox chckbxDonnerstag = new JCheckBox("Donnerstag");
		chckbxDonnerstag.setEnabled(false);
		if (oeffnungszeiten[3]!=null){chckbxDonnerstag.setEnabled(true);}
		chckbxDonnerstag.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox) e.getSource()).isSelected();
				if (oeffnungszeiten[3]!=null){
					spinnerDonnerstagvon.setEnabled(selected);
					spinnerDonnerstagbis.setEnabled(selected);
					chckbxDonnerstag.setEnabled(true);
				}else{
					chckbxDonnerstag.setEnabled(false);
				}
			}
		});
		chckbxDonnerstag.setBounds(12, 142, 109, 23);
		this.getContentPane().add(chckbxDonnerstag);

		final JCheckBox chckbxFreitag = new JCheckBox("Freitag");
		chckbxFreitag.setEnabled(false);
		if (oeffnungszeiten[4]!=null){chckbxFreitag.setEnabled(true);}
		chckbxFreitag.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox) e.getSource()).isSelected();
				if (oeffnungszeiten[4]!=null){
					spinnerFreitagvon.setEnabled(selected);
					spinnerFreitagbis.setEnabled(selected);
					chckbxFreitag.setEnabled(true);
				}else{
					chckbxFreitag.setEnabled(false);
				}
			}
		});
		chckbxFreitag.setBounds(12, 169, 95, 23);
		this.getContentPane().add(chckbxFreitag);

		final JCheckBox chckbxSamstag = new JCheckBox("Samstag");
		chckbxSamstag.setEnabled(false);
		if (oeffnungszeiten[5]!=null){chckbxSamstag.setEnabled(true);}
		chckbxSamstag.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox) e.getSource()).isSelected();
				if (oeffnungszeiten[5]!=null){
					spinnerSamstagvon.setEnabled(selected);
					spinnerSamstagbis.setEnabled(selected);
					chckbxSamstag.setEnabled(true);
				}else{
					chckbxSamstag.setEnabled(false);
				}
			}
		});
		chckbxSamstag.setBounds(12, 200, 95, 23);
		this.getContentPane().add(chckbxSamstag);

		final JCheckBox chckbxSonntag = new JCheckBox("Sonntag");
		chckbxSonntag.setEnabled(false);
		if (oeffnungszeiten[6]!=null){chckbxSonntag.setEnabled(true);}
		chckbxSonntag.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				boolean selected = ((JCheckBox) e.getSource()).isSelected();
				if (oeffnungszeiten[6]!=null){
					spinnerSonntagvon.setEnabled(selected);
					spinnerSonntagbis.setEnabled(selected);
					chckbxSonntag.setEnabled(true);
				}else{
					chckbxSonntag.setEnabled(false);
				}
			}
		});
		chckbxSonntag.setBounds(12, 231, 95, 23);
		this.getContentPane().add(chckbxSonntag);


		// Bisherige Arbeitszeiten auslesen und Steuerelemente füllen

		
		final TimeOfDayInterval[] arbeitszeiten = employee.getWorkingTime().clone();
		

			//wenn an dem tag geöffnet ist
			if (oeffnungszeiten[0] != null){
				//wenn keine arbeitszeiten fue den Tag existieren
				if (arbeitszeiten[0] == null) {
					//oeffnungszeiten von dem tag eintragen
					mo1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[0].getBegin()));
					mo2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[0].getEnd()));
					//und Tag ausblenden
					chckbxMontag.setSelected(false);
				} else {
					//sonst Tag einblenden
					chckbxMontag.setSelected(true);
					//wenn arbeitszeiten innerhalb der öffnungszeiten liegen
					if (arbeitszeiten[0].getBegin().after(oeffnungszeiten[0].getBegin())){
						//neue arbeitszeiten setzen
						mo1model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[0].getBegin()));
					}else{
						//sonst öffnungszeiten verwenden
						mo1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[0].getBegin()));
					}
					if (arbeitszeiten[0].getEnd().before(oeffnungszeiten[0].getEnd())){
						mo2model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[0].getEnd()));
					}else{
						mo2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[0].getEnd()));
					}
						
				}
			}else{
				//Wenn an dem Tag geschlossen ist
				chckbxMontag.setSelected(false);
			}

	
			if (oeffnungszeiten[1] != null){
				if (arbeitszeiten[1] == null) {
					di1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[1].getBegin()));
					di2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[1].getEnd()));
					chckbxDienstag.setSelected(false);
				} else {
					chckbxDienstag.setSelected(true);
					if (arbeitszeiten[1].getBegin().after(oeffnungszeiten[1].getBegin())){
						di1model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[1].getBegin()));
					}else{
						di1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[1].getBegin()));
					}
					if (arbeitszeiten[1].getEnd().before(oeffnungszeiten[1].getEnd())){
						di2model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[1].getEnd()));
					}else{
						di2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[1].getEnd()));
					}
						
				}
			}else{
				chckbxDienstag.setSelected(false);
			}
	
		
		
		
		

			if (oeffnungszeiten[2] != null){
				if (arbeitszeiten[2] == null) {
					mi1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[2].getBegin()));
					mi2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[2].getEnd()));
					chckbxMittwoch.setSelected(false);
				} else {
					chckbxMittwoch.setSelected(true);
					if (arbeitszeiten[2].getBegin().after(oeffnungszeiten[2].getBegin())){
						mi1model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[2].getBegin()));
					}else{
						mi1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[2].getBegin()));
					}
					if (arbeitszeiten[2].getEnd().before(oeffnungszeiten[2].getEnd())){
						mi2model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[2].getEnd()));
					}else{
						mi2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[2].getEnd()));
					}
						
				}
			}else{
				chckbxMittwoch.setSelected(false);
			}

		
		

	
			if (oeffnungszeiten[3] != null){
				if (arbeitszeiten[3] == null) {
					do1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[3].getBegin()));
					do2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[3].getEnd()));
					chckbxDonnerstag.setSelected(false);
				} else {
					chckbxDonnerstag.setSelected(true);
					if (arbeitszeiten[3].getBegin().after(oeffnungszeiten[3].getBegin())){
						do1model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[3].getBegin()));
					}else{
						do1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[3].getBegin()));
					}
					if (arbeitszeiten[3].getEnd().before(oeffnungszeiten[3].getEnd())){
						do2model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[3].getEnd()));
					}else{
						do2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[3].getEnd()));
					}
						
				}
			}else{
				chckbxDonnerstag.setSelected(false);
			}
		
		
		
		
	
			if (oeffnungszeiten[4] != null){
				if (arbeitszeiten[4] == null) {
					fr1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[4].getBegin()));
					fr2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[4].getEnd()));
					chckbxFreitag.setSelected(false);
				} else {
					chckbxFreitag.setSelected(true);
					if (arbeitszeiten[4].getBegin().after(oeffnungszeiten[1].getBegin())){
						fr1model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[4].getBegin()));
					}else{
						fr1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[4].getBegin()));
					}
					if (arbeitszeiten[4].getEnd().before(oeffnungszeiten[4].getEnd())){
						fr2model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[4].getEnd()));
					}else{
						fr2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[4].getEnd()));
					}
						
				}
			}else{
				chckbxFreitag.setSelected(false);
			}
		
		
		
		
	

			if (oeffnungszeiten[5] != null){
				if (arbeitszeiten[5] == null) {
					sa1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[5].getBegin()));
					sa2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[5].getEnd()));
					chckbxSamstag.setSelected(false);
				} else {
					chckbxSamstag.setSelected(true);
					if (arbeitszeiten[5].getBegin().after(oeffnungszeiten[5].getBegin())){
						sa1model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[5].getBegin()));
					}else{
						sa1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[5].getBegin()));
					}
					if (arbeitszeiten[5].getEnd().before(oeffnungszeiten[5].getEnd())){
						sa2model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[5].getEnd()));
					}else{
						sa2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[5].getEnd()));
					}
						
				}
			}else{
				chckbxSamstag.setSelected(false);
			}

		


		
		
	
			if (oeffnungszeiten[6] != null){
				if (arbeitszeiten[6] == null) {
					so1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[6].getBegin()));
					so2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[6].getEnd()));
					chckbxSonntag.setSelected(false);
				} else {
					chckbxSonntag.setSelected(true);
					if (arbeitszeiten[6].getBegin().after(oeffnungszeiten[6].getBegin())){
						so1model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[6].getBegin()));
					}else{
						so1model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[6].getBegin()));
					}
					if (arbeitszeiten[6].getEnd().before(oeffnungszeiten[6].getEnd())){
						so2model.setValue(CalendarUtil.getHourMinuteString(arbeitszeiten[6].getEnd()));
					}else{
						so2model.setValue(CalendarUtil.getHourMinuteString(oeffnungszeiten[6].getEnd()));
					}
						
				}
			}else{
				chckbxSonntag.setSelected(false);
			}	

		
		
		
		

		final JButton btnSpeichern = new JButton("Speichern");
		btnSpeichern.setMnemonic('s');
		btnSpeichern.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxMontag.isSelected()) {
					arbeitszeiten[0] = new TimeOfDayInterval(
							new TimeInterval(
									CalendarUtil
											.getDateByHoursMinutesString(spinnerMontagvon
													.getValue().toString()),
									CalendarUtil
											.getDateByHoursMinutesString(spinnerMontagbis
													.getValue().toString())));
				} else {
					arbeitszeiten[0] = null;
				}

				if (chckbxDienstag.isSelected()) {
					arbeitszeiten[1] = new TimeOfDayInterval(
							new TimeInterval(
									CalendarUtil
											.getDateByHoursMinutesString(spinnerDienstagvon
													.getValue().toString()),
									CalendarUtil
											.getDateByHoursMinutesString(spinnerDienstagbis
													.getValue().toString())));
				} else {
					arbeitszeiten[1] = null;
				}

				if (chckbxMittwoch.isSelected()) {
					arbeitszeiten[2] = new TimeOfDayInterval(
							new TimeInterval(
									CalendarUtil
											.getDateByHoursMinutesString(spinnerMittwochvon
													.getValue().toString()),
									CalendarUtil
											.getDateByHoursMinutesString(spinnerMittwochbis
													.getValue().toString())));
				} else {
					arbeitszeiten[2] = null;
				}

				if (chckbxDonnerstag.isSelected()) {
					arbeitszeiten[3] = new TimeOfDayInterval(
							new TimeInterval(
									CalendarUtil
											.getDateByHoursMinutesString(spinnerDonnerstagvon
													.getValue().toString()),
									CalendarUtil
											.getDateByHoursMinutesString(spinnerDonnerstagbis
													.getValue().toString())));
				} else {
					arbeitszeiten[3] = null;
				}

				if (chckbxFreitag.isSelected()) {
					arbeitszeiten[4] = new TimeOfDayInterval(
							new TimeInterval(
									CalendarUtil
											.getDateByHoursMinutesString(spinnerFreitagvon
													.getValue().toString()),
									CalendarUtil
											.getDateByHoursMinutesString(spinnerFreitagbis
													.getValue().toString())));
				} else {
					arbeitszeiten[4] = null;
				}

				if (chckbxSamstag.isSelected()) {
					arbeitszeiten[5] = new TimeOfDayInterval(
							new TimeInterval(
									CalendarUtil
											.getDateByHoursMinutesString(spinnerSamstagvon
													.getValue().toString()),
									CalendarUtil
											.getDateByHoursMinutesString(spinnerSamstagbis
													.getValue().toString())));
				} else {
					arbeitszeiten[5] = null;
				}

				if (chckbxSonntag.isSelected()) {
					arbeitszeiten[6] = new TimeOfDayInterval(
							new TimeInterval(
									CalendarUtil
											.getDateByHoursMinutesString(spinnerSonntagvon
													.getValue().toString()),
									CalendarUtil
											.getDateByHoursMinutesString(spinnerSonntagbis
													.getValue().toString())));
				} else {
					arbeitszeiten[6] = null;
				}
				/*
				 * Versucht neue Arbeitszeiten zu speichern
				 */
					if (employeeController.setWorkingTime(employee, arbeitszeiten)) {
						dispose();
					} else {
						JOptionPane.showMessageDialog(null,
								"Konnte Arbeitszeiten nicht setzen!", "Fehler!",
								JOptionPane.OK_CANCEL_OPTION);
					}
					;
			}
		});
		btnSpeichern.setBounds(12, 264, 121, 25);
		this.getContentPane().add(btnSpeichern);

		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.setMnemonic('a');
		btnAbbrechen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnAbbrechen.setBounds(189, 264, 121, 25);
		getContentPane().add(btnAbbrechen);
		JLabel lblArbeitszeitenVonMax = new JLabel("Arbeitszeiten von "	+ employee.getFirstName() + " " + employee.getLastName() + ":");
		lblArbeitszeitenVonMax.setBounds(12, 12, 298, 15);
		getContentPane().add(lblArbeitszeitenVonMax);
		// MONTAG
		spinnerMontagvon.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil.getDateByHoursMinutesString(spinnerMontagvon.getValue().toString()).after(CalendarUtil.getDateByHoursMinutesString(spinnerMontagbis.getValue().toString()))) {
					JOptionPane.showMessageDialog(null,"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!","Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerMontagvon.setValue(spinnerMontagbis.getPreviousValue());
				}
				if (oeffnungszeiten[0].getBegin().after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerMontagvon
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerMontagvon.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[0].getBegin()));
				}
			}
		});
		spinnerMontagbis.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil.getDateByHoursMinutesString(
						spinnerMontagvon.getValue().toString()).after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerMontagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerMontagbis.setValue(spinnerMontagvon.getNextValue());
				}
				if (oeffnungszeiten[0].getEnd().before(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerMontagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerMontagbis.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[0].getEnd()));
				}
			}
		});
		// DIENSTAG
		spinnerDienstagvon.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil.getDateByHoursMinutesString(
						spinnerDienstagvon.getValue().toString()).after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerDienstagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerDienstagvon.setValue(spinnerDienstagbis
							.getPreviousValue());
				}
				if (oeffnungszeiten[1].getBegin().after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerDienstagvon
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerDienstagvon.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[1].getBegin()));
				}
			}
		});
		spinnerDienstagbis.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil.getDateByHoursMinutesString(
						spinnerDienstagvon.getValue().toString()).after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerDienstagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerDienstagbis.setValue(spinnerDienstagvon
							.getNextValue());
				}
				if (oeffnungszeiten[1].getEnd().before(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerDienstagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerDienstagbis.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[1].getEnd()));
				}
			}
		});
		// MITTWOCH
		spinnerMittwochvon.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil.getDateByHoursMinutesString(
						spinnerMittwochvon.getValue().toString()).after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerMittwochbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerMittwochvon.setValue(spinnerMittwochbis
							.getPreviousValue());
				}
				if (oeffnungszeiten[2].getBegin().after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerMittwochvon
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerMittwochvon.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[2].getBegin()));
				}
			}
		});
		spinnerMittwochbis.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil.getDateByHoursMinutesString(
						spinnerMittwochvon.getValue().toString()).after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerMittwochbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerMittwochbis.setValue(spinnerMittwochvon
							.getNextValue());
				}
				if (oeffnungszeiten[2].getEnd().before(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerMittwochbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerMittwochbis.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[2].getEnd()));
				}
			}
		});
		// DONNERSTAG
		spinnerDonnerstagvon.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil
						.getDateByHoursMinutesString(
								spinnerDonnerstagvon.getValue().toString())
						.after(CalendarUtil
								.getDateByHoursMinutesString(spinnerDonnerstagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerDonnerstagvon.setValue(spinnerDonnerstagbis
							.getPreviousValue());
				}
				if (oeffnungszeiten[3]
						.getBegin()
						.after(CalendarUtil
								.getDateByHoursMinutesString(spinnerDonnerstagvon
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerDonnerstagvon.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[3].getBegin()));
				}
			}
		});
		spinnerDonnerstagbis.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil
						.getDateByHoursMinutesString(
								spinnerDonnerstagvon.getValue().toString())
						.after(CalendarUtil
								.getDateByHoursMinutesString(spinnerDonnerstagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerDonnerstagbis.setValue(spinnerDonnerstagvon
							.getNextValue());
				}
				if (oeffnungszeiten[3]
						.getEnd()
						.before(CalendarUtil
								.getDateByHoursMinutesString(spinnerDonnerstagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerDonnerstagbis.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[3].getEnd()));
				}
			}
		});
		// FREITAG
		spinnerFreitagvon.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil.getDateByHoursMinutesString(
						spinnerFreitagvon.getValue().toString()).after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerFreitagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerFreitagvon.setValue(spinnerFreitagbis
							.getPreviousValue());
				}
				if (oeffnungszeiten[4].getBegin().after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerFreitagvon
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerFreitagvon.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[4].getBegin()));
				}
			}
		});
		spinnerFreitagbis.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil.getDateByHoursMinutesString(
						spinnerFreitagvon.getValue().toString()).after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerFreitagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerFreitagbis.setValue(spinnerFreitagvon.getNextValue());
				}
				if (oeffnungszeiten[4].getEnd().before(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerFreitagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerFreitagbis.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[4].getEnd()));
				}
			}
		});
		// SAMSTAG
		spinnerSamstagvon.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil.getDateByHoursMinutesString(
						spinnerSamstagvon.getValue().toString()).after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerSamstagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerSamstagvon.setValue(spinnerSamstagbis
							.getPreviousValue());
				}
				if (oeffnungszeiten[5].getBegin().after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerSamstagvon
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerSamstagvon.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[5].getBegin()));
				}
			}
		});
		spinnerSamstagbis.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil.getDateByHoursMinutesString(
						spinnerSamstagvon.getValue().toString()).after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerSamstagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerSamstagbis.setValue(spinnerSamstagvon.getNextValue());
				}
				if (oeffnungszeiten[5].getEnd().before(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerSamstagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerSamstagbis.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[5].getEnd()));
				}
			}
		});
		// SONNTAG
		spinnerSonntagvon.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil.getDateByHoursMinutesString(
						spinnerSonntagvon.getValue().toString()).after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerSonntagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerSonntagvon.setValue(spinnerSonntagbis
							.getPreviousValue());
				}
				if (oeffnungszeiten[6].getBegin().after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerSonntagvon
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerSonntagvon.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[6].getBegin()));
				}
			}
		});
		spinnerSonntagbis.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (CalendarUtil.getDateByHoursMinutesString(
						spinnerSonntagvon.getValue().toString()).after(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerSonntagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Der Beginn der Arbeitszeit muss zeitlich vor dem Ende liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerSonntagbis.setValue(spinnerSonntagvon.getNextValue());
				}
				if (oeffnungszeiten[6].getEnd().before(
						CalendarUtil
								.getDateByHoursMinutesString(spinnerSonntagbis
										.getValue().toString()))) {
					JOptionPane
							.showMessageDialog(
									null,
									"Die Arbeitszeit muss innerhalb der Öffnungszeiten liegen!",
									"Fehler!", JOptionPane.OK_CANCEL_OPTION);
					spinnerSonntagbis.setValue(CalendarUtil
							.getHourMinuteString(oeffnungszeiten[6].getEnd()));
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
