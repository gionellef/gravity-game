package com.megahard.gravity.objects;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.util.Sound;

public class Button extends PowerSource {

	private int pressCounter = 0;

	public Button(GameContext game) {
		super(game, "button");
		size.set(1.6, 0.3);
		mass = 30;
		fixed = true;

		zIndex = 250;
	}

	@Override
	public void update() {
		super.update();

		if (pressCounter > 0) {
			pressCounter--;
			if (!isActivated())
				setPressed(true);
		} else {
			if (isActivated())
				setPressed(false);
		}
	}

	private void setPressed(boolean pressed) {
		sprite.setAction(pressed ? "pressed" : "default");
		if (pressed)
			getGame().playSoundAtLocation(Sound.button_press, position, 0.6);
		else
			getGame().playSoundAtLocation(Sound.button_release, position, 0.6);

		setActivated(pressed);
	}

	@Override
	public void onCollide(GameObject obj) {
		if (!obj.fixed) {
			if (obj.mass > 30) {
				pressCounter = 2;
			}
		}
	}

}
