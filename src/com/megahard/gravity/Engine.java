package com.megahard.gravity;

import java.util.LinkedList;
import java.util.List;

import com.megahard.gravity.GameMap.Tile;

public class Engine {
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
	}

	public void initialize(String levelData) {
		// populate the game state using level data
		state = new GameState();
		Tile F = Tile.Floor;
		Tile A = Tile.Air;
		state.map = new GameMap(8, 4, new Tile[] {
			F, F, F, F, F, F, F, F,
			F, A, A, A, A, A, A, F,
			F, A, A, A, A, F, A, F,
			F, F, F, F, F, F, F, F
		});
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
		// add all objects to be added
		state.objects.addAll(addObj);
		addObj.clear();

		// update all the objects
		for (GameObject o : state.objects) {
			o.update();
		}

		// Check inter-obejct collisions assumed rectangles with no rotations (not yet debugged)
		for (GameObject o : state.objects) {
			for (int i = state.objects.indexOf(o) + 1; i < state.objects.size(); i++) {
				GameObject o2 = state.objects.get(i);
				if (o.position.x < o2.position.x + o2.size.x
					&& o.position.x  + o.size.x < o2.position.x
					&& o.position.y < o2.position.y + o2.size.y
					&& o.position.y  + o.size.y < o2.position.y) {
					// collision detected
					o.collide(o2);
				}
			}
		}

		// remove all objects to be removed
		state.objects.removeAll(removeObj);
		removeObj.clear();
	}
}
