package com.megahard.gravity.objects;

import java.awt.event.KeyEvent;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;

public class Player extends GameObject {

	public Player(Engine game) {
		super(game, "person");
		size.set(0.9, 1.6);
		mass = 3;
		restitution = 0.05;
		friction = 0.6;
		
		sprite.setAction("run");
	}
	
	@Override
	public void update() {
		super.update();

		if(getGame().keyIsJustPressed(KeyEvent.VK_W)){
			// jump 
		}
		// TODO if etc
	}

}
