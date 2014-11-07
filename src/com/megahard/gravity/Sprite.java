package com.megahard.gravity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import com.megahard.gravity.SpriteStore.SpriteAction;
import com.megahard.gravity.SpriteStore.SpriteData;

public class Sprite {
	private Image image;
	private Rectangle region;
	private SpriteData data;
	
	private SpriteAction currentAction = null;
	private int currentFrame = 0;
	
	public Sprite(Image image, SpriteData data) {
		this.image = image;
		this.data = data;
		region = new Rectangle(0, 0, data.width, data.height);
		
		setAction("default");
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
	
	public void setAction(String action){
		SpriteAction act = getActionData(action);
		if(act != null){
			currentAction = act;
			setFrame(currentAction.x, currentAction.y);
			currentFrame = 0;
		}
	}
	
	private SpriteAction getActionData(String action) {
		for(SpriteAction a : data.actions){
			if(action.equals(a.name)){
				return a;
			}
		}
		return null;
	}
	
	public void update(){
		if(currentAction != null){
			currentFrame++;
			if(currentFrame >= currentAction.frames){
				currentFrame = 0;
			}
			int curX = currentAction.x + currentFrame;
			int curY = currentAction.y;
			while(curX >= data.sheetWidth){
				curX -= data.sheetWidth;
				curY++;
			}
			setFrame(curX, curY);
		}
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
