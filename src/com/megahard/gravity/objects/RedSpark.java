package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class RedSpark extends GameObject {

	public RedSpark(GameContext game) {
		super(game, "red-spark");
		size.set(0.01, 0.01);
		mass = 0.001;
		restitution = 1;
		
		zIndex = 1250;
	}
	
	@Override
	public void update() {
		super.update();
		velocity = velocity.times(0.8);
	}
	
	@Override
	public void onEndAction(String action) {
		getGame().removeObject(this);
	}

}
