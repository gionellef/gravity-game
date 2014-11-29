package com.megahard.gravity.objects;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.util.Pathfinder;
import com.megahard.gravity.util.Vector2;

public class Sentinel extends GameObject {

	private boolean isFacingLeft = false;

	private Pathfinder pathfinder;
	
	private List<Vector2> waypoints;
	private int waitTimer = 0;
	
	private boolean wandering = true; 
	private boolean alert = false;
	private SentinelSwitch mySwitch;

	public Sentinel(GameContext game) {
		super(game, "sentinel");
		size.set(1, 1);
		floating = true;
		mass = 15;
		restitution = 1;
		friction = 0.5;
		staticFriction = 0;

		zIndex = 590;
	}

	@Override
	public void init() {
		waypoints = new LinkedList<>();
		pathfinder = new Pathfinder(getGame().getMap(), size);
		mySwitch = findMySwitch();
	}

	private SentinelSwitch findMySwitch() {
		List<SentinelSwitch> ss = getGame().findObjects(SentinelSwitch.class);
		if(ss.isEmpty()) return null;
		
//		ss.sort(new Comparator<SentinelSwitch>() {
//			@Override
//			public int compare(SentinelSwitch o1, SentinelSwitch o2) {
//				double d1 = o1.position.distance(position);
//				double d2 = o2.position.distance(position);
//				return d1 == d2 ? 0 : d1 < d2 ? -1 : 1;
//			}
//		});
//		
		for(SentinelSwitch s : ss){
			if(!s.getSwitch()){
				return s;
			}
		}
		return null;
	}

	@Override
	public void update() {
		super.update();

		velocity.scale(0.9);

		if (waypoints == null || waypoints.isEmpty()) {
			wandering = true;
			Vector2 destination = randomWander();
			waypoints = pathfinder.findPath(position, destination);
		}
		
		doPath();
		
		doSentinel();
	}

	private Vector2 randomWander() {
		double x, y;
		do{
			x = position.x + Math.random() * 20 - 10;
			y = position.y + Math.random() * 20 - 10;
		}while(getGame().getMap().getTile(x, y).getCollidable());
		Vector2 destination = new Vector2(x, y);
		return destination;
	}

	private void doSentinel() {
		if(wandering){
			boolean found = false;
			double sightRadius = 8;
			Player player = getGame().findObject(Player.class, position.x - sightRadius, position.y - sightRadius/2, sightRadius*2, sightRadius, true);
			if(player != null){
				found = true;
			}else{
				GravWell well = getGame().findObject(GravWell.class, position.x - sightRadius, position.y - sightRadius/2, sightRadius*2, sightRadius, true);
				found = well != null;
			}
			
			if(found){
				if(!alert) setAlert(true);
				
				if(mySwitch != null){
					wandering = false;
					waypoints = pathfinder.findPath(position, mySwitch.position.plus(0, -1));
				}
				waitTimer = 15;
			}
		}
		
		if(alert && mySwitch != null){
			if(mySwitch.position.distance(position.plus(0, 0.5)) < 1){
				if(!mySwitch.getSwitch()){
					mySwitch.setSwitch(true);
					setSpriteAction("touch");
					waitTimer = 50;
				}

				mySwitch = findMySwitch();
				wandering = true;
			}
		}
	}

	private void doPath() {
		if (waypoints != null && !waypoints.isEmpty()) {
			Vector2 destination = waypoints.get(0);
			
			Vector2 delta = destination.minus(position);
			
			if(delta.length() > 3 || getGame().getMap().getTile(position.plus(delta.normalized())).getCollidable()){
				// lost, find new path to the final destination
				waypoints = pathfinder.findPath(position, waypoints.get(waypoints.size() - 1));
			}else if (delta.length() > 0.5 + velocity.length() * 3) {
				if(waitTimer > 0){
					waitTimer--;
				}else{
					move(delta);
				}
			} else {
				waypoints.remove(0);
				if(waypoints.isEmpty()){
					waitTimer = alert ? 50 : 100;
				}
			}
		}
	}
	
	public void setAlert(boolean value){
		if(alert != value){
			if(value){
				setSprite("sentinel-alert");
				setSpriteAction("shine");
			}else{
				setSprite("sentinel");
			}
		}
		alert = value;
	}

	public void move(Vector2 direction) {
		isFacingLeft = isFacingLeft ? velocity.x < 0.02 : velocity.x < -0.02;
		double thrust = alert ? 0.4 : 0.1;
		Vector2 force = direction.normalized().times(thrust);
		applyImpulse(force);
		setSpriteAction("fly", new String[] { "fly" });
	}

	@Override
	public void onEndAction(String action) {
		if (!alert && Math.random() < 0.1) {
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
