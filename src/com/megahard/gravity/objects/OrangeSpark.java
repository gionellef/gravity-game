package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class OrangeSpark extends GameObject {

	public OrangeSpark(GameContext game) {
		super(game, "orange-spark");
		size.set(0.01, 0.01);
		floating = true;
		mass = 0.001;
		restitution = 1;
		
		zIndex = 1250;
	}

	@Override
	public void update() {
		super.update();
		velocity = velocity.times(0.9);
	}
	
	@Override
	public void onEndAction(String action) {
		getGame().removeObject(this);
	}

}
