package com.megahard.gravity;

import java.util.Map;

public class GameMap {

	public class Tile {
//		Air(false, "air.png"),
//		Floor(true, "floor.png"),
//		Door(false, "door.png");

		private boolean collidable;
		private int tileIndex;
//		private final String graphics;

		public Tile(boolean collidable, int tileIndex) {
			this.collidable = collidable;
			this.tileIndex = tileIndex;
			
		}

		public boolean getCollidable() {
			return collidable;
		}
		
		public int getTileIndex() {
			return tileIndex;
		}

//		public String getGraphics() {
//			return graphics;
//		}
	}

	private int width;
	private int height;
	private int tilewidth;
	private int tileheight;
	private int imgwidth;
	private int imgheight;
	public Tile[] map;
	private Tilesets[] tilesets;
	private Layers[] layers;
	public String hah;

	public GameMap(int width, int height, Tile[] level) {
		this.width = width;
		this.height = height;
		// tiles are arranged in 2D array in row major order
		map =new Tile[width * height];
		convertLevelToMap(level);
	}
	
	

	public void initializeMap() {
		map = new Tile[width * height];
		int[] contents = layers[0].getData();
		for (int i = 0; i < contents.length; i++) {
			map[i] = new Tile(tilesets[0].tileproperties.get(String.valueOf(contents[i] - 1)).getCollidable(), contents[i]);

		}
		
		imgheight = tilesets[0].getImageheight();
		imgwidth = tilesets[0].getImagewidth();
	}

	private void convertLevelToMap(Tile[] level) {
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
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return new  Tile(false, 0);
		}
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

	public int getTilewidth() {
		return tilewidth;
	}

	public int getTileheight() {
		return tileheight;
	}
	
	public Tilesets[] getTilesets() {
		return tilesets;
	}

	
	public int getImgwidth() {
		return imgwidth;
	}


	public int getImgheight() {
		return imgheight;
	}

	private class Layers {
		private int[] data;

		public int[] getData() {
			return data;
		}
	}
	
	private class Tilesets {
		private Map<String, TileProperties> tileproperties;
		private int imageheight;
		private int imagewidth;

		private class TileProperties {
			private int collidable;

			public boolean getCollidable() {
				if (collidable == 0) {
					return true;
				} else {
					return false;
				}
			}

		}

		public int getImageheight() {
			return imageheight;
		}

		public int getImagewidth() {
			return imagewidth;
		}


	}

}
