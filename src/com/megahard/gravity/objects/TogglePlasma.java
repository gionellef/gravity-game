package com.megahard.gravity.objects;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameMap;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Sound;

public class TogglePlasma extends GameObject {

	private boolean online = false;
	private int sourceX = -1;
	private int sourceY = -1;
	private double power = 0;

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

	public TogglePlasma(GameContext game) {
		super(game, "toggle-plasma");
		size.set(0.7, 0.7);
		mass = 0.5;
		fixed = true;
		
		zIndex = 500;
	}
	
	public void init(){
		sprite.setAction("off");
		findSource();
	}
	
	public void setOnline(boolean o, boolean spark){
		setOnline(o);
		if(online && spark){
			getGame().playSoundAtLocation(Sound.spark, position, 1);
			castSparks(10);
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
				obj.kill();
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

		double dx = position.x - sourceX;
		double dy = position.y - sourceY;
		double d = dx*dx + dy*dy;
		
		if(online){
			if(Math.random() < 0.02){
				int n = 1 + (int) (Math.random() * 6);
				getGame().playSoundAtLocation(Sound.spark, position, 0.3 + n/12.0);
				castSparks(n);
			}

			for(int index : SOURCE_OFF){
				if(sourceTileIndex == index){
					power -= 9/(d+1);
					break;
				}
			}
			if(power <= 0){
				power = 0;
				setOnline(false);
			}
		}else{
			if(sourceX != -1 && sourceY != -1){
				for(int index : SOURCE_ON){
					if(sourceTileIndex == index){
						power += 8/(d+1);
						break;
					}
				}
				if(power >= 1){
					power = 1;
					setOnline(true, true);
				}
			}
		}
	}

	private void castSparks(int n) {
		for(int i = n; i > 0; i--){
			RedSpark s = new RedSpark(getGame());
			s.position.set(position.x, position.y);
			double a = Math.random() * Math.PI * 2;
			double r = Math.random() * 0.5;
			s.velocity.set(Math.cos(a) * r, Math.sin(a) * r - 0.2);
			getGame().addObject(s);
		}
	}

	@Override
	public void onStartAction(String action) {
		if(online)
			getGame().playSoundAtLocation(Sound.plasma, position, 0.4);
	}

}
