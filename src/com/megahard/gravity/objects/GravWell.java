package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Sound;
import com.megahard.gravity.Vector2;

public class GravWell extends GameObject {

	public double power = 1;
	
	private boolean online = true;
	private int time = 0;
	
	public GravWell(Engine game) {
		super(game, "gravwell");
		mass = 0;
		fixed = true;
	}
	
	public void init(){
		sprite.setAction("create");
		getGame().playSoundAtLocation(Sound.gravwell_start, position, 1);
	}

	@Override
	public void update() {
		super.update();
		
		time++;
		
		if(online){
			getGame().playSoundAtLocation(Sound.gravwell, position, 1);
			
			double radius = 12;
			for(GameObject o : getGame().findObjects(position.x - radius, position.y - radius, 2*radius, 2*radius, true)){
				Vector2 diff = position.sub(o.position);
				double d = diff.length();
				if(d > 1){
					double str = 0.1 + 0.8f/time;
					double accel = power * str / (d * d);
					if(accel > 0.005){
						o.velocity = o.velocity.add(diff.scale(accel));
					}
				}else{
					o.velocity = o.velocity.add(diff.sub(o.velocity).scale(0.1f));
				}
			}
		}

		if(Math.random() < 0.4){
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
