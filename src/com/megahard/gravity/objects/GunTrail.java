package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.util.Vector2;

public class GunTrail extends GameObject {

	private GameObject target = null;
	private double targetFollow = 0;
	private Vector2 oldVelocity = new Vector2();
	
	public GunTrail(GameContext game) {
		super(game, "gun-trail");
		floating = true;
		restitution = 0.5;
		
		zIndex = 1300;
	}

	@Override
	public void update() {
		super.update();
		
		if(target != null){
			velocity.add(target.velocity.minus(oldVelocity).times(targetFollow));
			oldVelocity = target.velocity;
		}
		
		zIndex--;
	}

	@Override
	public void onEndAction(String action) {
		getGame().removeObject(this);
	}

	public void setTarget(GameObject target, double t) {
		this.target = target;
		targetFollow = t;
	}
}
