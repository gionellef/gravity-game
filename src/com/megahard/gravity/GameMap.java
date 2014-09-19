package com.megahard.gravity;

public class GameMap {

	public static final int TILE_SIZE = 32;

	private int width;
	private int height;
	private int[] map;

	public GameMap(int width, int height) {
		this.width = width;
		this.height = height;
		map = new int[width * height];
	}

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}

	public int getTile(int x, int y) {
		return map[y * width + x];
	}

	public void setTile(int x, int y, int value) {
		map[y + width + x] = value;
	}
}
