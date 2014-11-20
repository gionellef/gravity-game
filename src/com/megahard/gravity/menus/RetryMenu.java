package com.megahard.gravity.menus;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.megahard.gravity.CustomButton;
import com.megahard.gravity.GravityApplet;

public class RetryMenu extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private String name = "Retry Level?";

	public Container c;
	private JFrame retryScreen;
	private CustomButton retryButton;
	private CustomButton menuButton;
	
	public RetryMenu(GravityApplet app) {
		retryScreen = new JFrame ("Retry Menu");
		
		menuButton = new CustomButton("Back to Main Menu", Color.black);
		menuButton.setPreferredSize(new Dimension(200,75));
		menuButton.setBounds(0, 150, 800, 75);
		menuButton.addActionListener(app);
		
		retryButton = new CustomButton("Retry", Color.black);
		retryButton.setPreferredSize(new Dimension(200,75));
		retryButton.setBounds(0, 225, 800, 75);
		retryButton.addActionListener(app);
		
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
	
	public JButton getRetryButton() {
		return retryButton;
	}
	
	public JButton getMenuButton() {
		return menuButton;
	}
	
}
