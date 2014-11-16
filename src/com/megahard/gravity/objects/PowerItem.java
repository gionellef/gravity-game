package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;

public class PowerItem extends GameObject {

	public PowerItem(Engine game) {
		super(game, "power-item");
		size.set(0.65, 0.65);
		friction = 0.2;
	}

}
