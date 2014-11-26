package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class Sentinel extends GameObject {

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
	}
}
