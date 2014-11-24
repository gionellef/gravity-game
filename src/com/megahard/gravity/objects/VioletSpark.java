package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class VioletSpark extends GameObject {

	public VioletSpark(GameContext game) {
		super(game, "violet-spark");
		size.set(0.01, 0.01);
		mass = 0.001;
		restitution = 1;
		
		zIndex = 1250;
	}

	@Override
	public void update() {
		super.update();
		velocity = velocity.times(0.9);
		velocity.y -= GameObject.GRAVITY;
	}
	
	@Override
	public void onEndAction(String action) {
		getGame().removeObject(this);
	}

}
