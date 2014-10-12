package com.megahard.gravity;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import com.megahard.gravity.GameMap.Tile;

public class Renderer extends Canvas implements Runnable, KeyListener{
	
	private Engine engine;
	public GameMap map;
	
	public enum Action {
		UP, DOWN, LEFT, RIGHT, JUMP, NONE;
		
		public String value() {
			if (this == UP) {
				return "up";
			}
			if (this == DOWN) {
				return "down";
			}
			if (this == LEFT) {
				return "left";
			}
			if (this == RIGHT) {
				return "right";
			}
			if (this == JUMP) {
				return "jump";
			}
			return "";
		}
	}
	
	private boolean running = false;
	
	public Action action;
	
	private static final int TILE_SIZE = 16;
	
	
	public void start() {
		running = true;
		new Thread(this).start();
		addKeyListener(this);
		action = Action.NONE;
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
			engine.update(action.value());
			render(engine.getState());

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		 switch (e.getKeyCode()) {
         case KeyEvent.VK_W:
             System.out.println("up");
             action = Action.UP;
             break;
         case KeyEvent.VK_A:
             System.out.println("left");
             action = Action.LEFT;
             break;
         case KeyEvent.VK_S:
             System.out.println("down");
             action = Action.DOWN;
             break;
         case KeyEvent.VK_D:
             System.out.println("right");
             action = Action.RIGHT;
             break;
         case KeyEvent.VK_SPACE:
             System.out.println("jump");
             action = Action.RIGHT;
             break;
         default:
        	 action = Action.NONE;
		 }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
