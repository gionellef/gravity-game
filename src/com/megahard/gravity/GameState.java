package com.megahard.gravity;

import java.util.ArrayList;

public class GameState {
	public GameMap map;
	public ArrayList<GameObject> objects;

	public GameState() {
		map = null;
		objects = new ArrayList<GameObject>();
	}
}
