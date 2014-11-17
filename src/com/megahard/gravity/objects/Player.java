package com.megahard.gravity.objects;

import java.awt.event.KeyEvent;
import java.util.Random;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameMap;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Sound;
import com.megahard.gravity.Sound.Clips;
import com.megahard.gravity.Vector2;

public class Player extends GameObject {
	private static final Clips[] STEP_SOUNDS = new Clips[]{
		Sound.step1,
		Sound.step2,
		Sound.step3,
		Sound.step4,
		Sound.step5,
		Sound.step6,
		Sound.step7,
	};
	private static final Clips[] JUMP_SOUNDS = new Clips[]{
		Sound.jump1,
		Sound.jump2,
		Sound.jump3,
		Sound.jump4,	
	};
	private static final Clips[] LAND_SOUNDS = new Clips[]{
		Sound.land1,
		Sound.land2,
		Sound.land3,
		Sound.land4,	
	};
	private static final Random RAND = new Random();
	
	private GravWell well = null;
	private boolean isRunning = true;
	private boolean isFacingLeft = false;
	private int gravsLeft = 0;
	private int jumpsLeft = 0;

	public Player(Engine game) {
		super(game, "person");
		size.set(0.95, 1.95);
		mass = 50;
		restitution = 0.05;
		friction = 0.6;
		staticFriction = 0.3;
	}
	
	@Override
	public void update() {
		super.update();
		
		// Footsteps
		if(sprite.getAction().startsWith("run")){
			if(sprite.getFrame() == 3 || sprite.getFrame() == 9){
				Clips sound = STEP_SOUNDS[RAND.nextInt(STEP_SOUNDS.length)];
				getGame().playSoundAtLocation(sound, position, 0.5); 
			}
		}
		
		// Animations
		if(standing){
			if(!isRunning){
				setSpriteAction("default", new String[]{"land", "conjure", "default"});
			}
		} else {
			if(!sprite.getAction().startsWith("fall")){
				setSpriteAction("fall", new String[]{"jump"});
			}
		}

		// State tracking
		if(standing){
			jumpsLeft = 1;
			staticFriction = 0.3;
		}else{
			staticFriction = 0;
		}
		isRunning = false;
		
		// Controls
		if(getGame().keyIsJustPressed(KeyEvent.VK_SPACE) 
				|| getGame().keyIsJustPressed(KeyEvent.VK_W)){
			jump();
		}else{
			if(getGame().keyIsDown(KeyEvent.VK_A)){
				run (true);
			}
			else if(getGame().keyIsDown(KeyEvent.VK_D)){
				run (false);
			}
		}
		if(getGame().mouseLeftIsJustPressed()){
			conjureGrav(getGame().getMouseGamePosition());
		}
		if(getGame().mouseLeftIsUp()){
			if(well != null){
				well.destroy();
				well = null;
			}
		}
		
		// Door
		if(standing && velocity.length() < 0.05){
			ExitDoor door = getGame().findObject(ExitDoor.class, position.x - 0.5, position.y - 1, 1, 2, false);
			if(door != null){
				getGame().finish(true);
				System.out.println("YOU WIN!");
			}
		}
	}
	
	@Override
	public void onCollide(GameObject obj) {
		Class<? extends GameObject> objClass = obj.getClass();
		if(objClass.equals(PowerItem.class)){
			gravsLeft++;
			getGame().removeObject(obj);
			getGame().playSoundAtLocation(Sound.power, position, 1);
		}
	}
	
	@Override
	public void onHitBottom() {
		setSpriteAction("land");
		double p = Math.min(1, velocity.y);
		int f = (int) (Math.pow(1 - p, 0.3) * sprite.getTotalFrames());
		sprite.setFrame(f);

		Clips sound = LAND_SOUNDS[RAND.nextInt(LAND_SOUNDS.length)];
		getGame().playSoundAtLocation(sound, position, 0.4 + p * 0.6); 
	}

	private void conjureGrav(Vector2 pos) {
		if(!isAreaClear(pos)){
			Vector2 pro = new Vector2();
			out: for(double r = 1; r < 4; r += 0.5){
				for(double a = 0; a < 2*Math.PI; a += Math.PI/2){
					pro.set(pos.x + r * Math.cos(a), pos.y + r * Math.sin(a));
					if(isAreaClear(pro)){
						pos = pro;
						break out;
					}
				}
			}
			if(pos != pro) return;
		}
		if(gravsLeft > 0){
			gravsLeft--;
			
			if(well != null) well.destroy();
			
			well = new GravWell(getGame());
			well.position = pos;
			getGame().addObject(well);
			
			if(sprite.getAction().startsWith("default")){
				setSpriteAction("conjure");
			}
		}
	}
	
	private boolean isAreaClear(Vector2 pos){
		Vector2 se = new Vector2(pos.x + 1, pos.y + 1);
		Vector2 ne = new Vector2(pos.x + 1, pos.y - 1);
		Vector2 nw = new Vector2(pos.x - 1, pos.y - 1);
		Vector2 sw = new Vector2(pos.x - 1, pos.y + 1);
		GameMap map = getGame().getMap();
		return
			!map.getTile(pos).getCollidable()
			&& !map.getTile(se).getCollidable()
			&& !map.getTile(ne).getCollidable()
			&& !map.getTile(nw).getCollidable()
			&& !map.getTile(sw).getCollidable();
	}

	private void run(boolean left) {
		double runStrength = 0.12;
		double airStrength = 0.06;
		double airMaxSpeed = 0.2;
		double sign = left ? -1 : 1;
		isFacingLeft = left;
		if(standing && !sprite.getAction().startsWith("land")){
			isRunning = true;
			velocity.x += runStrength * sign / friction;
			staticFriction = runStrength;
			setSpriteAction("run", new String[]{"run"});
		}else{
			if(velocity.x * sign < airMaxSpeed){
				velocity.x += airStrength * sign;
			}
		}
	}

	private void jump() {
		double jumpStrength = 0.5;
		
		if(standing){
			jumpsLeft++;
		}
		if(jumpsLeft > 0 && velocity.y < jumpStrength){
			jumpsLeft--;
			velocity.y = Math.min(velocity.y, -jumpStrength);
			
			setSpriteAction("jump");
			
			Clips sound = JUMP_SOUNDS[RAND.nextInt(JUMP_SOUNDS.length)];
			getGame().playSoundAtLocation(sound, position, 0.8); 
			getGame().playSoundAtLocation(Sound.airjump, position, standing ? 0.4 : 0.8);
		}
	}
	
	private void setSpriteAction(String action){
		setSpriteAction(action, null);
	}

	private void setSpriteAction(String action, String[] inhibit){
		if(inhibit != null){
			boolean cont = false;
			String current = sprite.getAction();
			if(current.endsWith("-left")){
				current = current.substring(0, current.length() - 5);
				if(!isFacingLeft && action.equals(current)){
					cont = true;
				}
			}else{
				if(isFacingLeft && action.equals(current)){
					cont = true;
				}
			}
			if(!cont)
				for(String i : inhibit)
					if(i.equals(current)) return;
		}
		
		if(isFacingLeft){
			action += "-left";
		}
		sprite.setAction(action);
	}
	
	public int getGravsLeft(){
		return gravsLeft;
	}
	
	public int getJumpsLeft(){
		return jumpsLeft;
	}
	
}
