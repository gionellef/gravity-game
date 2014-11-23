package com.megahard.gravity.menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.megahard.gravity.CustomButton;
import com.megahard.gravity.GravityApplet;

public class TitleScreen extends JPanel{

	private static final long serialVersionUID = 1L;


	public String name = "Gravity: The Awakening";

	
	public Container c;
	private JPanel menuscreen;
	
	private CustomButton startButton;
	private CustomButton exitButton;

	private Image background;
	
	public TitleScreen(GravityApplet app) {
		setForeground(Color.white);
		
		setLayout(new BorderLayout());
		
		JPanel innerPanel = new JPanel();
		innerPanel.setOpaque(false);
		innerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel titleLabel = new JLabel(name, SwingConstants.CENTER);
		titleLabel.setPreferredSize(new Dimension(800, 200));
		titleLabel.setForeground(Color.white);
		titleLabel.setFont(GravityApplet.fontTitle);
		
		startButton = new CustomButton("Play", Color.black);
		startButton.setPreferredSize(new Dimension(200,75));
		startButton.addActionListener(app);
		startButton.setFont(GravityApplet.font);
		
		exitButton = new CustomButton("Quit", Color.black);
		exitButton.setPreferredSize(new Dimension(200,75));
		exitButton.addActionListener(app);
		exitButton.setFont(GravityApplet.font);
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		innerPanel.add(titleLabel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		innerPanel.add(startButton, c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		innerPanel.add(exitButton, c);
		
		add(innerPanel);
		
		this.setPreferredSize(new Dimension(800,600));

		background = null;
		try {
			background = ImageIO.read(getClass().getResource("/back.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, this);
	}
	
	public JButton getStartButton() {
		return startButton;
	}
	
	public JButton getExitButton() {
		return exitButton;
	}
}
