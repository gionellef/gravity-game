package com.megahard.gravity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class Sprite {
	
	public static class SpriteAction {
		public String name;
		public int x;
		public int y;
		public int frames;
		public int delay;
		public String next;
	}

	static public class SpriteData{
		public int sheetWidth;
		public int width;
		public int height;
		public SpriteAction[] actions; 
	}
	
	
	private Image image;
	private Rectangle region;
	private SpriteData data;
	
	private GameObject obj = null;
	
	private SpriteAction currentAction = null;
	private int currentFrame = 0;
	private int delayCount = 0;
	
	public Sprite(Image image, SpriteData data) {
		this.image = image;
		this.data = data;
		region = new Rectangle(0, 0, data.width, data.height);
		
		setAction("default");
	}
	
	public void setListener(GameObject obj){
		this.obj = obj;
	}
	
	public int getWidth() {
		return region.width;
	}
	
	public int getHeight() {
		return region.height;
	}
	
	public void setIndex(int x, int y) {
		region.x = region.width * x;
		region.y = region.height * y;
	}
	
	public void setFrame(int i){
		if(currentAction != null){
			currentFrame = i % currentAction.frames;
			updateIndexForFrame();
		}
	}
	
	public void setAction(String action){
		setAction(getActionData(action));
	}
	
	public void setAction(SpriteAction action){
		if(action != null){
			currentAction = action;
			setIndex(currentAction.x, currentAction.y);
			currentFrame = 0;
			delayCount = 0;
		}
	}
	
	private SpriteAction getActionData(String action) {
		if(action == null){
			return null;
		}
		for(SpriteAction a : data.actions){
			if(action.equals(a.name)){
				return a;
			}
		}
		return null;
	}
	
	public void update(){
		if(currentAction != null){
			if(delayCount >= currentAction.delay){
				if(obj != null && currentFrame == 0){
					obj.onStartAction(currentAction.name);
				}
				
				currentFrame++;
				if(currentFrame >= currentAction.frames){
					SpriteAction next = getActionData(currentAction.next);
					if(next != null){
						setAction(next);
					}else{
						currentFrame = 0;
					}
					
					if(obj != null) obj.onEndAction(currentAction.name);
				}
				
				updateIndexForFrame();
								
				delayCount = 0;
			}else{
				delayCount++;
			}
		}
	}

	private void updateIndexForFrame() {
		int curX = currentAction.x + currentFrame;
		int curY = currentAction.y;
		while(curX >= data.sheetWidth){
			curX -= data.sheetWidth;
			curY++;
		}
		setIndex(curX, curY);
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

	public String getAction() {
		return currentAction == null ? null : currentAction.name;
	}

	public int getFrame() {
		return currentFrame;
	}

	public int getTotalFrames() {
		return currentAction == null ? 0 : currentAction.frames;
	}
	
}
