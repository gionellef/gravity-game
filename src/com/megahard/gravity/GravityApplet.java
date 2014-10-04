package com.megahard.gravity;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;

public class GravityApplet extends Applet {
	
	private int bufferWidth;
	private int bufferHeight;
	private Image bufferImage;
	private Graphics bufferGraphics;
	
	public Engine engine;
	public Renderer renderer;

	@Override
	public void init() {
		engine = new Engine();
		renderer = new Renderer();
		engine.initialize("");

		// The render loop thread
		new Thread(){
			@Override
			public void run() {

				boolean ever = true;
				for(;ever;){
					engine.update();

					repaint();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();

	}
	
	/* Idea of
	 * DOUBLEBUFFERING YO
	 * First, paint everything into an off-screen panel then when it's ready,
	 * paint the completed off-screen image into the current on-screen panel.
	 */
	@Override
	public void paint(Graphics g) {
		
		// checks buffersize of offscreen panel with onscreen panel
		// important when onscreen panel is resized, so offscreen will follow
		if (bufferWidth!=getSize().width || 
			bufferHeight!=getSize().height ||
			bufferImage==null || bufferGraphics==null) {
			
			// makes off screen and on screen dimensions same
			resetBuffer();
		}
		
		// clears off screen panel if something is drawn
		if (bufferGraphics != null) {
			bufferGraphics.clearRect(0, 0, bufferWidth, bufferHeight);
			
			// paint into offscren panel
			paintBuffer(bufferGraphics);
			
			// paint into onscreen panel
			g.drawImage(bufferImage, 0, 0, this);
		}
	}
	
	private void resetBuffer() {
		// to keep track of image size
		bufferWidth = getSize().width;
		bufferHeight = getSize().height;
		
		// clean up previous image
		if (bufferGraphics != null) {
			bufferGraphics.dispose();
			bufferGraphics = null;
		}
		if (bufferImage != null) {
			bufferImage.flush();
			bufferImage = null;
		}
		
		// Garbage collector, yea!
		System.gc();
		
		// create the new image in the size of the panel
		bufferImage = createImage (bufferWidth,bufferHeight);
		bufferGraphics = bufferImage.getGraphics();
	}
	
	public void paintBuffer(Graphics g) {
		renderer.render(g, engine.getState());
	}
	
	/*
	 * Override the update method when calling repaint() which clears the panel
	 * Routing it straight to paint without clearing panel so no flicker, yea?
	 */
	@Override
	public void update(Graphics g) {
		paint(g);
	}
}
