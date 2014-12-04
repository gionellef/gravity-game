package com.megahard.gravity.menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.megahard.gravity.GravityApplet;

public class TitleScreen extends JPanel{

	private static final long serialVersionUID = 1L;


	public String name = "Gravity: The Awakening";

	
	private CustomButton startButton;
	private CustomButton exitButton;
	public static JPanel innerPanel = new JPanel();

	private Image background;
	
	public TitleScreen(GravityApplet app) {
		setLayout(new BorderLayout());
		

		innerPanel.setOpaque(false);
		innerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel titleLabel = new JLabel(name, SwingConstants.CENTER);
		titleLabel.setFont(titleLabel.getFont().deriveFont(40f));
		titleLabel.setPreferredSize(new Dimension(800, 200));
		titleLabel.setForeground(Color.white);
		
		startButton = new CustomButton("Play");
		startButton.setPreferredSize(new Dimension(200,75));
		startButton.addActionListener(app);
		
		exitButton = new CustomButton("Exit");
		exitButton.setPreferredSize(new Dimension(200,75));
		exitButton.addActionListener(app);
		
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
