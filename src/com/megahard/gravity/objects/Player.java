package com.megahard.gravity.objects;

import java.awt.event.KeyEvent;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Vector2;

public class Player extends GameObject {
	
	private GravWell well = null;
	private boolean isRunning = true;
	private boolean isFalling = true;
	private int jumpsLeft = 0;

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
			} else if(!isRunning){
				switch (sprite.getAction()) {
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

		if(standing){
			jumpsLeft = 1;
		}
		isRunning = false;
		
		// Controls
		if(getGame().keyIsDown(KeyEvent.VK_A)){
			run(true);
		}
		if(getGame().keyIsDown(KeyEvent.VK_D)){
			run(false);
		}
		if(getGame().keyIsJustPressed(KeyEvent.VK_W)){
			jump();
		}

		if(getGame().keyIsJustPressed(KeyEvent.VK_E)){
			if(well != null){
				well.destroy();
				well = null;
			}
		}
		if(getGame().mouseLeftIsJustPressed()){
			conjureGrav(getGame().getMouseGamePosition());
		}
	}

	private void conjureGrav(Vector2 pos) {
		if(well != null) well.destroy();
		well = new GravWell(getGame());
		well.position = pos;
		getGame().addObject(well);
	}

	private void run(boolean left) {
		double runStrength = 0.2;
		double airStrength = 0.06;
		double airMaxSpeed = 0.2;
		double sign = left ? -1 : 1;
		String action = sprite.getAction();
		
		if(standing && !action.equals("land")){
			isRunning = true;
			velocity.x += runStrength * sign;
			switch (action) {
			case "run":
				break;
			default:
				sprite.setAction("run");
				break;
			}
		}else{
			if(velocity.x * sign < airMaxSpeed){
				velocity.x += airStrength * sign;
			}
		}
	}

	private void jump() {
		double jumpStrength = 0.5;
		
		if(standing){
			jumpsLeft++;
		}
		if(jumpsLeft > 0 && velocity.y < jumpStrength){
			jumpsLeft--;
			velocity.y = Math.min(velocity.y, -jumpStrength);
			sprite.setAction("jump");
		}
	}

}
