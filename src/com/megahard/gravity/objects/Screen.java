package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class Screen extends GameObject {

	public Screen(GameContext game) {
		super(game, "screen");
		fixed = true;
		
		zIndex = 130;
	}

	@Override
	public void init() {
		sprite.setFrame((int) (sprite.getTotalFrames() * Math.random()));
	}
}
