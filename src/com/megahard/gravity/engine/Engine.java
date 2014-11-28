package com.megahard.gravity.engine;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
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

import com.megahard.gravity.GravityApplet;
import com.megahard.gravity.engine.GameMap.Layers;
import com.megahard.gravity.engine.GameMap.Layers.GameObjects;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.engine.base.Script;
import com.megahard.gravity.objects.Player;
import com.megahard.gravity.util.J;
import com.megahard.gravity.util.Sound;
import com.megahard.gravity.util.Sound.Clips;
import com.megahard.gravity.util.Vector2;

public class Engine implements KeyListener, MouseListener, MouseMotionListener, GameContext{
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
	private boolean finished = false;
	private int finishTimer;
	private int messageExpiry;
	private boolean finishWin;
	private boolean finishEsc;


	public Engine() {
		state = new GameState();
		addObj = new LinkedList<GameObject>();
		removeObj = new LinkedList<GameObject>();
		
		keyStates = new HashMap<>();
		mouseStates = new HashMap<>();
		
		keyEvents = new ConcurrentLinkedQueue<>();
		mouseKeyEvents = new ConcurrentLinkedQueue<>();
		
		renderer = new Renderer(this);

		mouseX = mouseY = -1;
		
		messageExpiry = 0;
		
		finishListener = null;
	}

	public void initialize(String mapName) {
		// populate the game state using level data
		state = new GameState();
		loadMapAndObjects(mapName);

		commitAddObjects();
		
		// fade game in
		fadeScreen(false, 6);
		
		// initialize scripts
		for(Script s : state.scripts){
			s.onStart();
		}
	}

	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#addObject(com.megahard.gravity.GameObject)
	 */
	@Override
	public void addObject(GameObject obj) {
		addObj.add(obj);
		obj.active = true;
	}

	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#removeObject(com.megahard.gravity.GameObject)
	 */
	@Override
	public void removeObject(GameObject obj) {
		removeObj.add(obj);
		obj.active = false;
	}
	
	@Override
	public void finish(boolean win, boolean esc){
		if(finished)
			return;
		
		finishWin = win;
		finishEsc = esc;
		
		finished = true;
		finishTimer = esc ? 10 : 20;
		Color color = win || esc ? null : new Color(1, 0, 0, 0.5f);
		fadeScreen(color, Color.black, Math.max(0, finishTimer/2));
	}
	
	public void finalFinish(){
		Sound.stopAll();
		finishListener.onFinish(0, 0, finishWin,finishEsc);
	}
	
	public void setFinishListener(EngineFinishListener efl){
		finishListener  = efl;
	}
	
	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#playSoundAtLocation(com.megahard.gravity.Sound.Clips, double, double, double)
	 */
	@Override
	public void playSoundAtLocation(Clips sound, double x, double y, double volume){
		playSoundAtLocation(sound, new Vector2(x, y), volume);
	}
	
	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#playSoundAtLocation(com.megahard.gravity.Sound.Clips, com.megahard.gravity.Vector2, double)
	 */
	@Override
	public void playSoundAtLocation(Clips sound, Vector2 position, double volume){
		double distance = playerObject.position.minus(position).length();
		float v = (float) ((volume * 34) / (34 + distance));
		
		Vector2 cam = renderer.getCamera();
		if(cam != null){
			double halfWidth = (double)renderer.getWidth() / 2 / Renderer.TILE_SIZE / Renderer.SCALE_FACTOR;
			double halfHeight = (double)renderer.getHeight() / 2 / Renderer.TILE_SIZE / Renderer.SCALE_FACTOR;
			double leftEdge = cam.x - halfWidth;
			double rightEdge = cam.x + halfWidth;
			double topEdge = cam.y - halfHeight;
			double bottomEdge = cam.y + halfHeight; 
			if(position.x < leftEdge || position.y < topEdge || position.x >= rightEdge || position.y > bottomEdge){
				v *= 0.6;
			}
		}
		
		float p = (float) (1 - Math.atan2(3, position.x - playerObject.position.x)*2/Math.PI);
		sound.play(v, p);
	}

	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#getMap()
	 */
	@Override
	public GameMap getMap() {
		return state.map;
	}
	
	public void loadMapAndObjects(String mapName) {
		InputStream in = getClass().getResourceAsStream("/map/" + mapName + ".json");
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		GameMap map = J.gson.fromJson(input, GameMap.class);
		map.initializeMap();
		state.map = map;
		
		Layers[] layer = map.getLayers();
		GameObjects[] objects = layer[1].getObjects();
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				GameObjects object = objects[i];
				
				String name = object.getName();
				if(name.startsWith("script_")){
					loadScript(object);
				}else{
					loadObject(map, object);
				}

			}
		}
		
		sortObjects(state.objects);
	}

	private void loadScript(GameObjects object) {
		String type = object.getName().substring("script_".length());
		try {
			Script s = null;
			Rectangle2D.Double region =
					new Rectangle2D.Double(
						object.getX() / Renderer.TILE_SIZE,
						object.getY() / Renderer.TILE_SIZE,
						object.getWidth() / Renderer.TILE_SIZE,
						object.getHeight() / Renderer.TILE_SIZE);
			
			@SuppressWarnings("unchecked")
			Class<Script> subclass = (Class<Script>) Class
					.forName("com.megahard.gravity.scripts." + type);
			Constructor<Script> constructor = null;
			try{
				constructor = subclass.getConstructor(GameContext.class, Rectangle2D.Double.class, Map.class);
				s = constructor.newInstance(this, region, object.getProperties());
			} catch (NoSuchMethodException | SecurityException e1) {
				constructor = subclass.getConstructor(GameContext.class, Rectangle2D.Double.class);
				s = constructor.newInstance(this, region);
			}
			
			addScript(s);

		} catch (ClassNotFoundException e1) {
			System.err.println("Script type " + type + " not found!");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e1) {
			System.err.println("Invalid constructor for script type " + type);
			e1.printStackTrace();
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			System.err.println("Invalid constructor for script type " + type);
			e.printStackTrace();
		}
	}

	private void addScript(Script s) {
		state.scripts.add(s);
	}

	private void loadObject(GameMap map, GameObjects object) {
		String type = map.getObjectType(object.getGID());
		try {
			@SuppressWarnings("unchecked")
			Class<GameObject> subclass = (Class<GameObject>) Class
					.forName("com.megahard.gravity.objects." + type);
			Constructor<GameObject> constructor = null;
			constructor = subclass.getConstructor(GameContext.class);
			GameObject instance = constructor.newInstance(this);

			instance.position.set(object.getX() / Renderer.TILE_SIZE + 2,
					object.getY() / Renderer.TILE_SIZE - 2);
			addObject(instance);
			
			if (type.equals("Player")) {
				playerObject = (Player) instance;
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

	public void update() {
		if(!finished){
			updateInputEvents();
		}

		// Toggle debug rendering
		if(keyIsJustReleased(KeyEvent.VK_F8)){
			GravityApplet.debug = !GravityApplet.debug;
		}
		
		// Give up
		if(keyIsJustReleased(KeyEvent.VK_ESCAPE)){
			finish(false,true);
		}
		
		updateScripts();
		
		// No control in cinematic mode
		if(isCinematicMode()){
			clearInputs();
		}
		
		updateObjects();
		
		// update key states
		updateKeyStates();

		// Set camera
		if(state.cinematicMode){
			renderer.setCameraSmoothing(0.2);
			renderer.getCameraTarget().set(playerObject.position);
		}else if(mouseX >= 0 && mouseY >= 0){
			renderer.setCameraSmoothing(renderer.getCameraSmoothing()
					+ (1 - renderer.getCameraSmoothing()) * 0.05);
			renderer.getCameraTarget().set(
				playerObject.position.x + ((double)(mouseX - renderer.getWidth() / 2)
				/ Renderer.SCALE_FACTOR / Renderer.TILE_SIZE) * 0.5,
				playerObject.position.y + ((double)(mouseY - renderer.getHeight() / 2)
							/ Renderer.SCALE_FACTOR / Renderer.TILE_SIZE) * 0.5
			);
		}else{
			renderer.setCameraSmoothing(0.5);
			renderer.getCameraTarget().set(playerObject.position);
		}
		
		// Messages
		if(messageExpiry > 0 && state.time > messageExpiry){
			removeMessage();
		}
		
		// Dead player
		if(!playerObject.active){
			// Game over
			finish(false,false);
		}
		
		if(finished){
			if(finishTimer <= 0) finalFinish();
			finishTimer--;
		}
		
		state.time++;
	}

	private void updateScripts() {
		for(Script s : state.scripts){
			// check objects in regions first
			List<GameObject> objects = findObjects(s.getRegion().x, s.getRegion().y, s.getRegion().width, s.getRegion().height, true);
			for(GameObject o : objects){
				if(!s.overlaps.contains(o)){
					s.onEnter(o);
				}
			}
			for(GameObject o : s.overlaps){
				if(!objects.contains(o)){
					s.onExit(o);
				}
			}
			s.overlaps = objects;
			
			// update
			s.onUpdate();
		}
	}

	private void updateObjects() {
		// add all objects to be added
		commitAddObjects();

		// update all the objects
		for (GameObject o : state.objects) {
			o.update();
		}
		// Check inter-object collisions
		checkCollisions();

		// remove all objects to be removed
		state.objects.removeAll(removeObj);
		removeObj.clear();
	}

	private void commitAddObjects() {
		for(GameObject o : addObj){
			state.objects.add(o);
		}
		for(GameObject o : addObj){
			o.init();
		}
		addObj.clear();
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
		sortObjects(objects);

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

	private void sortObjects(ArrayList<GameObject> objects) {
		int n = objects.size();
		if(n < 2) return;
		for(int i = 1; i < n; i++){
			GameObject current = objects.get(i);
			int j = i - 1;
			while(j >= 0 && current.position.x - current.size.x/2 < objects.get(j).position.x - current.size.x/2){
				objects.set(j + 1, objects.get(j));
				j--;
			}
			objects.set(j + 1, current);
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

	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#keyIsDown(int)
	 */
	@Override
	public boolean keyIsDown(int keyCode){
		KeyState state = keyStates.get(keyCode);
		return state == KeyState.PRESS || state == KeyState.CLICK || state == KeyState.DOWN;
	}
	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#keyIsJustPressed(int)
	 */
	@Override
	public boolean keyIsJustPressed(int keyCode){
		KeyState state = keyStates.get(keyCode);
		return state == KeyState.PRESS || state == KeyState.CLICK;
	}
	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#keyIsUp(int)
	 */
	@Override
	public boolean keyIsUp(int keyCode){
		KeyState state = keyStates.get(keyCode);
		return state == null || state == KeyState.RELEASE || state == KeyState.UP;
	}
	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#keyIsJustReleased(int)
	 */
	@Override
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
		mouseX = -1;
		mouseY = -1;
	}
	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		MouseKeyEvent ke = new MouseKeyEvent();
		ke.button = e.getButton();
		ke.state = KeyState.PRESS;
		mouseKeyEvents.add(ke);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		MouseKeyEvent ke = new MouseKeyEvent();
		ke.button = e.getButton();
		ke.state = KeyState.RELEASE;
		mouseKeyEvents.add(ke);
	}

	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#mouseIsDown(int)
	 */
	@Override
	public boolean mouseIsDown(int button){
		KeyState state = mouseStates.get(button);
		return state == KeyState.PRESS || state == KeyState.CLICK || state == KeyState.DOWN;
	}
	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#mouseIsJustPressed(int)
	 */
	@Override
	public boolean mouseIsJustPressed(int button){
		KeyState state = mouseStates.get(button);
		return state == KeyState.PRESS || state == KeyState.CLICK;
	}
	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#mouseIsUp(int)
	 */
	@Override
	public boolean mouseIsUp(int button){
		KeyState state = mouseStates.get(button);
		return state == null || state == KeyState.RELEASE || state == KeyState.UP;
	}
	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#mouseIsJustReleased(int)
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#getMouseGamePosition()
	 */
	@Override
	public Vector2 getMouseGamePosition() {
		return new Vector2((double) (mouseX - renderer.getWidth() / 2)
				/ Renderer.SCALE_FACTOR / Renderer.TILE_SIZE
				+ renderer.getCamera().x,
				(double) (mouseY - renderer.getHeight() / 2)
						/ Renderer.SCALE_FACTOR / Renderer.TILE_SIZE
						+ renderer.getCamera().y);
	}
	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#getMouseScreenX()
	 */
	@Override
	public int getMouseScreenX(){
		return mouseX;
	}
	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#getMouseScreenY()
	 */
	@Override
	public int getMouseScreenY(){
		return mouseY;
	}

	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#findObject(java.lang.Class, double, double, double, double, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T findObject(Class<T> type, double x, double y, double w,
			double h, boolean inclusive) {
		// binary search
		int l = 0;
		int r = state.objects.size() - 1;
		int s = 0;
		while(l < r){
			s = (l + r)/2;
			double ox = state.objects.get(s).position.x;
			double nx = state.objects.get(s + 1).position.x;
			if(nx < x){
				l = s + 1;
			}else if(ox > x){
				r = s;
			}else{
				break;
			}
		}
		
		// find object
		for (int i=s; i<state.objects.size(); i++) {
			GameObject o = state.objects.get(i);
			if (!inclusive) {
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
	
	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#findObjects(double, double, double, double, boolean)
	 */
	@Override
	public List<GameObject> findObjects(double x, double y, double w, double h, boolean inclusive){
		// binary search
		int l = 0;
		int r = state.objects.size() - 1;
		int s = 0;
		while(l < r){
			s = (l + r)/2;
			double ox = state.objects.get(s).position.x;
			double nx = state.objects.get(s + 1).position.x;
			if(nx < x){
				l = s + 1;
			}else if(ox > x){
				r = s;
			}else{
				break;
			}
		}
		
		// get objects
		List<GameObject> list = new LinkedList<GameObject>();
		for (int i=s; i<state.objects.size(); i++) {
			GameObject o = state.objects.get(i);
			if (!inclusive) {
				if(o.position.x < x) continue;
				if(o.position.x > x + w) break;
				if (o.position.x >= x
				&& o.position.y >= y
				&& o.position.x < x + w
				&& o.position.y < y + h) {
					list.add(o);
				}
			} else {
				if(o.position.x + o.size.x / 2 < x) continue;
				if(o.position.x - o.size.x / 2 > x + w) break;
				if (o.position.x - o.size.x / 2 < x + w
				&& o.position.x + o.size.x / 2 >= x
				&& o.position.y - o.size.y / 2 < y + h
				&& o.position.y + o.size.y / 2 >= y) {
					list.add(o);
				}
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#getPlayerObject()
	 */
	@Override
	public Player getPlayerObject() {
		return playerObject;
	}

	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#isCinematicMode()
	 */
	@Override
	public boolean isCinematicMode() {
		return state.cinematicMode;
	}

	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#setCinematicMode(boolean)
	 */
	@Override
	public void setCinematicMode(boolean cinematicMode) {
		state.cinematicMode = cinematicMode;
		clearInputs();
	}

	private void clearInputs() {
		keyStates.clear();
		mouseStates.clear();
		keyEvents.clear();
		mouseKeyEvents.clear();
	}

	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#showMessage(java.lang.String, int)
	 */
	@Override
	public void showMessage(String message, int duration) {
		renderer.showMessage(message);
		messageExpiry = state.time + duration;
	}
	
	@Override
	public void showMessage(String image, String message, int duration) {
		renderer.showMessage(image, message);
		messageExpiry = state.time + duration;
	}

	/* (non-Javadoc)
	 * @see com.megahard.gravity.GameContext#findObject(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T findObject(Class<T> type) {
		for (GameObject o : state.objects) {
			if (o.getClass().equals(type)) {
				return (T) o;
			}
		}
		return null;
	}
	
	public void fadeScreen(boolean out, int duration){
		renderer.fadeBlack(out, duration);
	}
	
	public void fadeScreen(Color colorStart, Color colorEnd, int duration){
		renderer.fade(colorStart, colorEnd, duration);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findObjects(Class<T> type, int x, int y, int w,
			int h, boolean inclusive) {
		List<GameObject> objects = findObjects(x, y, w, h, inclusive);
		List<T> list = new LinkedList<T>();
		for(GameObject o : objects){
			if(o.getClass().equals(type)){
				list.add((T) o);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findObjects(Class<T> type) {
		List<T> list = new LinkedList<>();
		for (GameObject o : state.objects) {
			if (o.getClass().equals(type)) {
				list.add((T) o);
			}
		}
		return list;
	}

	@Override
	public void removeMessage() {
		messageExpiry = 0;
		renderer.removeMessage();
	}


}
