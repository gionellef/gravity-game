package com.megahard.gravity;


public class GameObject {
	private final Engine game;

	public boolean fixed;
	public Vector2 position;
	public Vector2 velocity;
	public Vector2 size;
	public double mass;
	public double restitution;
	public double friction;
	public boolean standing;
	public Sprite sprite;

	public GameObject(Engine game, String spriteName) {
		this.game = game;
		fixed = false;
		this.position = new Vector2();
		this.velocity = new Vector2();
		this.size = new Vector2();
		this.mass = 1;
		restitution = 0;
		friction = 1;
		standing = false;
		
		if (spriteName != null && spriteName != ""){
			this.sprite = SpriteStore.get().getSprite(spriteName);
		}
	}

	public void collide(GameObject obj) {

	}

	public void setSprite(String spriteName) {
		this.sprite = SpriteStore.get().getSprite(spriteName);
	}
	
	public void update() {
		if(!fixed){
			GameMap map = game.getMap();
			
			velocity.y += 0.05; // tmp
			standing = false;
	
			// max velocity
			if(velocity.length() > 1) {
				velocity = velocity.scale(1/velocity.length());
			}
	
			// Get object bounds
			final double E = 1e-10;
			double left = position.x - size.x / 2;
			double right = position.x + size.x / 2 - E;
			double up = position.y - size.y / 2;
			double down = position.y + size.y / 2 - E;
	
			// Do Y collision
			if (velocity.y > 0) {
				double nextDown = down + velocity.y;
	
				// Check if the bottom of the object collides with the map
				boolean blocked = false;
				for (double x = left; x < right; x += 1) {
					if (map.getTile(x, nextDown).getCollidable()) {
						blocked = true;
						break;
					}
				}
				if (!blocked && map.getTile(right, nextDown).getCollidable()) {
					blocked = true;
				}
	
				// collision resolution
				if (blocked) {
					position.y = Math.ceil(down) - size.y / 2;
					velocity.x *= friction;
					velocity.y *= -restitution;
					if(Math.abs(velocity.y) < 0.02){
						standing = true;
					}
				} else {
					position.y += velocity.y;
				}
			} else {
				double nextUp = up + velocity.y;
	
				// Check if the top of the object collides with the map
				boolean blocked = false;
				for (double x = left; x < right; x += 1) {
					if (map.getTile(x, nextUp).getCollidable()) {
						blocked = true;
						break;
					}
				}
				if (!blocked && map.getTile(right, nextUp).getCollidable()) {
					blocked = true;
				}
	
				// collision resolution
				if (blocked) {
					position.y = Math.floor(up) + size.y / 2;
					velocity.x *= friction;
					velocity.y *= -restitution;
				} else {
					position.y += velocity.y;
				}
			}
	
			up = position.y - size.y / 2;
			down = position.y + size.y / 2 - E;
	
			// Do X collision
			if (velocity.x > 0) {
				double nextRight = right + velocity.x;
	
				// Check if the right of the object collides with the map
				boolean blocked = false;
				for (double y = up; y < down; y += 1) {
					if (map.getTile(nextRight, y).getCollidable()) {
						blocked = true;
						break;
					}
				}
				if (!blocked && map.getTile(nextRight, down).getCollidable()) {
					blocked = true;
				}
	
				// collision resolution
				if (blocked) {
					position.x = Math.ceil(right) - size.x / 2;
					velocity.x *= -restitution;
				} else {
					position.x += velocity.x;
				}
			} else {
				double nextLeft = left + velocity.x;
	
				// Check if the left of the object collides with the map
				boolean blocked = false;
				for (double y = up; y < down; y += 1) {
					if (map.getTile(nextLeft, y).getCollidable()) {
						blocked = true;
						break;
					}
				}
				if (!blocked && map.getTile(nextLeft, down).getCollidable()) {
					blocked = true;
				}
	
				// collision resolution
				if (blocked) {
					position.x = Math.floor(left) + size.x / 2;
					velocity.x *= -restitution;
				} else {
					position.x += velocity.x;
				}
			}
		}

		if(sprite != null){
			sprite.update();
		}
	}

	protected Engine getGame(){
		return game;
	}
}
