package com.megahard.gravity;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class TitleScreen extends JPanel{

	public String name = "Gravity omg";
	
	private Container c;
	private JFrame menuscreen;
	
	private JButton startButton;
	private JButton exitButton;
	
	public TitleScreen(GravityApplet app) {
		menuscreen = new JFrame ("Gravity Menu");
		
		startButton = new JButton("New Game");
		getStartButton().setPreferredSize(new Dimension(200,75));
		getStartButton().setBounds(120,150,200,75);
		getStartButton().addActionListener(app);
		
		exitButton = new JButton("Give up");
		exitButton.setPreferredSize(new Dimension(200,75));
		exitButton.setBounds(120, 225, 200, 75);
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
		g.setColor(Color.pink);
		g.drawString(name, 75, 75);
	}

	public JButton getStartButton() {
		return startButton;
	}
	
	public JButton getExitButton() {
		return exitButton;
	}

}
