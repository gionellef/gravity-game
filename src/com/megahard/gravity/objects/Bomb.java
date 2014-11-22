package com.megahard.gravity.objects;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Sound;

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

		int f = sprite.getFrame();
		
		if (timeout > 48) {
			if (rotate >= 1) {
				if (!sprite.getAction().equals("rotate")) {
					sprite.setAction("rotate");
					sprite.setFrame(f);
				}
			} else {
				if (!sprite.getAction().equals("default")) {
					sprite.setAction("default");
					sprite.setFrame(f);
				}
			}
		}else{
			if (rotate >= 1) {
				if (!sprite.getAction().equals("rotate-fast")) {
					sprite.setAction("rotate-fast");
					sprite.setFrame(f);
				}
			} else {
				if (!sprite.getAction().equals("fast")) {
					sprite.setAction("fast");
					sprite.setFrame(f);
				}
			}
		}

		if(sprite.getAction().endsWith("fast")){
			if(f == 0 || f == 4){
				getGame().playSoundAtLocation(Sound.bomb_beep, position, 1);
			}
		}else{
			if(f == 0){
				getGame().playSoundAtLocation(Sound.bomb_beep, position, 0.8);
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
	
	@Override
	public void kill() {
		detonate();
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
