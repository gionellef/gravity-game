package com.megahard.gravity.objects;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.GameMap;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.util.Sound;
import com.megahard.gravity.util.Vector2;
import com.megahard.gravity.util.Sound.Clips;

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
	private NegravWell negwell = null;
	private boolean isRunning = true;
	private boolean isFacingLeft = false;
	private int gravitites = 0;
	private int jumpsLeft = 0;
	
	private boolean alive = true;

	public Player(GameContext game) {
		super(game, "isaac");
		size.set(0.95, 1.95);
		mass = 50;
		restitution = 0.05;
		friction = 0.6;
		staticFriction = 0.3;
		
		zIndex = 1000;
	}
	
	@Override
	public void update() {
		super.update();
		
		if(alive){
			// Footsteps
			if(sprite.getAction().startsWith("run")){
				if(sprite.getFrame() == 3 || sprite.getFrame() == 9){
					Clips sound = STEP_SOUNDS[RAND.nextInt(STEP_SOUNDS.length)];
					getGame().playSoundAtLocation(sound, position, 0.35); 
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
			if(getGame().keyIsJustPressed(KeyEvent.VK_E)){
				toggleSwitch();
			}
			if(getGame().mouseIsJustPressed(MouseEvent.BUTTON1)){
				conjureGrav(getGame().getMouseGamePosition(), false);
			}
			if(getGame().mouseIsUp(MouseEvent.BUTTON1)){
				if(well != null){
					well.die();
					well = null;
				}
			}
			if(getGame().mouseIsJustPressed(MouseEvent.BUTTON3)){
				conjureGrav(getGame().getMouseGamePosition(), true);
			}
			if(getGame().mouseIsUp(MouseEvent.BUTTON3)){
				if(negwell != null){
					negwell.die();
					negwell = null;
				}
			}
			
		}else{
			staticFriction = 0.1;
			gravitites = 0;
			jumpsLeft = 0;
			isRunning = false;
		}
	}
	
	private void toggleSwitch() {
		Switch sw = getGame().findObject(Switch.class, position.x - 0.5, position.y - 0.5, 1, 1, true);
		if(sw != null){
			sw.setSwitch(!sw.getSwitch());
		}
	}

	@Override
	public void die() {
		alive = false;
		// TODO die animation
//		setSpriteAction("die");
		getGame().removeObject(this);
		
		if(well != null){
			well.die();
			well = null;
		}
		if(negwell != null){
			negwell.die();
			negwell = null;
		}
	}
	
	@Override
	public void onCollide(GameObject obj) {
		Class<? extends GameObject> objClass = obj.getClass();
		if(objClass.equals(Gravitite.class)){
			gravitites++;
			getGame().removeObject(obj);
			getGame().playSoundAtLocation(Sound.power, position, 1);
		}else if(objClass.equals(BigGravitite.class)){
			gravitites += 5;
			getGame().removeObject(obj);
			getGame().playSoundAtLocation(Sound.power, position, 1);
		}
	}
	
	@Override
	public void onHitBottom() {
		double p = Math.min(1, velocity.y);
		
		if(p > 0.5){
			setSpriteAction("land");
			int f = (int) (Math.pow(2 - p*2, 0.3) * sprite.getTotalFrames());
			sprite.setFrame(f);
		}

		Clips sound = LAND_SOUNDS[RAND.nextInt(LAND_SOUNDS.length)];
		getGame().playSoundAtLocation(sound, position, 0.3 + p * 0.4); 
	}

	private void conjureGrav(Vector2 pos, boolean anti) {
		if(gravitites > 0){
			// Find a clear spot for the gravwell to appear
			if(!isAreaClear(pos)){
				Vector2 pro = new Vector2();
				out: for(double r = 0; r < 2; r += 0.1){
					for(double a = 0; a < 2*Math.PI; a += Math.PI/4){
						pro.set(pos.x + r * Math.cos(a), pos.y + r * Math.sin(a));
						if(isAreaClear(pro)){
							pos = pro;
							break out;
						}
					}
				}
				if(pos != pro) return;
			}
			
			// make it appear now
			gravitites--;
			if (anti) {
				if(negwell != null) negwell.die();
				
				negwell = new NegravWell(getGame());
				negwell.position = pos;
				getGame().addObject(negwell);
			}
			else {
				if(well != null) well.die();
				
				well = new GravWell(getGame());
				well.position = pos;
				getGame().addObject(well);
			}
			
			if(sprite.getAction().startsWith("default")){
				setSpriteAction("conjure");
			}
		}
	}
	
	private boolean isAreaClear(Vector2 pos){
		GameMap map = getGame().getMap();
		if(pos.x < 0 || pos.y < 0 || pos.x >= map.getWidth() || pos.y >= map.getHeight()){
			return false;
		}
		
		double radius = 0.5;
		Vector2 se = new Vector2(pos.x + radius, pos.y + radius);
		Vector2 ne = new Vector2(pos.x + radius, pos.y - radius);
		Vector2 nw = new Vector2(pos.x - radius, pos.y - radius);
		Vector2 sw = new Vector2(pos.x - radius, pos.y + radius);
		return
			!map.getTile(pos).getCollidable()
			&& !map.getTile(se).getCollidable()
			&& !map.getTile(ne).getCollidable()
			&& !map.getTile(nw).getCollidable()
			&& !map.getTile(sw).getCollidable();
	}

	public void run(boolean left) {
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

	public void jump() {
		double jumpStrength = 0.5;
		
		if(standing){
			jumpsLeft++;
		}
		if(jumpsLeft > 0){
			jumpsLeft--;
			if(velocity.y < jumpStrength){
				velocity.y = Math.min(velocity.y, -jumpStrength);
			}else{
				velocity.y = -jumpStrength/2;
			}
			setSpriteAction("jump");
			
			if(standing){
				Clips sound = JUMP_SOUNDS[RAND.nextInt(JUMP_SOUNDS.length)];
				getGame().playSoundAtLocation(sound, position, 0.5);
			}
			getGame().playSoundAtLocation(Sound.airjump, position, standing ? 0.4 : 0.6);
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
	
	public int getGravitites(){
		return gravitites;
	}
	
	public GravWell getGravWell(){
		return well;
	}
	
	public NegravWell getNegravWell(){
		return negwell;
	}
	
	public int getJumpsLeft(){
		return jumpsLeft;
	}

	public void setGravitites(int i) {
		gravitites = i;
	}
	
}
