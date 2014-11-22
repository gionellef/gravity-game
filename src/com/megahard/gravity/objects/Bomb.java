package com.megahard.gravity.objects;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameObject;

public class Bomb extends GameObject {

	private int timeout = 200;
	
	private double rotate = 1;
	
	public Bomb(GameContext game) {
		super(game, "bomb");
		size.set(0.3, 0.3);
		mass = 0.1;
		restitution = 0.5;
		friction = 0.5;
		staticFriction = 0.1;
		
		zIndex = 600;
	}

	@Override
	public void init() {
	}
	
	@Override
	public void update() {
		super.update();
		
		if (timeout > 48) {
			if (rotate >= 1) {
				if (!sprite.getAction().equals("rotate")) {
					int f = sprite.getFrame();
					sprite.setAction("rotate");
					sprite.setFrame(f);
				}
			} else {
				if (!sprite.getAction().equals("default")) {
					int f = sprite.getFrame();
					sprite.setAction("default");
					sprite.setFrame(f);
				}
			}
		}else{
			if (rotate >= 1) {
				if (!sprite.getAction().equals("fast-rotate")) {
					int f = sprite.getFrame();
					sprite.setAction("fast-rotate");
					sprite.setFrame(f);
				}
			} else {
				if (!sprite.getAction().equals("fast")) {
					int f = sprite.getFrame();
					sprite.setAction("fast");
					sprite.setFrame(f);
				}
			}
		}

		if(standing){
			rotate *= 0.5;
		}
		rotate += Math.abs(velocity.y);
		
		if(timeout > 0){
			timeout--;
		}else{
			detonate();
		}
	}
	
	public void detonate(){
		getGame().removeObject(this);
		
		Explosion explosion = new Explosion(getGame());
		explosion.position.set(position);
		getGame().addObject(explosion);
	}
	
	public void setTimeout(int value){
		timeout = value;
	}
}
