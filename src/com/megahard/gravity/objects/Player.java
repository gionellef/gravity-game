package com.megahard.gravity.objects;

import java.awt.event.KeyEvent;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;

public class Player extends GameObject {
	
	private GravWell well = null;

	public Player(Engine game) {
		super(game, "person");
		size.set(0.9, 1.6);
		mass = 3;
		restitution = 0.05;
		friction = 0.6;
		
		sprite.setAction("run");
	}
	
	@Override
	public void update() {
		super.update();

		if(getGame().keyIsJustPressed(KeyEvent.VK_W)){
			if(standing){
				velocity.y -= 0.6;
			}
		}
		if(getGame().keyIsDown(KeyEvent.VK_A)){
			if(standing){
				velocity.x -= 0.2;
			}else{
				velocity.x -= 0.04;
			}
		}
		if(getGame().keyIsDown(KeyEvent.VK_D)){
			if(standing){
				velocity.x += 0.2;
			}else{
				velocity.x += 0.04;
			}
		}
		
		if(getGame().mouseLeftIsJustPressed()){
			if(well != null){
				getGame().removeObject(well);
			}
			well = new GravWell(getGame());
			well.position = getGame().getMouseGamePosition();
			getGame().addObject(well);
		}
	}

}
