package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class CreditsGio extends GameObject {

	public CreditsGio(GameContext game) {
		super(game, "credits-gio");
		size.set(8, 4);
		floating = true;
		
		restitution = 1;
		
		zIndex = 2000;
	}

}
