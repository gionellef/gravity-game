package com.megahard.gravity.objects;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameObject;

public class Galileo extends GameObject {

	public Galileo(GameContext game) {
		super(game, "galileo");
		size.set(1.5, 2);
		mass = 70;
		fixed = true;
		
		zIndex = 600;
	}
	
	@Override
	public void update() {
		super.update();
		
		Player player = getGame().getPlayerObject();
		if(player != null){
			if(player.position.x < position.x - 0.5){
				if(!sprite.getAction().equals("default-left")){
					sprite.setAction("default-left");
				}
			}else if(player.position.x > position.x + 0.5){
				if(!sprite.getAction().equals("default")){
					sprite.setAction("default");
				}
			}
		}
	}

}
