package com.megahard.gravity;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
 
 
public class CustomButton extends JButton implements MouseListener { 
	 
	private static final long serialVersionUID = 1L;
	private Color defaultColor;
	private Color mouseOverColor;
	 
	public CustomButton(String text, Color defaultColor, Color mouseOverColor) { 
		super(text);
		setBackground(defaultColor); 
		
		this.setForeground(Color.white);
		this.setFocusPainted(false);
		
		this.defaultColor = defaultColor;
		this.mouseOverColor = mouseOverColor;
		addMouseListener(this);
	}

	public void mouseClicked(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	  
	public void mouseEntered(MouseEvent e) { 
		if(e.getSource()==this) {  this.setBackground(this.mouseOverColor); }
	}

	public void mouseExited(MouseEvent e) { 
		if(e.getSource()==this) { this.setBackground(this.defaultColor); }
	}

}