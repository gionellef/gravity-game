package com.megahard.gravity;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import com.megahard.gravity.GameMap.Tile;
import com.megahard.gravity.menus.LevelMenu;
import com.megahard.gravity.objects.Player;

public class Renderer extends Canvas {

	private static final String TILESET_PATH = "/tileset.png";

	private static final String BACKGROUND_PATH = "/back75.png";

	private static final Color BORDER_COLOR = new Color(0x252525);

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

	private String message;
	private String messageImageName;
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
			font = Font.createFont(Font.TRUETYPE_FONT, Renderer.class.getResourceAsStream(fontPath));

	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

	        ge.registerFont(font);
			
			font = font.deriveFont(10f);
	        
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		// ugly default
		if(font == null) 
			font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
	}
	


	private Image messageImage;
	
	public Renderer(GameContext engine) {
		game = engine;
		
		camera = new Vector2(Double.NaN,0);
		cameraTarget = new Vector2();
		cameraSmoothing = 1;

		background = SpriteStore.get().loadImage(BACKGROUND_PATH, true);
		tileset = SpriteStore.get().loadImage(TILESET_PATH, true);
		
		message = null;
		
		setBackground(Color.black);
	}

	
	public void render(GameState s) {
		
		if(backBuffer != null){
			int valid = backBuffer.validate(getGraphicsConfiguration());
			if(valid == VolatileImage.IMAGE_INCOMPATIBLE){
				backBuffer = null;
			}
		}
		if(backBuffer == null){
			backBuffer = createVolatileImage(GravityApplet.WIDTH/SCALE_FACTOR, GravityApplet.HEIGHT/SCALE_FACTOR);
		}

		if(GravityApplet.useGPU){
			background = SpriteStore.get().getVolatileImage(BACKGROUND_PATH);
			tileset = SpriteStore.get().getVolatileImage(TILESET_PATH);
		}
		
		int bufferWidth = backBuffer.getWidth();
		int bufferHeight = backBuffer.getHeight();
		int halfBufWidth = bufferWidth / 2;
		int halfBufHeight = bufferHeight / 2;
		
		int mapHeight = s.map.getHeight();
		int mapWidth = s.map.getWidth();

		if(Double.isNaN(camera.x)){
			camera.set(cameraTarget);
		}else{
			camera = camera.add(cameraTarget.sub(camera).scale(cameraSmoothing));
		}
		
		double cx = camera.x;
		double cy = camera.y;
		int cxm = (int) (cx * TILE_SIZE);
		int cym = (int) (cy * TILE_SIZE);
		
		do{
			Graphics2D g = (Graphics2D) backBuffer.getGraphics();
			
			// Draw background
			g.drawImage(background,
				(int)((bufferWidth-background.getWidth(null)) * cx / mapWidth),
				(int)((bufferHeight-background.getHeight(null)) * cy / mapHeight),
				null);
			
			// Draw map
			int yStart = (int) Math.max(0, Math.floor(cy - halfBufHeight/16) - 1);
			int yEnd = (int) Math.min(mapHeight - 1, Math.ceil(cy + halfBufHeight/16));
			int xStart = (int) Math.max(0, Math.floor(cx - halfBufWidth/16) - 1);
			int xEnd = (int) Math.min(mapWidth - 1, Math.ceil(cx + halfBufWidth/16));
	
			int tileSheetColumns = s.map.getImgwidth() / TILE_SIZE;
			for (int y = yStart; y <= yEnd; y++) {
			    int dy = (int) (y * TILE_SIZE) - cym + halfBufHeight;
				for (int x = xStart; x <= xEnd; x++) {
				    int dx = (int) (x * TILE_SIZE) - cxm + halfBufWidth;
				    
					Tile tile = s.map.getTile(x, y);
					int tileIndex = tile.getTileIndex();
					int frameX = (tileIndex % tileSheetColumns) * TILE_SIZE;
				    int frameY = (tileIndex / tileSheetColumns) * TILE_SIZE;
			    	g.drawImage(tileset,
				    	dx, dy, dx + TILE_SIZE, dy + TILE_SIZE,
				    	frameX, frameY, frameX + TILE_SIZE, frameY + TILE_SIZE,
				    	null);
				}
			}
			
			// Draw objects
			GameObject[] objects = s.objects.toArray(new GameObject[s.objects.size()]);
			// sort by z-index
			Arrays.sort(objects, ZCOMP);
			for (GameObject o : objects) {
				int dx = (int) (o.position.x * TILE_SIZE + 0.5) - cxm
						+ halfBufWidth - o.sprite.getWidth() / 2;
				int dy = (int) (o.position.y * TILE_SIZE + 0.5) - cym
						+ halfBufHeight - o.sprite.getHeight() / 2;
				if(dx >= -o.sprite.getWidth()
				&& dy >= -o.sprite.getHeight()
				&& dx < bufferWidth
				&& dy < bufferHeight){
					o.sprite.draw(g, dx, dy);
				}
			}
			
			// Draw borders
			g.setColor(BORDER_COLOR);
			if(cx * TILE_SIZE - halfBufWidth < 0){
				g.fillRect(0, 0, (int) (halfBufWidth - cx * TILE_SIZE + 1), bufferHeight);
			}
			if(cx * TILE_SIZE + halfBufWidth > s.map.getWidth() * TILE_SIZE){
				int borderWidth = (int) (cx * TILE_SIZE + halfBufWidth - s.map.getWidth() * TILE_SIZE + 1);
				g.fillRect(bufferWidth - borderWidth, 0, borderWidth, bufferHeight);
			}
			if(cy * TILE_SIZE - halfBufHeight < 0){
				g.fillRect(0, 0, bufferWidth, (int) (halfBufHeight - cy * TILE_SIZE + 1));
			}
			if(cy * TILE_SIZE + halfBufHeight > s.map.getHeight() * TILE_SIZE){
				int borderHeight = (int) (cy* TILE_SIZE + halfBufHeight - s.map.getHeight() * TILE_SIZE + 1);
				g.fillRect(0, bufferHeight - borderHeight, bufferWidth, borderHeight);
			}
			
			// Draw HUD
			Player player = game.getPlayerObject();
			if(player != null){
				g.setColor(Color.white);
				g.setFont(font);
				if(player.getGravsLeft() > 0){
					g.drawString("Gravitites: " + player.getGravsLeft(), 5, 20);
				}
				
				String mapName = GravityApplet.lm.maps.get(LevelMenu.lastMap)[0];
				g.drawString(mapName, 380 - mapName.length()*7/2, 20);
			}
	
			// Draw screen effects
			if(fadeTime > 0){
				if(fadeTimer > 0){
					fadeTimer--;
				}
				
				double t = (double) fadeTimer/fadeTime; 
	
				g.setColor(new Color(
					(int) (Math.min(255,
					Math.max(0,
					fadeColorStart.getRed() * t + fadeColorEnd.getRed() * (1 - t)))),
					(int) (Math.min(255,
					Math.max(0,
					fadeColorStart.getGreen() * t + fadeColorEnd.getGreen() * (1 - t)))),
					(int) (Math.min(255,
					Math.max(0,
					fadeColorStart.getBlue() * t + fadeColorEnd.getBlue() * (1 - t)))),
					(int) (Math.min(255,
					Math.max(0,
					fadeColorStart.getAlpha() * t + fadeColorEnd.getAlpha() * (1 - t))))));
	
				if(g.getColor().getAlpha() > 0){
					g.fillRect(0, 0, bufferWidth, bufferHeight);
				}
			}
			
			// Draw "cinematic mode"
			int cineStripHeight = 60;
			if(s.cinematicMode){
				g.setColor(Color.black);
				g.fillRect(0, 0, bufferWidth, cineStripHeight);
				g.fillRect(0, bufferHeight-cineStripHeight, bufferWidth, cineStripHeight);
			}
			
			// Draw messages
			if(message != null){
				int n = message.length();
				if(messageChars < n){
					messageChars += 3;
					if(messageChars > n){
						messageChars = n;
					}
				}
				int marginX = 15;
				int marginY = 5;
				g.setColor(Color.white);
				g.setFont(font);
				int w = drawStringWrapped(g,
					message.substring(0, messageChars),
					marginX,
					bufferHeight - cineStripHeight + font.getSize() + marginY,
					bufferWidth - 2 * marginX,
					cineStripHeight - 2 * marginY);
				if(w >= 0){
					// handle overflow (by scrolling)
					System.out.println("Warning! Message overflow:\n " + message.substring(w));
				}
				if(messageImageName != null && messageImage != null){
					g.drawImage(messageImage, marginX, bufferHeight
							- cineStripHeight - messageImage.getHeight(null)
							+ marginY / 2, null);
				}
			}
	
			// end of drawings
			g.dispose();
			
		}while(backBuffer.contentsLost());

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			requestFocus();
			bs = getBufferStrategy();
		}

		Graphics bg = bs.getDrawGraphics();
		bg.drawImage(backBuffer, 0, 0, GravityApplet.WIDTH,
				GravityApplet.HEIGHT, 0, 0, bufferWidth,
				bufferHeight, null);
		
		if(GravityApplet.debug){
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
			    bg.drawRect(px, py, TILE_SIZE * SCALE_FACTOR, TILE_SIZE * SCALE_FACTOR);
			    bg.drawString(Integer.toHexString(tile.getTileIndex()).toUpperCase(), px + 2, py + 10 + 2);
			    bg.drawString(tx + "," + ty, px + 2, py + 20 + 2);
    		}

        	bg.setColor(Color.red);
			for (GameObject o : s.objects) {
				int x = (int) (((o.position.x - o.size.x / 2) * TILE_SIZE + 0.5 - cxm + halfBufWidth)
						* SCALE_FACTOR);
				int y = (int) (((o.position.y - o.size.y / 2) * TILE_SIZE + 0.5 - cym + halfBufHeight)
						* SCALE_FACTOR);
				bg.drawRect(
					x, y,
					(int) (o.size.x * TILE_SIZE * SCALE_FACTOR),
					(int) (o.size.y * TILE_SIZE * SCALE_FACTOR)
				);
				bg.drawString(o.getClass().getSimpleName(), x + 1, y - 1);
			}

        	bg.setColor(Color.cyan);
			for (Script c : s.scripts) {
				int x = (int) ((c.getRegion().x * TILE_SIZE + 0.5 - cxm + halfBufWidth) * SCALE_FACTOR);
				int y = (int)((c.getRegion().y * TILE_SIZE + 0.5 - cym + halfBufHeight) * SCALE_FACTOR);
				bg.drawRect(
					x, y,
					(int)(c.getRegion().width * TILE_SIZE * SCALE_FACTOR),
					(int)(c.getRegion().height * TILE_SIZE * SCALE_FACTOR)
				);
				bg.drawString(c.getClass().getSimpleName(), x + 1, y - 1);
			}
		}
		
		bs.show();
	}
	
	// Method from http://stackoverflow.com/a/400676
	private int drawStringWrapped(Graphics g, String s, int x, int y, int width, int height){
		// FontMetrics gives us information about the width,
		// height, etc. of the current Graphics object's Font.
		FontMetrics fm = g.getFontMetrics();

		int lineHeight = fm.getHeight();

		int curX = x;
		int curY = y;

		String[] words = s.split("\\s");
		
		int i;
		for(i = 0; i < words.length; i++){
			String word = words[i];
			
			// Find out thw width of the word.
			int wordWidth = fm.stringWidth(word + " ");

			// If text exceeds the width, then move to next line.
			if (curX + wordWidth >= x + width)
			{
				curY += lineHeight;
				curX = x;
			}
			
			// If height exceeded break
			if(curY >= y + height){
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
		messageImageName = null;
		message = null;
	}
	
	public void fade(Color colorStart, Color colorEnd, int duration){
		fadeColorStart = colorStart;
		fadeColorEnd = colorEnd;
		
		if(colorStart == null && colorEnd != null){
			fadeColorStart = new Color(colorEnd.getRed(), colorEnd.getGreen(), colorEnd.getBlue(), 0);
		}else if(colorStart != null && colorEnd == null){
			fadeColorEnd = new Color(colorStart.getRed(), colorStart.getGreen(), colorStart.getBlue(), 0);
		}
		
		fadeTime = fadeTimer = duration;
	}
	
	public void fadeBlack(boolean out, int duration){
		if(out){
			fade(null, Color.black, duration);
		}else{
			fade(Color.black, null, duration);
		}
	}

	public void showMessage(String image, String message) {
		messageChars = 0;
		
		messageImageName = image;
		this.message = message;
		
		if(messageImageName != null){
			messageImage = SpriteStore.get().loadImage("/images/" + messageImageName + ".png", false);
		}
	}


	public void setCameraSmoothing(double v) {
		cameraSmoothing = v;
	}


	public double getCameraSmoothing() {
		return cameraSmoothing;
	}

}
