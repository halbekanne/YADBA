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

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import model.Appointment;
import model.CalendarUtil;
import model.Course;
import model.Employee;
import model.PracticalAppointment;
import model.Room;
import model.TimeOfDayInterval;
import model.Treatment;
import model.Vacation;
import view.calendar.MainCalendar.ColumnType;
import view.util.spantable.AttributiveCellTableModel;
import view.util.spantable.CellSpan;
import view.util.spantable.MultiSpanCellTable;
import view.util.spantable.MultiSpanCellTableUI;
import controller.OfficeController;

/**
 * @author Dominik Halfkann
 * 
 */
public class CalendarTable extends MultiSpanCellTable {

	private static final long serialVersionUID = 1L;

	private List<Date> rowHeaderValues;
	private final OfficeController officeCtrl;
	private ColumnType columnType;
	private Date day;
	private int firstRow = 0;
	private int lastRow;

	private Color bgAppNormal = new Color(128, 210, 79);
	private Color bgAppNormalSelect = new Color(179, 220, 108);
	private Color bgAppClosed = new Color(22, 167, 101);
	private Color bgAppClosedSelect = new Color(66, 214, 146);
	private Color bgAppPastOpen = new Color(250, 209, 101);
	private Color bgAppPastOpenSelect = new Color(251, 233, 131);
	private Color bgCourseNormal = new Color(159, 198, 231);
	private Color bgCourseNormalSelect = new Color(159, 225, 231);
	private Color bgVacation = new Color(172, 114, 94);
	private Color bgVacationSelect = new Color(208, 107, 100);
	private Color bgNotAvailable = new Color(194, 194, 194);

	private Color borderAppNormal = new Color(79, 185, 19);
	private Color borderAppNormalSelect = new Color(147, 192, 11);
	private Color borderAppClosed = new Color(0, 125, 57);
	private Color borderAppClosedSelect = new Color(80, 182, 142);
	private Color borderAppPastOpen = new Color(191, 150, 8);
	private Color borderAppPastOpenSelect = new Color(189, 182, 52);
	private Color borderCourseNormal = new Color(21, 135, 189);
	private Color borderCourseNormalSelect = new Color(11, 188, 178);
	private Color borderVacation = new Color(117, 72, 30);
	private Color borderVacationSelect = new Color(146, 68, 32);

	public CalendarTable(List<Date> rowHeaderValues,
			OfficeController officeController, ColumnType columnType, Date day) {
		super();
		if (rowHeaderValues != null && rowHeaderValues.size() > 0)
			setUI(new MultiSpanCellTableUI(rowHeaderValues.get(0)));
		this.rowHeaderValues = rowHeaderValues;
		this.officeCtrl = officeController;
		this.columnType = columnType;
		this.day = CalendarUtil.standardizeTime(day);
		this.lastRow = rowHeaderValues.size() - 1;
		this.setModel(new CalendarTableModel(rowHeaderValues.size(),
				createColumnNames()));
		this.createClosedTime();
		this.createAppointments();
		initializeTable();
		Thread r = new Thread() {

			@Override
			public void run() {
				while (true) {
					CalendarTable.this.repaint();
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						break;
					}
				}
			}

		};
		r.start();
	}

	public void updateCalendar(List<Date> rowHeaderValues,
			ColumnType columnType, Date day) {
		if (CalendarUtil.sameDay(new Date(), day) && rowHeaderValues != null
				&& rowHeaderValues.size() > 0) {
			setUI(new MultiSpanCellTableUI(rowHeaderValues.get(0)));
		} else {
			setUI(new MultiSpanCellTableUI());
		}
		this.rowHeaderValues = rowHeaderValues;
		this.columnType = columnType;
		this.day = day;
		this.clearSelection();
		this.setModel(new CalendarTableModel(rowHeaderValues.size(),
				createColumnNames()));
		this.createClosedTime();
		this.createAppointments();
		initializeTable();
	}

	private Object[] createColumnNames() {
		List<String> columnIdentifiers = new ArrayList<String>();
		if (columnType == ColumnType.Employee) {
			List<Employee> employees = officeCtrl.getOffice().getEmployees();
			for (int i = 0; i < employees.size(); i++) {
				Employee employee = employees.get(i);
				columnIdentifiers.add(employee.toString());
			}
			return columnIdentifiers.toArray();
		} else {
			List<Room> rooms = officeCtrl.getOffice().getRooms();
			for (int i = 0; i < rooms.size(); i++) {
				Room room = rooms.get(i);
				columnIdentifiers.add(room.getName());
			}
			return columnIdentifiers.toArray();
		}
	}

	private void initializeTable() {
		this.setRowHeight(MainCalendar.fixedRowHeight);
		this.setRowMargin(MainCalendar.fixedRowMargin);
		this.getColumnModel().setColumnMargin(0);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TableCellRenderer cellRenderer = new CalendarCellRenderer();
		for (int i = 0; i < this.getColumnCount(); i++) {
			TableColumn col = this.getColumnModel().getColumn(i);
			col.setMinWidth(MainCalendar.fixedColumnWidth);
			col.setCellRenderer(cellRenderer);
		}
	}

	private void createAppointments() {
		if (columnType == ColumnType.Employee) {
			List<Employee> employees = officeCtrl.getOffice().getEmployees();
			for (int i = 0; i < employees.size(); i++) {
				Employee employee = employees.get(i);
				createEmployeeUnavailableTime(employee, i);
				createCellDataByAppointments(
						officeCtrl
								.getEmployeeController()
								.getAppointmentsInTime(employee,
										CalendarUtil.createDayTimeInterval(day)),
						i);
			}
		} else {
			List<Room> rooms = officeCtrl.getOffice().getRooms();
			for (int i = 0; i < rooms.size(); i++) {
				Room room = rooms.get(i);
				createCellDataByAppointments(
						officeCtrl
								.getRoomController()
								.getPracticalAppoinmentsInTime(room,
										CalendarUtil.createDayTimeInterval(day)),
						i);
			}
		}
	}

	private void createClosedTime() {
		int weekday = CalendarUtil.getDayOfWeek(day);
		TimeOfDayInterval openingTime = officeCtrl.getOffice().getOpeningTime()[weekday];
		if (openingTime == null) {
			for (int a = 0; a < rowHeaderValues.size(); a++) {
				// make row gray
				for (int col = 0; col < this.getModel().getColumnCount(); col++) {
					makeCellGrey(a, col);
				}
			}
		} else {
			if (rowHeaderValues.size() > 0) {
				int a;
				for (a = 0; rowHeaderValues.get(a).before(
						openingTime.getBegin()); a++) {
					// make row gray
					for (int col = 0; col < this.getModel().getColumnCount(); col++) {
						makeCellGrey(a, col);
					}
				}
				firstRow = a;

				for (a = rowHeaderValues.size() - 1; rowHeaderValues.get(a)
						.after(openingTime.getEnd()); a--) {
					// make row gray
					for (int col = 0; col < this.getModel().getColumnCount(); col++) {
						makeCellGrey(a, col);
					}
				}
				lastRow = a;
			}
		}
	}

	private void createEmployeeUnavailableTime(Employee employee, int column) {
		int weekday = CalendarUtil.getDayOfWeek(day);
		TimeOfDayInterval workingTime = employee.getWorkingTime()[weekday];
		if (workingTime == null) {
			for (int a = 0; a < rowHeaderValues.size(); a++) {
				// make row gray
				makeCellGrey(a, column);
			}
		} else {
			if (rowHeaderValues.size() > 0) {
				int a;
				for (a = 0; rowHeaderValues.get(a).before(
						workingTime.getBegin()); a++) {
					// make row gray
					makeCellGrey(a, column);
				}
				firstRow = a;

				for (a = rowHeaderValues.size() - 1; rowHeaderValues.get(a)
						.after(workingTime.getEnd()); a--) {
					// make row gray
					makeCellGrey(a, column);
				}
				lastRow = a;
			}
		}
	}

	private class GrayCell {
	}

	private void makeCellGrey(int row, int column) {
		this.setValueAt(new GrayCell(), row, column);
	}

	private void createCellDataByAppointments(
			List<? extends Appointment> appointments, int column) {
		for (Appointment appointment : appointments) {
			int row = getRowForAppointment(appointment, column);

			if (row != -1) {
				// find rows to merge
				CellSpan cellAtt = (CellSpan) ((AttributiveCellTableModel) getModel())
						.getCellAttribute();
				List<Integer> rowList = new ArrayList<Integer>();

				for (int rowCount = row; rowCount <= lastRow
						&& CalendarUtil.combineDateTime(day,
								this.rowHeaderValues.get(rowCount)).before(
								appointment.getTime().getEnd()); rowCount++) {
					if (appointment instanceof Vacation
							&& this.getValueAt(rowCount, column) != null) {
						break;
					}
					rowList.add(rowCount);
					// rowCount++;
				}

				// for (int a = 0; a <
				// appointment.getTime().getDurationInMinutes() && rowCount <
				// lastRow; a += MainCalendar.minutesPerRow) {
				// System.out.println(appointment.getTime().getDurationInMinutes());
				//
				// }
				if (rowList.size() > 1)
					cellAtt.combine(listToIntArray(rowList),
							new int[] { column });

				// add value in table
				this.setValueAt(appointment, row, column);
			}
		}
	}

	private int getRowForAppointment(Appointment appointment, int column) {
		for (int i = 0; i < rowHeaderValues.size(); i++) {
			Date rowDate = rowHeaderValues.get(i);
			if (appointment.getTime().getBegin().before(day)) {
				if (appointment instanceof Vacation) {
					for (int a = firstRow; a < rowHeaderValues.size(); a++) {
						if (this.getValueAt(a, column) == null)
							return a;
					}
				} else {
					return firstRow;
				}
			} else {
				Date appDate = CalendarUtil.standardizeDate(appointment
						.getTime().getBegin());
				if (rowDate.after(appDate)) {
					if (appointment instanceof Vacation) {
						for (int a = i - 1; a < rowHeaderValues.size(); a++) {
							if (this.getValueAt(a, column) == null)
								return a;
						}
					} else {
						return i - 1;
					}
				}
			}
		}
		return -1;
	}

	private int[] listToIntArray(List<Integer> l) {
		int[] r = new int[l.size()];

		for (int i = 0; i < r.length; i++)
			r[i] = l.get(i).intValue();

		return r;
	}

	private class CalendarCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			label.setHorizontalAlignment(JLabel.LEFT);
			label.setVerticalAlignment(JLabel.TOP);

			if (value != null && value instanceof Appointment) {
				// value is string

				Color bgColor;
				Color borderColor;

				if (value instanceof PracticalAppointment) {
					// behandlungstermin
					if (!((PracticalAppointment) value).isOpen()) {
						// appointment is closed
						if (isSelected) {
							bgColor = bgAppClosedSelect;
							borderColor = borderAppClosedSelect;
						} else {
							bgColor = bgAppClosed;
							borderColor = borderAppClosed;
						}
					} else {
						if (((PracticalAppointment) value).getTime()
								.compareToNow() < 0) {
							// appointment is open and in the past
							if (isSelected) {
								bgColor = bgAppPastOpenSelect;
								borderColor = borderAppPastOpenSelect;
							} else {
								bgColor = bgAppPastOpen;
								borderColor = borderAppPastOpen;
							}
						} else {
							// appointment is open and in the future
							if (value instanceof Course) {
								if (isSelected) {
									bgColor = bgCourseNormalSelect;
									borderColor = borderCourseNormalSelect;
								} else {
									bgColor = bgCourseNormal;
									borderColor = borderCourseNormal;
								}
							} else {

								if (isSelected) {
									bgColor = bgAppNormalSelect;
									borderColor = borderAppNormalSelect;
								} else {
									bgColor = bgAppNormal;
									borderColor = borderAppNormal;
								}
							}
						}
					}
				} else if (value instanceof Vacation) {
					// fehlzeit
					if (isSelected) {
						bgColor = bgVacationSelect;
						borderColor = borderVacationSelect;
					} else {
						bgColor = bgVacation;
						borderColor = borderVacation;
					}
				} else {
					bgColor = this.getBackground();
					borderColor = getGridColor();
				}

				label.setBackground(bgColor);
				label.setBorder(BorderFactory.createMatteBorder(5, 1, 1, 1,
						borderColor));

				// if (value instanceof Vacation) {
				// //label.setBackground(Color.yellow);
				// } else if (isSelected) {
				// label.setBackground(new Color(177, 210, 158));
				// } else if (value instanceof PracticalAppointment &&
				// !((PracticalAppointment) value).isOpen()) {
				// label.setBackground(new Color(22, 167, 101));
				// label.setBorder(BorderFactory.createMatteBorder(2, 2, 1, 1,
				// new Color(0, 125, 57)));
				// } else if (value instanceof PracticalAppointment &&
				// ((PracticalAppointment) value).isOpen()
				// && ((PracticalAppointment) value).getTime().compareToNow() >=
				// 0) {
				// label.setBackground(new Color(128, 210, 79));
				// } else if (value instanceof PracticalAppointment &&
				// ((PracticalAppointment) value).isOpen()
				// && ((PracticalAppointment) value).getTime().compareToNow() <
				// 0) {
				// label.setBackground(new Color(255, 215, 0));
				// }

			} else if (value instanceof GrayCell) {
				label.setBackground(bgNotAvailable);
				label.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1,
						getGridColor()));
			} else {
				// if (isSelected) {
				// label.setBackground(super.getBackground());
				// } else {
				label.setBackground(Color.white);
				// }

				label.setBorder(null);
			}

			return label;
		}

		@Override
		protected void setValue(Object value) {
			setText((value == null) ? "" : getStringFromAppointment(value));
		}

		private String getStringFromAppointment(Object element) {
			if (element instanceof Appointment) {
				if (element instanceof PracticalAppointment) {
					if (element instanceof Course) {
						// Kurs
						Course app = (Course) element;
						String output = getDefaultStartHtlm(app)
								+ "<span style=\"font-size:8.5px;\"><b>"
								+ app.getActivity().getName() + " (Kurs/"
								+ app.getPatients().size() + " Personen)"
								+ "</b></span><br>"
								+ "<span style=\"font-size:8px;\">Bei "
								+ app.getEmployee().toString() + " ("
								+ app.getRoom().getName() + ")" + "</span>"
								+ getDefaultEndHtml();
						return output;
					} else if (element instanceof Treatment) {
						// Behandlung
						Treatment app = (Treatment) element;
						String output = getDefaultStartHtlm(app)
								+ "<span style=\"font-size:8.5px;\"><b>"
								+ app.getActivity().getName() + " bei "
								+ app.getPatient().toString()
								+ "</b></span><br>"
								+ "<span style=\"font-size:8px;\">Beh. von "
								+ app.getEmployee().toString() + " ("
								+ app.getRoom().getName() + ")" + "</span>"
								+ getDefaultEndHtml();
						return output;
					} else {
						return null;
					}

				} else {
					Vacation app = (Vacation) element;
					String output = "<html>"
							+ "<p style=\"margin:0px 3px;\">"
							+ "<i style=\"font-size:8px;\">"
							+ "Start:"
							+ CalendarUtil.dateTimeToString(app.getTime()
									.getBegin())
							+ "<br />"
							+ "Ende:"
							+ CalendarUtil.dateTimeToString(app.getTime()
									.getEnd()) + "</i><br />"
							+ "<span style=\"font-size:10px;\"><b>Urlaub"
							+ "</b></span>" + getDefaultEndHtml();
					return output;
				}

			} else if (element instanceof GrayCell) {
				return "";
			} else {
				return null;
			}
		}

		private String getDefaultStartHtlm(Appointment app) {
			String output = "<html>"
					+ "<p style=\"margin:-1px 3px;\">"
					+ "<span style=\"font-size:8px;\"><i>"
					+ CalendarUtil
							.getHourMinuteString(app.getTime().getBegin())
					+ " - "
					+ CalendarUtil.getHourMinuteString(app.getTime().getEnd())
					+ "</i>";
			if (app instanceof PracticalAppointment) {
				PracticalAppointment pa = (PracticalAppointment) app;
				if (!pa.isOpen()) {
					output += "<b> (Closed)</b>";
				}
			}
			output += "</span><br />";
			return output;
		}

		private String getDefaultEndHtml() {
			String output = "</p>" + "</html>";
			return output;
		}

	}

	private class CalendarTableModel extends AttributiveCellTableModel {

		private static final long serialVersionUID = 1L;

		public CalendarTableModel(int rowCount, Object[] columnNames) {
			super(columnNames, rowCount);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

	}

}
