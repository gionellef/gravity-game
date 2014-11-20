package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;

public class Gravity2 extends Script {

	private boolean firstRun = true;
	private boolean active;
	private int timer = 0;
	
	public Gravity2(Engine game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onUpdate() {
		if(active){
			timer++; 
			if(timer == 100){
				getGame().setCinematicMode(false);
				active = false;
			}
		}
	}

	@Override
	public void onEnter(GameObject object) {
		if(firstRun){
			getGame().setCinematicMode(true);
			getGame().showMessage("isaac", "What is this place?", 100);
			active = true;
			firstRun = false;
		}
	}

	@Override
	public void onExit(GameObject object) {
		// TODO Auto-generated method stub

	}

}
