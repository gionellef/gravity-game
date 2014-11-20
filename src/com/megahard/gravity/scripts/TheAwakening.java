package com.megahard.gravity.scripts;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;
import com.megahard.gravity.objects.GravWell;
import com.megahard.gravity.objects.Player;

public class TheAwakening extends Script {

	private boolean firstRun = true;
	private boolean active = false;
	private int timer;
	
	private Player player;
	
	public TheAwakening(Engine game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onUpdate() {
		if(active){
			timer++;
			
			if(timer == 5){
				// fade to white
				getGame().fadeScreen(Color.white, true, 10);
			}
			
			if(timer == 20){
				// change sprite
				player.setSprite("person");
				
				// create well
				GravWell gw = new GravWell(getGame());
				gw.power = 1.2;
				gw.position.set(player.position.x, player.position.y - 6);
				getGame().addObject(gw);
				
				// fade screen back
				getGame().fadeScreen(Color.white, false, 20);
			}
			
			if(timer == 30){
				getGame().setCinematicMode(false);
				active = false;
			}
		}
	}

	@Override
	public void onEnter(GameObject object) {
		if(!firstRun) return;

		if(object.getClass().equals(Player.class)){
			firstRun = false;
			active = true;
			timer = 0;
			
			player = (Player) object;
			
			getGame().setCinematicMode(true);
			player.velocity.x = 0;
		}
	}

	@Override
	public void onExit(GameObject object) {
	}

}
