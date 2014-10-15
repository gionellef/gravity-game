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

	@Override
	public void init() {
		engine = new Engine();

		engine.initialize("");

		setLayout(new BorderLayout());
		add(engine.getRenderer(), BorderLayout.CENTER);
	}

	public void start() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		running = true;
		try {
			while (running) {
				engine.update();
				engine.getRenderer().render(engine.getState());

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
