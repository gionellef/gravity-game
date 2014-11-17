package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;

public class VioletSpark extends GameObject {

	public VioletSpark(Engine game) {
		super(game, "violet-spark");
		size.set(0.01, 0.01);
		mass = 0.01;
		restitution = 1;
	}

	@Override
	public void update() {
		super.update();
		velocity = velocity.scale(0.9);
	}
	
	@Override
	public void onEndAction(String action) {
		getGame().removeObject(this);
	}

}
