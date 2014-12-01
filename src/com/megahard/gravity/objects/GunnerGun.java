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
		double targetAngle = (isFacingLeft ? Math.PI : 0) + Math.sin(t) * 0.6;
		t += Math.random() * 0.03;

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
				Vector2 p = position.displacement(target.position);
				if (hit) {
					p.add(Math.random() * target.size.x / 2 - target.size.x / 4,
							Math.random() * target.size.y / 2 - target.size.y
									/ 4);
					p.normalize();
					p.scale(distance);
				} else {
					p.add((Math.random() * 2 - 1) * distance / 4,
							(Math.random() * 2 - 1) * distance / 4);
				}
				drawTrail(p, hit);
			}
		} else {
			target = obj;
			targetHits = maxHits;
		}
	}

	private void drawTrail(Vector2 dir, boolean hit) {
		double cx, cy;
		double t = 0.6;
		double a = dir.angle();
		double distance = dir.length();
		while (t < 30) {
			cx = position.x + Math.cos(a) * t;
			cy = position.y + Math.sin(a) * t;
			if (hit) {
				if (t >= distance) {
					break;
				}
			} else {
				if (getGame().getMap().getTile(cx, cy).getCollidable()) {
					break;
				}
			}
			t += 0.18;

			GunTrail gt = new GunTrail(getGame());
			gt.position.set(cx, cy);
			gt.sprite.setFrame(Math.random() < 0.5 ? 0 : 1);
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
