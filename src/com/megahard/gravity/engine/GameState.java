package com.megahard.gravity.engine;

import java.util.ArrayList;

import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.engine.base.Script;

public class GameState {
	public class GameFlags {
		public boolean cinematic = false;
		
		public boolean debug = false;
		public boolean god = false;
		public boolean unli = false;
		public boolean paused = false;
	}

	public GameMap map;
	public ArrayList<GameObject> objects;
	public ArrayList<Script> scripts;
	public GameFlags flags;
	public int time;
	
	public GameState() {
		map = null;
		objects = new ArrayList<>();
		scripts = new ArrayList<>();
		flags = new GameFlags();
		time = 0;
	}
}
