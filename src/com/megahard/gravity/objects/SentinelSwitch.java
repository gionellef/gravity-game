package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.util.Sound;

public class SentinelSwitch extends PowerSource {

	public SentinelSwitch(GameContext game) {
		super(game, "switch-sentinel");
		fixed = true;
		size.set(1, 1);
	}
		
	public void setSwitch(boolean on){
		sprite.setAction(on ? "on" : "default");
		setActivated(on);
		
		getGame().playSoundAtLocation(Sound.switch_sentinel, position, 1);
	}

	public boolean getSwitch() {
		return isActivated();
	}
}
