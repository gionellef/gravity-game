package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;
import com.megahard.gravity.objects.Player;

public class Script_TheAwakening extends Script {

	private boolean firstRun = true;
	private boolean active = false; 
	private int timer = 0;
	
	public Script_TheAwakening(Engine game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
		// things to run when the game starts
	}

	@Override
	public void onUpdate() {
		// things to run always
		
		if(active){
			
			System.out.println("BLABLAHBLABLAH MESSAGES");
			
			if(timer > 10){
				active = false;
				getGame().setCinematicMode(false);
				// end of show
			}
			timer++;
		}
	}

	@Override
	public void onEnter(GameObject object) {
		// things to run when an object enters the region
		
		// run only once, when the player enters this
		if(firstRun && object.getClass().equals(Player.class)){
			// start the show!
			getGame().setCinematicMode(true);
			
			object.velocity.x = 0;
			
			active = true;
			firstRun = false;
		}
	}

	@Override
	public void onExit(GameObject object) {
		// things to run when an object exits the region
	}

}
