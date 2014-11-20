package com.megahard.gravity.objects;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameObject;

public class ExitDoor extends GameObject {

	public ExitDoor(GameContext game) {
		super(game, "door");
		mass = 90;
		fixed = true;
		
		zIndex = 200;
	}

}
