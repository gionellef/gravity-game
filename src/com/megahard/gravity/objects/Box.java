package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;

public class Box extends GameObject {

	public Box(Engine game) {
		super(game, "box");
		size.set(1.5, 1.5);
		mass = 40;
		restitution = 0.1;
		friction = 0.7;
		staticFriction = 0;
	}

}
