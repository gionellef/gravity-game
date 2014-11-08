package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Vector2;

public class GravWell extends GameObject {

	public GravWell(Engine game) {
		super(game, "gravwell"); // TODO insert gravwell sprite path
		fixed = true;
		sprite.setAction("create");
	}

	@Override
	public void update() {
		super.update();
		
		for(GameObject o : getGame().getState().objects){
			Vector2 diff = position.sub(o.position);
			double d = diff.length();
			if(d > 0){
				o.velocity = o.velocity.add(diff.scale(0.1f / (d * d)));
			}else{
				
			}
		}
	}
	
}
