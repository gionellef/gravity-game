package com.megahard.gravity.scripts;

import java.awt.geom.Rectangle2D.Double;
import java.util.LinkedList;
import java.util.Queue;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameMap;
import com.megahard.gravity.GameMap.Tile;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;
import com.megahard.gravity.objects.FallenPlatform;
import com.megahard.gravity.objects.Player;

public class Bridge extends Script {
	
	private boolean started = false;
	private double progress;
	private Queue<Integer> fallQueue;

	public Bridge(Engine game, Double region) {
		super(game, region);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onStart() {
		fallQueue = new LinkedList<>(); 
	}

	@Override
	public void onUpdate() {
		int y = (int) (getRegion().y + 0.5);

		if(!started){
			for(GameObject o : getObjects()){
				if(o.getClass().equals(Player.class)){
					started = true;
					progress = getRegion().x - 1;
					fallQueue.add((int)(getRegion().x + 2)/2*2);
				}
			}
		}else{
			progress += 0.12;
			fallQueue.add((int)(progress)/2*2);
		}
		
		if(fallQueue.size() > 0){
			int x = fallQueue.remove();
			for(int i = (int)(getRegion().x)/2*2; i <= x; i += 2){
				fellPlatform(i, y);
			}
		}
	}

	private void fellPlatform(int x, int y) {
		GameMap map = getGame().getMap();
		Tile tile = map.getTile(x, y);
		if(tile.getTileIndex() == 0xA0){
			FallenPlatform fp = new FallenPlatform(getGame());
			fp.position.set(x+1,y+1);
			getGame().addObject(fp);
	
			map.setTile(x, y, 0x104);
			map.setTile(x+1, y, 0x104);
			map.setTile(x, y+1, 0x116);
			map.setTile(x+1, y+1, 0x116);
		}
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
