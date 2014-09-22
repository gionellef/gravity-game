package com.megahard.gravity;

public class Engine {

	public void moveLeft(GameObject obj){
		//check if object doesn't exceed left limit of map
		if(obj.position.x - obj.velocity.x > 0){
			obj.position.x = obj.position.x - obj.velocity.x;
		}
		else{
			obj.position.x = 0;
		}
	}
	
	public void moveRight(GameState gamestate, GameObject obj){
		//check if object doesn't exceed right limit of map
		if(obj.position.x + obj.velocity.x < gamestate.map.getWidth()){
			obj.position.x = obj.position.x + obj.velocity.x;
		}
		else{
			obj.position.x = gamestate.map.getWidth() - 1;
		}
	}
	
	public void moveUp(GameState gamestate, GameObject obj){
		//check if object doesn't exceed upper limit of map
		if(obj.position.y + obj.velocity.y < gamestate.map.getHeight()){
			obj.position.y = obj.position.y + obj.velocity.y;
		}
		else{
			obj.position.y = gamestate.map.getHeight() - 1;
		}
	}
	
	public void moveDown(GameObject obj){
		//check if object doesn't exceed lower limit of map
		if(obj.position.y - obj.velocity.y > 0){
			obj.position.y = obj.position.y - obj.velocity.y;
		}
		else{
			obj.position.y = 0;
		}
	}
}
