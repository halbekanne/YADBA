/*******************************************************************************
 * Copyright (c) 2013 Dominik Halfkann, Michael Backhaus, Benjamin Kramer,
 * Fabian König, Karl Stelzner, Stefan Noll and Alexander Schieweck.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
/*
 * (swing1.1beta3)
 * 
 */

package view.util.spantable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Date;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableCellRenderer;

import model.CalendarUtil;
import view.calendar.MainCalendar;

/**
 * @version 1.0 11/26/98
 */

public class MultiSpanCellTableUI extends BasicTableUI {

	private int startTime = -1;
	@SuppressWarnings("unused")
	private Random random = new Random();

	public MultiSpanCellTableUI() {
		super();
	}
	
	public MultiSpanCellTableUI(Date startDate) {
		super();
		if (startDate != null) {
			startTime = CalendarUtil.getMinutesInDay(startDate);
		}
	}

	public void paint(Graphics g, JComponent c) {
		Rectangle oldClipBounds = g.getClipBounds();
		Rectangle clipBounds = new Rectangle(oldClipBounds);
		int tableWidth = table.getColumnModel().getTotalColumnWidth();
		clipBounds.width = Math.min(clipBounds.width, tableWidth);
		g.setClip(clipBounds);

		int firstIndex = table.rowAtPoint(new Point(0, clipBounds.y));
		int lastIndex = table.getRowCount() - 1;

		Rectangle rowRect = new Rectangle(0, 0, tableWidth, table.getRowHeight() + table.getRowMargin());
		rowRect.y = firstIndex * rowRect.height;

		for (int index = firstIndex; index <= lastIndex; index++) {
			if (rowRect.intersects(clipBounds)) {
				paintRow(g, index);
			}
			rowRect.y += rowRect.height;
		}
		if (startTime != -1) {
			g.setColor(Color.red);
			int height = getHeightForTimeLine();
			g.drawLine(0, height, tableWidth, height);
			g.setFont(new Font("System", Font.PLAIN, 10));
			g.drawString(CalendarUtil.getHourMinuteString(new Date()), 2, height-2);
		}
		g.setClip(oldClipBounds);
	}
	
	private int getHeightForTimeLine() {
		Date current = new Date();
		//int currentTime = (int)(random.nextFloat() * 4*60) + startTime;
		int currentTime = CalendarUtil.getMinutesInDay(current);
		int minutes = currentTime - startTime;
		return (int)(minutes / (MainCalendar.minutesPerRow * 1.0) * MainCalendar.fixedRowHeight)-1;
	}

	private void paintRow(Graphics g, int row) {
		Rectangle rect = g.getClipBounds();
		boolean drawn = false;

		AttributiveCellTableModel tableModel = (AttributiveCellTableModel) table.getModel();
		CellSpan cellAtt = (CellSpan) tableModel.getCellAttribute();
		int numColumns = table.getColumnCount();

		for (int column = 0; column < numColumns; column++) {
			Rectangle cellRect = table.getCellRect(row, column, true);
			int cellRow, cellColumn;
			if (cellAtt.isVisible(row, column)) {
				cellRow = row;
				cellColumn = column;
				// System.out.print("   "+column+" "); // debug
			} else {
				cellRow = row + cellAtt.getSpan(row, column)[CellSpan.ROW];
				cellColumn = column + cellAtt.getSpan(row, column)[CellSpan.COLUMN];
				// System.out.print("  ("+column+")"); // debug
			}
			if (cellRect.intersects(rect)) {
				drawn = true;
				paintCell(g, cellRect, cellRow, cellColumn);
			} else {
				if (drawn)
					break;
			}
		}

	}

	private void paintCell(Graphics g, Rectangle cellRect, int row, int column) {
		int spacingHeight = table.getRowMargin();
		int spacingWidth = table.getColumnModel().getColumnMargin();

		Color c = g.getColor();
		g.setColor(table.getGridColor());
		if (row % 2 != 0) {
			g.drawLine(cellRect.x, cellRect.y + (cellRect.height - 1), cellRect.x + (cellRect.width - 1), cellRect.y
					+ (cellRect.height - 1));
		}
		g.drawLine(cellRect.x + (cellRect.width - 1), cellRect.y, cellRect.x + (cellRect.width - 1), cellRect.y
				+ (cellRect.height - 1));
		// g.drawRect(cellRect.x,cellRect.y,cellRect.width-1,cellRect.height-1);
		g.setColor(c);

		cellRect.setBounds(cellRect.x + spacingWidth / 2, cellRect.y + spacingHeight / 2,
				cellRect.width - spacingWidth, cellRect.height - spacingHeight);

		if (table.isEditing() && table.getEditingRow() == row && table.getEditingColumn() == column) {
			Component component = table.getEditorComponent();
			component.setBounds(cellRect);
			component.validate();
		} else {
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			Component component = table.prepareRenderer(renderer, row, column);

			if (component.getParent() == null) {
				rendererPane.add(component);
			}
			rendererPane.paintComponent(g, component, table, cellRect.x - 1, cellRect.y - 1, cellRect.width + 1,
					cellRect.height + 1, true);
		}
	}
}
