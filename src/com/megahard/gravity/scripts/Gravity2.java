package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class Gravity2 extends ScriptSequencer {
	
	public Gravity2(GameContext game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onEnter(GameObject object) {
		addMessage("isaac-pre", "What is this place?", 100);
		addMessage("Press 'Enter' to skip dialogue (in this case, monologue (in general, cutscenes))", 1000);
		
		beginSequence(true, true, true);
	}

	@Override
	public void onExit(GameObject object) {
	}

	@Override
	protected void onSkip() {
	}

	@Override
	protected void onEnd() {
	}

}
