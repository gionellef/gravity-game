package com.megahard.gravity;

import java.util.LinkedList;
import java.util.List;

public class Engine {
	private GameState state;
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
	}

	public void addObject(GameObject obj) {
		addObj.add(obj);
	}

	public void removeObject(GameObject obj) {
		removeObj.add(obj);
	}

	public void update() {
		// add all objects to be added
		state.objects.addAll(addObj);
		addObj.clear();

		// update all the objects
		for (GameObject o : state.objects) {
			o.update();
		}

		// remove all objects to be removed
		state.objects.removeAll(removeObj);
		removeObj.clear();
	}
}
