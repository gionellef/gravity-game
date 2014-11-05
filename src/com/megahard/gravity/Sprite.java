package com.megahard.gravity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class Sprite {
	private Image image;
	private int indexX;
	private int indexY;
	private Rectangle rec;
	
	public Sprite(Image image) {
		this.image = image;
	}
	
	public int getWidth() {
		return image.getWidth(null);
	}
	
	public int getHeight() {
		return image.getHeight(null);
	}
	
	public void setIndex(int x, int y) {
		indexX = x;
		indexY = y;
	}
	
	/*
	 * Draws the sprite in the g
	 */
	public void draw(Graphics g, int x, int y) {
		g.drawImage(image, x, y, rec.width, rec.height, indexX, indexY, rec.width, rec.height, null);
	}
	
}
