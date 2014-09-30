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
	}
}
