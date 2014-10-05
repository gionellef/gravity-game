package com.megahard.gravity;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.megahard.gravity.GameMap.Tile;

public class Renderer extends Canvas implements Runnable {
	
	private Engine engine;
	public GameMap map;
	
	private boolean running = false;
	
	private static final int TILE_SIZE = 16;
	
	
	public void start() {
		running = true;
		new Thread(this).start();
	}
	
	public void stop() {
		running = false;
	}
	
	public void render(GameState s) {

		BufferStrategy bs = getBufferStrategy();
		
		if (bs == null) {
			createBufferStrategy (2);
			requestFocus();
			return;
		}
		
		if (bs.getDrawGraphics() == null)
			running = false;
		Graphics g = bs.getDrawGraphics();
			
			
		g.fillRect(0, 0, getWidth(), getHeight());
		
		for (int y = 0; y < map.getHeight(); y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				Tile tile = map.getTile(x, y);
				g.setColor(tile.getCollidable() ? Color.black : Color.white);
				g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
		}
		
		for (GameObject o : s.objects) {
			g.setColor(Color.CYAN);
			g.fillRect((int)((o.position.x - o.size.x/2) * TILE_SIZE), (int)((o.position.y - o.size.y/2) * TILE_SIZE), 
					(int)(o.size.x * TILE_SIZE), (int)(o.size.y * TILE_SIZE));
		}
		
		g.dispose();
		bs.show();
	}

	private void init() {
		engine = new Engine();
		engine.initialize("");
		GameState s = engine.getState();
		map = s.map;
	}
	
	@Override
	public void run() {
		init();
		while(running){
			engine.update();
			render(engine.getState());

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
