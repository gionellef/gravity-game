package com.megahard.gravity;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.megahard.gravity.GameMap.Tile;

public class Renderer extends Canvas {

	private static final long serialVersionUID = 1L;

	private Vector2 camera;

	public static final int TILE_SIZE = 16;

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
		BufferedImage sprite = null;
		try {
			sprite = ImageIO.read(this.getClass().getResource("/img/wall.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		g.fillRect(0, 0, getWidth(), getHeight());
		int mapHeight = s.map.getHeight();
		int mapWidth = s.map.getWidth();
		int columns = s.map.getImgheight() / TILE_SIZE;
		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				Tile tile = s.map.getTile(x, y);
				int frame = tile.getTileIndex() - 1;
				int frameX = (frame % columns) * TILE_SIZE;
			    int frameY = (frame / columns) * TILE_SIZE;
				BufferedImage imgFrame = sprite.getSubimage(frameX, frameY, TILE_SIZE, TILE_SIZE);
				g.drawImage(imgFrame, (int) ((x - camera.x) * TILE_SIZE + getWidth() / 2),
						(int) ((y - camera.y) * TILE_SIZE + getHeight() / 2),
						TILE_SIZE, TILE_SIZE, null);
//				g.setColor(tile.getCollidable() ? Color.black : Color.white);
//				g.fillRect((int) ((x - camera.x) * TILE_SIZE + getWidth() / 2),
//						(int) ((y - camera.y) * TILE_SIZE + getHeight() / 2),
//						TILE_SIZE, TILE_SIZE);
//				g.setColor(Color.gray);
//				g.drawRect((int) ((x - camera.x) * TILE_SIZE + getWidth() / 2),
//						(int) ((y - camera.y) * TILE_SIZE + getHeight() / 2),
//						TILE_SIZE, TILE_SIZE);
			}
		}

		for (GameObject o : s.objects) {
			if (o.sprite!= null) {
				o.sprite.setIndex(1,1);
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
