package com.megahard.gravity;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.*;

public class TitleScreen extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String name = "Gravity omg";
	
	private Container c;
	private JFrame menuscreen;
	
	private JButton startButton;
	private JButton exitButton;
	
	public TitleScreen(GravityApplet app) {
		menuscreen = new JFrame ("Gravity Menu");
		
		startButton = new JButton("New Game");
		startButton.setPreferredSize(new Dimension(200,75));
		startButton.setBounds(0,150,800,75);
		startButton.setBackground(new Color(26,71,42));
		startButton.setForeground(new Color(170,170,170));
		startButton.setFocusPainted(false);
		startButton.addMouseListener(app);
		startButton.addActionListener(app);
		
		exitButton = new JButton("Quit");
		exitButton.setPreferredSize(new Dimension(200,75));
		exitButton.setBounds(0, 225, 800, 75);
		exitButton.setBackground(new Color(26,71,42));
		exitButton.setForeground(new Color(170,170,170));
		exitButton.setFocusPainted(false);
		exitButton.addMouseListener(app);
		exitButton.addActionListener(app);
		
		menuscreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = menuscreen.getContentPane();
		
		this.setLayout(null);
		this.add(getStartButton());
		this.add(exitButton);
		c.add(this);
		
		this.setPreferredSize(new Dimension(800,600));
		menuscreen.pack();
		
	}
	
	public void paintComponent(Graphics g) {
		g.setFont(new Font(name, 10,50));
		g.setColor(new Color(192,192,192));
		g.drawString(name, 75, 75);
	}

	public JButton getStartButton() {
		return startButton;
	}
	
	public JButton getExitButton() {
		return exitButton;
	}

}
