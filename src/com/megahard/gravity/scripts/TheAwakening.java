package com.megahard.gravity.scripts;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;
import java.util.LinkedList;
import java.util.List;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;
import com.megahard.gravity.objects.GravWell;
import com.megahard.gravity.objects.Player;
import com.megahard.gravity.objects.VioletSpark;

public class TheAwakening extends Script {

	private boolean firstRun = true;
	private boolean active = false;
	private int timer;
	
	private Player player;
	private GravWell gw;
	private List<GravWell> wells;
	
	public TheAwakening(Engine game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
		wells = new LinkedList<>();
	}

	@Override
	public void onUpdate() {
		if(active){
			timer++;
			
			if(timer == 10){
				getGame().showMessage("isaac-pre", "...", 30);
			}
			
			if(timer == 40){
				getGame().showMessage("isaac-pre", "What is this violet rock?", 100);
			}
			
			if(timer == 140){
				getGame().showMessage("isaac-pre", "It feels strange... I feel attracted to it somehow...", 100);
			}
			
			if(timer > 240 && timer < 340){
				double m = 0.1 * (1 - (double)(340 - timer)/(340-240));
				player.velocity.y *= 0.9;
				player.velocity.y += (((getRegion().y * 2 + getRegion().getHeight())/2 - player.position.y) - player.velocity.y) * m;
				
				castSparkOnPlayer(1);
			}
			
			if(timer == 260){
				getGame().showMessage("isaac-pre", "Huh!?", 80);
			}
			
			if(timer == 340){
				getGame().showMessage("isaac-pre", "AAAAAARRGH!!!", 150);
				
				player.setGravs(0);
				gw = new GravWell(getGame());
				gw.power = 1.2;
				gw.position.set(player.position.x, player.position.y);
				getGame().addObject(gw);
			}
			
			if(timer == 410){
				// fade to white
				getGame().fadeScreen(null, Color.white, 150);
			}
			
			if(timer > 360 && timer < 640){
				if(Math.random() < 0.1){
					GravWell g = new GravWell(getGame());
					g.power = 0.2;
					g.position.set(player.position.x + Math.random()*16-8, player.position.y + Math.random()*8-4);
					getGame().addObject(g);
					wells.add(g);
				}
				if(Math.random() < wells.size() * 0.01){
					wells.remove(0).destroy();
				}

				double m = 0.1;
				player.position.x += (getRegion().x - player.position.x) * m;
				player.position.y += ((getRegion().y * 2 + getRegion().getHeight())/2 - player.position.y) * m;
				player.velocity.x *= 0.3;
				player.velocity.y *= 0.3;
			}
			
			if(timer == 640){
				// change sprite
				player.setSprite("person");
				
				// fade screen back
				getGame().fadeScreen(Color.white, null, 40);

				while(!wells.isEmpty()){
					wells.remove(0).destroy();
				}
			}
			
			if(timer == 700){
				gw.destroy();
			}
			
			if(timer == 750){
				getGame().showMessage("isaac", "Augh... What just happened!?", 60);
			}
			if(timer == 820){
				getGame().showMessage("isaac", "I feel... ", 80);
			}
			
			if(timer == 900){
				getGame().showMessage("isaac", "I feel... strange...", 80);
			}
				
			if(timer == 980){
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

	private void castSparkOnPlayer(double range) {
		VioletSpark s = new VioletSpark(getGame());
		s.position.set(player.position.x, player.position.y);
		double a = Math.random() * Math.PI * 2;
		double r = Math.random() * range;
		s.velocity.set(Math.cos(a) * r, Math.sin(a) * r - 0.2);
		getGame().addObject(s);
	}

	@Override
	public void onExit(GameObject object) {
	}

}
