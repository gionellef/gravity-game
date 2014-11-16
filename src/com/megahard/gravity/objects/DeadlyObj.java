package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;

public class DeadlyObj extends GameObject {

	public DeadlyObj(Engine game) {
		super(game, "deadlyobj");
		size.set(0.95, 0.95);
		fixed = true;
	}

}
