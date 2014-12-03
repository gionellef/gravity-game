package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.objects.Player;

public class Finale extends ScriptSequencer {

	public Finale(GameContext game, Double region) {
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
		// talk talk talk
		addMessage("isaac", "This must be the control center", 100);

		// send message
		addMessage("isaac", "YABADABADOO!", 80);

		// center camera to the Screens
		addRunnable(new Runnable() {
			@Override
			public void run() {
				getGame().setCameraTarget(getCenter());
			}
		}, 50);
	}

	@Override
	public void onEnter(GameObject object) {
		if (object.getClass().equals(Player.class)) {
			beginSequence(true, true, false);
		}
	}

	@Override
	public void onExit(GameObject object) {
	}

}
