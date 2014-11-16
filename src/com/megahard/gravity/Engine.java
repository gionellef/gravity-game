package com.megahard.gravity;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.megahard.gravity.GameMap.Layers;
import com.megahard.gravity.GameMap.Layers.GameObjects;

public class Engine implements KeyListener, MouseListener, MouseMotionListener{
	// TEST CODE
	private GameObject player;

	// Keys
	private enum KeyState{
		UP, PRESS, DOWN, RELEASE, CLICK;
	}
	private Map<Integer, KeyState> keyStates;
	
	// Mouse
	private int mouseX;
	private int mouseY;
	private KeyState mouseLeftState;
	private KeyState mouseRightState;
	
	private Renderer renderer;

	public Renderer getRenderer() {
		return renderer;
	}

	private GameState state;

	public GameState getState() {
		return state;
	}

	private List<GameObject> addObj;
	private List<GameObject> removeObj;

	private EngineFinishListener finishListener;

	public Engine() {
		state = new GameState();
		addObj = new LinkedList<GameObject>();
		removeObj = new LinkedList<GameObject>();
		
		keyStates = new HashMap<>();
		
		mouseX = 0;
		mouseY = 0;
		mouseLeftState = KeyState.UP;
		mouseRightState = KeyState.UP;
		
		renderer = new Renderer();
		
		finishListener = null;
	}

	public void initialize(String levelData) {
		// populate the game state using level data
		state = new GameState();
		state.map = loadMapAndObjects("untitled");
	}

	public void addObject(GameObject obj) {
		addObj.add(obj);
		obj.active = true;
	}

	public void removeObject(GameObject obj) {
		removeObj.add(obj);
		obj.active = false;
	}
	
	public void finish(){
		finishListener.onFinish(0, 0, "YOU WON or lost. I don't know.");
	}
	
	public void setFinishListener(EngineFinishListener efl){
		finishListener  = efl;
	}

	public GameMap getMap() {
		return state.map;
	}
	
	@SuppressWarnings("unchecked")
	public GameMap loadMapAndObjects(String mapName) {
		InputStream in = getClass().getResourceAsStream("/map/" + mapName + ".json");
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		GameMap map = J.gson.fromJson(input, GameMap.class);
		map.initializeMap();
		
		Layers[] layer = map.getLayers();
		GameObjects[] objects = layer[1].getObjects();
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				GameObjects object = objects[i];
				
				String type = object.getType();
				Class<GameObject> subclass = null;
				try {
					subclass = (Class<GameObject>) Class
							.forName("com.megahard.gravity.objects." + type);
					Constructor<GameObject> constructor = null;
					constructor = subclass.getConstructor(Engine.class);
					GameObject o2 = null;
					o2 = constructor.newInstance(this);

					o2.position.set(object.getX() / 16 + 2,
							object.getY() / 16 - 2);
					addObject(o2);

					if (object.getType().equals("Player")) {
						player = o2;
						renderer.setCamera(player.position);
					}
				} catch (ClassNotFoundException e1) {
					System.err.println("Object type " + type + " not found!");
					e1.printStackTrace();
				} catch (NoSuchMethodException | SecurityException e1) {
					System.err.println("Invalid constructor for object type " + type);
					e1.printStackTrace();
				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					System.err.println("Invalid constructor for object type " + type);
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	public void update() {
		// add all objects to be added
		state.objects.addAll(0, addObj);
		addObj.clear();

		// update all the objects
		for (GameObject o : state.objects) {
			o.update();
		}

		// Check inter-object collisions
		for (GameObject o : state.objects) {
			for (int i = state.objects.indexOf(o) + 1; i < state.objects.size(); i++) {
				GameObject o2 = state.objects.get(i);

				if (o.position.x - o.size.x / 2 < o2.position.x + o2.size.x / 2
						&& o.position.x + o.size.x / 2 > o2.position.x
								- o2.size.x / 2
						&& o.position.y - o.size.y / 2 < o2.position.y
								+ o2.size.y / 2
						&& o.position.y + o.size.y / 2 > o2.position.y
								- o2.size.y / 2) {
					o.onCollide(o2);
					o2.onCollide(o);
				}
			}
		}

		// remove all objects to be removed
		state.objects.removeAll(removeObj);
		removeObj.clear();
		
		// update key states
		for(Entry<Integer, KeyState> e : keyStates.entrySet()){
			KeyState value = e.getValue();
			if(value == KeyState.PRESS){
				e.setValue(KeyState.DOWN);
			}else if(value == KeyState.RELEASE || value == KeyState.CLICK){
				e.setValue(KeyState.UP);
			}
		}
		if(mouseLeftState == KeyState.PRESS){
			mouseLeftState = KeyState.DOWN;
		}else if(mouseLeftState == KeyState.RELEASE || mouseLeftState == KeyState.CLICK){
			mouseLeftState = KeyState.UP;
		}
		if(mouseRightState == KeyState.PRESS){
			mouseRightState = KeyState.DOWN;
		}else if(mouseRightState == KeyState.RELEASE || mouseRightState == KeyState.CLICK){
			mouseRightState = KeyState.UP;
		}
		
		// Game over
		if(!player.active){
			finish();
		}
		//if( reach door) finish();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyStates.get(keyCode) != KeyState.DOWN){
			keyStates.put(keyCode, KeyState.PRESS);
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		KeyState prev = keyStates.get(keyCode);
		keyStates.put(keyCode, prev == KeyState.PRESS ? KeyState.CLICK : KeyState.RELEASE);
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}

	public boolean keyIsDown(int keyCode){
		KeyState state = keyStates.get(keyCode);
		return state == KeyState.PRESS || state == KeyState.CLICK || state == KeyState.DOWN;
	}
	public boolean keyIsJustPressed(int keyCode){
		KeyState state = keyStates.get(keyCode);
		return state == KeyState.PRESS || state == KeyState.CLICK;
	}
	public boolean keyIsUp(int keyCode){
		KeyState state = keyStates.get(keyCode);
		return state == null || state == KeyState.RELEASE || state == KeyState.UP;
	}
	public boolean keyIsJustReleased(int keyCode){
		KeyState state = keyStates.get(keyCode);
		return state == KeyState.RELEASE || state == KeyState.CLICK;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
		switch(e.getButton()){
		case MouseEvent.BUTTON1:
			mouseLeftState = KeyState.PRESS;
			break;
		case MouseEvent.BUTTON3:
			mouseRightState = KeyState.PRESS;
			break;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		switch(e.getButton()){
		case MouseEvent.BUTTON1:
			mouseLeftState = mouseLeftState == KeyState.PRESS ? KeyState.CLICK : KeyState.RELEASE;
			break;
		case MouseEvent.BUTTON3:
			mouseRightState = mouseRightState == KeyState.PRESS ? KeyState.CLICK : KeyState.RELEASE;
			break;
		}
	}

	public boolean mouseLeftIsDown(){
		return mouseLeftState == KeyState.PRESS || mouseLeftState == KeyState.DOWN;
	}
	public boolean mouseLeftIsJustPressed(){
		return mouseLeftState == KeyState.PRESS || mouseLeftState == KeyState.CLICK;
	}
	public boolean mouseLeftIsUp(){
		return mouseLeftState == KeyState.RELEASE || mouseLeftState == KeyState.UP;
	}
	public boolean mouseLeftIsJustReleased(){
		return mouseLeftState == KeyState.RELEASE || mouseLeftState == KeyState.CLICK;
	}
	public boolean mouseRightIsDown(){
		return mouseRightState == KeyState.PRESS || mouseRightState == KeyState.DOWN;
	}
	public boolean mouseRightIsJustPressed(){
		return mouseRightState == KeyState.PRESS || mouseRightState == KeyState.CLICK;
	}
	public boolean mouseRightIsUp(){
		return mouseRightState == KeyState.RELEASE || mouseRightState == KeyState.UP;
	}
	public boolean mouseRightIsJustReleased(){
		return mouseRightState == KeyState.RELEASE || mouseRightState == KeyState.CLICK;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	public Vector2 getMouseGamePosition() {
		return new Vector2((double) (mouseX - renderer.getWidth() / 2)
				/ Renderer.SCALE_FACTOR / Renderer.TILE_SIZE
				+ renderer.getCamera().x,
				(double) (mouseY - renderer.getHeight() / 2)
						/ Renderer.SCALE_FACTOR / Renderer.TILE_SIZE
						+ renderer.getCamera().y);
	}
	public int getMouseScreenX(){
		return mouseX;
	}
	public int getMouseScreenY(){
		return mouseY;
	}

	@SuppressWarnings("unchecked")
	public <T> T findObject(Class<T> type, double x, double y,
			double w, double h, boolean edgeNotCenter) {
		for(GameObject o : state.objects){
			if(!edgeNotCenter && o.position.x >= x && o.position.y >= y && o.position.x < x + w && o.position.y < y + h){
				if(o.getClass().equals(type)){
					return (T)o;
				}
			}
			if(edgeNotCenter && o.position.x - o.size.x / 2 < x + w && o.position.x + o.size.x / 2 > x
				&& o.position.y - o.size.y / 2 < y + h && o.position.y + o.size.y / 2 > y) {
				if(o.getClass().equals(type)){
					return (T)o;
				}
			}
		}
		return null;
	}
	
	
	
	public List<GameObject> findObjects(double x, double y, double w, double h){
		List<GameObject> list = new LinkedList<GameObject>();
		for(GameObject o : state.objects){
			if(o.position.x >= x && o.position.y >= y && o.position.x < x + w && o.position.y < y + h){
				list.add(o);
			}
		}
		return list;
	}
}
