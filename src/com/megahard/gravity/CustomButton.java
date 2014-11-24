package com.megahard.gravity;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
 
 
public class CustomButton extends JButton implements MouseListener { 
	 
	private static final long serialVersionUID = 1L;
	private static final Color DEFAULT_COLOR = new Color(1, 1, 1, 0.5f);
	 
	public CustomButton(String text){
		this(text, DEFAULT_COLOR);
	}
	
	public CustomButton(String text, Color mouseOverColor) { 
		super(text);
		
		opaquize (false);
		
		setBackground(mouseOverColor);
		addMouseListener(this);
	}

	public void mouseClicked(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	  
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
	}

}