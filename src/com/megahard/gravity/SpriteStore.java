package com.megahard.gravity;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.megahard.gravity.Sprite.SpriteData;

/*
 * Singleton
 * Sprite resource manager
 */
public class SpriteStore {
	private static SpriteStore single = new SpriteStore();
	private HashMap<String, VolatileImage> volatileImages = new HashMap<>();
	private HashMap<String, BufferedImage> images = new HashMap<>();
	
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
		// Create new sprite
		String imagePath = getImagePath(name);
		Image image = loadImage(imagePath, false);
		SpriteData data = loadData(name);
		Sprite sprite = new Sprite(imagePath, image, data);
		
		return sprite;
	}

	public String getImagePath(String name) {
		return "/objects/" + name + "/graphics.png";
	}

	private SpriteData loadData(String name) {
		String dataPath = "/objects/" + name + "/data.json";
		InputStream in = getClass().getResourceAsStream(dataPath);
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		SpriteData data = J.gson.fromJson(input, SpriteData.class);
		return data;
	}

	public Image loadImage(String imagePath, boolean vram) {
		if (images.containsKey(imagePath)) {
			return vram ? getVolatileImage(imagePath) : images.get(imagePath);
		}
		
		BufferedImage sourceImage = null;
		try {
			sourceImage = ImageIO.read(this.getClass().getResource(imagePath));
		} catch (Exception e) {
			System.out.println("Failed to load: " + imagePath);
		}
		
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage bimage = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);

		Graphics2D bg = bimage.createGraphics();
		bg.drawImage(sourceImage,0,0,null);
		bg.dispose();
		
		images.put(imagePath, bimage);
		
		return vram ? getVolatileImage(imagePath) : bimage;
	}

	public VolatileImage getVolatileImage(String imagePath){
		VolatileImage vimage = volatileImages.get(imagePath);
		
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		if(vimage == null){
			vimage = createVolatileImage(imagePath);
			volatileImages.put(imagePath, vimage);
		}
		int valid = vimage.validate(gc);
		if(valid == VolatileImage.IMAGE_RESTORED) {
			redrawVolatileImage(vimage, imagePath);
		}else if(valid != VolatileImage.IMAGE_OK){
			vimage = createVolatileImage(imagePath);
			volatileImages.put(imagePath, vimage);
		}

		return vimage;
	}

	private VolatileImage createVolatileImage(String imagePath) {
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		BufferedImage bimage = images.get(imagePath);

		VolatileImage vimage;
		try {
			vimage = gc.createCompatibleVolatileImage(bimage.getWidth(),
					bimage.getHeight(), new ImageCapabilities(true),
					VolatileImage.BITMASK);
		} catch (AWTException e) {
			vimage = gc.createCompatibleVolatileImage(bimage.getWidth(),
					bimage.getHeight(), Transparency.BITMASK);
		}

		redrawVolatileImage(vimage, imagePath);
		
		return vimage;
	}

	private void redrawVolatileImage(VolatileImage vimage, String imagePath) {
		BufferedImage bimage = images.get(imagePath);
		do{
			Graphics2D vg = vimage.createGraphics();
			vg.setComposite(AlphaComposite.Src);
			vg.clearRect(0, 0, vimage.getWidth(), vimage.getHeight());
			vg.drawImage(bimage,0,0,null);
			vg.dispose();
		}while(vimage.contentsLost());
	}
	

}
