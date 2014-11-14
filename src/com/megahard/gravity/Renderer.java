package com.megahard.gravity;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.megahard.gravity.GameMap.Tile;

public class Renderer extends Canvas {

	private static final long serialVersionUID = 1L;

	private Vector2 camera;
	private BufferedImage buffer;

	public static final int TILE_SIZE = 16;
	public static final int SCALE_FACTOR = 2;

	public Renderer() {
		camera = new Vector2();
		buffer = new BufferedImage(GravityApplet.WIDTH/SCALE_FACTOR, GravityApplet.HEIGHT/SCALE_FACTOR, BufferedImage.TYPE_INT_RGB);
	}

	public void render(GameState s) {

		Graphics2D g = (Graphics2D) buffer.getGraphics();
		BufferedImage wallSpriteSheet = null;
		try {
			wallSpriteSheet = ImageIO.read(this.getClass().getResource("/img/wall.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		int mapHeight = s.map.getHeight();
		int mapWidth = s.map.getWidth();
		int columns = s.map.getImgheight() / TILE_SIZE;
		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				Tile tile = s.map.getTile(x, y);
				int frame = tile.getTileIndex() - 1;
				int frameX = (frame % columns) * TILE_SIZE;
			    int frameY = (frame / columns) * TILE_SIZE;
			    int dx = (int) ((x - camera.x) * TILE_SIZE + buffer.getWidth() / 2);
			    int dy = (int) ((y - camera.y) * TILE_SIZE + buffer.getHeight() / 2);
			    g.drawImage(wallSpriteSheet,
			    	dx, dy, dx + TILE_SIZE, dy + TILE_SIZE,
			    	frameX, frameY, frameX + TILE_SIZE, frameY + TILE_SIZE,
			    	null);
			}
		}

		for (GameObject o : s.objects) {
			if (o.sprite!= null) {
				o.sprite.draw(g, (int) ((o.position.x - camera.x) * TILE_SIZE
						+ buffer.getWidth() / 2 - o.sprite.getWidth() / 2),
						(int) ((o.position.y - camera.y) * TILE_SIZE
								+ buffer.getHeight() / 2 - o.sprite.getHeight() / 2));
			} else {
				g.setColor(Color.red);
				g.fillRect((int) ((o.position.x - o.size.x / 2 - camera.x)
						* TILE_SIZE + buffer.getWidth() / 2), (int) ((o.position.y
						- o.size.y / 2 - camera.y)
						* TILE_SIZE + buffer.getHeight() / 2),
						(int) (o.size.x * TILE_SIZE),
						(int) (o.size.y * TILE_SIZE));
			}
		}

		g.dispose();

		BufferStrategy bs = getBufferStrategy();

		if (bs == null) {
			createBufferStrategy(2);
			requestFocus();
			return;
		}
		bs.getDrawGraphics().drawImage(buffer, 0, 0, GravityApplet.WIDTH,
				GravityApplet.HEIGHT, 0, 0, buffer.getWidth(),
				buffer.getHeight(), null);
		bs.show();
	}

	public Vector2 getCamera() {
		return camera;
	}

	public void setCamera(Vector2 camera) {
		this.camera = camera;
	}

}
