package com.megahard.gravity;

public class GameMap {

	public enum Tile {
		Air(false, "air.png"),
		Floor(true, "floor.png");

		private final boolean collidable;
		private final String graphics;

		Tile(boolean collidable, String graphics) {
			this.collidable = collidable;
			this.graphics = graphics;
		}

		public boolean getCollidable() {
			return collidable;
		}

		public String getGraphics() {
			return graphics;
		}
	}

	private int width;
	private int height;
	private Tile[] map;

	public GameMap(int width, int height, Tile[] level) {
		this.width = width;
		this.height = height;
		// tiles are arranged in 2D array in row major order
		map = new Tile[width * height];
		for (int i = 0; i < level.length; i++) {
			map[i] = level[i];
		}
	}

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}

	public Tile getTile(int x, int y) {
		return map[y * width + x];
	}

	public Tile getTile(double x, double y) {
		return getTile((int) x, (int) y);
	}

	public Tile getTile(Vector2 position) {
		return getTile(position.x, position.y);
	}

	public void setTile(int x, int y, Tile value) {
		map[y + width + x] = value;
	}
}
