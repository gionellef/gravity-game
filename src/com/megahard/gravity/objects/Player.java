package com.megahard.gravity.objects;

import java.awt.event.KeyEvent;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Vector2;

public class Player extends GameObject {
	
	private GravWell well = null;
	private boolean isRunning = true;
	private boolean isFalling = true;
	private boolean isFacingLeft = false;
	private int gravsLeft = 0;
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
				setSpriteAction("land");
			} else if(!isRunning){
				setSpriteAction("default", new String[]{"land", "conjure", "default"});
			}
		} else {
			isFalling = true;
			setSpriteAction("fall", new String[]{"jump", "fall"});
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
		if(gravsLeft > 0){
			gravsLeft--;
			
			if(well != null) well.destroy();
			
			well = new GravWell(getGame());
			well.position = pos;
			getGame().addObject(well);
			
			if(sprite.getAction().startsWith("default")){
				setSpriteAction("conjure");
			}
		}
	}

	private void run(boolean left) {
		double runStrength = 0.2;
		double airStrength = 0.06;
		double airMaxSpeed = 0.2;
		double sign = left ? -1 : 1;
		isFacingLeft = left;
		if(standing && !sprite.getAction().startsWith("land")){
			isRunning = true;
			velocity.x += runStrength * sign;
			setSpriteAction("run", new String[]{"run"});
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
			setSpriteAction("jump");
		}
	}
	
	private void setSpriteAction(String action){
		setSpriteAction(action, null);
	}

	private void setSpriteAction(String action, String[] inhibit){
		if(inhibit != null){
			boolean cont = false;
			String current = sprite.getAction();
			if(current.endsWith("-left")){
				current = current.substring(0, current.length() - 5);
				if(!isFacingLeft && action.equals(current)){
					cont = true;
				}
			}else{
				if(isFacingLeft && action.equals(current)){
					cont = true;
				}
			}
			if(!cont)
				for(String i : inhibit)
					if(i.equals(current)) return;
		}
		
		if(isFacingLeft){
			action += "-left";
		}
		sprite.setAction(action);
	}
	
}
