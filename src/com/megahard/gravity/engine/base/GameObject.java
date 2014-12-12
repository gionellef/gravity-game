package com.megahard.gravity.engine.base;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.GameMap;
import com.megahard.gravity.engine.SpriteStore;
import com.megahard.gravity.util.Vector2;


/**
 * Base class for all objects in the game. This class handles common procedures
 * for all objects, such as physics, collision with the map, and callbacks.
 */
public class GameObject {
	private final GameContext game;

	/**
	 * Whether the object moves.
	 */
	public boolean fixed;
	/**
	 * Whether the object is affected by regular downward gravity.
	 */
	public boolean floating;
	
	/**
	 * Position of the object relative to the map, in tile units
	 */
	public Vector2 position;
	/**
	 * Velocity of the object, in tile units
	 */
	public Vector2 velocity;
	/**
	 * Size of the object, in tile units
	 */
	public Vector2 size;
	
	/**
	 * Mass of the object, in kilograms
	 */
	public double mass;
	/**
	 * How much the object bounces when colliding with walls.
	 * A value from 0 to 1.
	 *  0 = no bounce
	 *  1 = full bounce (preserves speed)
	 */
	public double restitution;
	/**
	 * How much the object slides on a surface.
	 * A value from 0 to 1.
	 *  0 = no sliding
	 *  1 = no friction, object won't slow down when sliding
	 */
	public double friction;
	/**
	 * Minimum speed needed to overcome static friction. An object sliding on
	 * a surface will immediately stop if its speed is less than its
	 * staticFriction.
	 * Value >= 0.
	 */
	public double staticFriction;
	
	/**
	 * Read-only! This should be private, with a getter.
	 * Whether the object is standing on something.
	 */
	public boolean standing;
	/**
	 * Whether the object is in the game.
	 */
	public boolean active;
	
	/**
	 * The sprite of the object, for rendering.
	 */
	public com.megahard.gravity.engine.Sprite sprite;
	/**
	 * Z-index of the object, for rendering.
	 */
	public int zIndex;

	/**
	 * Acceleration by regular downward gravity.
	 */
	public static final double GRAVITY = 0.05;

	/**
	 * Creates a new object
	 * 
	 * @param game The game where the object is to be placed.
	 * @param spriteName Name of the sprite of the object.
	 */
	public GameObject(GameContext game, String spriteName) {
		this.game = game;
		
		fixed = false;
		floating = false;
		
		position = new Vector2();
		velocity = new Vector2();
		size = new Vector2();
		
		mass = 1;
		restitution = 0;
		friction = 1;
		staticFriction = 0;
		
		standing = false;
		active = false;
		
		if (spriteName != null && spriteName != ""){
			this.sprite = SpriteStore.get().getSprite(spriteName);
			sprite.setListener(this);
		}
		zIndex = 0;
	}

	/**
	 * Sets a new sprite for the object.
	 * @param spriteName Name of the sprite.
	 */
	public void setSprite(String spriteName) {
		this.sprite = SpriteStore.get().getSprite(spriteName);
	}
	
	/**
	 * Updates the object's state. Subclasses can override this but must call
	 * super.update().
	 */
	public void update() {
		if(!active){
			return;
		}
		
		if(!fixed){
			GameMap map = game.getMap();
			
			// things that don't float fall
			if(!floating){
				velocity.y += GRAVITY;
			}
	
			// max velocity
			if(velocity.length() > 1) {
				velocity = velocity.times(1/velocity.length());
			}
	
			// Get object bounds
			final double E = 1e-3;
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
					double targetY = Math.floor(nextDown) - E - size.y / 2;
					if(position.y < targetY) onHitBottom();
					position.y = targetY;
					if(Math.abs(velocity.x) < staticFriction) velocity.x = 0;
					else velocity.x *= friction;
					velocity.y *= -restitution;
				} else {
					position.y += velocity.y;
				}
			} else if(velocity.y < 0){
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
					position.y = Math.ceil(nextUp) + E + size.y / 2;
					if(Math.abs(velocity.x) < staticFriction) velocity.x = 0;
					else velocity.x *= friction;
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
					position.x = Math.floor(nextRight) - E - size.x / 2;
					velocity.x *= -restitution;
				} else {
					position.x += velocity.x;
				}
			} else if(velocity.x < 0){
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
					position.x = Math.ceil(nextLeft) + E + size.x / 2;
					velocity.x *= -restitution;
				} else {
					position.x += velocity.x;
				}
			}
			
			left = position.x - size.x / 2;
			right = position.x + size.x / 2 - E;
	
			double bottom = down + GRAVITY + 2 * E;
			standing = map.getTile(left, bottom).getCollidable()
					|| map.getTile(right, bottom).getCollidable(); 
		}

		if(position.y - size.y/2 - 4 > getGame().getMap().getHeight()){
			getGame().removeObject(this);
		}

		if(sprite != null){
			sprite.update();
		}
	}

	/**
	 * Make this object die. The default action on death is removal of the
	 * object. Subclasses can override this.
	 */
	public void die() {
		getGame().removeObject(this);
	}
	
	/**
	 * Applies a force to the object, accelerating it according to Newton's
	 * formula: force = mass * acceleration.
	 * @param force The force to apply
	 */
	public void applyImpulse(Vector2 force){
		velocity = velocity.plus(force.times(1/mass));
	}
	
	/**
	 * Gets the GameContext for the object
	 * @return the GameContext for the object
	 */
	protected GameContext getGame(){
		return game;
	}

	// Overiddables
	
	/**
	 * Overridable. Called after the object is added to the game.
	 */
	public void init(){
	}
	
	/**
	 * Overridable. Called when the object's bottom collides with the map.
	 */
	public void onHitBottom(){
	}

	/**
	 * Overridable. Called when this object overlaps (collides) another object.
	 * 
	 * @param obj the other object
	 */
	public void onCollide(GameObject obj) {
	}

	/**
	 * Overridable. Called when the sprite of the object begins
	 * a sequence (or action).
	 * 
	 * @param action the name of the action that began
	 */
	public void onStartAction(String action) {
	}

	/**
	 * Overridable. Called when the sprite of the object ends
	 * a sequence (or action).
	 * 
	 * @param action the name of the action that ended
	 */
	public void onEndAction(String action) {
	}
}
