package com.megahard.gravity;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

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

	private Thread gameThread;
	private Engine engine;
	private MusicPlayer music;
	private Thread musThread = new Thread(music);
	private boolean win;
	private boolean esc;

	private Thread watchThread;
	private boolean watching = false;
	
	private static final int FPS = 30;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public static boolean USE_GPU = false;
	
	@Override
	public void init() {
		Sound.touch();
		music = new MusicPlayer();
		
		lm = new LevelMenu(this);
		rm = new RetryMenu(this);
		ts = new TitleScreen(this);
		showTitleScreen();
		
		setSize(WIDTH, HEIGHT);
		setLayout(new BorderLayout());
		
		watchThread = new Thread(){
			public void run() {
				try {
					while(true){
						Thread.sleep(100);
						
						if(gameThread != null && gameThread.isAlive())
							gameThread.join();
						if(watching && !running){
							watching = false;
							onRealFinish();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		};
		watchThread.start();
	}
	
	@Override
	public void run() {
		watching = true;
		running = true;
		long mspf = 1000/FPS;
		long threshold = mspf * 10;
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
					try {
						engine.getRenderer().render(engine.getState());
					} catch (IllegalStateException e) {
						// no error. haha
					}
					
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
	}

	public void stop() {
		running = false;
	}
	
	@Override
	public void onFinish(int score, int time, boolean win, boolean esc) {
		stop();
		
		this.win = win;
		this.esc = esc;
	}
	
	private void onRealFinish(){
		remove(engine.getRenderer());
		engine = null;
		
		if (win) {
			if (LevelMenu.lastMap<lm.maps.size()-1)
				LevelMenu.lastMap++;
			else
				LevelMenu.lastMap = 0;
			
		}
			
		if (!esc)
			renderGameScreen();
		else
			showRetryMenu();
		
		validate();
		repaint();
	}
	
	private void renderGameScreen() {
		engine = new Engine();
		engine.initialize(lm.maps.get(LevelMenu.lastMap)[1]);
		engine.getRenderer().addKeyListener(engine);
		engine.getRenderer().addMouseListener(engine);
		engine.getRenderer().addMouseMotionListener(engine);
		
		add(engine.getRenderer(), BorderLayout.CENTER);
		
		engine.setFinishListener(this);
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	private void startMusic() {
		music.play(MusicPlayer.mFiles.get(new Random().nextInt(
				MusicPlayer.mFiles.size()))[1]);
		musThread = new Thread(music);
		musThread.start();
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
			startMusic();
		}
		
		if (a.equals(lm.getLeft())) {
			
			if (LevelMenu.lastMap > 0)
				lm.setLevelLabel(lm.maps.get(--LevelMenu.lastMap)[0]);
			
		}
		
		if (a.equals(lm.getRight())) {
			
			if (LevelMenu.lastMap < lm.maps.size()-1)
				lm.setLevelLabel(lm.maps.get(++LevelMenu.lastMap)[0]);
			
		}
		
		if (a.equals(rm.getMenuButton())) {
			showTitleScreen();
			remove (rm);
			validate();
			repaint();
			music.stop();
		}
		
		if (a.equals(rm.getRetryButton())) {
			renderGameScreen();
			remove (rm);
			validate();
		}
	}
	
	private void showTitleScreen() {
		add(ts);
		validate();
		repaint();
	}

	private void showLevelMenu() {
		add(lm);
	}
	
	private void showRetryMenu() {
		add(rm);
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
		frame.setResizable(false);
		applet.init();
		
	}
}
