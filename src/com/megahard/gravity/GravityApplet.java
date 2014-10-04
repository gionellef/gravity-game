package com.megahard.gravity;

import java.applet.Applet;
import java.awt.Graphics;

public class GravityApplet extends Applet {
	public Engine engine;
	public Renderer renderer;

	@Override
	public void init() {
		engine = new Engine();
		renderer = new Renderer();
		engine.initialize("");

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

	@Override
	public void paint(Graphics g) {
		renderer.render(g, engine.getState());
	}
}
