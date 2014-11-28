package com.megahard.gravity.objects;

import java.util.LinkedList;
import java.util.List;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.util.Pathfinder;
import com.megahard.gravity.util.Vector2;

public class Sentinel extends GameObject {

	private boolean isFacingLeft = false;
	
	private List<Vector2> waypoints;
	private int waitTimer = 0;
	
	private Pathfinder pathfinder;

	public Sentinel(GameContext game) {
		super(game, "sentinel");
		size.set(1, 1);
		floating = true;
		mass = 15;
		restitution = 1;
		friction = 0.5;
		staticFriction = 0;

		zIndex = 600;
	}

	@Override
	public void init() {
		waypoints = new LinkedList<>();
		pathfinder = new Pathfinder(getGame().getMap());
	}

	@Override
	public void update() {
		super.update();

		velocity.scale(0.9);

		if (waypoints == null || waypoints.isEmpty()) {
			double x, y;
			do{
				x = position.x + Math.random() * 20 - 10;
				y = position.y + Math.random() * 20 - 10;
			}while(getGame().getMap().getTile(x, y).getCollidable());
			
			waypoints = pathfinder.findPath(position, new Vector2(x, y), size.length()/2);
			
			waitTimer = 100;
		}
		
		doPath();
	}

	private void doPath() {
		if (waypoints != null && !waypoints.isEmpty()) {
			Vector2 destination = waypoints.get(0);
			Vector2 delta = destination.minus(position);
			
			if(delta.length() > 3 || getGame().getMap().getTile(destination).getCollidable()){
				// lost, find new path to the final destination
				waypoints = pathfinder.findPath(position, waypoints.get(waypoints.size() - 1), size.length()/2);
			}else if (delta.length() > 1) {
				if(waitTimer > 0){
					waitTimer--;
				}else{
					move(delta);
				}
			} else {
				if(delta.length() > 0){
					move(delta);
				}
				waypoints.remove(0);
			}
		}
	}

	public void move(Vector2 direction) {
		isFacingLeft = direction.x < 0;
		double thrust = 0.1;
		applyImpulse(direction.normalized().times(thrust));
		setSpriteAction("fly", new String[] { "fly" });
	}

	@Override
	public void onEndAction(String action) {
		if (Math.random() < 0.1) {
			if (action.startsWith("default")) {
				setSpriteAction("shine");
			}
		}
	}

	private void setSpriteAction(String action) {
		setSpriteAction(action, null);
	}

	private void setSpriteAction(String action, String[] inhibit) {
		if (inhibit != null) {
			boolean cont = false;
			String current = sprite.getAction();
			if (current.endsWith("-left")) {
				current = current.substring(0, current.length() - 5);
				if (!isFacingLeft && action.equals(current)) {
					cont = true;
				}
			} else {
				if (isFacingLeft && action.equals(current)) {
					cont = true;
				}
			}
			if (!cont)
				for (String i : inhibit)
					if (i.equals(current))
						return;
		}

		if (isFacingLeft) {
			action += "-left";
		}
		sprite.setAction(action);
	}

}
