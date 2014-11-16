package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Sound;

public class DeadlyObj extends GameObject {

	public DeadlyObj(Engine game) {
		super(game, "deadlyobj");
		size.set(0.95, 0.95);
		fixed = true;
	}
	
	public void init(){
		sprite.setAction("default");
		sprite.setFrame((int)(Math.random() * sprite.getTotalFrames()));
	}
	
	@Override
	public void update() {
		super.update();
		
		if(Math.random() < 0.05){
			getGame().playSoundAtLocation(Sound.spark, position, 0.4);
			for(int i = 1 + (int) (Math.random() * 4); i >= 0; i--){
				RedSpark s = new RedSpark(getGame());
				s.position.set(position.x, position.y);
				double a = Math.random() * Math.PI * 2;
				double r = Math.random() * 0.5;
				s.velocity.set(Math.cos(a) * r, Math.sin(a) * r - 0.2);
				getGame().addObject(s);
			}
		}
	}
	
	@Override
	public void onEndAction(String action) {
		getGame().playSoundAtLocation(Sound.plasma, position, 0.5);
	}

}
