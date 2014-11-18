package com.megahard.gravity;

import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class Script {
	
	private Engine game;
	
	private Rectangle2D.Double region;
	public List<GameObject> overlaps; // ENGINE-INTERNAL USE ONLY. Kung gusto mong malinis, gawa ka ng interface

	public Script(Engine game, Rectangle2D.Double region) {
		this.game = game;
		this.region = region;
		overlaps = new LinkedList<>();
	}
	
	public abstract void onStart();
	public abstract void onUpdate();
	public abstract void onEnter(GameObject object);
	public abstract void onExit(GameObject object);
	
	protected Engine getGame(){
		return game;
	}
	protected Rectangle2D.Double getRegion(){
		return region;
	}
	protected List<GameObject> getObjects(){
		return Collections.unmodifiableList(overlaps);
	}
}
