package com.megahard.gravity;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.imageio.ImageIO;

import com.megahard.gravity.GameMap.Tile;
import com.megahard.gravity.menus.LevelMenu;
import com.megahard.gravity.objects.Player;

public class Renderer extends Canvas {

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
	private BufferedImage buffer;

	private BufferedImage back;
	private BufferedImage tileset;

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
	
	public boolean debug = false;

	private BufferedImage messageImage;
	
	public Renderer(GameContext engine) {
		game = engine;
		camera = new Vector2();
		buffer = new BufferedImage(GravityApplet.WIDTH/SCALE_FACTOR, GravityApplet.HEIGHT/SCALE_FACTOR, BufferedImage.TYPE_INT_RGB);

		back = null;
		tileset = null;
		try {
			back = ImageIO.read(this.getClass().getResource("/back75.png"));
			tileset = ImageIO.read(this.getClass().getResource("/tileset.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		message = null;
		
		setBackground(Color.black);
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
		int cxm = (int) (cx * TILE_SIZE);
		int cym = (int) (cy * TILE_SIZE);
		
		// Draw background
		g.drawImage(back,
			(int)((bufferWidth-back.getWidth()) * cx / mapWidth),
			(int)((bufferHeight-back.getHeight()) * cy / mapHeight),
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
				Tile tile = s.map.getTile(x, y);
				int tileIndex = tile.getTileIndex();
				int frameX = (tileIndex % tileSheetColumns) * TILE_SIZE;
			    int frameY = (tileIndex / tileSheetColumns) * TILE_SIZE;
				
			    int dx = (int) (x * TILE_SIZE) - cxm + halfBufWidth;
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
				g.drawImage(messageImage, marginX, bufferHeight - cineStripHeight - messageImage.getHeight() + marginY/2, null);
			}
		}

		// end of drawings
		g.dispose();

		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			requestFocus();
			bs = getBufferStrategy();
		}
		
		bs.getDrawGraphics().drawImage(buffer, 0, 0, GravityApplet.WIDTH,
				GravityApplet.HEIGHT, 0, 0, bufferWidth,
				bufferHeight, null);
		
		if(debug){
			Graphics bg = bs.getDrawGraphics();
        	bg.setColor(Color.red);
    		bg.setFont(font);
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

	public Vector2 getCamera() {
		return camera;
	}

	public void setCamera(Vector2 camera) {
		this.camera = camera;
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
			try {
				messageImage = ImageIO.read(this.getClass().getResource("/images/" + messageImageName + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
