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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.megahard.gravity.GameMap.Layers;
import com.megahard.gravity.GameMap.Layers.GameObjects;
import com.megahard.gravity.Sound.Clips;
import com.megahard.gravity.objects.Player;

public class Engine implements KeyListener, MouseListener, MouseMotionListener{
	private Player playerObject;

	// Keys
	private enum KeyState{
		UP, PRESS, DOWN, RELEASE, CLICK;
	}
	private Map<Integer, KeyState> keyStates;
	private class KeyEvent2{
		public int keyCode;
		public KeyState state;
	}
	
	// Mouse
	private int mouseX;
	private int mouseY;
	private Map<Integer, KeyState> mouseStates;
	private class MouseKeyEvent{
		public int button;
		public KeyState state;
	}

	private ConcurrentLinkedQueue<KeyEvent2> keyEvents;
	private ConcurrentLinkedQueue<MouseKeyEvent> mouseKeyEvents;
	
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
		mouseStates = new HashMap<>();
		
		keyEvents = new ConcurrentLinkedQueue<>();
		mouseKeyEvents = new ConcurrentLinkedQueue<>();
		
		mouseX = 0;
		mouseY = 0;
		
		renderer = new Renderer(this);
		
		finishListener = null;
	}

	public void initialize(String levelData) {
		// populate the game state using level data
		state = new GameState();
		state.map = loadMapAndObjects(levelData);
	}

	public void addObject(GameObject obj) {
		addObj.add(obj);
		obj.active = true;
	}

	public void removeObject(GameObject obj) {
		removeObj.add(obj);
		obj.active = false;
	}
	
	public void finish(boolean win){
		Sound.stopAll();
		finishListener.onFinish(0, 0, win);
	}
	
	public void setFinishListener(EngineFinishListener efl){
		finishListener  = efl;
	}
	
	public void playSoundAtLocation(Clips sound, double x, double y, double volume){
		playSoundAtLocation(sound, new Vector2(x, y), volume);
	}
	
	public void playSoundAtLocation(Clips sound, Vector2 position, double volume){
		double distance = playerObject.position.sub(position).length();
		float v  =(float) ((volume * 34) / (34 + distance));
		float p = (float) (1 - Math.atan2(1.5, position.x - playerObject.position.x)*2/Math.PI);
		sound.play(v, p);
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
				
				String type = map.getObjectType(object.getGID());
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

					if (type.equals("Player")) {
						playerObject = (Player) o2;
						renderer.setCamera(playerObject.position);
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
		for(GameObject o : addObj){
			o.init();
			state.objects.add(0, o);
		}
		addObj.clear();
		
		// process input events
		updateInputEvents();

		// update all the objects
		for (GameObject o : state.objects) {
			o.update();
		}
		
		// update key states
		updateKeyStates();

		// Check inter-object collisions
		checkCollisions();

		// remove all objects to be removed
		state.objects.removeAll(removeObj);
		removeObj.clear();
		
		//debug rendering
		if(keyIsJustReleased(KeyEvent.VK_F8)){
			renderer.debug = !renderer.debug;
		}
		
		// dead player
		if(!playerObject.active){
			// Game over
			finish(false);
		}
		
		// give up
		if(keyIsJustReleased(KeyEvent.VK_ESCAPE)){
			finish(false);
		}
	}

	private void updateInputEvents() {
		while(true){
			KeyEvent2 ke = keyEvents.poll();
			if(ke == null) break;

			int keyCode = ke.keyCode;
			KeyState prev = keyStates.get(keyCode);
			
			if(ke.state == KeyState.PRESS){
				if(keyStates.get(keyCode) != KeyState.DOWN){
					keyStates.put(keyCode, KeyState.PRESS);
				}
			}
			else if(ke.state == KeyState.RELEASE)
				keyStates.put(keyCode, prev == KeyState.PRESS ? KeyState.CLICK : KeyState.RELEASE);
		}
		
		while(true){
			MouseKeyEvent ke = mouseKeyEvents.poll();
			if(ke == null) break;

			int button = ke.button;
			KeyState prev = mouseStates.get(button);
			
			if(ke.state == KeyState.PRESS){
				if(mouseStates.get(button) != KeyState.DOWN){
					mouseStates.put(button, KeyState.PRESS);
				}
			}
			else if(ke.state == KeyState.RELEASE)
				mouseStates.put(button, prev == KeyState.PRESS ? KeyState.CLICK : KeyState.RELEASE);
		}		
	}

	private void updateKeyStates() {
		for(Entry<Integer, KeyState> e : keyStates.entrySet()){
			KeyState value = e.getValue();
			if(value == KeyState.PRESS){
				e.setValue(KeyState.DOWN);
			}else if(value == KeyState.RELEASE || value == KeyState.CLICK){
				e.setValue(KeyState.UP);
			}
		}
		for(Entry<Integer, KeyState> e : mouseStates.entrySet()){
			KeyState value = e.getValue();
			if(value == KeyState.PRESS){
				e.setValue(KeyState.DOWN);
			}else if(value == KeyState.RELEASE || value == KeyState.CLICK){
				e.setValue(KeyState.UP);
			}
		}
	}

	private void checkCollisions() {
		// Using the sweep and prune algorithm
		
		ArrayList<GameObject> objects = state.objects;
		int n = objects.size();
		
		if(n < 2) return;
		
		// insertion sort objects
		for(int i = 1; i < n; i++){
			GameObject current = objects.get(i);
			int j = i - 1;
			while(j >= 0 && current.position.x - current.size.x/2 < objects.get(j).position.x - current.size.x/2){
				objects.set(j + 1, objects.get(j));
				j--;
			}
			objects.set(j + 1, current);
		}

		double[][] spans = new double[n][4]; // {xmin, xmax, ymin, ymax}
		for(int i = 0; i < n; i++){
			GameObject obj = objects.get(i);
			spans[i][0] = obj.position.x - obj.size.x/2;
			spans[i][1] = obj.position.x + obj.size.x/2;
			spans[i][2] = obj.position.y - obj.size.y/2;
			spans[i][3] = obj.position.y + obj.size.y/2;
		}
		
		for(int i = 0; i < n; i++){
			GameObject current = objects.get(i);
			for(int j = i + 1; j < n; j++){
				GameObject other = objects.get(j);
				
				if(spans[j][0] > spans[i][1])
					break;
				if(spans[i][2] > spans[j][3]
				|| spans[i][3] < spans[j][2])
					continue;
				
				current.onCollide(other);
				other.onCollide(current);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		KeyEvent2 ke = new KeyEvent2();
		ke.keyCode = e.getKeyCode();
		ke.state = KeyState.PRESS;
		keyEvents.add(ke);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		KeyEvent2 ke = new KeyEvent2();
		ke.keyCode = e.getKeyCode();
		ke.state = KeyState.RELEASE;
		keyEvents.add(ke);
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
		MouseKeyEvent ke = new MouseKeyEvent();
		ke.button = e.getButton();
		ke.state = KeyState.PRESS;
		mouseKeyEvents.add(ke);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		MouseKeyEvent ke = new MouseKeyEvent();
		ke.button = e.getButton();
		ke.state = KeyState.RELEASE;
		mouseKeyEvents.add(ke);
	}

	public boolean mouseIsDown(int button){
		KeyState state = mouseStates.get(button);
		return state == KeyState.PRESS || state == KeyState.CLICK || state == KeyState.DOWN;
	}
	public boolean mouseIsJustPressed(int button){
		KeyState state = mouseStates.get(button);
		return state == KeyState.PRESS || state == KeyState.CLICK;
	}
	public boolean mouseIsUp(int button){
		KeyState state = mouseStates.get(button);
		return state == null || state == KeyState.RELEASE || state == KeyState.UP;
	}
	public boolean mouseIsJustReleased(int button){
		KeyState state = mouseStates.get(button);
		return state == KeyState.RELEASE || state == KeyState.CLICK;
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
	public <T> T findObject(Class<T> type, double x, double y, double w,
			double h, boolean edgeNotCenter) {
		for (GameObject o : state.objects) {
			if (!edgeNotCenter) {
				if(o.position.x < x) continue;
				if(o.position.x > x + w) break;
				if (o.position.x >= x
				&& o.position.y >= y
				&& o.position.x < x + w
				&& o.position.y < y + h) {
					if (o.getClass().equals(type)) {
						return (T) o;
					}
				}
			} else {
				if(o.position.x + o.size.x / 2 < x) continue;
				if(o.position.x - o.size.x / 2 > x + w) break;
				if (o.position.x - o.size.x / 2 < x + w
				&& o.position.x + o.size.x / 2 >= x
				&& o.position.y - o.size.y / 2 < y + h
				&& o.position.y + o.size.y / 2 >= y) {
					if (o.getClass().equals(type)) {
						return (T) o;
					}
				}
			}
		}
		return null;
	}
	
	public List<GameObject> findObjects(double x, double y, double w, double h){
		List<GameObject> list = new LinkedList<GameObject>();
		for(GameObject o : state.objects){
			if(o.position.x >= x
			&& o.position.y >= y
			&& o.position.x < x + w
			&& o.position.y < y + h){
				list.add(o);
			}
		}
		return list;
	}

	public Player getPlayerObject() {
		return playerObject;
	}
}
