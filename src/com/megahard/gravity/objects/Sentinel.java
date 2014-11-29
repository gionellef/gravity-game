package com.megahard.gravity.objects;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.GameMap;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.util.Pathfinder;
import com.megahard.gravity.util.Vector2;

public class Sentinel extends GameObject {

	private boolean isFacingLeft = false;

	static private Pathfinder pathfinder;

	private List<Vector2> waypoints;
	private int waitTimer = 0;

	private boolean wandering = true;
	private boolean alert = false;

	private SentinelSwitch mySwitch = null;
	private Bomb myBomb = null;

	public Sentinel(GameContext game) {
		super(game, "sentinel");
		size.set(1.1, 1.1);
		floating = true;
		mass = 15;
		restitution = 1;
		friction = 1;
		staticFriction = 0;

		zIndex = 590;
	}

	@Override
	public void init() {
		waypoints = new LinkedList<>();
		pathfinder = new Pathfinder(getGame().getMap(), size);
	}

	private SentinelSwitch findMySwitch() {
		List<SentinelSwitch> slist = getGame()
				.findObjects(SentinelSwitch.class);

		final Vector2 p;
		Player player = getGame().getPlayerObject();
		if (player == null) {
			p = position;
		} else {
			p = player.position;
		}

		ListIterator<SentinelSwitch> iter = slist.listIterator();
		while (iter.hasNext()) {
			SentinelSwitch s = iter.next();
			if (s.getSwitch()) {
				iter.remove();
			}
		}

		if (slist.isEmpty())
			return null;

		return Collections.min(slist, new Comparator<SentinelSwitch>() {
			@Override
			public int compare(SentinelSwitch o1, SentinelSwitch o2) {
				double d1 = o1.position.distance(p);
				double d2 = o2.position.distance(p);
				return d1 == d2 ? 0 : d1 < d2 ? -1 : 1;
			}
		});
	}

	private Bomb findMyBomb() {
		List<Bomb> blist = getGame().findObjects(Bomb.class);

		ListIterator<Bomb> iter = blist.listIterator();
		while (iter.hasNext()) {
			Bomb b = iter.next();
			if (b.getTimeout() < 50 && !b.standing) {
				iter.remove();
			}
		}

		if (blist.isEmpty())
			return null;

		return Collections.min(blist, new Comparator<Bomb>() {
			@Override
			public int compare(Bomb o1, Bomb o2) {
				double d1 = o1.position.distance(position) - o1.getTimeout()
						* 0.1;
				double d2 = o2.position.distance(position) - o2.getTimeout()
						* 0.1;
				return d1 == d2 ? 0 : d1 < d2 ? -1 : 1;
			}
		});
	}

	@Override
	public void update() {
		super.update();

		velocity.scale(0.9);

		if (waypoints == null || waypoints.isEmpty()) {
			wandering = true;
			Vector2 destination = randomWander();
			waypoints = pathfinder.findPath(position, destination);
		}

		doPath();

		doSentinel();
	}

	private Vector2 randomWander() {
		double x, y;
		do {
			x = position.x + Math.random() * 20 - 10;
			y = position.y + Math.random() * 20 - 10;
		} while (getGame().getMap().getTile(x, y).getCollidable());
		Vector2 destination = new Vector2(x, y);
		return destination;
	}

	private void doSentinel() {
		if (wandering) {
			boolean found = false;
			double sightRadius = 12;
			Player player = getGame().findObject(Player.class,
					position.x + (isFacingLeft ? -sightRadius : 0),
					position.y - sightRadius / 2, sightRadius, sightRadius,
					true);
			if (player != null) {
				if (hasLineOfSight(player.position)) {
					found = true;
				}
			} else {
				GravWell well = getGame().findObject(GravWell.class,
						position.x + (isFacingLeft ? -sightRadius : 0),
						position.y - sightRadius / 2, sightRadius, sightRadius,
						true);
				found = well != null && hasLineOfSight(well.position);
			}

			if (found) {
				if (!alert) {
					setAlert(true);
					waitTimer = 15;
				}

				if (mySwitch == null) {
					mySwitch = findMySwitch();
				}
				if (mySwitch == null) {
					// no more switches to switch!
					myBomb = findMyBomb();
				} else {
					wandering = false;
					waypoints = pathfinder.findPath(position,
							mySwitch.position.plus(0, -1));
				}
			}
		}

		Player player = getGame().getPlayerObject();
		if (alert) {
			if (player == null) {
				setAlert(false);
			} else {
				if (mySwitch != null) {
					if (mySwitch.getSwitch()) {
						mySwitch = null;
					} else {
						if (mySwitch.position.distance(position.plus(0, 1)) < 1.5) {
							mySwitch.setSwitch(true);
							setSpriteAction("touch");
							waitTimer = 50;

							mySwitch = null;
						}
					}
				} else if (myBomb != null) {
					if (myBomb.getTimeout() > 50) {
						if (myBomb.position.distance(position.plus(0, 1)) < 1) {
							// carry the bomb
							myBomb.applyImpulse(velocity
									.plus(position.plus(0, 1).minus(
											myBomb.position))
									.minus(myBomb.velocity).times(0.04));
							myBomb.setTimeout(100);

							if (position.distance(player.position) < 4) {
								// throw the bomb to the player
								myBomb.applyImpulse(position
										.displacement(player.position)
										.normalized().times(0.01)
										.plus(0, -0.03));
								myBomb.setTimeout(30);
								myBomb = null;

								waypoints = pathfinder.findPath(position,
										position.plus(0, -1));
								waitTimer = 0;
							} else if (wandering || waypoints == null) {
								// go to the player
								wandering = false;
								waypoints = pathfinder.findPath(position,
										player.position.plus(0, -0.5));
								waitTimer = 0;
							}
						} else if (wandering
								|| waypoints == null
								|| waypoints.isEmpty()
								|| waypoints.get(waypoints.size() - 1)
										.distance(myBomb.position) > 3) {
							// go to the bomb
							wandering = false;
							waypoints = pathfinder.findPath(position,
									myBomb.position.plus(0, -2));
							waitTimer = 15;
						}
					} else {
						// get another bomb
						myBomb = findMyBomb();
					}
				}
			}
		}
	}

	private boolean hasLineOfSight(Vector2 point) {
		// Bresenham algorithm
		GameMap map = getGame().getMap();
		int x0 = (int) position.x;
		int y0 = (int) position.y;
		int x1 = (int) point.x;
		int y1 = (int) point.y;

		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;
		int err = dx - dy;
		int e2;
		int currentX = x0;
		int currentY = y0;
		while (true) {
			if (map.getTile(currentX, currentY).getCollidable()) {
				return false;
			}

			if (currentX == x1 && currentY == y1) {
				break;
			}
			e2 = 2 * err;
			if (e2 > -1 * dy) {
				err = err - dy;
				currentX = currentX + sx;
			}
			if (e2 < dx) {
				err = err + dx;
				currentY = currentY + sy;
			}
		}

		return true;
	}

	private void doPath() {
		if (waypoints != null && !waypoints.isEmpty()) {
			Vector2 destination = waypoints.get(0);

			Vector2 delta = destination.minus(position);

			if (delta.length() > 3
					|| getGame().getMap()
							.getTile(position.plus(delta.normalized()))
							.getCollidable()) {
				// lost, find new path to the final destination
				waypoints = pathfinder.findPath(position,
						waypoints.get(waypoints.size() - 1));
			} else if (delta.length() > 1) {
				if (waitTimer > 0) {
					waitTimer--;
				} else {
					move(delta);
				}
			} else {
				waypoints.remove(0);
				if (waypoints.isEmpty()) {
					waitTimer = alert ? 50 : 100;
				}
			}
		}
	}

	public void setAlert(boolean value) {
		if (alert != value) {
			if (value) {
				setSprite("sentinel-alert");
				setSpriteAction("shine");
			} else {
				setSprite("sentinel");
			}
		}
		alert = value;
	}

	public void move(Vector2 direction) {
		if (isFacingLeft) {
			if (direction.x > 0) {
				isFacingLeft = velocity.x < 0.02;
			}
		} else {
			if (direction.x < 0) {
				isFacingLeft = velocity.x < -0.02;
			}
		}

		double thrust = alert ? 0.4 : 0.1;
		Vector2 force = direction.normalized().times(thrust);
		applyImpulse(force);
		setSpriteAction("fly", new String[] { "fly" });
	}

	@Override
	public void onEndAction(String action) {
		if (!alert && Math.random() < 0.1) {
			if (action.startsWith("default")) {
				setSpriteAction("shine");
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
