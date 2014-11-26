package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;

public class Switch extends PowerSource {

	public Switch(GameContext game) {
		super(game, "switch");
		fixed = true;
		size.set(1, 1);
	}
		
	public void setSwitch(boolean on){
		sprite.setAction(on ? "on" : "default");
		setActivated(on);
	}

	public boolean getSwitch() {
		return isActivated();
	}
}
