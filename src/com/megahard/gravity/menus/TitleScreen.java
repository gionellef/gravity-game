package com.megahard.gravity.menus;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.megahard.gravity.CustomButton;
import com.megahard.gravity.GravityApplet;

public class TitleScreen extends JPanel{

	private static final long serialVersionUID = 1L;

	public String name = "Gravity: The Awakening";
	
	public Container c;
	private JFrame menuscreen;
	
	private CustomButton startButton;
	private CustomButton exitButton;
	
	
	public TitleScreen(GravityApplet app) {
		menuscreen = new JFrame ("Gravity Menu");
		
		startButton = new CustomButton("New Game", Color.black);
		startButton.setPreferredSize(new Dimension(200,75));
		startButton.setBounds(0,150,800,75);
		startButton.addActionListener(app);
		
		exitButton = new CustomButton("Quit", Color.black);
		exitButton.setPreferredSize(new Dimension(200,75));
		exitButton.setBounds(0, 225, 800, 75);
		exitButton.addActionListener(app);
		
		menuscreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = menuscreen.getContentPane();
		
		this.setLayout(null);
		this.add(startButton);
		this.add(exitButton);
		
		c.add(this);
		
		this.setPreferredSize(new Dimension(800,600));
		menuscreen.pack();
		
	}
	
	public void paintComponent(Graphics g) {
		
		Image bg = null;
		try {
			bg = ImageIO.read(getClass().getResource("/back.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		g.drawImage(bg, 0, 0, this);
		
		g.setFont(new Font(name, 10,50));
		g.setColor(new Color(240,240,240));
		g.drawString(name, (800/2-(name.length()*23/2)), 100);
	}

	public JButton getStartButton() {
		return startButton;
	}
	
	public JButton getExitButton() {
		return exitButton;
	}
}
