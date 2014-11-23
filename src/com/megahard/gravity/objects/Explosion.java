package com.megahard.gravity.objects;

import java.util.List;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameMap;
import com.megahard.gravity.GameMap.Tile;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Sound;

public class Explosion extends GameObject {
	
	private static final int[] DESTRUCTIBLE_TILES = {
		121,
		145,
		146,
		147,
		171,
	};
	private static final int DESTROYED = 230;
	
	// {original tile, destroyed top, destroyed bottom, destroyed left, destroyed right}
	private static final int[][] PARTIALLY_DESTRUCTIBLE_TILES= {
		{000,	000,	190,	000,	163},
		{001,	001,	191,	169,	168},
		{002,	002,	192,	164,	002},
		{025,	220,	195,	025,	188},
		{026,	221,	196,	194,	193},
		{027,	222,	197,	189,	027},
		{050,	215,	050,	050,	213},
		{051,	216,	051,	219,	218},
		{052,	217,	214,	052,	052},
	};
	
	private static final Class<?>[] DESTRUCTIBLE_OBJECTS = {
		Player.class,
		Bomb.class,
		Gravitite.class,
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
		
		List<GameObject> objects = getGame().findObjects(position.x - radius, position.y - radius, radius * 2, radius * 2, false);
		for(GameObject o : objects){
			boolean dest = false;
			for(Class<?> type : DESTRUCTIBLE_OBJECTS){
				if(o.getClass().equals(type)){
					dest = true;
					break;
				}
			}
			
			if(dest){
				double distance = o.position.sub(position).length();
				if(distance < radius){
					o.kill();
				}
			}
		}
		destroyTiles();
	}
	
	private void destroyTiles() {
		int r = (int)radius;
		int ix = (int)position.x;
		int iy = (int)position.y;
		
		boolean[][] destroy = new boolean[r * 2 + 1][r * 2 + 1];

		for(int y = r * 2; y >= 0; y--){
			int ty = iy + y - r;
			for(int x = r * 2; x >= 0; x--){
				int tx = ix + x - r;
				
				if(isTileDestructible(ty, tx)){
					destroy[x][y] = true;
				}
			}
		}
		
		for(int y = r * 2; y >= 0; y--){
			int ty = iy + y - r;
			for(int x = r * 2; x >= 0; x--){
				int tx = ix + x - r;
				
				if(destroy[x][y]){
					destroyTile(tx, ty);
					partiallyDestroyTile(tx, ty+1, 1);
					partiallyDestroyTile(tx, ty-1, 2);
					partiallyDestroyTile(tx+1, ty, 3);
					partiallyDestroyTile(tx-1, ty, 4);
				}
			}
		}
	}

	private boolean isTileDestructible(int y, int x) {
		Tile tile = getGame().getMap().getTile(x, y);
		for(int i : DESTRUCTIBLE_TILES){
			if(i == tile.getTileIndex())
				return true;
		}
		return false;
	}
	
	private void destroyTile(int x, int y){
		getGame().getMap().setTile(x, y, DESTROYED);
	}
	
	// direction: 1=up, 2=down, 3=left, 4=right
	private void partiallyDestroyTile(int x, int y, int direction){
		GameMap map = getGame().getMap();
		Tile tile = map.getTile(x, y);
		for(int[] r : PARTIALLY_DESTRUCTIBLE_TILES){
			if(tile.getTileIndex() == r[0]){
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
