package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.Engine;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.engine.base.Script;
import com.megahard.gravity.objects.Player;

public class IsaacPre extends Script {

	public IsaacPre(Engine game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
		Player player = getGame().getPlayerObject();
		if(player != null)
			player.setSprite("isaac-pre");
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onEnter(GameObject object) {
	}

	@Override
	public void onExit(GameObject object) {
	}

}
