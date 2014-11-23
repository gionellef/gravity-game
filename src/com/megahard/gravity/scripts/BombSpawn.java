package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;
import java.util.Map;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;
import com.megahard.gravity.objects.Bomb;

public class BombSpawn extends Script {

	private int timer = 0;
	private int interval = 200;
	private int delay = 100;
	
	public BombSpawn(Engine game, Double region, Map<String, String> properties) {
		super(game, region, properties);
	}

	@Override
	public void onStart() {
		interval = Integer.parseInt(getProperty("interval"));
		delay = Integer.parseInt(getProperty("delay"));
		
		timer = delay;
	}

	@Override
	public void onUpdate() {
		timer--;
		if(timer <= 0){
			timer = interval;

			Bomb b = new Bomb(getGame());
			b.position.set(getCenter());
			getGame().addObject(b);
		}
	}

	@Override
	public void onEnter(GameObject object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExit(GameObject object) {
		// TODO Auto-generated method stub

	}

}
