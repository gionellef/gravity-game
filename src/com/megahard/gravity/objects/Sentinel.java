package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class Sentinel extends GameObject {

	private boolean isFacingLeft = false;
	
	public Sentinel(GameContext game) {
		super(game, "sentinel");
		size.set(1,1);
		floating = true;
		mass = 15;
		restitution = 1;
		friction = 0.5;
		staticFriction = 0;
		
		zIndex = 600;
	}

	@Override
	public void update() {
		super.update();
		
		velocity.scale(0.9);
	}
	
	public void move(boolean left){
		double p = 0.1;
		double sign = left ? -1 : 1;
		isFacingLeft = left;
		velocity.x += p * sign;
		setSpriteAction("fly", new String[]{"fly"});
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
