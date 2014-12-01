package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.Sprite.SpriteAction;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.util.Vector2;

public class GunnerGun extends GameObject {

	private boolean isFacingLeft = false;

	private double angle = 0;
	private int reloadTime = 10;
	private int reloadTimer = 0;

	private GameObject target = null;
	private int targetHits = 0;
	private int maxHits = 3;
	private boolean isFiring = false;

	private double t = 0;

	public GunnerGun(GameContext game) {
		super(game, "gunner-gun");
		size.set(0.6, 0.6);
		fixed = true;

		zIndex = 596;
	}

	@Override
	public void init() {
		sprite.setAction((SpriteAction) null);
	}

	@Override
	public void update() {
		super.update();

		// calculate target angle
		double targetAngle = (isFacingLeft ? Math.PI : 0) + Math.sin(t) * 0.5;
		t += 0.04;

		Player player = findPlayer();
		if (player != null) {
			targetAngle = position.displacement(player.position).angle();
		}

		// move the angle
		double deltaAngle = targetAngle - angle;
		if (deltaAngle < -Math.PI) {
			deltaAngle += 2 * Math.PI;
		} else if (deltaAngle > Math.PI) {
			deltaAngle -= 2 * Math.PI;
		}
		angle += deltaAngle * 0.2;

		// set sprite index based on angle
		if (isFacingLeft) {
			double a = angle < 0 ? angle + 2 * Math.PI : angle;
			if (a < Math.PI / 2) {
				a = angle = Math.PI / 2;
			} else if (a > Math.PI * 3 / 2) {
				a = angle = Math.PI * 3 / 2;
			}

			int f = (int) Math.floor(9 * (Math.PI * 3 / 2 - a) / Math.PI);
			sprite.setIndex(f, 1);
		} else {
			if (angle < -Math.PI / 2) {
				angle = -Math.PI / 2;
			} else if (angle > Math.PI / 2) {
				angle = Math.PI / 2;
			}

			int f = (int) Math.floor(9 * (Math.PI / 2 - angle) / Math.PI);
			sprite.setIndex(f, 0);
		}

		// fire at will
		if (player != null) {
			if (Math.abs(deltaAngle) < 0.2) {
				fire(player);
			}
		} else {
			isFiring = false;
		}
		if (reloadTimer > 0) {
			reloadTimer--;
		}

		// targets regenerate over time
		if (Math.random() < 0.01 && targetHits < maxHits) {
			targetHits++;
		}
	}

	private void fire(GameObject obj) {
		if (target == obj) {
			isFiring = true;

			if (reloadTimer <= 0) {
				reloadTimer = reloadTime;

				Vector2 delta = position.displacement(obj.position);
				double distance = delta.length();
				double chanceToHit = 40 * obj.size.length()
						/ (distance * Math.abs(delta.angle() - angle) * 2 + 1);
				boolean hit = Math.random() < chanceToHit;

				if (hit) {
					double str = 16;
					obj.applyImpulse(delta.normalized().times(str));

					if (targetHits > 0) {
						targetHits--;
					} else {
						obj.die();
					}
				}

				// graphical effects
				drawTrail(distance, hit, hit ? delta.angle() : angle);
			}
		} else {
			target = obj;
			targetHits = maxHits;
		}
	}

	private void drawTrail(double distance, boolean hit, double angle) {
		double x, y;
		double t = 0.6;
		double a = (angle + (Math.random() * 2 - 1) * (hit ? 0 : 0.2));
		while (t < 30) {
			x = position.x + Math.cos(a) * t;
			y = position.y + Math.sin(a) * t;
			if (hit) {
				if (t >= distance) {
					// spark at target
					castTrailSpark(x, y);
					break;
				}
			} else {
				if (getGame().getMap().getTile(x, y).getCollidable()) {
					break;
				}
			}
			t += 0.06 + (hit ? 0 : t * 0.01);

			GunTrail gt = new GunTrail(getGame());
			gt.position.set(x, y);
			gt.velocity.set(Math.cos(a) * 0.06 + Math.random() * 0.002 - 0.001,
					Math.sin(a) * 0.06 + Math.random() * 0.002 - 0.001);
			getGame().addObject(gt);
		}
	}

	private void castTrailSpark(double x, double y) {
		for (int i = 0; i < 10; i++) {
			GunTrail gt = new GunTrail(getGame());
			gt.position.set(x, y);
			gt.velocity.set(Math.random() * 0.08 - 0.04,
					Math.random() * 0.08 - 0.04);
			getGame().addObject(gt);
		}
	}

	private Player findPlayer() {
		double sightRadius = 15;
		Player player = getGame().findObject(Player.class,
				position.x + (isFacingLeft ? -sightRadius : 0),
				position.y - sightRadius, sightRadius, 2 * sightRadius, true);
		return player != null && hasLineOfSight(player.position) ? player
				: null;
	}

	private boolean hasLineOfSight(Vector2 point) {
		return getGame().getMap().hasLineOfSight(position, point);
	}

	public void setFacing(boolean left) {
		isFacingLeft = left;
	}

	public GameObject getTarget() {
		return target;
	}

	public boolean isFiring() {
		return isFiring;
	}
}
