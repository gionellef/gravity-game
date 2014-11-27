package com.megahard.gravity.engine;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import com.megahard.gravity.GravityApplet;
import com.megahard.gravity.engine.GameMap.Tile;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.engine.base.Script;
import com.megahard.gravity.menus.LevelMenu;
import com.megahard.gravity.objects.Player;
import com.megahard.gravity.util.Quad;
import com.megahard.gravity.util.Vector2;

public class Renderer extends Canvas {

	private static final Color MESSAGE_BACKGROUND = new Color(0, 0, 0, 0.5f);

	private static final String TILESET_PATH = "/tileset.png";

	private static final String BACKGROUND_PATH = "/back75.png";

	private static final Color BORDER_COLOR = new Color(0x242523);

	private static final long serialVersionUID = 1L;

	public static final int TILE_SIZE = 16;
	public static final int SCALE_FACTOR = 2;

	private static final Comparator<GameObject> ZCOMP = new Comparator<GameObject>() {
		@Override
		public int compare(GameObject o1, GameObject o2) {
			return o1.zIndex - o2.zIndex;
		}
	};

	private GameContext game;

	private Vector2 camera;
	private Vector2 cameraTarget;
	private double cameraSmoothing;

	private VolatileImage backBuffer;
	private Image background;
	private Image tileset;

	private Point mapCachePosition;
	private VolatileImage mapCache;

	private String message;
	private int messageChars;

	private int fadeTime = 0;
	private int fadeTimer = 0;
	private Color fadeColorEnd;
	private Color fadeColorStart;

	private static Font font;
	static {
		String fontPath = "/munro.ttf";
		font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT,
					Renderer.class.getResourceAsStream(fontPath));

			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();

			ge.registerFont(font);

			font = font.deriveFont(10f);

		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		// ugly default
		if (font == null)
			font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
	}

	private Image messageImage;

	public Renderer(GameContext engine) {
		game = engine;

		camera = null;
		cameraTarget = new Vector2();
		cameraSmoothing = 1;

		background = SpriteStore.get().loadImage(BACKGROUND_PATH, true);
		tileset = SpriteStore.get().loadImage(TILESET_PATH, true);

		message = null;

		setBackground(Color.black);
	}

	public void render(GameState s) {
		if (backBuffer != null) {
			int valid = backBuffer.validate(getGraphicsConfiguration());
			if (valid == VolatileImage.IMAGE_INCOMPATIBLE) {
				backBuffer = null;
			}
		}
		if (backBuffer == null) {
			backBuffer = createVolatileImage(
					GravityApplet.WIDTH / SCALE_FACTOR, GravityApplet.HEIGHT
							/ SCALE_FACTOR);
		}

		if (GravityApplet.useGPU) {
			background = SpriteStore.get().getVolatileImage(BACKGROUND_PATH);
			tileset = SpriteStore.get().getVolatileImage(TILESET_PATH);
		}

		int bufferWidth = backBuffer.getWidth();
		int bufferHeight = backBuffer.getHeight();
		int halfBufWidth = bufferWidth / 2;
		int halfBufHeight = bufferHeight / 2;

		int mapHeight = s.map.getHeight();
		int mapWidth = s.map.getWidth();

		if (camera == null) {
			camera = new Vector2(cameraTarget);
		} else {
			camera = camera.plus(cameraTarget.minus(camera).times(
					cameraSmoothing));
		}

		double cx = camera.x;
		double cy = camera.y;
		int cxm = (int) (cx * TILE_SIZE);
		int cym = (int) (cy * TILE_SIZE);

		do {
			Graphics2D g = backBuffer.createGraphics();

			renderBackground(g, bufferWidth, bufferHeight, mapHeight, mapWidth,
					cx, cy);
			renderMap(g, s, bufferWidth, bufferHeight, halfBufWidth, halfBufHeight, mapHeight, mapWidth,
					cx, cy, cxm, cym);
			renderObjects(g, s, bufferWidth, bufferHeight, halfBufWidth,
					halfBufHeight, cxm, cym);
			renderBorders(g, s, bufferWidth, bufferHeight, halfBufWidth,
					halfBufHeight, cx, cy);
			renderHUD(g);
			renderFade(g, bufferWidth, bufferHeight);
			renderMessages(g, s, bufferWidth, bufferHeight);

			g.dispose();
		} while (backBuffer.contentsLost());

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(1);
			requestFocus();
			bs = getBufferStrategy();
		}

		Graphics bg = bs.getDrawGraphics();
		bg.drawImage(backBuffer, 0, 0, GravityApplet.WIDTH,
				GravityApplet.HEIGHT, 0, 0, bufferWidth, bufferHeight, null);

		if (GravityApplet.debug) {
			bg.setFont(font);

			{
				bg.setColor(Color.green);
				Vector2 mouse = game.getMouseGamePosition();
				int tx = (int) (mouse.x);
				int ty = (int) (mouse.y);
				int dx = tx * TILE_SIZE - cxm + halfBufWidth;
				int dy = ty * TILE_SIZE - cym + halfBufHeight;
				int px = dx * SCALE_FACTOR;
				int py = dy * SCALE_FACTOR;

				Tile tile = s.map.getTile(mouse.x, mouse.y);
				bg.drawRect(px, py, TILE_SIZE * SCALE_FACTOR, TILE_SIZE
						* SCALE_FACTOR);
				bg.drawString(Integer.toHexString(tile.getTileIndex())
						.toUpperCase(), px + 2, py + 10 + 2);
				bg.drawString(tx + "," + ty, px + 2, py + 20 + 2);
			}

			bg.setColor(Color.red);
			for (GameObject o : s.objects) {
				int x = (int) (((o.position.x - o.size.x / 2) * TILE_SIZE + 0.5
						- cxm + halfBufWidth) * SCALE_FACTOR);
				int y = (int) (((o.position.y - o.size.y / 2) * TILE_SIZE + 0.5
						- cym + halfBufHeight) * SCALE_FACTOR);
				bg.drawRect(x, y, (int) (o.size.x * TILE_SIZE * SCALE_FACTOR),
						(int) (o.size.y * TILE_SIZE * SCALE_FACTOR));
				bg.drawString(o.getClass().getSimpleName(), x + 1, y - 1);
			}

			bg.setColor(Color.cyan);
			for (Script c : s.scripts) {
				int x = (int) ((c.getRegion().x * TILE_SIZE + 0.5 - cxm + halfBufWidth) * SCALE_FACTOR);
				int y = (int) ((c.getRegion().y * TILE_SIZE + 0.5 - cym + halfBufHeight) * SCALE_FACTOR);
				bg.drawRect(x, y,
						(int) (c.getRegion().width * TILE_SIZE * SCALE_FACTOR),
						(int) (c.getRegion().height * TILE_SIZE * SCALE_FACTOR));
				bg.drawString(c.getClass().getSimpleName(), x + 1, y - 1);
			}
		}

		bs.show();
	}

	private void renderBackground(Graphics2D g, int bufferWidth,
			int bufferHeight, int mapHeight, int mapWidth, double cx, double cy) {
		g.drawImage(
				background,
				(int) ((bufferWidth - background.getWidth(null)) * cx / mapWidth),
				(int) ((bufferHeight - background.getHeight(null)) * cy / mapHeight),
				null);
	}

	private void renderMap(Graphics2D g, GameState s, int bufferWidth, int bufferHeight, int halfBufWidth,
			int halfBufHeight, int mapHeight, int mapWidth, double cx,
			double cy, int cxm, int cym) {

		int cacheWidth = (bufferWidth + TILE_SIZE);
		int cacheHeight = (bufferHeight + TILE_SIZE);

		int cacheIntervalX = (cacheWidth - bufferWidth) / TILE_SIZE * TILE_SIZE;
		int cacheIntervalY = (cacheHeight - bufferHeight) / TILE_SIZE * TILE_SIZE;

		// Check if cache image is valid
		boolean redraw = false;
		GraphicsConfiguration gc = getGraphicsConfiguration();
		if (mapCache == null) {
			mapCache = gc.createCompatibleVolatileImage(cacheWidth, cacheHeight, VolatileImage.BITMASK);
			redraw = true;
		} else {
			int valid = mapCache.validate(gc);
			if (valid == VolatileImage.IMAGE_INCOMPATIBLE) {
				mapCache = gc.createCompatibleVolatileImage(cacheWidth, cacheHeight, VolatileImage.BITMASK);
			}
			if(valid != VolatileImage.IMAGE_OK){
				redraw = true;
			}
		}

		// calculate part of map to be rendered to screen
		int pxStart = cxm - halfBufWidth;
		int pyStart = cym - halfBufHeight;

		// calculate part of map to be rendered to cache
		int pxCacheStart = pxStart / cacheIntervalX * cacheIntervalX;
		int pyCacheStart = pyStart / cacheIntervalY * cacheIntervalY;
		
		
		// Check if cache contents is valid
		if(mapCachePosition == null){
			redraw = true;
		}else{
			if(mapCachePosition.x != pxCacheStart || mapCachePosition.y != pyCacheStart){
				redraw = true;
			}
		}
		if(s.map.getDirty()){
			redraw = true;
			s.map.setDirty(false);
		}

		if (redraw) {
			Graphics2D cg = mapCache.createGraphics();
			cg.setComposite(AlphaComposite.Src);

			mapCachePosition = new Point(pxCacheStart, pyCacheStart);

			int txCacheStart = pxCacheStart / TILE_SIZE;
			int tyCacheStart = pyCacheStart / TILE_SIZE;

			// draw tiles to cache
			int tileSheetColumns = s.map.getImgwidth() / TILE_SIZE;
			for (int y = 0; y <= cacheHeight / TILE_SIZE; y++) {
				int py = y * TILE_SIZE;
				int ty = y + tyCacheStart;
				for (int x = 0; x <= cacheWidth / TILE_SIZE; x++) {
					int px = x * TILE_SIZE;
					int tx = x + txCacheStart;
					
					if(tx >= 0 && ty >= 0 && tx < mapWidth && ty < mapHeight){
						Tile tile = s.map.getTile(tx, ty);
						int tileIndex = tile.getTileIndex();
						int frameX = (tileIndex % tileSheetColumns) * TILE_SIZE;
						int frameY = (tileIndex / tileSheetColumns) * TILE_SIZE;
						cg.drawImage(tileset, px, py, px + TILE_SIZE, py
								+ TILE_SIZE, frameX, frameY, frameX + TILE_SIZE,
								frameY + TILE_SIZE, null);
					}
				}
			}

			cg.dispose();
		}

		int xSrc = pxStart - pxCacheStart;
		int ySrc = pyStart - pyCacheStart;

		g.drawImage(mapCache,
				0, 0, bufferWidth, bufferHeight,
				xSrc, ySrc, xSrc + bufferWidth, ySrc + bufferHeight,
				null
			);
	}

	private void renderMessages(Graphics2D g, GameState s, int bufferWidth,
			int bufferHeight) {
		int cineStripHeight = 60;
		if (s.cinematicMode) {
			g.setColor(Color.black);
			g.fillRect(0, 0, bufferWidth, cineStripHeight);
			g.fillRect(0, bufferHeight - cineStripHeight, bufferWidth,
					cineStripHeight);
		}

		// Draw messages
		if (message != null) {
			if (!s.cinematicMode) {
				// draw message background for readability
				g.setColor(MESSAGE_BACKGROUND);
				g.fillRect(0, bufferHeight - cineStripHeight, bufferWidth,
						cineStripHeight);
			}
			int n = message.length();
			if (messageChars < n) {
				messageChars += 3;
				if (messageChars > n) {
					messageChars = n;
				}
			}
			int marginX = 15;
			int marginY = 5;
			g.setColor(Color.white);
			g.setFont(font);
			int w = drawStringWrapped(g, message.substring(0, messageChars),
					marginX, bufferHeight - cineStripHeight + font.getSize()
							+ marginY, bufferWidth - 2 * marginX,
					cineStripHeight - 2 * marginY);
			if (w >= 0) {
				// handle overflow (by scrolling)
				System.out.println("Warning! Message overflow:\n "
						+ message.substring(w));
			}
			if (messageImage != null) {
				g.drawImage(messageImage, marginX, bufferHeight
						- cineStripHeight - messageImage.getHeight(null)
						+ marginY / 2, null);
			}
		}
	}

	private void renderFade(Graphics2D g, int bufferWidth, int bufferHeight) {
		if (fadeTime > 0) {
			if (fadeTimer > 0) {
				fadeTimer--;
			}

			double t = (double) fadeTimer / fadeTime;

			g.setColor(new Color((int) (Math.min(
					255,
					Math.max(0,
							fadeColorStart.getRed() * t + fadeColorEnd.getRed()
									* (1 - t)))), (int) (Math.min(
					255,
					Math.max(
							0,
							fadeColorStart.getGreen() * t
									+ fadeColorEnd.getGreen() * (1 - t)))),
					(int) (Math.min(
							255,
							Math.max(0, fadeColorStart.getBlue() * t
									+ fadeColorEnd.getBlue() * (1 - t)))),
					(int) (Math.min(
							255,
							Math.max(0, fadeColorStart.getAlpha() * t
									+ fadeColorEnd.getAlpha() * (1 - t))))));

			if (g.getColor().getAlpha() > 0) {
				g.fillRect(0, 0, bufferWidth, bufferHeight);
			}
		}
	}

	private void renderHUD(Graphics2D g) {
		Player player = game.getPlayerObject();
		if (player != null) {
			g.setColor(Color.white);
			g.setFont(font);
			if (player.getGravsLeft() > 0) {
				g.drawString("Gravitites: " + player.getGravsLeft(), 5, 20);
			}

			String mapName = GravityApplet.lm.maps.get(LevelMenu.lastMap)[0];
			g.drawString(mapName, 380 - mapName.length() * 7 / 2, 20);
		}
	}

	private void renderBorders(Graphics2D g, GameState s, int bufferWidth,
			int bufferHeight, int halfBufWidth, int halfBufHeight, double cx,
			double cy) {
		int borderThickness = 32;
		if (cx * TILE_SIZE - halfBufWidth - borderThickness < 0) {
			// left
			g.setColor(BORDER_COLOR);
			int borderWidth = (int) (halfBufWidth - cx * TILE_SIZE + 1);
			g.fillRect(0, 0, borderWidth, bufferHeight);
			for (int i = 1; i < borderThickness; i++) {
				g.setColor(new Color((int) Quad.easeInOut(i, 256, -256,
						borderThickness) << 24
						| BORDER_COLOR.getRGB()
						& 0xFFFFFF, true));
				g.drawLine(borderWidth + i, 0, borderWidth + i, bufferHeight);
			}
		}
		if (cx * TILE_SIZE + halfBufWidth + borderThickness > s.map.getWidth()
				* TILE_SIZE) {
			// right
			g.setColor(BORDER_COLOR);
			int borderWidth = (int) (cx * TILE_SIZE + halfBufWidth
					- s.map.getWidth() * TILE_SIZE + 1);
			g.fillRect(bufferWidth - borderWidth, 0, borderWidth, bufferHeight);
			for (int i = 1; i < borderThickness; i++) {
				g.setColor(new Color((int) Quad.easeInOut(i, 256, -256,
						borderThickness) << 24
						| BORDER_COLOR.getRGB()
						& 0xFFFFFF, true));
				g.drawLine(bufferWidth - borderWidth - i, 0, bufferWidth
						- borderWidth - i, bufferHeight);
			}
		}
		if (cy * TILE_SIZE - halfBufHeight - borderThickness < 0) {
			// top
			g.setColor(BORDER_COLOR);
			int borderHeight = (int) (halfBufHeight - cy * TILE_SIZE + 1);
			g.fillRect(0, 0, bufferWidth, borderHeight);
			for (int i = 1; i < borderThickness; i++) {
				g.setColor(new Color((int) Quad.easeInOut(i, 256, -256,
						borderThickness) << 24
						| BORDER_COLOR.getRGB()
						& 0xFFFFFF, true));
				g.drawLine(0, borderHeight + i, bufferWidth, borderHeight + i);
			}
		}
		if (cy * TILE_SIZE + halfBufHeight + borderThickness > s.map
				.getHeight() * TILE_SIZE) {
			// bottom
			g.setColor(BORDER_COLOR);
			int borderHeight = (int) (cy * TILE_SIZE + halfBufHeight
					- s.map.getHeight() * TILE_SIZE + 1);
			g.fillRect(0, bufferHeight - borderHeight, bufferWidth,
					borderHeight);
			for (int i = 1; i < borderThickness; i++) {
				g.setColor(new Color((int) Quad.easeInOut(i, 256, -256,
						borderThickness) << 24
						| BORDER_COLOR.getRGB()
						& 0xFFFFFF, true));
				g.drawLine(0, bufferHeight - borderHeight - i, bufferWidth,
						bufferHeight - borderHeight - i);
			}
		}
	}

	private void renderObjects(Graphics2D g, GameState s, int bufferWidth,
			int bufferHeight, int halfBufWidth, int halfBufHeight, int cxm,
			int cym) {
		GameObject[] objects = s.objects.toArray(new GameObject[s.objects
				.size()]);
		// sort by z-index
		Arrays.sort(objects, ZCOMP);
		for (GameObject o : objects) {
			int dx = (int) (o.position.x * TILE_SIZE + 0.5) - cxm
					+ halfBufWidth - o.sprite.getWidth() / 2;
			int dy = (int) (o.position.y * TILE_SIZE + 0.5) - cym
					+ halfBufHeight - o.sprite.getHeight() / 2;
			if (dx >= -o.sprite.getWidth() && dy >= -o.sprite.getHeight()
					&& dx < bufferWidth && dy < bufferHeight) {
				o.sprite.draw(g, dx, dy);
			}
		}
	}

	// Method from http://stackoverflow.com/a/400676
	private int drawStringWrapped(Graphics g, String s, int x, int y,
			int width, int height) {
		// FontMetrics gives us information about the width,
		// height, etc. of the current Graphics object's Font.
		FontMetrics fm = g.getFontMetrics();

		int lineHeight = fm.getHeight();

		int curX = x;
		int curY = y;

		String[] words = s.split("\\s");

		int i;
		for (i = 0; i < words.length; i++) {
			String word = words[i];

			// Find out thw width of the word.
			int wordWidth = fm.stringWidth(word + " ");

			// If text exceeds the width, then move to next line.
			if (curX + wordWidth >= x + width) {
				curY += lineHeight;
				curX = x;
			}

			// If height exceeded break
			if (curY >= y + height) {
				break;
			}

			g.drawString(word, curX, curY);

			// Move over to the right for next word.
			curX += wordWidth;
		}

		return i == words.length ? -1 : i;
	}

	public Vector2 getCameraTarget() {
		return cameraTarget;
	}

	public void setCameraTarget(Vector2 camera) {
		this.cameraTarget = camera;
	}

	public void showMessage(String message) {
		showMessage(null, message);
	}

	public void removeMessage() {
		messageImage = null;
		message = null;
	}

	public void fade(Color colorStart, Color colorEnd, int duration) {
		fadeColorStart = colorStart;
		fadeColorEnd = colorEnd;

		if (colorStart == null && colorEnd != null) {
			fadeColorStart = new Color(colorEnd.getRed(), colorEnd.getGreen(),
					colorEnd.getBlue(), 0);
		} else if (colorStart != null && colorEnd == null) {
			fadeColorEnd = new Color(colorStart.getRed(),
					colorStart.getGreen(), colorStart.getBlue(), 0);
		}

		fadeTime = fadeTimer = duration;
	}

	public void fadeBlack(boolean out, int duration) {
		if (out) {
			fade(null, Color.black, duration);
		} else {
			fade(Color.black, null, duration);
		}
	}

	public void showMessage(String image, String message) {
		messageChars = 0;

		this.message = message;

		if (image == null) {
			messageImage = null;
		} else {
			messageImage = SpriteStore.get().loadImage(
					"/images/" + image + ".png", false);
		}
	}

	public void setCameraSmoothing(double v) {
		cameraSmoothing = v;
	}

	public double getCameraSmoothing() {
		return cameraSmoothing;
	}

	public Vector2 getCamera() {
		return camera;
	}

}
