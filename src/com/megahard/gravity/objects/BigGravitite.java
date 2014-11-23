package com.megahard.gravity.objects;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameObject;

public class BigGravitite extends GameObject {

	public BigGravitite(GameContext game) {
		super(game, "big-gravitite");
		size.set(1.125, 1.125);
		mass = 3;
		friction = 0.6;
		staticFriction = 0;
		
		zIndex = 540;
	}

	@Override
	public void onCollide(GameObject obj) {
		if(obj.getClass().equals(getClass())){
			velocity = velocity.add(position.sub(obj.position).normalize().scale(0.01f));
		}
	}
}
