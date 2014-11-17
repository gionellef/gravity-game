package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;

public class Button extends GameObject {

	private int pressed = 0;
	
	public Button(Engine game) {
		super(game, "button");
		size.set(2, 0.3);
		mass = 30;
		fixed = true;
	}
	
	@Override
	public void update() {
		super.update();
		if(pressed > 0){
			pressed--;
			sprite.setAction("pressed");
		}else{
			sprite.setAction("default");
		}
	}
	
	@Override
	public void onCollide(GameObject obj) {
		if(!obj.fixed && obj.mass > 10){
			pressed = 6;
		}
	}

}
