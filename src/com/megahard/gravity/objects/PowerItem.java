package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;

public class PowerItem extends GameObject {

	public PowerItem(Engine game) {
		super(game, "power-item");
		size.set(0.65, 0.65);
		mass = 1;
		friction = 0.6;
		staticFriction = 0;
	}

	@Override
	public void onCollide(GameObject obj) {
		if(obj.getClass().equals(PowerItem.class)){
			velocity = velocity.add(position.sub(obj.position).normalize().scale(0.01f));
		}
	}
}
