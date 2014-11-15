package com.megahard.gravity;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GravityApplet extends Applet implements Runnable, ActionListener {

	/**
	 * 
	 */
	
	public static TitleScreen ts;
	
	private static final long serialVersionUID = 1L;

	private boolean running = true;
	
	private Engine engine;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	@Override
	public void init() {
		Sound.touch();
		
		ts = new TitleScreen(this);
		add(ts);
		this.setBackground(Color.black);
		
		setSize(WIDTH, HEIGHT);
		setLayout(new BorderLayout());
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

	@Override
	public void actionPerformed(ActionEvent e) {
		Object a = e.getSource();
		
		if (a.equals(ts.getStartButton())) {
			System.out.println("uhm");
			
			
			engine = new Engine();
			engine.initialize("");
			engine.getRenderer().addKeyListener(engine);
			engine.getRenderer().addMouseListener(engine);
			engine.getRenderer().addMouseMotionListener(engine);
			
			add(engine.getRenderer(), BorderLayout.CENTER);
			
			new Thread(this).start();
			
			remove(ts);
			validate();
		}
		
		if (a.equals(ts.getExitButton())) {
			running = false;
			System.exit(1);
		}
	}	
	
}
