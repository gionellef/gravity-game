package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.GameMap;
import com.megahard.gravity.engine.base.GameObject;

public class Gunner extends GameObject {

	private boolean isFacingLeft = false;
	private boolean isWalking = false;

	private boolean goingLeft = false;
	private int waitTimer = 0;

	public Gunner(GameContext game) {
		super(game, "gunner");
		size.set(0.9, 1.45);
		mass = 30;
		restitution = 0.05;
		friction = 0.5;
		staticFriction = 0.3;

		zIndex = 595;
	}

	@Override
	public void update() {
		super.update();

		// Animations
		if (standing) {
			if (!isWalking) {
				setSpriteAction("default", new String[] { "default" });
			}
		} else {
			// if(!sprite.getAction().startsWith("fall")){
			// setSpriteAction("fall");
			// }
		}

		// State tracking
		if (standing) {
			staticFriction = 0.3;
		} else {
			staticFriction = 0;
		}
		isWalking = false;

		// AI
		doGunner();
	}

	private void doGunner() {
		if (waitTimer > 0) {
			waitTimer--;
		} else {
			double nextX = position.x + (goingLeft ? -1 : 1);
			if (findBoundary(position.x, nextX)) {
				goingLeft = !goingLeft;
				waitTimer = 50;
				if (getGame().getMap().getTile(nextX, position.y)
						.getCollidable()) {
					walk(goingLeft);
				} else {
					walk(!goingLeft);
				}
			} else {
				walk(goingLeft);
			}

			if (Math.random() < 0.01) {
				waitTimer = 50 + (int) (Math.random() * 100);
				if (Math.random() < 0.1) {
					goingLeft = !goingLeft;
				}
			}
			
			double sightRadius = 7;
			Player player = getGame().findObject(Player.class, position.x + (isFacingLeft ? -sightRadius : sightRadius), position.y - sightRadius, sightRadius, 2 * sightRadius, true);
			if(player != null){
				// TEMPORARY, throw bombs
				Bomb b = new Bomb(getGame());
				b.position.set(position);
				b.applyImpulse(player.position.minus(position));
				getGame().addObject(b);
			}
		}
	}

	private boolean findBoundary(double x0, double x1) {
		GameMap map = getGame().getMap();
		for (int y = -4; y <= 1; y++) {
			if (map.getTile(x0, position.y + y).getCollidable() != map.getTile(
					x1, position.y + y).getCollidable()) {
				return true;
			}
		}
		return false;
	}

	public void walk(boolean left) {
		double runStrength = 0.05;
		double sign = left ? -1 : 1;
		isFacingLeft = left;
		if (standing) {
			isWalking = true;
			velocity.x += runStrength * sign / friction;
			staticFriction = runStrength;
			setSpriteAction("walk", new String[] { "walk" });
		}
	}

	@Override
	public void onEndAction(String action) {
		if (Math.random() < 0.1) {
			if (action.startsWith("default")) {
				setSpriteAction("blink");
			}
		}
	}

	private void setSpriteAction(String action) {
		setSpriteAction(action, null);
	}

	private void setSpriteAction(String action, String[] inhibit) {
		if (inhibit != null) {
			boolean cont = false;
			String current = sprite.getAction();
			if (current.endsWith("-left")) {
				current = current.substring(0, current.length() - 5);
				if (!isFacingLeft && action.equals(current)) {
					cont = true;
				}
			} else {
				if (isFacingLeft && action.equals(current)) {
					cont = true;
				}
			}
			if (!cont)
				for (String i : inhibit)
					if (i.equals(current))
						return;
		}

		if (isFacingLeft) {
			action += "-left";
		}
		sprite.setAction(action);
	}

}
