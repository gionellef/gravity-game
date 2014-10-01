package com.megahard.gravity;

import java.awt.Color;
import java.awt.Graphics;

import com.megahard.gravity.GameMap.Tile;

public class Renderer {
	public void render(Graphics g, GameState s) {
		GameMap map = s.map;

		int TILE_SIZE = 16;

		for (int y = 0; y < map.getHeight(); y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				Tile tile = map.getTile(x, y);
				g.setColor(tile.getCollidable() ? Color.black : Color.white);
				g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
		}
		
		for (GameObject o : s.objects) {
			g.setColor(Color.CYAN);
			g.fillRect((int)((o.position.x - o.size.x/2) * TILE_SIZE), (int)((o.position.y - o.size.y/2) * TILE_SIZE), 
					(int)(o.size.x * TILE_SIZE), (int)(o.size.y * TILE_SIZE));
		}
	}
}
