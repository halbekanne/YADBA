/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.CalendarUtil;
import model.Employee;
import model.Rank;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.auth.LoginService;

import view.appointment.KursBeitretenDialog;
import view.appointment.NeuerKursDialog;
import view.appointment.NeuerTerminDialog;
import view.appointment.TerminBearbeitenPanel;
import view.calendar.MainCalendar;
import view.calendar.MainCalendar.ColumnType;
import view.export.KalenderExportierenDialog;
import view.listener.AppointmentInfoUpdateHandler;
import view.management.BehandlungsVerwaltungsDialog;
import view.management.MaterialVerwaltungsDialog;
import view.management.MitarbeiterVerwaltungsDialog;
import view.management.OeffnungszeitenDialog;
import view.management.PraxisFormular;
import view.management.RaumverwaltungsDialog;
import view.patient.PatientenAktenDialog;
import view.patient.PatientenSuchenDialog;
import controller.OfficeController;

/**
 * 
 * Hauptfenster der Anwendung.
 * 
 * @author Dominik Halfkann & Alex
 * 
 */
public class MainFrame extends JFrame {

	/**  */
	private static final long serialVersionUID = 1L;

	private final OfficeController officeCtrl;

	private final Action actImport = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Import");
			putValue(SHORT_DESCRIPTION, "Importiert ein BackUp");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/import.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_I);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter(
					"YADBA Backup (*.yadba)", "yadba"));
			int returnVal = fc.showOpenDialog(getParent());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if (!officeCtrl.getIOController().loadBackUp(
						file.getAbsolutePath())) {
					JOptionPane.showMessageDialog(getParent(),
							"Konnte Datei nicht laden!");
				}
			}
			updateMainFrame();
		}
	};

	Action actExport = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Export");
			putValue(SHORT_DESCRIPTION, "Exportiert ein BackUp");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/export.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_E);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter(
					"YADBA Backup (*.yadba)", "yadba"));
			fc.setSelectedFile(new File("YADBA-Backup.yadba"));
			int returnVal = fc.showSaveDialog(getParent());

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if (!fc.getSelectedFile().getAbsolutePath().endsWith(".yadba")) {
					file = new File(file + ".yadba");
				}
				if (!officeCtrl.getIOController().saveBackUp(
						file.getAbsolutePath())) {
					JOptionPane.showMessageDialog(getParent(),
							"Konnte Datei nicht speichern!");
				}
			}
			updateMainFrame();
		}
	};

	Action actCalExport = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Kalender exportieren (.ics)");
			putValue(SHORT_DESCRIPTION,
					"Exportiert Termine im Kalender im iCalender Format");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/ical-export.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_X);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dialog = new KalenderExportierenDialog(officeCtrl);
			dialog.setLocationRelativeTo(MainFrame.this);
			dialog.setVisible(true);
			updateMainFrame();
		}
	};

	Action actCalImport = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Kalender importieren (.ics)");
			putValue(SHORT_DESCRIPTION,
					"Importiert Termine aus einem Kalender im iCalender Format");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/ical-import.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_M);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane
					.showMessageDialog(
							MainFrame.this,
							"<html>Das importieren von Kalender im iCalendar-Fromat<br>ist nur in der <font size=\"4\"><i>Pro-Version</i></font> möglich.</html>",
							"Pro-Version-Funktion", 1);
			updateMainFrame();
		}
	};

	Action actNewTreatment = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Termin erstellen");
			putValue(SHORT_DESCRIPTION, "Erstellt einen neuen Termin");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/new-appointment.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(MainFrame.class
					.getResource("/icons/32/new-appointment.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_N);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dialog = new NeuerTerminDialog(officeCtrl);
			dialog.setLocationRelativeTo(MainFrame.this);
			dialog.setVisible(true);
			updateMainFrame();
		}
	};

	Action actNewCourse = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Kurs erstellen");
			putValue(SHORT_DESCRIPTION, "Erstellt einen neuen Kurs");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/new-cours.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(MainFrame.class
					.getResource("/icons/32/new-cours.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_K);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control K"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dialog = new NeuerKursDialog(officeCtrl);
			dialog.setLocationRelativeTo(MainFrame.this);
			dialog.setVisible(true);
			updateMainFrame();
		}
	};

	Action actExtendCourse = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Kurs erweitern");
			putValue(SHORT_DESCRIPTION,
					"Erweitert bestehende Kurse um Patienten");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/new-cours-plus.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(MainFrame.class
					.getResource("/icons/32/new-cours-plus.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_E);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift control K"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dialog = new KursBeitretenDialog(officeCtrl);
			dialog.setLocationRelativeTo(MainFrame.this);
			dialog.setVisible(true);
			updateMainFrame();
		}
	};

	Action actLogIn = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "LogIn");
			putValue(SHORT_DESCRIPTION, "Beendet die aktuelle Sitzung");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/document-decrypt-3.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(MainFrame.class
					.getResource("/icons/32/document-decrypt-3.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_L);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// einloggen
			if (officeCtrl.getUser() == null) {
				JXLoginPane loginPanel = new JXLoginPane(new LoginService() {
					@Override
					public boolean authenticate(String user, char[] password,
							String server) throws Exception {
						return officeCtrl.login(user, new String(password));
					}
				});
				JXLoginPane.Status status = JXLoginPane.showLoginDialog(
						MainFrame.this, loginPanel);

				if (status == JXLoginPane.Status.SUCCEEDED) {
					doLogin(true);
				}
				// ausloggen
			} else {
				officeCtrl.logout();
				doLogin(false);
			}

		}

	};

	Action actQuitProgram = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Beenden");
			putValue(SHORT_DESCRIPTION, "Beendet das Programm");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/system-exit.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_B);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			officeCtrl.shutDown();
			dispose();
		}

	};

	Action actNewPatient = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Patienten hinzufügen");
			putValue(SHORT_DESCRIPTION, "Einen neuen Patienten anlegen");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/contact-new.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(MainFrame.class
					.getResource("/icons/32/contact-new.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_N);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control R"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			PatientenAktenDialog dialog = new PatientenAktenDialog(officeCtrl);
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
			updateMainFrame();
		}
	};

	Action actManagePatienten = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Patienten verwalten");
			putValue(SHORT_DESCRIPTION,
					"Patienten suchen, ansehen und bearbeiten");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/patienten-verwalten.png")));
			putValue(LARGE_ICON_KEY, new ImageIcon(MainFrame.class
					.getResource("/icons/32/patienten-verwalten.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_V);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dialog = new PatientenSuchenDialog(officeCtrl);
			dialog.setLocationRelativeTo(MainFrame.this);
			dialog.setVisible(true);
			updateMainFrame();
		}
	};

	Action actManageOffice = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Praxisdaten bearbeiten");
			putValue(SHORT_DESCRIPTION, "Die Daten der Praxis bearbeiten.");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/manage-office.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_P);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dialog = new PraxisFormular(officeCtrl);
			dialog.setLocationRelativeTo(MainFrame.this);
			dialog.setVisible(true);
			updateMainFrame();
		}
	};

	Action actManageOpeningTimes = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Öffnungszeiten bearbeiten");
			putValue(SHORT_DESCRIPTION,
					"Bearbeitet die Öffnungszeiten der Praxis");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/manage-opening-time.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_Z);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dialog = new OeffnungszeitenDialog(officeCtrl);
			dialog.setLocationRelativeTo(MainFrame.this);
			dialog.setVisible(true);
			updateMainFrame();
		}
	};

	Action actManageRooms = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Räume verwalten");
			putValue(SHORT_DESCRIPTION,
					"Räume erstellen, bearbeiten und löschen");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/manage-rooms.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_R);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dialog = new RaumverwaltungsDialog(officeCtrl);
			dialog.setLocationRelativeTo(MainFrame.this);
			dialog.setVisible(true);
			updateMainFrame();

		}
	};

	Action actManageEmployees = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Mitarbeiter verwalten");
			putValue(SHORT_DESCRIPTION,
					"Mitarbeiter erstellen, bearbeiten und löschen");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/manage-employees.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_M);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dialog = new MitarbeiterVerwaltungsDialog(officeCtrl);
			dialog.setLocationRelativeTo(MainFrame.this);
			dialog.setVisible(true);
			updateMainFrame();
		}
	};

	Action actManageActivities = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Behandlungen verwalten");
			putValue(SHORT_DESCRIPTION,
					"Behandlungen erstellen, bearbeiten und löschen");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/manage-activities.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_B);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dialog = new BehandlungsVerwaltungsDialog(officeCtrl);
			dialog.setLocationRelativeTo(MainFrame.this);
			dialog.setVisible(true);
			updateMainFrame();
		}
	};

	Action actManageMaterial = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Material verwalten");
			putValue(SHORT_DESCRIPTION,
					"Material erstellen, bearbeiten und löschen");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/manage-materiel.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_A);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JDialog dialog = new MaterialVerwaltungsDialog(officeCtrl);
			dialog.setLocationRelativeTo(MainFrame.this);
			dialog.setVisible(true);
			updateMainFrame();
		}
	};

	Action actOnlineHelp = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Online Hilfe");
			putValue(SHORT_DESCRIPTION, "Zeigt die Online-Hilfe an.");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/help-contents.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_H);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (java.awt.Desktop.isDesktopSupported()) {
				java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
				if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
					java.net.URI uri;
					try {
						uri = new java.net.URI("http://google.de");
						desktop.browse(uri);
					} catch (URISyntaxException use) {
						JOptionPane
								.showMessageDialog(
										null,
										"Konnte Browser für Onlne-Hilfe nicht starten!",
										"Online-Hilfe nicht gefunden",
										JOptionPane.ERROR_MESSAGE);
					} catch (IOException ioe) {
						JOptionPane
								.showMessageDialog(
										null,
										"Konnte Browser für Onlne-Hilfe nicht starten!",
										"Online-Hilfe nicht gefunden",
										JOptionPane.ERROR_MESSAGE);

					}
				}
			}
			updateMainFrame();
		}
	};

	Action actAnleitung = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Anleitung");
			putValue(SHORT_DESCRIPTION, "Öffnet dei beilgeledte Anleitung");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/help-instructionbook.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_H);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F1"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (java.awt.Desktop.isDesktopSupported()) {
				
				java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
				if (desktop.isSupported(java.awt.Desktop.Action.OPEN)) {
					try {
						File anleitung = new File("resources/anleitung.pdf");
						if (new File(MainFrame.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getName().endsWith(".jar")) {
							InputStream input = ClassLoader.getSystemResourceAsStream("anleitung.pdf");
							byte[] inputBits = new byte[input.available()];
							input.read(inputBits);

							anleitung = File.createTempFile("YADBA-Anleitung", ".pdf");
							anleitung.deleteOnExit();

							final byte[] buffer = new byte[1024];
							int read;
							FileOutputStream output = new FileOutputStream(anleitung);
							while ((read = input.read(buffer)) != -1) {
								output.write(buffer, 0, read);
							}
							input.close();
							output.close();
						}
						desktop.open(anleitung);
					} catch (FileNotFoundException e1) {
						JOptionPane
						.showMessageDialog(
								null,
								"Die Anleitung konnte nicht geöffnet werden.",
								"Anleitung nicht gefunden",
								JOptionPane.ERROR_MESSAGE);
					} catch (Exception e1) {
						JOptionPane
						.showMessageDialog(
								null,
								"Die Anleitung konnte nicht geöffnet werden.",
								"Fehler beim öffnen der Anleitung",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	};


	Action actProSupport = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Premium-Support");
			putValue(SHORT_DESCRIPTION,
					"Der Premium-Support ist nur in der Pro-Version für ausgewählte Kunden verfügbar.");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F12"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane
					.showMessageDialog(
							MainFrame.this,
							"<html>Der <i>Premium-Support</i> ist nur in der <font size=\"4\"><i>Pro-Version</i></font> für ausgewählte Kunden verf&uuml;gbar.</html>",
							"Pro-Version", 1);
			updateMainFrame();
		}
	};
	

	Action actAbout = new AbstractAction() {
		/**  */
		private static final long serialVersionUID = 1L;

		{
			putValue(NAME, "Über");
			putValue(SHORT_DESCRIPTION, "Zeigt die Credits an");
			putValue(SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/help-about.png")));
			putValue(MNEMONIC_KEY, KeyEvent.VK_B);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane
					.showMessageDialog(
							getParent(),
							"<html><center><font size=+2><b><u>YADBA</u></b</font><br/>"
									+ "(Yet Another Delightful Backronym, Ay!)<br/><br/>"
									+ "Die Verwendung dieses Programms erfolgt ohne jegliche Gewähr.<br/>"
									+ "Alle Personen sind fiktiv, übereinstimmungen mit Namen realer Personen sind rein zufällig.<br/><br/>"
									+ "<u>Erstellt von:</u><br/>"
									+ "Dominik, Michael, Benjamin, Fabian, Karl, Stefan & Alexander<br>"
									+ "(Sopra 13a-Gr04)<center></html>",
							"Über YADBA", JOptionPane.PLAIN_MESSAGE);

		}
	};

	private MainCalendar mainCalendar;

	private JComboBox<String> comboBox;

	private JXDatePicker datePicker;

	private JLabel lblUser;

	private JSplitPane mainSplitPane;

	private JSplitPane rightSplitPanel;

	private JPanel filterPanel;

	private JMenu mnVerwaltung;

	public void updateMainFrame() {
		int choice = comboBox.getSelectedIndex();
		if (choice == 0) {
			mainCalendar.updateCalendar(ColumnType.Employee, datePicker
					.getDate());
		} else {
			mainCalendar.updateCalendar(ColumnType.Room, datePicker.getDate());
		}
	}

	/**
	 * Create a main frame.
	 */
	public MainFrame(OfficeController o) {
		this.officeCtrl = o;
		initialize();
		if (Main.DEBUG_MODE) {
			officeCtrl.login("DEBUG", "PASSWORD");
			doLogin(true);
		}
		this.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				updateMainFrame();
			}

			@Override
			public void focusLost(FocusEvent e) {
				// not needed here
			}

		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				MainFrame.class.getResource("/icons/32/yadba.png")));
		this.setTitle("YADBA");
		this.setBounds(100, 100, 1037, 838);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Beim Beenden Daten sichern.
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				officeCtrl.shutDown();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				officeCtrl.shutDown();
			}
		});

		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu mnProgramm = new JMenu("Programm");
		mnProgramm.setMnemonic('p');
		menuBar.add(mnProgramm);

		JMenuItem mntmImport = new JMenuItem(actImport);
		mnProgramm.add(mntmImport);

		JMenuItem mntmExport = new JMenuItem(actExport);
		mnProgramm.add(mntmExport);

		JMenuItem mntmKalenderImportierenical = new JMenuItem(actCalImport);
		mntmKalenderImportierenical.setForeground(Color.GRAY);
		mnProgramm.add(mntmKalenderImportierenical);

		JMenuItem mntmKalenderExportierenical = new JMenuItem(actCalExport);
		mnProgramm.add(mntmKalenderExportierenical);

		mnProgramm.addSeparator();

		JMenuItem mntmLoginlogout = new JMenuItem(actLogIn);
		mnProgramm.add(mntmLoginlogout);

		JMenuItem mntmBeenden = new JMenuItem(actQuitProgram);
		mnProgramm.add(mntmBeenden);

		JMenu mnTermin = new JMenu("Termin");
		mnTermin.setMnemonic('t');
		menuBar.add(mnTermin);

		JMenuItem mntmTerminErstellen = new JMenuItem(actNewTreatment);
		mnTermin.add(mntmTerminErstellen);

		JMenuItem mntmKursErstellen = new JMenuItem(actNewCourse);
		mnTermin.add(mntmKursErstellen);

		JMenuItem mntmKursErweitern = new JMenuItem(actExtendCourse);
		mnTermin.add(mntmKursErweitern);

		JMenu mnPatient = new JMenu("Patient");
		mnPatient.setMnemonic('a');
		menuBar.add(mnPatient);

		JMenuItem mntmPatientHinzufgen = new JMenuItem(actNewPatient);
		mnPatient.add(mntmPatientHinzufgen);

		JMenuItem mntmPatientenVerwalen = new JMenuItem(actManagePatienten);
		mnPatient.add(mntmPatientenVerwalen);

		mnVerwaltung = new JMenu("Verwaltung");
		mnVerwaltung.setMnemonic('v');
		menuBar.add(mnVerwaltung);

		JMenuItem mntmPraxis = new JMenuItem(actManageOffice);
		mnVerwaltung.add(mntmPraxis);

		JMenuItem mntmffnungszeiten = new JMenuItem(actManageOpeningTimes);
		mnVerwaltung.add(mntmffnungszeiten);

		JMenuItem mntmMitarbeiter = new JMenuItem(actManageEmployees);
		mnVerwaltung.add(mntmMitarbeiter);

		JMenuItem mntmRume = new JMenuItem(actManageRooms);
		mnVerwaltung.add(mntmRume);

		JMenuItem mntmBehandlungen = new JMenuItem(actManageActivities);
		mnVerwaltung.add(mntmBehandlungen);

		JMenuItem mntmMaterial = new JMenuItem(actManageMaterial);
		mnVerwaltung.add(mntmMaterial);

		JMenu mnHilfe = new JMenu("Hilfe");
		mnHilfe.setMnemonic('h');
		menuBar.add(mnHilfe);

		JMenuItem mntmOnlinehilfe = new JMenuItem(actOnlineHelp);
		mnHilfe.add(mntmOnlinehilfe);

		JMenuItem mntmAnleitung = new JMenuItem(actAnleitung);
		mnHilfe.add(mntmAnleitung);
		
		JMenuItem mntmProSupport = new JMenuItem(actProSupport);
		// mntmProSupport.setVisible(false);
		mntmProSupport.setForeground(Color.GRAY);
		mnHilfe.add(mntmProSupport);

		JMenuItem mntmber = new JMenuItem(actAbout);
		mnHilfe.add(mntmber);
		getContentPane().setLayout(new BorderLayout(0, 0));

		// Toolbar
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);

		toolBar.add(actNewTreatment);
		toolBar.add(actNewCourse);
		toolBar.add(actExtendCourse);
		toolBar.addSeparator();

		toolBar.add(actNewPatient);
		toolBar.add(actManagePatienten);

		toolBar.add(Box.createGlue());
		toolBar.add(actLogIn);

		// content
		mainSplitPane = new JSplitPane();
		getContentPane().add(mainSplitPane);
		mainSplitPane.setResizeWeight(1.0);

		final JPanel leftPanel = new JPanel();
		mainSplitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(new BorderLayout(0, 0));

		filterPanel = new JPanel();
		leftPanel.add(filterPanel, BorderLayout.NORTH);
		filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JMenuBar menuBar_1 = new JMenuBar();
		menuBar_1.setMargin(new Insets(2, 2, 2, 2));
		menuBar_1.setBorderPainted(false);
		filterPanel.add(menuBar_1);
		
		JMenu mnNewMenu = new JMenu("");
		mnNewMenu.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		menuBar_1.add(mnNewMenu);
		mnNewMenu.setIcon(new ImageIcon(MainFrame.class
				.getResource("/icons/32/zoom-fit-height.png")));
		
		final JSlider slider = new JSlider();
		slider.setMaximumSize(new Dimension(140, 32));
		slider.setPreferredSize(new Dimension(140, 32));
		slider.setSnapToTicks(true);
		slider.setMaximum(16);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (mainCalendar != null) {
					mainCalendar.updateCalendarHeight(slider.getValue());
				}
			}
		});
		slider.setValue(8);
		mnNewMenu.add(slider);
		
		JMenu mnNewMenu_1 = new JMenu("");
		mnNewMenu_1.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.GRAY));
		menuBar_1.add(mnNewMenu_1);
		mnNewMenu_1.setIcon(new ImageIcon(MainFrame.class
				.getResource("/icons/32/zoom-fit-width.png")));
		
		final JSlider slider_1 = new JSlider();
		slider_1.setMaximumSize(new Dimension(140, 32));
		slider_1.setPreferredSize(new Dimension(140, 32));
		slider_1.setSnapToTicks(true);
		slider_1.setMaximum(10);
		slider_1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (mainCalendar != null) {
					mainCalendar.updateCalendarWidth(slider_1.getValue());
				}
			}
		});
		slider_1.setValue(6);
		mnNewMenu_1.add(slider_1);

		final JButton btnPrev = new JButton();
		btnPrev.setIcon(new ImageIcon(MainFrame.class
				.getResource("/icons/16/go-previous.png")));
		filterPanel.add(btnPrev);

		datePicker = new JXDatePicker();
		datePicker.setDate(new Date());
		filterPanel.add(datePicker);

		final JButton btnNext = new JButton();
		btnNext.setIcon(new ImageIcon(MainFrame.class
				.getResource("/icons/16/go-next.png")));
		filterPanel.add(btnNext);

		comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"Mitarbeiter", "R\u00E4ume" }));
		filterPanel.add(comboBox);

		mainCalendar = new MainCalendar(officeCtrl, ColumnType.Employee,
				datePicker.getDate());
		leftPanel.add(mainCalendar);

		rightSplitPanel = new JSplitPane();
		rightSplitPanel.setResizeWeight(1.0);
		rightSplitPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rightSplitPanel.setDividerLocation(500);
		mainSplitPane.setRightComponent(rightSplitPanel);

		final TerminBearbeitenPanel appointmentInfoPanel = new TerminBearbeitenPanel(
				this, officeCtrl);
		rightSplitPanel.setLeftComponent(appointmentInfoPanel);

		final JXMonthView picker = new JXMonthView();
		picker.setTraversable(true);
		rightSplitPanel.setRightComponent(picker);

		final JPanel statusBar = new JPanel();
		final FlowLayout flowLayout = (FlowLayout) statusBar.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(statusBar, BorderLayout.SOUTH);

		lblUser = new JLabel("Nicht angemeldet");
		statusBar.add(lblUser);

		// Listeners
		mainCalendar.getCalendarTable().getSelectionModel()
				.addListSelectionListener(
						new AppointmentInfoUpdateHandler(mainCalendar
								.getCalendarTable(), appointmentInfoPanel));

		mainCalendar.getCalendarTable().getColumnModel().getSelectionModel()
				.addListSelectionListener(
						new AppointmentInfoUpdateHandler(mainCalendar
								.getCalendarTable(), appointmentInfoPanel));

		mainCalendar.getCalendarTable().getTableHeader().addMouseListener(
				new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent mouseEvent) {
						int index = mainCalendar.getCalendarTable()
								.columnAtPoint(mouseEvent.getPoint());

						Object object = null;
						if (mainCalendar.getColumnType() == ColumnType.Employee) {
							String[] name = mainCalendar.getCalendarTable()
									.getColumnName(index).split(", ");

							object = officeCtrl.getEmployeeController()
									.getEmployeeByName(name[1], name[0]);
						} else {
							String name = mainCalendar.getCalendarTable()
									.getColumnName(index);
							object = officeCtrl.getRoomController()
									.getRoomByName(name);
						}
						if (object != null) {
							KalenderExportierenDialog dialog = new KalenderExportierenDialog(
									officeCtrl);
							dialog.changePreset(object, null);
							dialog.setLocationRelativeTo(MainFrame.this);
							dialog.setVisible(true);
						}
					}
				});

		btnPrev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				datePicker.setDate(CalendarUtil.increaseDay(datePicker
						.getDate(), -1));
				mainCalendar.updateCalendar(datePicker.getDate());
			}
		});

		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				datePicker.setDate(CalendarUtil.increaseDay(datePicker
						.getDate(), 1));
				mainCalendar.updateCalendar(datePicker.getDate());
			}
		});

		datePicker.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainCalendar.updateCalendar(datePicker.getDate());
			}
		});

		picker.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				datePicker.setDate(picker.getSelectionDate());
				mainCalendar.updateCalendar(picker.getSelectionDate());
			}
		});

		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = comboBox.getSelectedIndex();
				if (choice == 0) {
					mainCalendar.updateCalendar(ColumnType.Employee);
				} else {
					mainCalendar.updateCalendar(ColumnType.Room);
				}

			}
		});

		doLogin(false);
	}

	private void doLogin(boolean login) {
		boolean superLogin = login
				&& officeCtrl.getUser().getRank() == Rank.BOSS;
		if (Main.DEBUG_MODE) {
			superLogin = true;
			// boolean notSoSuperButBetterThenNormalLogin = login &&
			// officeCtrl.getUser().getRank() == Rank.REPLACEMENT;
		}

		if (login) {
			actLogIn.putValue(Action.NAME, "LogOut");
			actLogIn.putValue(Action.SHORT_DESCRIPTION,
					"Die aktuelle Sitzung beenden");
			actLogIn.putValue(Action.SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/document-decrypt-3.png")));
			actLogIn.putValue(Action.LARGE_ICON_KEY, new ImageIcon(
					MainFrame.class
							.getResource("/icons/32/document-decrypt-3.png")));

			Employee user = officeCtrl.getUser();
			if (Main.DEBUG_MODE) {
				lblUser.setText("DEBUG MODE");
			} else {
				lblUser.setText("Angemeldet als: " + user.getFirstName() + " "
						+ user.getLastName() + " ("
						+ user.getRank().getViewName() + ")");
			}

			mainSplitPane.setRightComponent(rightSplitPanel);
		} else {
			actLogIn.putValue(Action.NAME, "LogIn");
			actLogIn.putValue(Action.SHORT_DESCRIPTION,
					"Anmelden um das Programm zu verwenden");
			actLogIn.putValue(Action.SMALL_ICON, new ImageIcon(MainFrame.class
					.getResource("/icons/16/document-encrypt-3.png")));
			actLogIn.putValue(Action.LARGE_ICON_KEY, new ImageIcon(
					MainFrame.class
							.getResource("/icons/32/document-encrypt-3.png")));

			lblUser.setText("Nicht angemeldet");
			mainSplitPane.setRightComponent(null);
		}

		actImport.setEnabled(login);
		actExport.setEnabled(login);
		actCalExport.setEnabled(login);
		actCalImport.setEnabled(login);

		actNewTreatment.setEnabled(login);
		actNewCourse.setEnabled(login);

		actNewPatient.setEnabled(login);
		actManagePatienten.setEnabled(login);
		actExtendCourse.setEnabled(login);

		mnVerwaltung.setVisible(superLogin);
		actManageOffice.setEnabled(superLogin);
		actManageOpeningTimes.setEnabled(superLogin);
		actManageEmployees.setEnabled(superLogin);
		actManageRooms.setEnabled(superLogin);
		actManageActivities.setEnabled(superLogin);
		actManageMaterial.setEnabled(superLogin);

		filterPanel.setVisible(login);
	}
}
