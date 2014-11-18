package com.megahard.gravity.menus;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.megahard.gravity.CustomButton;
import com.megahard.gravity.GravityApplet;

public class RetryMenu extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private String name = "Retry?";

	public Container c;
	private JFrame retryScreen;
	private CustomButton retryButton;
	private CustomButton menuButton;
	
	public RetryMenu(GravityApplet app) {
		retryScreen = new JFrame ("Retry Menu");
		
		retryButton = new CustomButton("Retry Level", new Color(34,47,91), new Color(14,26,64));
		retryButton.setPreferredSize(new Dimension(200,75));
		retryButton.setBounds(0,150,400,75);
		retryButton.addActionListener(app);
		
		menuButton = new CustomButton("Back to Main Menu", new Color(34,47,91), new Color(14,26,64));
		menuButton.setPreferredSize(new Dimension(200,75));
		menuButton.setBounds(400, 150, 400, 75);
		menuButton.addActionListener(app);
		
		retryScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = retryScreen.getContentPane();
		
		this.setLayout(null);
		this.add(retryButton);
		this.add(menuButton);
		c.add(this);
		
		this.setPreferredSize(new Dimension(800,600));
		retryScreen.pack();
		
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(new Color(10,10,10));
		g.fillRect(0, 0, GravityApplet.WIDTH, GravityApplet.HEIGHT);
		
		g.setFont(new Font(name, 10,50));
		g.setColor(new Color(240,240,240));
		g.drawString(name, 325, 100);
	}
	
	public JButton getRetryButton() {
		return retryButton;
	}
	
	public JButton getMenuButton() {
		return menuButton;
	}

}
