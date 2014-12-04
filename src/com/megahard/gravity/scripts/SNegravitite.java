package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.objects.Player;

public class SNegravitite extends ScriptSequencer {

	public SNegravitite(GameContext game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
		addMessage("These are negravitites.", 70);
		addMessage("Negravitites give you power to conjure Antigravity Wells.", 80);
		addMessage("Press and hold the Right Mouse Button to create an Antigravity Well.", 100);
	}

	@Override
	public void onEnter(GameObject object) {
		if (object.getClass().equals(Player.class)) {
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
