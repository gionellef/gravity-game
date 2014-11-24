package com.megahard.gravity.engine;

import java.util.HashMap;
import java.util.Map;

import com.megahard.gravity.util.Vector2;

public class GameMap {

	public class Tile {
		private boolean collidable;
		private int tileIndex;

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
	}

	private int width;
	private int height;
	private int tilewidth;
	private int tileheight;
	private int imgwidth;
	private int imgheight;
	public Tile[] map;
	private Tilesets[] tilesets;
	public Layers[] layers;

	private Map<Integer, Tile> tileMap;

	

	public GameMap(int width, int height, Tile[] level) {
		this.width = width;
		this.height = height;
		// tiles are arranged in 2D array in row major order
		map =new Tile[width * height];
		convertLevelToMap(level);
	}
	
	

	public void initializeMap() {
		tileMap = new HashMap<>(); 
		map = new Tile[width * height];
		for (int i = 0; i < layers[0].getData().length; i++) {
			int tileIndex = layers[0].getData()[i] - tilesets[0].firstgid;
			Tile tile = createOrGetTileFromIndex(tileIndex);
			map[i] = tile;
		}
		
		imgheight = tilesets[0].getImageheight();
		imgwidth = tilesets[0].getImagewidth();
	}

	private Tile createOrGetTileFromIndex(int tileIndex) {
		Tile tile = tileMap.get(tileIndex);
		if(tile == null)
			tile = new Tile(tilesets[0].tileproperties.get(String.valueOf(tileIndex)).getCollidable(), tileIndex);
		return tile;
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
			// dummy tile
			return new Tile(false, 0);
		}
		return map[y * width + x];
	}

	public Tile getTile(double x, double y) {
		return getTile((int) x, (int) y);
	}

	public Tile getTile(Vector2 position) {
		return getTile(position.x, position.y);
	}

	public void setTile(int x, int y, int index) {
		Tile tile = createOrGetTileFromIndex(index);
		map[y * width + x] = tile;
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
	
	

	public Layers[] getLayers() {
		return layers;
	}

	
	public String getObjectType(int gid) {
		int offset = (tilesets[0].getImagewidth() / tilesets[0].getTileWidth()) * (tilesets[0].getImageheight() / tilesets[0].getTileHeight());
		return tilesets[1].tileproperties.get(String.valueOf(gid - offset - 1)).getType();
	}



	public class Layers {
		private int[] data;
		private GameObjects[] objects;

		public int[] getData() {
			return data;
		}
		
		public GameObjects[] getObjects() {
			return objects;
		}
		
		public class GameObjects {
			private double height;
			private double width;
//			private String type;
			private int gid;
			private String name;
			private double x;
			private double y;
			private Map<String, String> properties;
//			
//			public class Properties {
//
//
//			} // end class Properties

			public double getHeight() {
				return height;
			}
			public double getWidth() {
				return width;
			}
//			public String getType() {
//				int offset = (tilesets[0].getImagewidth() / tilesets[0].getTileWidth()) * (tilesets[0].getImageheight() / tilesets[0].getTileHeight());
//				System.out.println(offset);
//				return tilesets[1].tileproperties.get(String.valueOf(gid - offset - 1)).getType();
//			}
			public String getName() {
				return name;
			}

			public double getX() {
				return x;
			}
			public double getY() {
				return y;
			}
			
			public int getGID() {
				return gid;
			}
			public Map<String, String> getProperties() {
				return properties;
			}


		}
	}
	
	public class Tilesets {
		private int firstgid;
		private Map<String, TileProperties> tileproperties;
		private int imageheight;
		private int imagewidth;
		private int tileheight;
		private int tilewidth;

		private class TileProperties {
			private int collidable;
			private String type;
			public boolean getCollidable() {
				if (collidable == 0) {
					return true;
				} else {
					return false;
				}
			}
			public String getType() {
				return type;
			}


		}

		public int getImageheight() {
			return imageheight;
		}

		public int getImagewidth() {
			return imagewidth;
		}
		
		public int getTileHeight() {
			return tileheight;
		}

		public int getTileWidth() {
			return tilewidth;
		}


	}

}
