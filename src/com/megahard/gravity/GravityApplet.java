package com.megahard.gravity;

import java.applet.Applet;
import java.awt.BorderLayout;

public class GravityApplet extends Applet implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean running = true;

	private Engine engine;
	private Renderer renderer;

	@Override
	public void init() {
		engine = new Engine();
		renderer = new Renderer();

		engine.initialize("");

		setLayout(new BorderLayout());
		add(renderer, BorderLayout.CENTER);
	}

	public void start() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		running = true;
		try {
			while (running) {
				engine.update(renderer.action);
				renderer.render(engine.getState());

				Thread.sleep(25);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		running = false;
	}
}
