package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.objects.Player;
import com.megahard.gravity.objects.TutorialTarget;

public class NewfoundPower extends ScriptSequencer {

	public NewfoundPower(GameContext game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
		addMessage("These Gravitites give you energy to conjure Gravity Wells.", 100);
		addRunnable(new Runnable() {
			@Override
			public void run() {
				TutorialTarget tt = new TutorialTarget(getGame());
				tt.position.set(20, 29);
				getGame().addObject(tt);
			}
		}, 0);
		addMessage("Press and hold the Left Mouse Button to create a Gravity Well.", 200);
		addMessage("Gravity Wells distort gravity around it. Use it wisely!", 100);
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
