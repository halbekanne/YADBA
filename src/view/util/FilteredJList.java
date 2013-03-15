/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package view.util;
import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 
 * @author Stefan Noll
 *
 */
@SuppressWarnings("unchecked")
public class FilteredJList extends JList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FilterField filterField;
	private int DEFAULT_FIELD_WIDTH = 20;

	public FilteredJList() {
		super();
		setModel(new FilterModel());
		filterField = new FilterField(DEFAULT_FIELD_WIDTH);

	}

	public void setModel(ListModel m) {
		if (!(m instanceof FilterModel))
			throw new IllegalArgumentException();
		super.setModel(m);
	}

	public void addItem(Object o) {
		((FilterModel) getModel()).addElement(o);
	}
	
	public void clear(){
		((FilterModel) getModel()).clear();
	}

	public JTextField getFilterField() {
		return filterField;
	}

	protected class FilterModel extends AbstractListModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		ArrayList items;
		ArrayList filterItems;

		public FilterModel() {
			super();
			items = new ArrayList();
			filterItems = new ArrayList();
		}

		public Object getElementAt(int index) {
			if (index < filterItems.size())
				return filterItems.get(index);
			else
				return null;
		}

		public int getSize() {
			return filterItems.size();
		}

		public void addElement(Object o) {
			items.add(o);
			refilter();

		}
		
		public void clear(){
			items.clear();
			refilter();
		}

		private void refilter() {
			filterItems.clear();
			String term = getFilterField().getText();
			for (int i = 0; i < items.size(); i++)
				if (items.get(i).toString().indexOf(term, 0) != -1)
					filterItems.add(items.get(i));
			fireContentsChanged(this, 0, getSize());
		}
	}

	// FilterField inner class listed below
	// inner class provides filter-by-keystroke field
	protected class FilterField extends JTextField implements DocumentListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FilterField(int width) {
			super(width);
			getDocument().addDocumentListener(this);
		}

		public void changedUpdate(DocumentEvent e) {
			((FilterModel) getModel()).refilter();
		}

		public void insertUpdate(DocumentEvent e) {
			((FilterModel) getModel()).refilter();
		}

		public void removeUpdate(DocumentEvent e) {
			((FilterModel) getModel()).refilter();
		}
	}

	public static void main(String[] args) {
		String[] listItems = { "Chris", "Joshua", "Daniel", "Michael", "Don",
				"Kimi", "Kelly", "Keagan" };
		JFrame frame = new JFrame("FilteredJList");
		frame.getContentPane().setLayout(new BorderLayout());
		// populate list
		FilteredJList list = new FilteredJList();
		for (int i = 0; i < listItems.length; i++)
			list.addItem(listItems[i]);
		// add to gui
		JScrollPane pane = new JScrollPane(list,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		frame.getContentPane().add(pane, BorderLayout.CENTER);
		frame.getContentPane().add(list.getFilterField(), BorderLayout.NORTH);
		frame.pack();
		frame.setVisible(true);
	}
}
