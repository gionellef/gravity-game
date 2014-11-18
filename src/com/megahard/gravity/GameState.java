package com.megahard.gravity;

import java.util.ArrayList;

public class GameState {
	public GameMap map;
	public ArrayList<GameObject> objects;
	public ArrayList<Script> scripts;
	public boolean cinematicMode;
	public int time;

	public GameState() {
		map = null;
		objects = new ArrayList<>();
		scripts = new ArrayList<>();
		cinematicMode = false;
		time = 0;
	}
}
