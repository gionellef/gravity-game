package com.megahard.gravity;

import java.util.LinkedList;
import java.util.List;

public class Engine {
	public GameMap map;
	public List<GameObject> objects;

	public Engine(String levelData) {
		objects = new LinkedList<>();
	}

	public void update() {
		for (GameObject o : objects) {
			o.update();
		}
	}
}
