package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;

public class RedSpark extends GameObject {

	public RedSpark(Engine game) {
		super(game, "red-spark");
		size.set(0.01, 0.01);
		mass = 0.001;
		restitution = 1;
		
		zIndex = 350;
	}
	
	@Override
	public void update() {
		super.update();
		velocity = velocity.scale(0.8);
	}
	
	@Override
	public void onEndAction(String action) {
		getGame().removeObject(this);
	}

}
