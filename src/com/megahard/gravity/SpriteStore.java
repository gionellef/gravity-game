package com.megahard.gravity;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/*
 * Singleton
 * Sprite resource manager
 */
public class SpriteStore {
	
	private static SpriteStore single = new SpriteStore();
	private BufferedImage sourceImage;
	
	
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
	public Sprite getSprite(String ref) {
		
		// If sprite is already in cache (hashmap)
		if (sprites.get(ref) != null) {
			return (Sprite) sprites.get(ref);
		}
		
		sourceImage = null;
		System.out.println(ref);
		try {
			sourceImage = ImageIO.read(this.getClass().getResource(ref));
		} catch (IOException e) {
			System.out.println("Failed to load: " + ref);
		}
		
		// create an accelerated image of the right size to store our sprite in
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);
		
		// draw our source image into the accelerated image
		image.getGraphics().drawImage(sourceImage,0,0,null);
		
		// create a sprite, add it the cache then return it
		Sprite sprite = new Sprite(image, 16, 16);
		sprites.put(ref,sprite);
		
		return sprite;
	}
	

}
