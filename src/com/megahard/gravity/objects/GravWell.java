package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Sound;
import com.megahard.gravity.Vector2;

public class GravWell extends GameObject {

	private boolean online = true;
	private int time = 0;
	
	public GravWell(Engine game) {
		super(game, "gravwell");
		fixed = true;
		
		sprite.setAction("create");
		getGame().playSoundAtLocation(Sound.gravwell_start, position.x, position.y, 0);
	}

	@Override
	public void update() {
		super.update();
		
		time++;
		
		if(online){
			getGame().playSoundAtLocation(Sound.gravwell, position.x, position.y, Math.random() * 2 - 1);
			for(GameObject o : getGame().getState().objects){
				Vector2 diff = position.sub(o.position);
				double d = diff.length();
				if(d > 1){
					double str = 0.1 + 0.5f/time;
					double accel = str / (d * d);
					if(accel > 0.01){
						o.velocity = o.velocity.add(diff.scale(accel));
					}
				}else{
					o.velocity = o.velocity.add(diff.sub(o.velocity).scale(0.1f));
				}
			}
			
			if(Math.random() < 1){
				double a = Math.random() * Math.PI * 2;
				double r = Math.random() * 2;
				double x = position.x + Math.cos(a) * r;
				double y = position.y + Math.sin(a) * r;
				if(!getGame().getMap().getTile(x, y).getCollidable()){
					VioletSpark s = new VioletSpark(getGame());
					s.position.set(x, y);
					s.velocity.set(Math.cos(a - Math.PI/3) * -r/3, Math.sin(a - Math.PI/3) * -r/3 - 0.2);
					getGame().addObject(s);
				}
			}
		}
	}
	
	@Override
	public void onEndAction(String action) {
		if(action.equals("destroy")){
			getGame().removeObject(this);
		}else if(action.equals("default")){
			if(!online){
				sprite.setAction("destroy");
			}
		}
	}

	public void destroy() {
		online = false;
	}
	
}
