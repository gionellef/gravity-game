package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;
import java.util.Map;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.objects.Player;
import com.megahard.gravity.objects.Sentinel;
import com.megahard.gravity.util.Vector2;

public class TheSentinel extends ScriptSequencer {

	private Sentinel sentinel;

	public TheSentinel(GameContext game, Double region,
		Map<String, String> properties) {
		super(game, region, properties);
	}

	@Override
	protected void onSkip() {
	}

	@Override
	protected void onEnd() {
		sentinel.setPassive(false);
		getGame().showMessage(null, "SPECIMEN MUST NOT ESCAPE!", 60);
	}

	@Override
	public void onStart() {
		sentinel = getGame().findObject(Sentinel.class);
		sentinel.setPassive(true);
		sentinel.goTo(new Vector2(20, 11));
		
		addMessage(null, "UNIDENTIFIABLE OBJECT DETECTED", 80);
		
		addMessage("isaac", "Huh? What's that?", 80);

		addMessage(null, "...", 80);
		addMessage(null, "!!!", 60);
		addMessage(null, "OBJECT IDENTIFIED!", 80);
		
		addMessage(null, "OBJECT IS NOT AUTHORIZED TO PROCEED", 80);
		
		addRunnable(new Runnable() {
			@Override
			public void run() {
				sentinel.setPassive(false);
			}
		}, 0);
		addMessage(null, "ALERT! ALERT! ALERT!", 60);
	}

	@Override
	public void onEnter(GameObject object) {
		if(object.getClass().equals(Player.class)){
			beginSequence(true, true, true);
		}
	}

	@Override
	public void onExit(GameObject object) {
	}

}
