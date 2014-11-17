package com.megahard.gravity;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;

public class LevelMenu extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private String name = "Level Select";
	
	public Container c;
	private JFrame levelScreen;
	private CustomButton playButton;
	private BasicArrowButton left;
	private BasicArrowButton right;
	private JLabel levelLabel;
	
	public int lastMap;
	public ArrayList<String> maps;
	
	public LevelMenu (GravityApplet app) {
		levelScreen = new JFrame ("Select Level Menu");
		
		lastMap = 0;
		
		storeMaps();
		
		playButton = new CustomButton("PLAY", new Color(34,47,91), new Color(14,26,64));
		playButton.setPreferredSize(new Dimension(200,75));
		playButton.setBounds(0,225,800,75);
		playButton.addActionListener(app);
		
		left = new BasicArrowButton(BasicArrowButton.WEST);
		left.setPreferredSize(new Dimension(50,50));
		left.setBounds(275,150,50,50);
		left.addActionListener(app);
		
		right = new BasicArrowButton(BasicArrowButton.EAST);
		right.setPreferredSize(new Dimension(50,50));
		right.setBounds(475,150,50,50);
		right.addActionListener(app);
		
		levelLabel = new JLabel (maps.get(lastMap));
		levelLabel.setPreferredSize(new Dimension(200, 75));
		levelLabel.setBounds(325, 150, 200, 75);
		levelLabel.setForeground(Color.red);
		
		levelScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = levelScreen.getContentPane();
		
		this.setLayout(null);
		this.add(playButton);
		this.add(left);
		this.add(right);
		this.add(levelLabel);
		c.add(this);
		
		this.setPreferredSize(new Dimension(800,600));
		levelScreen.pack();	
	}

	public void paintComponent(Graphics g) {
		g.setColor(new Color(10,10,10));
		g.fillRect(0, 0, GravityApplet.WIDTH, GravityApplet.HEIGHT);
		
		g.setFont(new Font(name, 10,50));
		g.setColor(new Color(240,240,240));
		g.drawString(name, 75, 100);
	}
	
	public void setLevelLabel(String newstring) {
		this.levelLabel.setText(newstring);
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
//		Pattern pattern = Pattern.compile(".json");
		maps = new ArrayList<String>();
		
		
		File folder = new File ("map/");
		File[] mapFiles = folder.listFiles();
		
		for (File mapFile : mapFiles) {
			String fileName = mapFile.getName();
			System.out.println(fileName);
			
//			boolean accept = pattern.matcher(fileName).matches();
//			if (accept) {
				maps.add(fileName.substring(0, fileName.length() - 5));
//			}
		}
	}

}
