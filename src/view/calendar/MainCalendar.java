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
package view.calendar;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

import model.CalendarUtil;
import model.TimeOfDayInterval;
import controller.OfficeController;

/**
 * @author Dominik Halfkann
 * 
 */
public class MainCalendar extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum ColumnType {
		Room, Employee
	}

	public static int fixedRowHeaderWidth = 50;
	public static int fixedRowHeight = 14;
	public static int fixedRowMargin = 0;
	public static int fixedColumnWidth = 200;
	public static int minutesPerRow = 5;

	private final OfficeController officeController;
	private ColumnType columnType;
	private final List<Date> rowHeaderValues = new ArrayList<Date>();
	private final CalendarTable calendarTable;
	private Date day;

	public MainCalendar(OfficeController officeController,
			ColumnType columnType, Date day) {
		super();
		this.columnType = columnType;
		this.officeController = officeController;
		this.day = day;

		generateRowHeaderValues(officeController.getOffice().getOpeningTime(),
				minutesPerRow);

		JList<String> rowHeader = createRowHeader();
		setRowHeaderView(rowHeader);

		calendarTable = new CalendarTable(rowHeaderValues, officeController,
				columnType, new Date());

		setViewportView(calendarTable);
	}

	public JTable getCalendarTable() {
		return calendarTable;
	}

	public void updateCalendar(ColumnType columnType, Date day) {
		this.columnType = columnType;
		updateCalendar(day);
	}

	public void updateCalendar(ColumnType columnType) {
		this.columnType = columnType;
		updateCalendar();
	}

	public void updateCalendar(Date day) {
		this.day = day;
		updateCalendar();
	}
	
	public void updateCalendarHeight(int value) {
		fixedRowHeight = 6 + value;
		updateCalendar();
	}

	public void updateCalendarWidth(int value) {
		fixedColumnWidth = 80 + value * 20;
		updateCalendar();
	}

	public void updateCalendar() {
		this.setVisible(false);
		int verticalBarPos = this.getVerticalScrollBar().getValue();
		this.getVerticalScrollBar().setValue(0);
		generateRowHeaderValues(officeController.getOffice().getOpeningTime(),
				minutesPerRow);

		JList<String> rowHeader = createRowHeader();
		setRowHeaderView(rowHeader);

		calendarTable.updateCalendar(rowHeaderValues, columnType, day);
		this.getVerticalScrollBar().setValue(verticalBarPos);
		this.setVisible(true);
	}

	/**
	 * Der Renderer für die RowHeader-Liste.
	 * 
	 * @author Alex
	 */
	private class RowHeaderRenderer extends JLabel implements
			ListCellRenderer<Object> {

		/**  */
		private static final long serialVersionUID = 1L;

		/**
		 * Creates the renderer.
		 * 
		 * @param table
		 *            The table from which the column header style will be
		 *            adapted for the row header.
		 */
		public RowHeaderRenderer(JTable table) {
			JTableHeader header = table.getTableHeader();
			setOpaque(true);
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			setHorizontalAlignment(CENTER);
			setForeground(header.getForeground());
			setBackground(header.getBackground());
			setFont(header.getFont());
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	private JList<String> createRowHeader() {
		ListModel<String> listModel = new AbstractListModel<String>() {
			/**  */
			private static final long serialVersionUID = 1L;

			public int getSize() {
				return rowHeaderValues.size() / 2;
			}

			public String getElementAt(int index) {
				return CalendarUtil.getHourMinuteString(rowHeaderValues
						.get(index * 2));
			}

		};
		JList<String> rowHeader = new JList<String>(listModel);
		rowHeader.setFixedCellWidth(fixedRowHeaderWidth);
		rowHeader.setFixedCellHeight((fixedRowHeight + fixedRowMargin) * 2);
		rowHeader.setCellRenderer(new RowHeaderRenderer(new JTable()));
		return rowHeader;
	}

	/**
	 * Fills the rowHeaderValues List with Time-Strings depending on the opening
	 * times and the minutes per row.
	 */
	private void generateRowHeaderValues(
			TimeOfDayInterval[] timeOfDayIntervals, int minutesPerRow) {
		if (timeOfDayIntervals != null && timeOfDayIntervals.length >= 1) {
			// get maximum and minimum times
			Date min = null;
			Date max = null;
			for (TimeOfDayInterval timeOfDayInterval : timeOfDayIntervals) {
				if (min == null && max == null) {
					if (timeOfDayInterval != null) {
						min = timeOfDayInterval.getBegin();
						max = timeOfDayInterval.getEnd();
					}
				} else {
					if (timeOfDayInterval != null) {
						if (timeOfDayInterval.getBegin().before(min)) {
							min = timeOfDayInterval.getBegin();
						}
						if (timeOfDayInterval.getEnd().after(max)) {
							max = timeOfDayInterval.getEnd();
						}
					}
				}
			}

			if (min != null && max != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(min);
				int startTime = calendar.get(Calendar.HOUR_OF_DAY) * 60
						+ calendar.get(Calendar.MINUTE);
				calendar.setTime(max);
				int endTime = calendar.get(Calendar.HOUR_OF_DAY) * 60
						+ calendar.get(Calendar.MINUTE);

				rowHeaderValues.clear();
				for (int i = startTime; i < endTime; i += minutesPerRow) {
					int hours = i / 60;
					int minutes = i % 60;
					Date date = CalendarUtil.getDefaultDate(hours, minutes);
					rowHeaderValues.add(date);
				}
			}

		}
	}

	/**
	 * Gibt den aktuellen Typ zur&uuml;ck.
	 * 
	 * @return der Typ der angezeigt wird.
	 */
	public ColumnType getColumnType() {
		return columnType;
	}

}
