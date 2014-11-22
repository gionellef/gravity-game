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
		if(obj.getClass().equals(Gravitite.class)){
			velocity = velocity.add(position.sub(obj.position).normalize().scale(0.01f));
		}
	}
}
