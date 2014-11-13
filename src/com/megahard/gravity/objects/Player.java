package com.megahard.gravity.objects;

import java.awt.event.KeyEvent;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Vector2;

public class Player extends GameObject {
	
	private GravWell well = null;
	private boolean isFalling = true; 

	public Player(Engine game) {
		super(game, "person");
		size.set(0.9, 1.6);
		mass = 3;
		restitution = 0.05;
		friction = 0.6;
	}
	
	@Override
	public void update() {
		super.update();
		
		// Animations
		if(standing){
			if (isFalling) {
				isFalling = false;
				sprite.setAction("land");
			} else {
				switch (sprite.getAction()) {
				case "run":
				case "land":
				case "default":
					break;
				default:
					sprite.setAction("default");
					break;
				}
			}
		} else {
			isFalling = true;
			switch (sprite.getAction()) {
			case "jump":
			case "fall":
				break;
			default:
				sprite.setAction("fall");
				break;
			}
		}
		
		// Controls
		if(getGame().keyIsJustPressed(KeyEvent.VK_W)){
			jump();
		}
		if(getGame().keyIsDown(KeyEvent.VK_A)){
			run(true);
		}
		if(getGame().keyIsDown(KeyEvent.VK_D)){
			run(false);
		}
		
		if(getGame().mouseLeftIsJustPressed()){
			conjureGrav(getGame().getMouseGamePosition());
		}
	}

	private void conjureGrav(Vector2 pos) {
		if(well != null){
			getGame().removeObject(well);
		}
		well = new GravWell(getGame());
		well.position = pos;
		getGame().addObject(well);
	}

	private void run(boolean left) {
		double sign = left ? -1 : 1;
		if(standing){
			velocity.x += 0.2 * sign;
		}else{
			velocity.x += 0.08 * sign;
		}
	}

	private void jump() {
		if(standing){
			velocity.y -= 0.6;
			sprite.setAction("jump");
		}
	}

}
