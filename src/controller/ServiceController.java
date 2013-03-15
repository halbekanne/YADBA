/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package controller;

import model.*;

public class ServiceController {
	
	private OfficeController officeCtrl;
	
	public ServiceController(OfficeController o){
		officeCtrl = o;
	}
	
	/**
	 * Speichert eine Leistung im Office ab.
	 * @param s Eine gültige Leistung mit einzigartigem Namen
	 * @return true, wenns geklappt hat, false, wenn die Leistung ungültig war.
	 */
	public boolean saveService(Service s){
		if (s== null || !s.validService())
			return false;
		if (getServiceByName(s.getName()) != null)
			return false;
		if (s instanceof Material){
			officeCtrl.getOffice().getMaterials().add((Material) s);
		} else {
			officeCtrl.getOffice().getActivities().add((Activity) s);
		}
		return true;
	}

//FIXME Soll das überhaupt erlaubt werden? Könnte zu Konflikten mit vergangenen Terminen führen.
//Benutzer kann auch einfach ne neue Behandlung anlegen, das könnte besser funktionieren.	
//	public boolean editService(Service oldS, Service newS){
//		if (oldS == null || newS == null || !oldS.validService() || !newS.validService() || oldS.getClass() != newS.getClass())
//			return false;
//		if (oldS.getName().compareTo( newS.getName()) != 0 && getServiceByName(newS.getName()) != null)
//			return false;
//		oldS.setName(newS.getName());
//		oldS.setPrice(newS.getPrice());
//		if (oldS instanceof Activity){
//			((Activity) oldS).setDuration(((Activity) newS).getDuration()); 
//			((Activity) oldS).setNumberOfSessions(((Activity) newS).getNumberOfSessions());
//		}
//		return true;
//	}
	
	/**
	 * Löscht eine Leistung aus der Liste vom Office. Eventuelle Referenzen von Terminen bleiben erhalten
	 * FIXME Macht das Sinn so? Nein, wird eh nur beim Testen verwendet
	 * @param s Die Leistung
	 * @return true, wenns geklappt hat, false, wenn die Leistung ungültig war.
	 */
	public boolean removeService(Service s){
		if (s== null || !s.validService())
			return false;
		if (s instanceof Material){
			officeCtrl.getOffice().getMaterials().remove(s);
		} else {
			officeCtrl.getOffice().getActivities().remove(s);
		}
		return true;
	}
	
	/**
	 * Gibt zu einem gegebenen String den Service mit diesem Namen zurück oder null, falls ein solcher Service nicht existiert
	 * @param name Der Name des gesuchten Services
	 * @return Der Service mit dem Namen name oder null, falls ein solcher nicht existiert.
	 */
	public Service getServiceByName(String name){
		if (name == null)
			return null;
		for (Service service : officeCtrl.getOffice().getActivities())
			if (name.equals(service.getName()))
				return service;
		for (Service service : officeCtrl.getOffice().getMaterials())
			if (name.equals(service.getName()))
				return service;
		return null;
	}
	
}
