package com.megahard.gravity.scripts;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;

public class Gravity1 extends Script {
	
	private int timer = 0;

	public Gravity1(Engine game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
		getGame().fadeScreen(null, Color.black, 1);
		getGame().setCinematicMode(true);
	}

	@Override
	public void onUpdate() {
		timer++;
		
		if(timer == 50){
			getGame().showMessage("isaac-pre", "Where am I?", 100);
		}
		if(timer == 100){
			getGame().fadeScreen(Color.black, null, 100);
		}
		if(timer == 200){
			getGame().showMessage("Use 'A' and 'D' to move, and 'W' or 'Space' to jump.", 100);
		}
		if(timer == 250){
			getGame().setCinematicMode(false);
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
