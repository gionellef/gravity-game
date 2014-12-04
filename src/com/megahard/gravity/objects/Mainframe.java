package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class Mainframe extends GameObject {

	public Mainframe(GameContext game, String spriteName) {
		super(game, "mainframe");
		fixed = true;
		
		zIndex = 120;
	}

}
