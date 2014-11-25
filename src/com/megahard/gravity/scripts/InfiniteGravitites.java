package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.engine.base.Script;
import com.megahard.gravity.objects.Player;

public class InfiniteGravitites extends Script {

	public InfiniteGravitites(GameContext game, Double region) {
		super(game, region);
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
