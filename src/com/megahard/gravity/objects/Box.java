package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class Box extends GameObject {

	public Box(GameContext game) {
		super(game, "box");
		size.set(1.5, 1.5);
		mass = 40;
		restitution = 0.1;
		friction = 0.7;
		staticFriction = 0;
		
		zIndex = 400;
	}

}
