package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;
import com.megahard.gravity.objects.Player;

public class NoobParadise1 extends Script {
	
	private boolean firstRun = true;
	private boolean active = false;
	private int timer = 0;
	private int offset = 20;

	public NoobParadise1(Engine game, Double region) {
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
			final String[] messages = {"The game's objective is to find the door and go inside it","Use 'a' to go left, 's' to go down, 'd' to go right and 'w' to jump", ""};

			final int[] durations = { 10,20, 10};

			int i;
			int d = offset;
			for (i = 0; i < messages.length; i++) {
				if (d == timer) {
					getGame().showMessage(messages[i], halfbeat * durations[i]);
					break;
				} else if (d < timer) {
					d += halfbeat * durations[i];
				} else {
					break;
				}
			}

			// end this
			if (i == messages.length) {
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
		

	}

}
