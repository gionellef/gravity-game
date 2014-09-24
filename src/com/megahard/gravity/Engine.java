package com.megahard.gravity;

public class Engine {
	private GameState state;

	public Engine(String levelData) {
		state = new GameState();
	}

	public void addObject(GameObject obj) {
		state.objects.add(obj);
	}

	public void update() {
		// update all the objects
		for (GameObject o : state.objects) {
			o.update();
		}
	}
}
