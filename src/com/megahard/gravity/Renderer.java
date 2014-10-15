package com.megahard.gravity;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import com.megahard.gravity.GameMap.Tile;

public class Renderer extends Canvas implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vector2 camera;

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

	public Action action;

	private static final int TILE_SIZE = 16;

	public Renderer() {
		camera = new Vector2();
		action = Action.NONE;
		addKeyListener(this);
	}

	public void render(GameState s) {

		BufferStrategy bs = getBufferStrategy();

		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		Graphics g = bs.getDrawGraphics();

		g.fillRect(0, 0, getWidth(), getHeight());
		for (int y = 0; y < s.map.getHeight(); y++) {
			for (int x = 0; x < s.map.getWidth(); x++) {
				Tile tile = s.map.getTile(x, y);
				g.setColor(tile.getCollidable() ? Color.black : Color.white);
				g.fillRect((int) ((x - camera.x) * TILE_SIZE + getWidth() / 2),
						(int) ((y - camera.y) * TILE_SIZE + getHeight() / 2),
						TILE_SIZE, TILE_SIZE);
			}
		}

		for (GameObject o : s.objects) {
			g.setColor(Color.CYAN);
			g.fillRect((int) ((o.position.x - o.size.x / 2 - camera.x)
					* TILE_SIZE + getWidth() / 2), (int) ((o.position.y
					- o.size.y / 2 - camera.y)
					* TILE_SIZE + getHeight() / 2),
					(int) (o.size.x * TILE_SIZE), (int) (o.size.y * TILE_SIZE));
		}

		g.dispose();
		bs.show();
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
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TEST CODE
		action = Action.NONE;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public Vector2 getCamera() {
		return camera;
	}

	public void setCamera(Vector2 camera) {
		this.camera = camera;
	}

}
