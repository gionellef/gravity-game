package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;
import java.util.Map;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.engine.base.Script;
import com.megahard.gravity.objects.Bomb;

public class BombSpawn extends Script {

	private int timer = 0;
	private int interval = 200;
	private int delay = 100;
	private int timeout = 200;
	
	public BombSpawn(GameContext game, Double region, Map<String, String> properties) {
		super(game, region, properties);
	}

	@Override
	public void onStart() {
		if(getProperty("interval") != null)
			interval = Integer.parseInt(getProperty("interval"));
		if(getProperty("delay") != null)
			delay = Integer.parseInt(getProperty("delay"));
		if(getProperty("timeout") != null)
			timeout = Integer.parseInt(getProperty("timeout"));
		
		timer = delay;
	}

	@Override
	public void onUpdate() {
		timer--;
		if(timer <= 0){
			timer = interval;

			Bomb b = new Bomb(getGame());
			b.position.set(getCenter());
			b.setTimeout(timeout);
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
