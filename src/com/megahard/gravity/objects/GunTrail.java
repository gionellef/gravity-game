package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class GunTrail extends GameObject {

	public GunTrail(GameContext game) {
		super(game, "gun-trail");
		floating = true;
		restitution = 0.5;
		
		zIndex = 1300;
	}

	@Override
	public void update() {
		super.update();
		
		zIndex--;
	}

	@Override
	public void onEndAction(String action) {
		getGame().removeObject(this);
	}
}
