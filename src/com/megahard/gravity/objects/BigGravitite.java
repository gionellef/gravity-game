package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class BigGravitite extends GameObject {

	public BigGravitite(GameContext game) {
		super(game, "big-gravitite");
		size.set(1.125, 1.125);
		mass = 12;
		friction = 0.6;
		staticFriction = 0;
		
		zIndex = 540;
	}

	@Override
	public void onCollide(GameObject obj) {
		if(obj.getClass().equals(getClass())){
			velocity = velocity.plus(position.minus(obj.position).normalized().times(0.01f));
		}
	}
	
	@Override
	public void kill() {
		super.kill();
		
		for(int i=0; i<3; i++){
			Gravitite g = new Gravitite(getGame());
			g.position.set(position);
			g.velocity.set(velocity);
			g.velocity.add(Math.random() * 0.2-0.1, Math.random() * 0.2-0.1);
			getGame().addObject(g);
		}
		
		castSparks(15, 0.2);
	}

	private void castSparks(int n, double r) {
		for(int i=0; i<n; i++){
			VioletSpark s = new VioletSpark(getGame());
			s.position.set(position);
			s.velocity.set(r * (Math.random() * 2 - 1), r * (Math.random() * 2 - 1));
			getGame().addObject(s);
		}
	}
	
	
}
