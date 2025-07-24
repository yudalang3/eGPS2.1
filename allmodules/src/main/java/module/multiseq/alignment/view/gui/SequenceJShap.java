package module.multiseq.alignment.view.gui;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Copyright (c) 2019 Chinese Academy of Sciences. All rights reserved.
 * 
 * @ClassName SequenceJShap
 * 
 * @author mhl
 * 
 * @Date Created on:2019-07-11 14:14
 * 
 */
public abstract class SequenceJShap {
	/**
	 * The tool tip
	 */
	protected String tipText;

	/**
	 * 
	 * Tests if the specified coordinates are inside the boundary of the JShape.
	 * 
	 * @param x, y - the specified coordinates
	 * @returns true if the specified coordinates are inside the Shape boundary;
	 *          false otherwise.
	 */
	public abstract boolean contains(double x, double y);

	/**
	 * The tooltip text is changable when you move the cursor.
	 */
	public synchronized String getTipText() {
		return tipText;
	}

	public abstract void repaint();

	/**
	 * Initializes the start position and width and height information of the
	 * selected element when the specified position information is in JShape
	 * 
	 * @param x, y - the specified coordinates
	 * 
	 * @author mhl
	 *
	 * @Date Created on:2019-07-16 17:00
	 */
	public abstract void assignValuesIfMousePointMeetShap(List<UserSelectedViewElement> selectionElements, double x, double y);

	/**
	 *
	 * @see {@link egps.remnant.multiple.seq.alignment.viewer.gui.SequenceJShap#assignValuesIfMousePointMeetShap}
	 *
	 * @Date Created on:2019-07-16 17:04
	 */
	public abstract void assignValuesIfMousePointMeetShaps(List<UserSelectedViewElement> selectionElements, Rectangle2D dragRect);

}
