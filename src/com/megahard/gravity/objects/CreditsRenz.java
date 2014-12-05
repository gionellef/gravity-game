package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class CreditsRenz extends GameObject {

	public CreditsRenz(GameContext game) {
		super(game, "credits-renz");
		size.set(8, 4);
		floating = true;

		restitution = 1;
		zIndex = 2000;
	}

}
