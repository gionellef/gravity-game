package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.Engine;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.engine.base.Script;
import com.megahard.gravity.objects.Player;

public class SParadoxFall extends Script {

	public SParadoxFall(Engine game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onEnter(GameObject object) {
		if(object.getClass().equals(Player.class)){
			double d = 4;
			for (GameObject o : getGame().findObjects(getRegion().x - 10,
					getRegion().y - 10, getRegion().width + 20,
					getRegion().height + 20, true)) {
				o.position.x -= d;
				o.position.y -= d;
			}
		}
	}

	@Override
	public void onExit(GameObject object) {
	}

}
