package com.megahard.gravity.objects;

import java.util.List;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.GameMap;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.util.Vector2;

public class Gunner extends GameObject {

	private boolean isFacingLeft = false;
	private boolean isWalking = false;

	private boolean goingLeft = false;
	private int waitTimer = 0;

	private double t = 0;
	private GunnerGun gun;

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
	public void init() {
		gun = new GunnerGun(getGame());
		gun.position.set(position);
		getGame().addObject(gun);
	}

	@Override
	public void update() {
		super.update();

		// Animations
		if (standing) {
			if (!isWalking) {
				if (gun.isFiring()) {
					setSpriteAction("fire", new String[] {
						"fire"
					});
				} else {
					setSpriteAction("default", new String[] {
						"default"
					});
				}
			}
		} else {
			// falling
			if (!sprite.getAction().startsWith("walk")) {
				setSpriteAction("walk");
			}
		}

		// State tracking
		if (standing) {
			staticFriction = 0.3;
		} else {
			staticFriction = 0;
		}
		isWalking = false;

		// AI
		if (gun.isFiring()) {
			waitTimer = 5;
		}
		doWalk();

		gun.setFacing(isFacingLeft);
		gun.position.set(position.plus(Math.cos(t * 0.5) * 0.1
			+ (isFacingLeft ? 0.2 : -0.2), Math.sin(t) * 0.05 + 0.2));
		t += Math.random() * 0.06;
	}

	private void doWalk() {
		// Get away from bombs!
		Vector2 escapeVector = new Vector2();
		List<Bomb> bombs = getGame().findObjects(Bomb.class,
			position.x + (goingLeft ? -3 : -2),
			position.y - 2, 5, 4, true);
		Bomb critBomb = null;
		for (Bomb b : bombs) {
			if (b.getTimeout() < 60) {
				escapeVector.add(position.minus(b.position).times(
					1.0 / (b.getTimeout() + 1)));

				if (critBomb == null
					|| b.getTimeout() < critBomb.getTimeout()) {
					critBomb = b;
				}

				if (b.position.x < position.x == goingLeft) {
					waitTimer = Math.max(waitTimer, b.getTimeout());
				}
			}
		}

		if (escapeVector.length() > 0) {

			// Run [walk] away from bombs!
			double pathX = critBomb.position.x + escapeVector.normalized().x
				* 2;
			boolean blockedPath = getGame().getMap()
				.getTile(pathX, position.y)
				.getCollidable();
			walk(blockedPath ? position.x > pathX : escapeVector.x < 0);
		} else {

			// normal patrolling
			if (waitTimer > 0) {
				waitTimer--;
			} else {
				double nextX = position.x + (goingLeft ? -1 : 1);
				if (findBoundary(position.x, nextX)) {
					goingLeft = !goingLeft;
					waitTimer = 100;
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
			}
		}
	}

	private boolean findBoundary(double x0, double x1) {
		GameMap map = getGame().getMap();
		for (int y = -2; y <= 1; y++) {
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
		if (standing) {
			isFacingLeft = left;

			isWalking = true;
			velocity.x += runStrength * sign / friction;
			staticFriction = runStrength;
			setSpriteAction("walk", new String[] {
				"walk"
			});
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

	@Override
	public void die() {
		super.die();
		gun.die();
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
