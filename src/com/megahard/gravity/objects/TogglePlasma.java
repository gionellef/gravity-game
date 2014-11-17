package com.megahard.gravity.objects;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameMap;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Sound;

public class TogglePlasma extends GameObject {

	private boolean online = false;
	private int sourceX = -1;
	private int sourceY = -1;

	private static final int[] SOURCE_ON = {
		59,
		78,
		110,
		133,
	};
	private static final int[] SOURCE_OFF = {
		59 + 8,
		78 + 8,
		110 + 8,
		133 + 8,
	};

	private int n = 0;
	
	public TogglePlasma(Engine game) {
		super(game, "toggle-plasma");
		size.set(0.2, 0.2);
		mass = 0.5;
		fixed = true;
	}
	
	public void init(){
		sprite.setAction("off");
		findSource();
	}
	
	public void setOnline(boolean o, boolean spark){
		setOnline(o);
		if(online && spark){
			getGame().playSoundAtLocation(Sound.spark, position, 1);
			for(int i=0; i<10; i++){
				castSpark(1);
			}
		}
	}

	public void setOnline(boolean o){
		online = o;
		sprite.setAction(online ? "default" : "off");
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
		GameMap map = getGame().getMap();
		
		int maxRadius = 12;
		
		int x = (int) position.x;
		int y = (int) position.y;
		
		out: for(int r=1; r<maxRadius; r++){
			for(int i=r; i>=0; i--){
				for(int sx=-1; sx<=1; sx += 2){
					for(int sy=-1; sy<=1; sy += 2){
						int rsx = (r - i) * sx;
						int rsy = i * sy;
						int tileIndex = map.getTile(x + rsx, y + rsy).getTileIndex();
						for(int index : SOURCE_ON){
							if(tileIndex == index){
								sourceX = x + rsx;
								sourceY = y + rsy;
								break out;
							}
						}
						for(int index : SOURCE_OFF){
							if(tileIndex == index){
								sourceX = x + rsx;
								sourceY = y + rsy;
								break out;
							}
						}
					}
				}
			}
		}

		if(sourceX != -1 && sourceY != -1){
			for(int index : SOURCE_ON){
				if(map.getTile(sourceX, sourceY).getTileIndex() == index){
					setOnline(true);
					break;
				}
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		GameMap map = getGame().getMap();
		
		int sourceTileIndex = map.getTile(sourceX, sourceY).getTileIndex();
		if(online){
			if(Math.random() < 0.1 && n < 20){
				n += 3;
				getGame().playSoundAtLocation(Sound.spark, position, n/20.0);
				castSpark(0.5);
			}

			for(int index : SOURCE_OFF){
				if(sourceTileIndex == index){
					setOnline(false);
					break;
				}
			}
		}else{
			if(sourceX != -1 && sourceY != -1){
				for(int index : SOURCE_ON){
					if(sourceTileIndex == index){
						setOnline(true, true);
						break;
					}
				}
			}
		}
		
		if(n > 0){
			n--;
		}
	}

	private void castSpark(double range) {
		RedSpark s = new RedSpark(getGame());
		s.position.set(position.x, position.y);
		double a = Math.random() * Math.PI * 2;
		double r = Math.random() * range;
		s.velocity.set(Math.cos(a) * r, Math.sin(a) * r - 0.2);
		getGame().addObject(s);
	}
	
	@Override
	public void onEndAction(String action) {
		if(action == "default")
			getGame().playSoundAtLocation(Sound.plasma, position, 0.8);
	}

}
