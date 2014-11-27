package com.megahard.gravity.engine.base;

import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.util.Vector2;

public abstract class Script {
	
	private GameContext game;
	
	private Rectangle2D.Double region;
	public List<GameObject> overlaps; // ENGINE-INTERNAL USE ONLY. Kung gusto mong malinis, gawa ka ng interface
	private Map<String, String> properties;

	public Script(GameContext game, Rectangle2D.Double region, Map<String, String> properties) {
		this.game = game;
		this.region = region;
		this.properties = properties;
		overlaps = new LinkedList<>();
	}

	public Script(GameContext game, Rectangle2D.Double region) {
		this(game, region, Collections.<String, String> emptyMap());
	}
	
	public abstract void onStart();
	public abstract void onUpdate();
	public abstract void onEnter(GameObject object);
	public abstract void onExit(GameObject object);
	
	protected GameContext getGame(){
		return game;
	}
	public Rectangle2D.Double getRegion(){
		return region;
	}
	protected List<GameObject> getObjects(){
		return Collections.unmodifiableList(overlaps);
	}
	protected String getProperty(String property){
		return properties.get(property);
	}

	protected Vector2 getCenter() {
		return new Vector2(region.getCenterX(), region.getCenterY());
	}
}
