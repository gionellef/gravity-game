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
	}

	@Override
	public void paint(Graphics g) {
		renderer.render(g, engine.getState());
	}
}
