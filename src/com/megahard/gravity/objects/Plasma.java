package com.megahard.gravity.objects;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Sound;

public class Plasma extends GameObject {

	public Plasma(GameContext game) {
		super(game, "plasma");
		size.set(0.7, 0.7);
		mass = 0.5;
		fixed = true;
		
		zIndex = 500;
	}
	
	public void init(){
		sprite.setAction("default");
		sprite.setFrame((int)(Math.random() * sprite.getTotalFrames()));
	}
	
	@Override
	public void onCollide(GameObject obj) {
		if(obj.getClass().equals(Player.class)){
			obj.kill();
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		if(Math.random() < 0.02){
			int n = 1 + (int) (Math.random() * 6);
			getGame().playSoundAtLocation(Sound.spark, position, 0.5 + n/12.0);
			castSparks(n);
		}
	}

	private void castSparks(int n) {
		for(int i = n; i > 0; i--){
			RedSpark s = new RedSpark(getGame());
			s.position.set(position.x, position.y);
			double a = Math.random() * Math.PI * 2;
			double r = Math.random() * 0.5;
			s.velocity.set(Math.cos(a) * r, Math.sin(a) * r - 0.2);
			getGame().addObject(s);
		}
	}
	
	@Override
	public void onStartAction(String action) {
		getGame().playSoundAtLocation(Sound.plasma, position, 1);
	}

}
