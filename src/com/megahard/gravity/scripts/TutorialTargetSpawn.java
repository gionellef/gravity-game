package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.engine.base.Script;
import com.megahard.gravity.objects.Player;
import com.megahard.gravity.objects.TutorialTarget;

public class TutorialTargetSpawn extends Script {

	private TutorialTarget tt;
	
	public TutorialTargetSpawn(GameContext game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onUpdate() {
		Player player = getGame().findObject(Player.class);
		if(player != null){
			double distance = getCenter().distance(player.position);
			if(tt == null){
				if(distance < 8){
					if(player.getGravitites() > 0){
						tt = new TutorialTarget(getGame());
						tt.position.set(getCenter());
						getGame().addObject(tt);
					}
				}
			}else{
				if(distance > 10){
					tt.die();
					tt = null;
				}
			}
		}
	}

	@Override
	public void onEnter(GameObject object) {
	}

	@Override
	public void onExit(GameObject object) {
	}

}
