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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

import com.megahard.gravity.GravityApplet;

public class LevelMenu extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private String name = "Level Select";
	
	public Container c;
	private CustomButton playButton;
	private BasicArrowButton left;
	private BasicArrowButton right;
	private JLabel levelLabel;
	
	public static int lastMap = 0;
	public ArrayList<String[]> maps;

	private Image background;
	
	public LevelMenu (GravityApplet app) {
		storeMaps();
		
		setLayout(new BorderLayout());
		
		JPanel innerPanel = new JPanel();
		innerPanel.setOpaque(false);
		innerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel titleLabel = new JLabel(name, SwingConstants.CENTER);
		titleLabel.setFont(titleLabel.getFont().deriveFont(40f));
		titleLabel.setPreferredSize(new Dimension(800, 200));
		titleLabel.setForeground(Color.white);
		
		playButton = new CustomButton("Play", innerPanel);
		playButton.setPreferredSize(new Dimension(200,75));
		playButton.addActionListener(app);

		left = new CustomArrowButton(CustomArrowButton.WEST);
		left.setPreferredSize(new Dimension(50,50));
		left.addActionListener(app);
		
		right = new CustomArrowButton(CustomArrowButton.EAST);
		right.setPreferredSize(new Dimension(50,50));
		right.addActionListener(app);
		
		levelLabel = new JLabel (maps.get(lastMap)[0], SwingConstants.CENTER);
		levelLabel.setPreferredSize(new Dimension(200, 75));
		levelLabel.setForeground(new Color (240,240,240));


		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		innerPanel.add(titleLabel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		innerPanel.add(left, c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		innerPanel.add(levelLabel, c);
		
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		innerPanel.add(right, c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		innerPanel.add(playButton, c);
		
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
	
	public void setLevelLabel(String newstring) {
		this.levelLabel.setText(newstring);
		this.levelLabel.setBounds((200/2 - levelLabel.getText().length()*5/2) + 275, 150, 200, 75);
		repaint();
	}

	public String getLevelLabel() {	
		return levelLabel.getText();
	}
	
	public JButton getPlayButton() {
		return playButton;
	}
	
	public BasicArrowButton getLeft() {
		return left;
	}
	
	public BasicArrowButton getRight() {
		return right;
	}
	
	public void storeMaps() {
		maps = new ArrayList<String[]>();

		InputStream in = getClass().getResourceAsStream("/map/maps.txt");
		Scanner scanner = new Scanner(in);
		
		while(scanner.hasNextLine()){
			String[] values = scanner.nextLine().split(",");
			//String mapName = values[0];
			//String fileName = values[1];
			maps.add(values);
		}
		
		scanner.close();
	}

}
