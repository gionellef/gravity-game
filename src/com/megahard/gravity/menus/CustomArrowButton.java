package com.megahard.gravity.menus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * JButton object that draws a scaled Arrow in one of the cardinal directions.
 * <p>
 * <strong>Warning:</strong> Serialized objects of this class will not be
 * compatible with future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running the
 * same version of Swing. As of 1.4, support for long term storage of all
 * JavaBeans&trade; has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @author David Kloba
 * 
 *         Lean Rada
 */
public class CustomArrowButton extends BasicArrowButton implements
		MouseListener {

	private static final long serialVersionUID = 1L;

	private static final Color HOVER_COLOR = new Color(1, 1, 1, 0.4f);
	private static final Color PRESS_COLOR = Color.white;

	private static final Color ARROW_COLOR = Color.white;
	private static final Color ARROW_PRESS_COLOR = Color.black;

	private boolean hover = false;

	/**
	 * Creates a {@code BasicArrowButton} whose arrow is drawn in the specified
	 * direction.
	 *
	 * @param direction
	 *            the direction of the arrow; one of
	 *            {@code SwingConstants.NORTH}, {@code SwingConstants.SOUTH},
	 *            {@code SwingConstants.EAST} or {@code SwingConstants.WEST}
	 */
	public CustomArrowButton(int direction) {
		super(direction);
		addMouseListener(this);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == this) {
			hover = true;
		}
	}

	public void mouseExited(MouseEvent e) {
		if (e.getSource() == this) {
			hover = false;
		}
	}

	public void paint(Graphics g) {
		Color origColor;
		boolean isPressed, isEnabled;
		int w, h, size;

		w = getSize().width;
		h = getSize().height;
		origColor = g.getColor();
		isPressed = getModel().isPressed();
		isEnabled = isEnabled();

		if (isPressed) {
			g.setColor(PRESS_COLOR);
			g.fillRect(0, 0, w, h);
		} else if (hover) {
			g.setColor(HOVER_COLOR);
			g.fillRect(0, 0, w, h);
		}

		// / Draw the proper Border
		if (getBorder() != null && !(getBorder() instanceof UIResource)) {
			paintBorder(g);
		}

		// If there's no room to draw arrow, bail
		if (h < 5 || w < 5) {
			g.setColor(origColor);
			return;
		}

		// Draw the arrow
		size = Math.min((h - 4) / 3, (w - 4) / 3);
		size = Math.max(size, 2);
		paintTriangle(g, (w - size) / 2, (h - size) / 2, size, direction,
				isEnabled);

		g.setColor(origColor);

	}

	/**
	 * Paints a triangle.
	 *
	 * @param g
	 *            the {@code Graphics} to draw to
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param size
	 *            the size of the triangle to draw
	 * @param direction
	 *            the direction in which to draw the arrow; one of
	 *            {@code SwingConstants.NORTH}, {@code SwingConstants.SOUTH},
	 *            {@code SwingConstants.EAST} or {@code SwingConstants.WEST}
	 * @param isEnabled
	 *            whether or not the arrow is drawn enabled
	 */
	public void paintTriangle(Graphics g, int x, int y, int size,
			int direction, boolean isEnabled) {
		Color oldColor = g.getColor();
		Color color = getModel().isPressed() ? ARROW_PRESS_COLOR : ARROW_COLOR;
		int mid, i, j;

		j = 0;
		size = Math.max(size, 2);
		mid = (size / 2) - 1;

		g.translate(x, y);
		if (isEnabled)
			g.setColor(color);
		else
			g.setColor(color);

		switch (direction) {
		case NORTH:
			for (i = 0; i < size; i++) {
				g.drawLine(mid - i, i, mid + i, i);
			}
			if (!isEnabled) {
				g.setColor(color);
				g.drawLine(mid - i + 2, i, mid + i, i);
			}
			break;
		case SOUTH:
			if (!isEnabled) {
				g.translate(1, 1);
				g.setColor(color);
				for (i = size - 1; i >= 0; i--) {
					g.drawLine(mid - i, j, mid + i, j);
					j++;
				}
				g.translate(-1, -1);
				g.setColor(color);
			}

			j = 0;
			for (i = size - 1; i >= 0; i--) {
				g.drawLine(mid - i, j, mid + i, j);
				j++;
			}
			break;
		case WEST:
			for (i = 0; i < size; i++) {
				g.drawLine(i, mid - i, i, mid + i);
			}
			if (!isEnabled) {
				g.setColor(color);
				g.drawLine(i, mid - i + 2, i, mid + i);
			}
			break;
		case EAST:
			if (!isEnabled) {
				g.translate(1, 1);
				g.setColor(color);
				for (i = size - 1; i >= 0; i--) {
					g.drawLine(j, mid - i, j, mid + i);
					j++;
				}
				g.translate(-1, -1);
				g.setColor(color);
			}

			j = 0;
			for (i = size - 1; i >= 0; i--) {
				g.drawLine(j, mid - i, j, mid + i);
				j++;
			}
			break;
		}
		g.translate(-x, -y);
		g.setColor(oldColor);
	}

}
