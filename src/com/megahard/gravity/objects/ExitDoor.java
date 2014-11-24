package com.megahard.gravity.objects;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Sound;

public class ExitDoor extends GameObject {

	private int timer = 0;
	
	public ExitDoor(GameContext game) {
		super(game, "door");
		mass = 90;
		fixed = true;
		
		zIndex = 200;
	}
	
	@Override
	public void init() {
		
	}

	@Override
	public void update() {
		super.update();
		
		Player player = getGame().getPlayerObject();
		if(player != null){
			double d = player.position.sub(position).length();
			if(d < 7){
				timer = 100;
				if(sprite.getAction().equals("default")){
					sprite.setAction("opening");
					getGame().playSoundAtLocation(Sound.door_open, position, 1);
				}else if(d < 1 && sprite.getAction().equals("open")){
					if(player.standing && player.velocity.length() < 0.01){
						getGame().finish(true,false);
					}
				}
			}else{
				if(timer > 0){
					timer--;
				}else{
					if(sprite.getAction().equals("open")){
						sprite.setAction("closing");
						getGame().playSoundAtLocation(Sound.door_close, position, 1);
					}
				}
			}
		}
	}
}
