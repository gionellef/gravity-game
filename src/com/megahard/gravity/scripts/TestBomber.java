package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;
import com.megahard.gravity.objects.Bomb;

public class TestBomber extends Script {
	
	private int time = 0;

	public TestBomber(Engine game, Double region) {
		super(game, region);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdate() {
		time++;
		if(time % 100 == 0){
			Bomb b = new Bomb(getGame());
			b.position.set(getRegion().x, getRegion().y);
			b.velocity.set(Math.random() * 0.5, 0);
			getGame().addObject(b);
		}
	}

	@Override
	public void onEnter(GameObject object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExit(GameObject object) {
		// TODO Auto-generated method stub

	}

}
