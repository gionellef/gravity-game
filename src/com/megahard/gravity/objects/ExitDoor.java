package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;

public class ExitDoor extends GameObject {

	public ExitDoor(Engine game) {
		super(game, "door");
		fixed = true;
	}

}
