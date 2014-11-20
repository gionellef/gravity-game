package com.megahard.gravity;

import javax.swing.JButton;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
 
 
public class CustomButton extends JButton implements MouseListener { 
	 
	private static final long serialVersionUID = 1L;
	private Color mouseOverColor;
	 
	public CustomButton(String text, Color mouseOverColor) { 
		super(text);
		
		this.setFont(new Font(text,10,25));
		this.setForeground(Color.white);
		this.setFocusPainted(false);
		opaquize (false);
		
		this.mouseOverColor = mouseOverColor;
		addMouseListener(this);
	}
	
	

	public void mouseClicked(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	  
	public void mouseEntered(MouseEvent e) { 
		if(e.getSource()==this) {  this.setBackground(this.mouseOverColor); opaquize(true);}
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