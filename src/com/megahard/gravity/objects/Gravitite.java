package com.megahard.gravity.objects;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameObject;

public class Gravitite extends GameObject {

	public Gravitite(GameContext game) {
		super(game, "gravitite");
		size.set(0.65, 0.65);
		mass = 1;
		friction = 0.6;
		staticFriction = 0;
		
		zIndex = 550;
	}

	@Override
	public void onCollide(GameObject obj) {
		if(obj.getClass().equals(getClass())){
			velocity = velocity.plus(position.minus(obj.position).normalized().times(0.01f));
		}
	}
	
	@Override
	public void kill() {
		super.kill();
		castSparks(3, 0.1);
	}

	private void castSparks(int n, double r) {
		for(int i=0; i<n; i++){
			VioletSpark s = new VioletSpark(getGame());
			s.position.set(position);
			s.velocity.set(r * (Math.random() * 2 - 1), r * (Math.random() * 2 - 1));
			getGame().addObject(s);
		}
	}
	
}
