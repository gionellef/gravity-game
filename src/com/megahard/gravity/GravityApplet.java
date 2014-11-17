package com.megahard.gravity;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JApplet;
import javax.swing.JFrame;

public class GravityApplet extends JApplet implements Runnable, ActionListener, EngineFinishListener {

	public static TitleScreen ts;
	public static LevelMenu lm;
	
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

	// application mode
	public static void main(String args[]) {
		GravityApplet applet = new GravityApplet();

		JFrame frame = new JFrame("Gravity");
		frame.getContentPane().add(applet);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		applet.init();
	}

	private void showTitleScreen() {
		add(ts);
	}

	private void showLevelMenu() {
		add(lm);
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
	public void onFinish(int score, int time, boolean win) {
		stop();
		System.out.println("win = " + win);
		showTitleScreen();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object a = e.getSource();
		
		if (a.equals(ts.getStartButton())) {
			lm = new LevelMenu(this);
			showLevelMenu();
			
        	remove(ts);
			validate();
			repaint();
		}
		
		if (a.equals(ts.getExitButton())) {
			stop();
			System.exit(1);
		}
		
		if (a.equals(lm.getPlayButton())) {
			engine = new Engine();
			engine.initialize(lm.getLevelLabel());
			engine.getRenderer().addKeyListener(engine);
			engine.getRenderer().addMouseListener(engine);
			engine.getRenderer().addMouseMotionListener(engine);
			
			add(engine.getRenderer(), BorderLayout.CENTER);
			
			engine.setFinishListener(this);
			
			new Thread(this).start();
			
			remove(lm);
			validate();
		}
		
		if (a.equals(lm.getLeft())) {
			System.out.println("Map: " + lm.lastMap);
			if (lm.lastMap > 0)
				lm.setLevelLabel(lm.maps.get(--lm.lastMap));
		}
		
		if (a.equals(lm.getRight())) {
			System.out.println("Map: " + lm.lastMap);
			if (lm.lastMap < lm.maps.size()-1)
				lm.setLevelLabel(lm.maps.get(++lm.lastMap));
		}
	}
	
}
