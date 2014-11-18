package com.megahard.gravity;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JFrame;

import com.megahard.gravity.menus.LevelMenu;
import com.megahard.gravity.menus.RetryMenu;
import com.megahard.gravity.menus.TitleScreen;

public class GravityApplet extends JApplet implements Runnable, ActionListener, EngineFinishListener {

	public static TitleScreen ts;
	public static LevelMenu lm;
	public static RetryMenu rm;
	
	private static final long serialVersionUID = 1L;

	private boolean running = true;
	
	private Engine engine;
	
	private static final int FPS = 30;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	@Override
	public void init() {
		Sound.touch();
		
		lm = new LevelMenu(this);
		rm = new RetryMenu(this);
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
	
	@Override
	public void run() {
		running = true;
		long mspf = 1000/FPS;
		long threshold = mspf * 4;
		long start;
		long error = 0;
		try {
			while (running) {
				start = System.currentTimeMillis();

				// processing
				if(error > threshold) error = 0;
				engine.update();
				while(error > mspf){
					error -= mspf;
					engine.update();
				}
				if(error <= 0){
					engine.getRenderer().render(engine.getState());
				}
					
				Thread.sleep(1);

				// delay
				error += System.currentTimeMillis() - start - mspf;
				if(error < 0){
					Thread.sleep(-error);
					error = 0;
				}
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
		
		showRetryMenu();
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
			renderGameScreen();
			
			remove(lm);
			validate();
		}
		
		if (a.equals(lm.getLeft())) {
			
			if (LevelMenu.lastMap > 0)
				lm.setLevelLabel(lm.maps.get(--LevelMenu.lastMap)[0]);
			
			System.out.println("Map: " + LevelMenu.lastMap);
		}
		
		if (a.equals(lm.getRight())) {
			
			if (LevelMenu.lastMap < lm.maps.size()-1)
				lm.setLevelLabel(lm.maps.get(++LevelMenu.lastMap)[0]);
			
			System.out.println("Map: " + LevelMenu.lastMap);
		}
		
		if (a.equals(rm.getMenuButton())) {
			showTitleScreen();
			remove (rm);
			validate();
			repaint();
		}
		
		if (a.equals(rm.getRetryButton())) {
			renderGameScreen();
			remove (rm);
			validate();
		}
	}
	
	private void renderGameScreen() {
		
		engine = new Engine();
		engine.initialize(lm.maps.get(LevelMenu.lastMap)[1]);
		engine.getRenderer().addKeyListener(engine);
		engine.getRenderer().addMouseListener(engine);
		engine.getRenderer().addMouseMotionListener(engine);
		
		add(engine.getRenderer(), BorderLayout.CENTER);
		
		engine.setFinishListener(this);
		
		new Thread(this).start();
	}
	
	private void showTitleScreen() {
		add(ts);
	}

	private void showLevelMenu() {
		add(lm);
	}
	
	private void showRetryMenu() {
		add(rm);
	}
	
}
