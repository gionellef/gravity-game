package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;
import com.megahard.gravity.objects.GravWell;
import com.megahard.gravity.objects.Player;

public class TheAwakening extends Script {

	private boolean firstRun = true;
	
	public TheAwakening(Engine game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onEnter(GameObject object) {
		if(!firstRun) return;

		if(object.getClass().equals(Player.class)){
			firstRun = false;
			
			getGame().setCinematicMode(true);
			object.velocity.x = 0;
			
			// create well
			GravWell gw = new GravWell(getGame());
			gw.power = 1.2;
			gw.position.set(object.position.x, object.position.y - 6);
			getGame().addObject(gw);
			
			object.setSprite("person");
			getGame().setCinematicMode(false);
		}
	}

	@Override
	public void onExit(GameObject object) {
	}

}
