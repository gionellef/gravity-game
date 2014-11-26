package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.objects.Player;

public class Escape extends ScriptSequencer {

	public Escape(GameContext game, Double region) {
		super(game, region);
	}

	@Override
	protected void onSkip() {
	}

	@Override
	protected void onEnd() {
	}

	@Override
	public void onStart() {
		addMessage("Press 'E' to use the switch.", 150);
	}

	@Override
	public void onEnter(GameObject object) {
		if(object.getClass().equals(Player.class)){
			beginSequence(true, false, true);
		}
	}

	@Override
	public void onExit(GameObject object) {
	}

}
