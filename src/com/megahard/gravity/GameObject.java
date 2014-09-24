package com.megahard.gravity;

import java.awt.Graphics;

public class GameObject {
	public Vector2 position;
	public Vector2 velocity;
	public Vector2 size;
	public double mass;
	public Graphics graphics;

	public GameObject() {
	}

	public void update() {
		position = position.translate(velocity); // movement due to velocity
		velocity.y += 1; // acceleration due to gravity

		// COLLISION DETECTION GOES HERE
	}
}
