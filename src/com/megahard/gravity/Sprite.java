package com.megahard.gravity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import com.megahard.gravity.SpriteStore.SpriteData;

public class Sprite {
	private Image image;
	private Rectangle region;
	
	public Sprite(Image image, SpriteData data) {
		this.image = image;
		region = new Rectangle(0, 0, data.width, data.height);
	}
	
	public int getWidth() {
		return region.width;
	}
	
	public int getHeight() {
		return region.height;
	}
	
	public void setFrame(int x, int y) {
		region.x = region.width * x;
		region.y = region.height * y;
	}
	
	/*
	 * Draws the sprite in the g
	 */
	public void draw(Graphics2D g, int x, int y) {
		g.drawImage(image,
			x, y, x + region.width, y + region.height,
			region.x, region.y, region.x + region.width, region.y + region.height,
			null);
	}
	
}
