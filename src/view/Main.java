/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view;

import java.awt.EventQueue;
import view.firstrun.FirstrunDialog;
import controller.OfficeController;

public class Main {

	public static boolean DEBUG_MODE = true;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OfficeController o = new OfficeController();
					if (!o.startUp()) {
						FirstrunDialog form=new FirstrunDialog(o);
						form.setVisible(true);
					}
					MainFrame window = new MainFrame(o);
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
