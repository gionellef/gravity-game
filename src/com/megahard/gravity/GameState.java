package com.megahard.gravity;

import java.util.LinkedList;
import java.util.List;

public class GameState {
	public GameMap map;
	public List<GameObject> objects;

	public GameState() {
		map = null;
		objects = new LinkedList<GameObject>();
	}
}
