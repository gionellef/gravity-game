package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.objects.Player;

public class SadWorld extends ScriptSequencer {

	public SadWorld(GameContext game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onEnter(GameObject object) {
		if (object.getClass().equals(Player.class)) {
			addMessage("You can double jump by pressing Jump in midair", 150);
			beginSequence(true, false, true);
		}

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
