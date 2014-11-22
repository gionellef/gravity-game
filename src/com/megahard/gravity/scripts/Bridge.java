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
		
		GameMap map = getGame().getMap();
		for(GameObject o : getObjects()){
			if(o.getClass().equals(Player.class)){
				int x = (int)(o.position.x) / 2 * 2;
				if(x < getRegion().x){
					x += 2;
				}
				if(!fallQueue.contains(x)){
					fallQueue.add(x);
				}
			}
		}
		
		if(fallQueue.size() > 1){
			int x = fallQueue.remove();
			for(int i = (int)(getRegion().x)/2*2; i <= x; i += 2){
				fellPlatform(map, i, y);
			}
		}
	}

	private void fellPlatform(GameMap map, int x, int y) {
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
