package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;

public class SentinelSwitch extends PowerSource {

	public SentinelSwitch(GameContext game) {
		super(game, "switch-sentinel");
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
