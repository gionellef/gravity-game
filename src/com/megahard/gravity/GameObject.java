package com.megahard.gravity;

import java.awt.Graphics;

public class GameObject {
	private final Engine game;

	public Vector2 position;
	public Vector2 velocity;
	public Vector2 size;
	public double mass;
	public Graphics graphics;

	public GameObject(Engine game) {
		this.game = game;
		this.position = new Vector2();
		this.velocity = new Vector2();
		this.size = new Vector2();
		this.mass = 0;
	}
	

	public void collide(GameObject obj) {

	}

	public void update() {
		GameMap map = game.getMap();

		// Do movement
		position = position.add(velocity);
		velocity.y += 0.001; // tmp

		// Get object bounds
		double left = position.x - size.x / 2;
		double right = position.x + size.x / 2;
		double up = position.y - size.y / 2;
		double down = position.y + size.y / 2;

		// Do Y collision
		if (velocity.y >= 0) {
			// Check if the bottom of the object collides with the map
			boolean blocked = false;
			for (double x = left; x < right; x += 1) {
				if (map.getTile(x, down).getCollidable()) {
					blocked = true;
					break;
				}
			}
			if (!blocked && map.getTile(right, down).getCollidable()) {
				blocked = true;
			}

			// collision resolution
			if (blocked) {
				position.y = Math.floor(down) - size.y / 2;
				velocity.y = 0;
			}
		} else {
			// Check if the top of the object collides with the map
			boolean blocked = false;
			for (double x = left; x < right; x += 1) {
				if (map.getTile(x, up).getCollidable()) {
					blocked = true;
					break;
				}
			}
			if (!blocked && map.getTile(right, up).getCollidable()) {
				blocked = true;
			}

			// collision resolution
			if (blocked) {
				position.y = Math.ceil(up) + size.y / 2;
				velocity.y = 0;
			}
		}

		up = position.y - size.y / 2 + 0.01;
		down = position.y + size.y / 2 - 0.01;

		// Do X collision
		if (velocity.x > 0) {
			// Check if the right of the object collides with the map
			boolean blocked = false;
			for (double y = up; y < down; y += 1) {
				if (map.getTile(right, y).getCollidable()) {
					blocked = true;
					break;
				}
			}
			if (!blocked && map.getTile(right, down).getCollidable()) {
				blocked = true;
			}

			// collision resolution
			if (blocked) {
				position.x = Math.floor(right) - size.x / 2;
				velocity.x = 0;
			}
		} else {
			// Check if the left of the object collides with the map
			boolean blocked = false;
			for (double y = up; y < down; y += 1) {
				if (map.getTile(left, y).getCollidable()) {
					blocked = true;
					break;
				}
			}
			if (!blocked && map.getTile(left, down).getCollidable()) {
				blocked = true;
			}

			// collision resolution
			if (blocked) {
				position.x = Math.ceil(left) + size.x / 2;
				velocity.x = 0;
			}
		}
		
		// maxx velcoity`
		if(velocity.length() > 1) {
			velocity = velocity.scale(1/velocity.length());
		}
		
	}
}
