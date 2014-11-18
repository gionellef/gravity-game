package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;
import com.megahard.gravity.objects.Player;

public class STheAwakening extends Script {

	private boolean firstRun = true;
	private boolean active = false;
	private int timer = 0;
	private int offset = 20;

	public STheAwakening(Engine game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
		// things to run when the game starts
	}

	@Override
	public void onUpdate() {
		// things to run always

		if (active) {

			final int halfbeat = 5;
			final String[] words = { "Never", "gonna", "give", "you", "up", "",
					"Never", "gonna", "let", "you", "down", "", "Never",
					"gonna", "come", "around", "and", "desert", "you", "", };
			final int[] durations = { 2, 2, 3, 3, 4, 2, 2, 2, 3, 3, 4, 2, 2, 2,
					4, 6, 4, 6, 6, 2, };

			int i;
			int d = offset;
			for (i = 0; i < words.length; i++) {
				if (d == timer) {
					getGame().showMessage(words[i], halfbeat * durations[i]);
					break;
				} else if (d < timer) {
					d += halfbeat * durations[i];
				} else {
					break;
				}
			}

			// end this
			if (i == words.length) {
				getGame().showMessage(
					"BYE! TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE TESTING LONG LINE",
					40);

				active = false;
				getGame().setCinematicMode(false);
				// end of show
			}

			timer++;
		}
	}

	@Override
	public void onEnter(GameObject object) {
		// things to run when an object enters the region

		// run only once, when the player enters this
		if (firstRun && object.getClass().equals(Player.class)) {
			// start the show!
			getGame().setCinematicMode(true);

			object.velocity.x = 0;

			active = true;
			firstRun = false;
		}
	}

	@Override
	public void onExit(GameObject object) {
		// things to run when an object exits the region
	}

}
