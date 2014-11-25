package com.megahard.gravity.scripts;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;
import java.util.LinkedList;
import java.util.List;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.objects.Box;
import com.megahard.gravity.objects.GravWell;
import com.megahard.gravity.objects.Player;
import com.megahard.gravity.objects.VioletSpark;

public class TheAwakening extends ScriptSequencer {
	
	private Player player;
	private GravWell gw;
	private List<GravWell> wells;
	
	public TheAwakening(GameContext game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
		wells = new LinkedList<>();
		
		addDelay(10);
		addMessage("isaac-pre", "...", 30);
		addMessage("isaac-pre", "What is this rock?", 100);
		addMessage("isaac-pre", "It feels strange... I feel attracted to it somehow...", 100);
		
		// Float
		Runnable floater = new Runnable() {
			private int timer = 0;
			@Override
			public void run() {
				timer++;
				double m = 0.1 * (double)timer/100;
				player.velocity.y *= 0.9;
				player.velocity.y += (((getRegion().y * 2 + getRegion().getHeight())/2 - 1 - player.position.y) - player.velocity.y) * m;
				player.position.x += (getRegion().x - player.position.x) * m;
				
				castSparkOnPlayer(1);
				
				if(timer == 20){
					getGame().showMessage("isaac-pre", "Huh!?", 80);
				}
			}
		};
		for(int i = 0; i < 100; i++){
			addRunnable(floater, 1);
		}
		
		// First gravity well
		addRunnable(new Runnable() {
			@Override
			public void run() {
				getGame().showMessage("isaac-pre", "AAAAAARRGH!!!", 150);
				
				player.setGravs(0);
				gw = new GravWell(getGame());
				gw.power = 1.2;
				gw.position.set(player.position.x, player.position.y);
				getGame().addObject(gw);				
			}
		}, 50);
		
		// Gravity circle
		Runnable circler = new Runnable() {
			private int timer = 0;
			@Override
			public void run() {
				timer++;
				
				if(Math.random() < 0.06 && wells.size() < 6){
					GravWell g = new GravWell(getGame());
					g.power = 0.3;
					g.position.set(player.position);
					getGame().addObject(g);
					wells.add((int) (Math.random() * wells.size()), g);
				}
				
				int i = 0;
				double r = 3 + wells.size() * 0.3;
				for(GravWell w : wells){
					double a = Math.PI * 2 * (1 - (double)i / wells.size()) + timer * 0.016;
					w.position.x += (player.position.x + Math.cos(a) * r - w.position.x) * 0.1;
					w.position.y += (player.position.y + Math.sin(a) * r - w.position.y) * 0.1;
					i++;
				}
				
				for(Box b : getGame().findObjects(Box.class)){
					b.velocity.x *= 0.95;
					b.velocity.y *= 0.95;
					double d = b.position.minus(player.position).length();
					if(d < 2){
						double a = Math.atan2(b.position.y - player.position.y, b.position.x - player.position.x);
						b.velocity.x += Math.cos(a);
						b.velocity.y += Math.sin(a);
					}
					b.velocity.y -= GameObject.GRAVITY;
				}

				double m = 0.1;
				player.position.x += (getRegion().x - player.position.x) * m;
				player.position.y += ((getRegion().y * 2 + getRegion().getHeight())/2 - player.position.y) * m;
				player.velocity.x *= 0.9;
				player.velocity.y *= 0.9;

				castSparkOnPlayer(8);
				castSparkOnPlayer(4);
				
				if(timer == 100){
					getGame().fadeScreen(null, Color.white, 80);
				}
			}
		};
		for(int i = 0; i < 200; i++){
			addRunnable(circler, 1);
		}
		
		// Transformed
		addRunnable(new Runnable() {
			@Override
			public void run() {
				// change sprite
				player.setSprite("isaac");
				
				// fade screen back
				getGame().fadeScreen(Color.white, null, 40);
				
				// throw boxes away
				for(Box b : getGame().findObjects(Box.class)){
					double a = Math.atan2(b.position.y - player.position.y, b.position.x - player.position.y);
					b.velocity.x += Math.cos(a) * 0.2;
					b.velocity.y += Math.sin(a) * 0.2;
				}
			}
		}, 30);
		
		// Destroy gravity circle
		addRunnable(new Runnable() {
			@Override
			public void run() {
				while(!wells.isEmpty()){
					wells.remove(0).kill();
				}				
			}
		}, 30);
		addRunnable(new Runnable() {
			@Override
			public void run() {
				gw.kill();
			}
		}, 30);
		
		addMessage("isaac", "What just happened!?", 60);
		addMessage("isaac", "My hair! It turned white!", 80);
		addMessage("isaac", "I feel...", 80);
		addMessage("isaac", "I feel strange...", 80);
		addMessage("isaac", "I feel a strange power...", 100);
		addMessage("isaac", "It's like something has awakened inside of me.", 100);
	}

	@Override
	public void onEnter(GameObject object) {
		if(object.getClass().equals(Player.class)){
			player = (Player) object;
			player.velocity.x = 0;
			beginSequence(true, true, false);
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

	@Override
	protected void onSkip() {
	}

	@Override
	protected void onEnd() {
	}

}
