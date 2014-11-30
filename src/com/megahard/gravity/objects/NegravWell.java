package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.util.Sound;
import com.megahard.gravity.util.Vector2;

public class NegravWell extends GameObject {

	public double power = 1;
	
	private boolean online = true;
	private int time = 0;
	
	public NegravWell(GameContext game) {
		super(game, "negravwell");
		mass = 0;
		fixed = true;
		
		zIndex = 300;
	}
	
	public void init(){
		sprite.setAction("create");
		getGame().playSoundAtLocation(Sound.gravwell_start, position, 0.8);
	}

	@Override
	public void update() {
		super.update();
		
		time++;
		
		if(online || (sprite.getAction().equals("create") && sprite.getFrame() == 0)){
			double radius = 19;
			for(GameObject o : getGame().findObjects(position.x - radius, position.y - radius, 2*radius, 2*radius, true)){
				Vector2 diff = position.minus(o.position);
				double d = diff.length();
				double str = 0.12 + 0.78/time;
				double accel = -power * str / (d * d + 1);
				if(accel < -0.001){
					o.velocity.add(diff.times(accel));
				}
			}
		}

		if(Math.random() < 0.5){
			spark();
		}
	}

	private void spark() {
		double a = Math.random() * Math.PI * 2;
		double r = Math.random() * 2;
		double x = position.x + Math.cos(a) * r;
		double y = position.y + Math.sin(a) * r;
		if(!getGame().getMap().getTile(x, y).getCollidable()){
			OrangeSpark s = new OrangeSpark(getGame());
			s.position.set(x, y);
			getGame().addObject(s);
		}
	}
	
	@Override
	public void onStartAction(String action) {
		getGame().playSoundAtLocation(Sound.gravwell, position, 0.5);
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

	public void die() {
		online = false;
	}
	
}
