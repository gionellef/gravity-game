package com.megahard.gravity;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.megahard.gravity.GameMap.Tile;

public class Renderer extends Canvas {

	private static final long serialVersionUID = 1L;

	public static final int TILE_SIZE = 16;
	public static final int SCALE_FACTOR = 2;

	private Vector2 camera;
	private BufferedImage buffer;

	private BufferedImage back;
	private BufferedImage tileset;
	
	public boolean debug = false;
	private Font debugFont = new Font(Font.SANS_SERIF, 0, 8);

	public Renderer() {
		camera = new Vector2();
		buffer = new BufferedImage(GravityApplet.WIDTH/SCALE_FACTOR, GravityApplet.HEIGHT/SCALE_FACTOR, BufferedImage.TYPE_INT_RGB);

		back = null;
		tileset = null;
		try {
			back = ImageIO.read(this.getClass().getResource("/back.png"));
			tileset = ImageIO.read(this.getClass().getResource("/tileset.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void render(GameState s) {
		Graphics2D g = (Graphics2D) buffer.getGraphics();
		
		int bufferWidth = buffer.getWidth();
		int bufferHeight = buffer.getHeight();
		int halfBufWidth = bufferWidth / 2;
		int halfBufHeight = bufferHeight / 2;
		
		int mapHeight = s.map.getHeight();
		int mapWidth = s.map.getWidth();
		
		double cx = camera.x;
		double cy = camera.y;
		// prevent "camera overflow"
//		if(cx + bufferWidth/2/TILE_SIZE >= mapWidth){
//			cx = mapWidth - bufferWidth/2/TILE_SIZE;
//		}else if(cx - bufferWidth/2/TILE_SIZE < 0){
//			cx = bufferWidth/2/TILE_SIZE;
//		}
//		if(cy + bufferHeight/2/TILE_SIZE >= mapHeight){
//			cy = mapHeight - bufferHeight/2/TILE_SIZE;
//		}else if(cy - bufferHeight/2/TILE_SIZE < 0){
//			cy = bufferHeight/2/TILE_SIZE;
//		}

		g.drawImage(back,
			(int)((bufferWidth-back.getWidth()) * cx / mapWidth),
			(int)((bufferHeight-back.getHeight()) * cy / mapHeight),
			null);
		int columns = s.map.getImgwidth() / TILE_SIZE;
		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				Tile tile = s.map.getTile(x, y);
				int frame = tile.getTileIndex();
				int frameX = (frame % columns) * TILE_SIZE;
			    int frameY = (frame / columns) * TILE_SIZE;
			    int dx = (int) ((x - cx) * TILE_SIZE + halfBufWidth);
			    int dy = (int) ((y - cy) * TILE_SIZE + halfBufHeight);
			    if(dx >= -TILE_SIZE && dy >= - TILE_SIZE && dx < bufferWidth && dy < bufferHeight){
			    	g.drawImage(tileset,
				    	dx, dy, dx + TILE_SIZE, dy + TILE_SIZE,
				    	frameX, frameY, frameX + TILE_SIZE, frameY + TILE_SIZE,
				    	null);
				}
			}
		}

		for (GameObject o : s.objects) {
			if (o.sprite != null) {
				int dx = (int) ((o.position.x - cx) * TILE_SIZE
						+ halfBufWidth - o.sprite.getWidth() / 2);
				int dy = (int) ((o.position.y - cy) * TILE_SIZE
						+ halfBufHeight - o.sprite.getHeight() / 2);
				if(dx >= -o.sprite.getWidth() && dy >= -o.sprite.getHeight() && dx < bufferWidth && dy < bufferHeight){
					o.sprite.draw(g, dx, dy);
				}
			}else{
				g.fillRect((int) ((o.position.x - o.size.x / 2 - cx)
						* TILE_SIZE + halfBufWidth), (int) ((o.position.y
						- o.size.y / 2 - cy)
						* TILE_SIZE + halfBufHeight),
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
				GravityApplet.HEIGHT, 0, 0, bufferWidth,
				bufferHeight, null);
		
		if(debug){
			Graphics bg = bs.getDrawGraphics();
        	bg.setColor(Color.red);
    		bg.setFont(debugFont);
			for (int y = 0; y < mapHeight; y++) {
				for (int x = 0; x < mapWidth; x++) {
					Tile tile = s.map.getTile(x, y);
				    int dx = (int) ((x - cx) * TILE_SIZE + halfBufWidth);
				    int dy = (int) ((y - cy) * TILE_SIZE + halfBufHeight);
				    if(dx >= -TILE_SIZE && dy >= - TILE_SIZE && dx < bufferWidth && dy < bufferWidth){
				    	bg.drawString(Integer.toHexString(tile.getTileIndex()).toUpperCase(), dx * SCALE_FACTOR + 1, dy * SCALE_FACTOR + 9);
				    }
				}
			}

			for (GameObject o : s.objects) {
				bg.drawRect(
						(int) ((o.position.x - o.size.x / 2 - cx) * TILE_SIZE + halfBufWidth)
								* SCALE_FACTOR,
						(int) ((o.position.y - o.size.y / 2 - cy) * TILE_SIZE + halfBufHeight)
								* SCALE_FACTOR, (int) (o.size.x * TILE_SIZE)
								* SCALE_FACTOR, (int) (o.size.y * TILE_SIZE)
								* SCALE_FACTOR);
			}
		}
		
		bs.show();
	}

	public Vector2 getCamera() {
		return camera;
	}

	public void setCamera(Vector2 camera) {
		this.camera = camera;
	}

}
