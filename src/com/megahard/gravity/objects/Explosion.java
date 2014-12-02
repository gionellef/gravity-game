package com.megahard.gravity.objects;

import java.util.List;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.GameMap;
import com.megahard.gravity.engine.GameMap.Tile;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.util.Sound;
import com.megahard.gravity.util.Vector2;

public class Explosion extends GameObject {

	private static final int[] DESTRUCTIBLE_TILES = { 121, 145, 146, 147, 171, };
	private static final int DESTROYED = 230;

	// {original tile, destroyed top, destroyed bottom, destroyed left,
	// destroyed right}
	private static final int[][] PARTIALLY_DESTRUCTIBLE_TILES = {
			{ 0, 0, 190, 0, 163 },
			{ 1, 1, 191, 169, 168 },
			{ 2, 2, 192, 164, 2 },
			{ 25, 220, 195, 25, 188 },
			{ 26, 221, 196, 194, 193 },
			{ 27, 222, 197, 189, 27 },
			{ 50, 215, 50, 50, 213 },
			{ 51, 216, 51, 219, 218 },
			{ 52, 217, 52, 214, 52 },
			{ 75, 221, 196, 194, 193 },
			{ 76, 221, 196, 194, 193 },
			{ 100, 221, 196, 194, 193 },
			{ 101, 221, 196, 194, 193 },
		};

	private static final Class<?>[] DESTRUCTIBLE_OBJECTS = {
		Player.class,
		
		Gravitite.class,
		BigGravitite.class,
		Negravitite.class,
		
		Sentinel.class,
		Gunner.class,
	};

	private double radius = 1.7;

	public Explosion(GameContext game) {
		super(game, "explosion");
		fixed = true;

		zIndex = 1200;
	}

	public void setRadius(double value) {
		this.radius = value;
	}

	@Override
	public void onStartAction(String action) {
		getGame().playSoundAtLocation(Sound.explosion, position, 1);

		List<GameObject> objects = getGame().findObjects(position.x - radius,
				position.y - radius, radius * 2, radius * 2, false);
		for (GameObject o : objects) {
			boolean dest = false;
			for (Class<?> type : DESTRUCTIBLE_OBJECTS) {
				if (o.getClass().equals(type)) {
					dest = true;
				}
			}

			double strength = radius * 12;
			Vector2 delta = o.position.minus(position);
			delta.add(Math.random() * 0.1 - 0.05, Math.random() * 0.1 - 0.05);
			double distance = delta.length();
			Vector2 pushForce = delta.times(strength
					/ (distance * distance + 1));
			if (distance < radius*2) {
				o.applyImpulse(pushForce);
				if (distance < radius && dest) {
					o.die();
				}
			}
		}
		destroyTiles();
		getAwayFromWalls();
	}

	private void getAwayFromWalls() {
		GameMap map = getGame().getMap();
		if (map.getTile(position.plus(0, -0.5)).getCollidable()) {
			position.y += 0.5;
		}
		if (map.getTile(position.plus(-0.5, 0)).getCollidable()) {
			position.x += 0.5;
		}
		if (map.getTile(position.plus(0.5, 0)).getCollidable()) {
			position.x -= 0.5;
		}
		if (map.getTile(position.plus(0, 0.5)).getCollidable()) {
			position.y -= 0.5;
		}
	}

	private void destroyTiles() {
		int r = (int) radius;
		int ix = (int) position.x;
		int iy = (int) position.y;

		boolean[][] destroy = new boolean[r * 2 + 1][r * 2 + 1];

		for (int y = r * 2; y >= 0; y--) {
			int ty = iy + y - r;
			for (int x = r * 2; x >= 0; x--) {
				int tx = ix + x - r;

				if (isTileDestructible(ty, tx)) {
					destroy[x][y] = true;
					partiallyDestroyTile(tx, ty + 1, 1);
					partiallyDestroyTile(tx, ty - 1, 2);
					partiallyDestroyTile(tx + 1, ty, 3);
					partiallyDestroyTile(tx - 1, ty, 4);
				}
			}
		}

		for (int y = r * 2; y >= 0; y--) {
			int ty = iy + y - r;
			for (int x = r * 2; x >= 0; x--) {
				int tx = ix + x - r;

				if (destroy[x][y]) {
					destroyTile(tx, ty);
				}
			}
		}
	}

	private boolean isTileDestructible(int y, int x) {
		Tile tile = getGame().getMap().getTile(x, y);
		for (int i : DESTRUCTIBLE_TILES) {
			if (i == tile.getTileIndex())
				return true;
		}
		return false;
	}

	private void destroyTile(int x, int y) {
		getGame().getMap().setTile(x, y, DESTROYED);
	}

	// direction: 1=up, 2=down, 3=left, 4=right
	private void partiallyDestroyTile(int x, int y, int direction) {
		GameMap map = getGame().getMap();
		Tile tile = map.getTile(x, y);
		for (int[] r : PARTIALLY_DESTRUCTIBLE_TILES) {
			if (tile.getTileIndex() == r[0]) {
				map.setTile(x, y, r[direction]);
				break;
			}
		}
	}

	@Override
	public void onEndAction(String action) {
		getGame().removeObject(this);
	}
}
