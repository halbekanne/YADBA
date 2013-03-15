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

/**
 * @version 1.0 11/22/98
 */

public interface CellSpan {
  public final int ROW    = 0;
  public final int COLUMN = 1;
  
  public int[] getSpan(int row, int column);
  public void setSpan(int[] span, int row, int column);
  
  public boolean isVisible(int row, int column);
  
  public void combine(int[] rows, int[] columns);
  public void split(int row, int column);

}
