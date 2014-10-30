package com.megahard.gravity;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.megahard.gravity.GameMap.Tile;

public class Renderer extends Canvas {

	private static final long serialVersionUID = 1L;

	private Vector2 camera;

	private static final int TILE_SIZE = 16;

	public Renderer() {
		camera = new Vector2();
	}

	public void render(GameState s) {

		BufferStrategy bs = getBufferStrategy();

		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		Graphics g = bs.getDrawGraphics();

		g.fillRect(0, 0, getWidth(), getHeight());
		for (int y = 0; y < s.map.getHeight(); y++) {
			for (int x = 0; x < s.map.getWidth(); x++) {
				Tile tile = s.map.getTile(x, y);
				g.setColor(tile.getCollidable() ? Color.black : Color.white);
				g.fillRect((int) ((x - camera.x) * TILE_SIZE + getWidth() / 2),
						(int) ((y - camera.y) * TILE_SIZE + getHeight() / 2),
						TILE_SIZE, TILE_SIZE);
				g.setColor(Color.gray);
				g.drawRect((int) ((x - camera.x) * TILE_SIZE + getWidth() / 2),
						(int) ((y - camera.y) * TILE_SIZE + getHeight() / 2),
						TILE_SIZE, TILE_SIZE);
			}
		}

		for (GameObject o : s.objects) {
			if (o.sprite!= null) {
				o.sprite.draw(g, (int) ((o.position.x - camera.x) * TILE_SIZE
						+ getWidth() / 2 - o.sprite.getWidth() / 2),
						(int) ((o.position.y - camera.y) * TILE_SIZE
								+ getHeight() / 2 - o.sprite.getHeight() / 2));
			} else {
				g.setColor(o.color);
				g.fillRect((int) ((o.position.x - o.size.x / 2 - camera.x)
						* TILE_SIZE + getWidth() / 2), (int) ((o.position.y
						- o.size.y / 2 - camera.y)
						* TILE_SIZE + getHeight() / 2),
						(int) (o.size.x * TILE_SIZE),
						(int) (o.size.y * TILE_SIZE));
				g.setColor(Color.black);
				g.drawRect((int) ((o.position.x - o.size.x / 2 - camera.x)
						* TILE_SIZE + getWidth() / 2), (int) ((o.position.y
						- o.size.y / 2 - camera.y)
						* TILE_SIZE + getHeight() / 2),
						(int) (o.size.x * TILE_SIZE),
						(int) (o.size.y * TILE_SIZE));
			}
		}

		g.dispose();
		bs.show();
	}

	public Vector2 getCamera() {
		return camera;
	}

	public void setCamera(Vector2 camera) {
		this.camera = camera;
	}

}
