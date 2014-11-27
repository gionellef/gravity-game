package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;

public class TutorialTarget extends GameObject {

	public TutorialTarget(GameContext game) {
		super(game, "tutorial-target");
		fixed = true;
	}
	
	@Override
	public void update() {
		super.update();
		
		GravWell w = getGame().findObject(GravWell.class, position.x - 3, position.y - 3, 6, 6, true);
		if(w != null){
			die();
		}
	}
}
