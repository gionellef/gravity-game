package com.megahard.gravity.objects;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameObject;

public class FallenPlatform extends GameObject {

	public FallenPlatform(GameContext game) {
		super(game, "fallen-platform");
		size.set(0.9375, 0.9375);
		mass = 400;
		restitution = 0;
		friction = 0.1;
		staticFriction = 0.5;
		
		zIndex = 100;
	}

	@Override
	public void onHitBottom() {
		fixed = true;
	}
}
