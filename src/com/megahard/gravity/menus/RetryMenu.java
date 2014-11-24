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

public class RetryMenu extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private String name = "Retry Level?";

	private CustomButton retryButton;
	private CustomButton menuButton;

	private Image background;
	
	public RetryMenu(GravityApplet app) {
		setLayout(new BorderLayout());
		
		JPanel innerPanel = new JPanel();
		innerPanel.setOpaque(false);
		innerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel titleLabel = new JLabel(name, SwingConstants.CENTER);
		titleLabel.setFont(titleLabel.getFont().deriveFont(40f));
		titleLabel.setPreferredSize(new Dimension(800, 200));
		titleLabel.setForeground(Color.white);
		
		retryButton = new CustomButton("Retry");
		retryButton.setPreferredSize(new Dimension(200,75));
		retryButton.addActionListener(app);
		
		menuButton = new CustomButton("Quit");
		menuButton.setPreferredSize(new Dimension(200,75));
		menuButton.addActionListener(app);
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		innerPanel.add(titleLabel, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		innerPanel.add(retryButton, c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		innerPanel.add(menuButton, c);
		
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

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, this);
	}
	
	public JButton getRetryButton() {
		return retryButton;
	}
	
	public JButton getMenuButton() {
		return menuButton;
	}
	
}
