package com.megahard.gravity.scripts;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class Gravity1 extends ScriptSequencer {
	
	public Gravity1(GameContext game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
		addRunnable(new Runnable(){
			@Override
			public void run() {
				getGame().fadeScreen(null, Color.black, 1);	
			}
		}, 50);
		addMessage("isaac-pre", "Where am I?", 100);
		
		beginSequence(true, true, true);
	}

	@Override
	public void onEnter(GameObject object) {
	}

	@Override
	public void onExit(GameObject object) {
	}

	@Override
	protected void onSkip() {
		onEnd();
	}

	@Override
	protected void onEnd() {
		getGame().fadeScreen(Color.black, null, 100);
		getGame().showMessage("Use 'A' and 'D' to move, and 'W' or 'Space' to jump.", 200);	
	}

}
