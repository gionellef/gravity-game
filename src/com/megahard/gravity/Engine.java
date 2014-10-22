package com.megahard.gravity;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import com.megahard.gravity.GameMap.Tile;

public class Engine {
	// TEST CODE
	private GameObject player;

	private Renderer renderer;

	public Renderer getRenderer() {
		return renderer;
	}

	private GameState state;

	public GameState getState() {
		return state;
	}

	private List<GameObject> addObj;
	private List<GameObject> removeObj;

	public Engine() {
		state = new GameState();
		addObj = new LinkedList<GameObject>();
		removeObj = new LinkedList<GameObject>();
		renderer = new Renderer();
	}

	public void initialize(String levelData) {
		// populate the game state using level data
		state = new GameState();

		// TEST CODE
		Tile F = Tile.Floor;
		Tile A = Tile.Air;
		new GameMap(8, 10, new Tile[] { F, F, F, F, F, F, F, F, F, A, A, A, A,
				A, A, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A,
				A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A,
				F, F, A, F, A, A, A, A, F, F, A, A, A, A, A, A, F, F, F, F, F,
				F, F, F, F });

		state.map = new GameMap(32, 24, new Tile[] { F, F, F, F, F, F, F, F, F,
				F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F,
				F, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A,
				A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F,
				F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A, A, A,
				A, A, F, F, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A,
				A, A, A, A, A, A, A, A, A, A, A, A, A, F, F, A, A, A, A, A, A,
				A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A,
				A, A, A, F, F, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A,
				A, A, A, A, A, A, A, A, A, A, A, A, A, A, F, F, A, A, A, A, A,
				A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A,
				A, A, A, A, F, F, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A,
				A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, F, F, F, F, F, F,
				F, F, F, F, F, F, A, A, F, F, F, F, F, F, F, F, F, F, F, F, F,
				F, F, F, F, F, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A,
				F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A, A,
				A, A, A, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F,
				A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A,
				A, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A,
				A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F,
				F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, A, A, A, A, A, A,
				A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, F, F, A,
				A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A,
				A, A, A, A, A, A, A, A, F, F, A, A, A, A, A, A, A, A, A, A, A,
				A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, F, F,
				A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A,
				A, A, A, A, A, A, A, A, A, F, F, A, A, A, A, A, A, A, A, A, A,
				A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, A, F,
				F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A, A, A,
				A, A, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A,
				A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A,
				F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A, A,
				A, A, A, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F,
				A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A,
				A, F, F, A, A, A, A, A, A, F, F, A, A, A, A, A, A, F, F, A, A,
				A, A, A, A, F, F, A, A, A, A, A, A, F, F, F, F, F, F, F, F, F,
				F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F, F,
				F, F, F });

		GameObject o = new GameObject(this);
		o.position.set(1.5, 1.5);
		o.velocity.set(0.08, 0);
		o.size.set(0.9, 0.9);
		o.restitution = 0.2;
		o.friction = 0.9;
		o.color = Color.yellow;
		addObject(o);

		GameObject o2 = new GameObject(this);
		o2.position.set(30, 15);
		o2.velocity.set(-0.1, 0);
		o2.size.set(1.9, 1.9);
		o2.friction = 0.9;
		addObject(o2);

		GameObject o3 = new GameObject(this);
		o3.position.set(2, 12);
		o3.velocity.set(0.1, 0);
		o3.size.set(1.2, 1.2);
		o3.restitution = 1;
		o3.friction = 0.5;
		addObject(o3);

		GameObject gro = new GameObject(this) {
			public void update() {
				super.update();

				List<GameObject> objects = state.objects;
				for (GameObject other : objects) {
					if (other != this) {
						Vector2 dir = position.add(other.position.scale(-1));
						double d = dir.length();
						other.velocity = other.velocity.add(dir.scale(0.08 / (d*d)));
					}
				}
				
				position.set(20, 10);
			};
			@Override
			public void collide(GameObject obj) {
				// TEST, DISAPPEAR ON TOUCH
				removeObject(obj);
			}
		};
		gro.size.set(0.5,0.5);
		gro.color = new Color(0.8f, 0, 1);
		addObject(gro);

		player = o;
		renderer.setCamera(player.position);
	}

	public void addObject(GameObject obj) {
		addObj.add(obj);
	}

	public void removeObject(GameObject obj) {
		removeObj.add(obj);
	}

	public GameMap getMap() {
		return state.map;
	}

	public void update() {
		// TEST CODE
		if (renderer.action == Renderer.Action.UP) {
			player.velocity.y -= 0.05;
		}
		if (renderer.action == Renderer.Action.DOWN) {
			player.velocity.y += 0.05;
		}
		if (renderer.action == Renderer.Action.LEFT) {
			player.velocity.x -= 0.05;
		}
		if (renderer.action == Renderer.Action.RIGHT) {
			player.velocity.x += 0.05;
		}

		// add all objects to be added
		state.objects.addAll(addObj);
		addObj.clear();

		// update all the objects
		for (GameObject o : state.objects) {
			o.update();
		}

		// Check inter-obejct collisions assumed rectangles with no rotations
		// (not yet debugged)
		for (GameObject o : state.objects) {
			for (int i = state.objects.indexOf(o) + 1; i < state.objects.size(); i++) {
				GameObject o2 = state.objects.get(i);

				if (o.position.x - o.size.x / 2 < o2.position.x + o2.size.x / 2
						&& o.position.x + o.size.x / 2 > o2.position.x
								- o2.size.x / 2
						&& o.position.y - o.size.y / 2 < o2.position.y
								+ o2.size.y / 2
						&& o.position.y + o.size.y / 2 > o2.position.y
								- o2.size.y / 2) {
					o.collide(o2);
					o2.collide(o);
				}
			}
		}

		// remove all objects to be removed
		state.objects.removeAll(removeObj);
		removeObj.clear();
	}

}
