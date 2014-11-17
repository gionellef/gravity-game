package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameMap;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Sound;

public class TogglePlasma extends GameObject {

	private boolean online = true;
	private int sourceX = -1;
	private int sourceY = -1;

	private static final int SOURCE_ON_TILE_INDEX = 0x3C;
	private static final int SOURCE_OFF_TILE_INDEX = 0x3C;

	private int n = 0;
	
	public TogglePlasma(Engine game) {
		super(game, "toggle-plasma");
		size.set(0.2, 0.2);
		mass = 0.5;
		fixed = true;
	}
	
	public void init(){
		sprite.setAction("default");
		sprite.setFrame((int)(Math.random() * sprite.getTotalFrames()));
		
		findSource();
	}
	
	public void setOnline(boolean o){
		online = o;
	}
	
	@Override
	public void onCollide(GameObject obj) {
		if(online){
			if(obj.getClass().equals(Player.class)){
				getGame().removeObject(obj);
			}
		}
	}
	
	private void findSource(){
		int x = (int) position.x;
		int y = (int) position.y;
		for(int r=1; r<10; r++){
			for(int i=r; i>=0; i--){
				GameMap map = getGame().getMap();
				if(map.getTile(x + i, y - i).getTileIndex() == SOURCE_ON_TILE_INDEX){
					sourceX = x + i;
					sourceY = y - i;
					break;
				}
				if(map.getTile(x - i, y + i).getTileIndex() == SOURCE_ON_TILE_INDEX){
					sourceX = x - i;
					sourceY = y + i;
					break;
				}
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		if(online){
			if(Math.random() < 0.1 && n < 20){
				n += 3;
				getGame().playSoundAtLocation(Sound.spark, position, n/20.0);
				RedSpark s = new RedSpark(getGame());
				s.position.set(position.x, position.y);
				double a = Math.random() * Math.PI * 2;
				double r = Math.random() * 0.5;
				s.velocity.set(Math.cos(a) * r, Math.sin(a) * r - 0.2);
				getGame().addObject(s);
			}
		}
		if(n > 0){
			n--;
		}
	}
	
	@Override
	public void onEndAction(String action) {
		if(action == "default")
			getGame().playSoundAtLocation(Sound.plasma, position, 0.8);
	}

}
