package com.megahard.gravity.engine;

import java.util.ArrayList;

import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.engine.base.Script;

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