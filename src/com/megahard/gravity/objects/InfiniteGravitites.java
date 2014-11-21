package com.megahard.gravity.objects;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;

public class InfiniteGravitites extends Script {

	public InfiniteGravitites(Engine game, Double region) {
		super(game, region);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdate() {
		Player player = getGame().getPlayerObject();
		if(player != null){
			player.setGravs(99);
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
