package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;
import java.util.Map;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.engine.base.Script;
import com.megahard.gravity.objects.CreditsGio;
import com.megahard.gravity.objects.CreditsLean;
import com.megahard.gravity.objects.CreditsRenz;

public class Credits extends Script {

	public Credits(GameContext game, Double region,
			Map<String, String> properties) {
		super(game, region, properties);
		// TODO Auto-generated constructor stub
	}

	public Credits(GameContext game, Double region) {
		super(game, region);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onStart() {
		CreditsRenz renz = new CreditsRenz(getGame());
		getGame().addObject(renz);
		renz.position.set(8, 19);
		CreditsLean lean = new CreditsLean(getGame());
		getGame().addObject(lean);
		lean.position.set(15, 16);
		
		CreditsGio gio = new CreditsGio(getGame());
		getGame().addObject(gio);
		gio.position.set(22, 13);

	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub

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
