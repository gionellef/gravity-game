package com.megahard.gravity.objects;

import java.util.List;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameObject;

public class Explosion extends GameObject {

	private double radius = 1.5;
	
	public Explosion(GameContext game) {
		super(game, "explosion");
		fixed = true;
	}

	public void setRadius(double value) {
		this.radius = value;
	}

	@Override
	public void onStartAction(String action) {
		List<GameObject> objects = getGame().findObjects(position.x - radius, position.y - radius, radius * 2, radius * 2, false);
		for(GameObject o : objects){
			if(this == o) continue;
			double distance = o.position.sub(position).length();
			if(distance < radius){
				o.kill();
			}
		}
	}
	
	@Override
	public void onEndAction(String action) {
		getGame().removeObject(this);
	}
}
