package com.megahard.gravity;

import java.util.LinkedList;
import java.util.List;

import com.megahard.gravity.GameMap.Tile;

public class Engine {
	public GameState state;
	private List<GameObject> addObj;
	private List<GameObject> removeObj;

	public Engine() {
		state = new GameState();
		addObj = new LinkedList<>();
		removeObj = new LinkedList<>();
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

		// Check inter-obejct collisions

		// remove all objects to be removed
		state.objects.removeAll(removeObj);
		removeObj.clear();
	}
}
