package com.megahard.gravity;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GravityApplet extends Applet implements Runnable, ActionListener, EngineFinishListener, MouseListener {

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
		showTitleScreen();
		
		setSize(WIDTH, HEIGHT);
		setLayout(new BorderLayout());
	}


	private void showTitleScreen() {
		add(ts);
		this.setBackground(Color.black);
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

		remove(engine.getRenderer());
		engine = null;
	}

	public void stop() {
		running = false;
	}
	
	@Override
	public void onFinish(int score, int time, String message) {
		stop();
		showTitleScreen();
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
			
			engine.setFinishListener(this);
			
			new Thread(this).start();
			
			remove(ts);
			validate();
		}
		
		if (a.equals(ts.getExitButton())) {
			stop();
			System.exit(1);
		}
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == ts.getStartButton()) {
			ts.getStartButton().setBackground(new Color(42,98,61));
		}
		if(e.getSource() == ts.getExitButton()) {
			ts.getExitButton().setBackground(new Color(42,98,61));
		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource() == ts.getStartButton()) {
			ts.getStartButton().setBackground(new Color(26,71,42));
		}
		if(e.getSource() == ts.getExitButton()) {
			ts.getExitButton().setBackground(new Color(26,71,42));
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {}


	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}	
	
}
