package com.megahard.gravity.menus;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
 
 
public class CustomButton extends JButton implements MouseListener { 
	 
	private static final long serialVersionUID = 1L;
	private static final Color DEFAULT_COLOR = new Color(1, 1, 1, 0.4f);
	private JPanel innerPanel = new JPanel(); 
	
	public CustomButton(String text, JPanel innerPanel){
		this(text, DEFAULT_COLOR, innerPanel);
	}
	
	public CustomButton(String text, Color mouseOverColor, JPanel i) { 
		super(text);
		
		opaquize (false);
		
		setBackground(mouseOverColor);
		addMouseListener(this);
		this.innerPanel = i;
	}

	public void mouseClicked(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) {
		if(e.getSource()==this) { opaquize(false);
		repaint();}
	}
	  
	public void mouseEntered(MouseEvent e) { 
		if(e.getSource()==this) { opaquize(true);}
	}

	public void mouseExited(MouseEvent e) { 
		if(e.getSource()==this) { opaquize(false);}
	}
	
	private void opaquize(boolean o) {
		this.setOpaque(o);
		this.setContentAreaFilled(o);
		this.setBorderPainted(o);
		innerPanel.repaint();
	}

}