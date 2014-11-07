package com.megahard.gravity;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.imageio.ImageIO;

/*
 * Singleton
 * Sprite resource manager
 */
public class SpriteStore {
	
	public static class SpriteAction {
		public String name;
		public int x;
		public int y;
		public int frames;
	}

	static public class SpriteData{
		public int sheetWidth;
		public int width;
		public int height;
		public SpriteAction[] actions; 
	}
	
	private static SpriteStore single = new SpriteStore();
	private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	/*
	 * returns the single instance of this class
	 */
	public static SpriteStore get() {
		return single;
	}
	
	/*
	 * Retrieves a sprite
	 * @param ref reference to the image
	 * @return sprite instance containing an image of the reference
	 */
	public Sprite getSprite(String name) {
		
		// If sprite is already in cache (hashmap)
		if (sprites.get(name) != null) {
			return (Sprite) sprites.get(name);
		}
		
		// Create new sprite
		Image image = loadImage(name);
		SpriteData data = loadData(name);
		
		// create a sprite, add it the cache then return it
		Sprite sprite = new Sprite(image, data);
		sprites.put(name,sprite);
		
		return sprite;
	}

	private SpriteData loadData(String name) {
		String dataPath = "/objects/" + name + "/data.json";
		InputStream in = getClass().getResourceAsStream(dataPath);
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		SpriteData data = J.gson.fromJson(input, SpriteData.class);
		return data;
	}

	private Image loadImage(String name) {
		String imagePath = "/objects/" + name + "/graphics.png";
		BufferedImage sourceImage = null;
		try {
			sourceImage = ImageIO.read(this.getClass().getResource(imagePath));
		} catch (Exception e) {
			System.out.println("Failed to load: " + imagePath);
		}
		
		// create an accelerated image of the right size to store our sprite in
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);
		
		// draw our source image into the accelerated image
		image.getGraphics().drawImage(sourceImage,0,0,null);
		return image;
	}
	

}
