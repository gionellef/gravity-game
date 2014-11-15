package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Vector2;

public class GravWell extends GameObject {

	private boolean active = true;
	
	public GravWell(Engine game) {
		super(game, "gravwell");
		fixed = true;
		sprite.setAction("create");
	}

	@Override
	public void update() {
		super.update();
		
		if(active){
			for(GameObject o : getGame().getState().objects){
				Vector2 diff = position.sub(o.position);
				double d = diff.length();
				if(d > 1){
					o.velocity = o.velocity.add(diff.scale(0.1f / (d * d)));
				}else{
					o.velocity = o.velocity.add(diff.sub(o.velocity).scale(0.1f));
				}
			}
		}
	}
	
	@Override
	public void onEndAction(String action) {
		if(action.equals("destroy")){
			getGame().removeObject(this);
		}else if(action.equals("default")){
			if(!active){
				sprite.setAction("destroy");
			}
		}
	}

	public void destroy() {
		active = false;
	}
	
}
