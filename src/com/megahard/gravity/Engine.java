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
	}

	public void initialize(String levelData) {
		// populate the game state using level data
		state = new GameState();
		state.map = loadMapAndObjects("3");
//		loadObjects();
	}

	public void addObject(GameObject obj) {
		addObj.add(obj);
	}

	public void removeObject(GameObject obj) {
		removeObj.add(obj);
	}

//	public void loadObjects() {
//		InputStream in = getClass().getResourceAsStream("/objects.txt");
//		BufferedReader input = new BufferedReader(new InputStreamReader(in));
//		StringBuffer sb = new StringBuffer();
//		String line;
//		try {
//			while ((line = input.readLine()) != null) {
//				sb.append(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			input.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		String content = sb.toString();
//
//		Matcher m;
//
//		// variables
//		
//		//spritePath="<insert path here>"
//		Pattern path = Pattern.compile("(.*?)spritePath=\"(.*?)\"(.*?)");
//		
//		//position=(<x value>,<y value>)
//		Pattern position = Pattern
//				.compile("(.*?)position=\\((\\d+(?:\\.\\d*)?|\\.\\d+),(\\d+(?:\\.\\d*)?|\\.\\d+)\\)(.*?)");
//		
//		//size=(<x value>,<y value>)
//		Pattern size = Pattern
//				.compile("(.*?)size=\\((\\d+(?:\\.\\d*)?|\\.\\d+),(\\d+(?:\\.\\d*)?|\\.\\d+)\\)(.*?)");
//		
//		//velocity=(<x value>,<y value>)
//		Pattern velocity = Pattern
//				.compile("(.*?)velocity=\\((\\d+(?:\\.\\d*)?|\\.\\d+),(\\d+(?:\\.\\d*)?|\\.\\d+)\\)(.*?)");
//		
//		//restitution=<value here>
//		Pattern restitution = Pattern
//				.compile("(.*?)restitution=(\\d+(?:\\.\\d*)?|\\.\\d+)(.*?)");
//		
//		//friction=<value here>
//		Pattern friction = Pattern
//				.compile("(.*?)friction=(\\d+(?:\\.\\d*)?|\\.\\d+)(.*?)");
//		
//		// insert the word "player" if the object is the player
//		Pattern hasPlayer = Pattern
//				.compile("(.*?)player(.*?)");
//		
//		StringTokenizer st = new StringTokenizer(content, "#");
//		while (st.hasMoreTokens()) {
//			
//			String objects = st.nextToken().replaceAll("\\s", "");
//			String pathName = "";
//
//			m = path.matcher(objects);
//			if (m.matches()) {
////				System.out.println("path : " + m.group(2));
//				pathName = m.group(2);
//			}
//			
//			GameObject o2;
//			if (pathName.equals("")) {
//				o2 = new GameObject(this, "");
//			} else {
//				o2 = new GameObject(this, pathName);
//			}
//			
//			
//
//
//			
//			m = position.matcher(objects);
//			if (m.matches()) {
//				o2.position.set(Double.valueOf(m.group(2)), Double.valueOf(m.group(3)));
////				System.out.println("pos: " + Double.valueOf(m.group(2)) + " " + Double.valueOf(m.group(3)));
//			}
//			m = size.matcher(objects);
//			if (m.matches()) {
//				o2.size.set(Double.valueOf(m.group(2)), Double.valueOf(m.group(3)));
////				System.out.println("size: " + Double.valueOf(m.group(2)) + " " + Double.valueOf(m.group(3)));
//			}
//
//			m = velocity.matcher(objects);
//			if (m.matches()) {
////				System.out.println("X : " + m.group(2));
////				System.out.println("Y : " + m.group(3));
//				o2.velocity.set(Double.valueOf(m.group(2)), Double.valueOf(m.group(3)));
////				System.out.println("velocity: " + Double.valueOf(m.group(2)) + " " + Double.valueOf(m.group(3)));
//			}
//			m = restitution.matcher(objects);
//			if (m.matches()) {
//				System.out.println("res : " + m.group(2));
//				o2.restitution = Double.valueOf(m.group(2));
//			}
//			m = friction.matcher(objects);
//			if (m.matches()) {
//				System.out.println("f : " + m.group(2));
//				o2.friction = Double.valueOf(m.group(2));
//			}
//			
//			
//			
//			addObject(o2);
//			
//			m = hasPlayer.matcher(objects);
//			if (m.matches()) {
//				player = o2;
//				renderer.setCamera(player.position);
//			}
//			
//
//		}
//
//	}

	public GameMap getMap() {
		return state.map;
	}
	

	
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
					subclass = (Class<GameObject>) Class.forName("com.megahard.gravity.objects." + type);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				Constructor<GameObject> constructor = null;
				try {
					constructor = subclass.getConstructor(Engine.class);
				} catch (NoSuchMethodException | SecurityException e1) {
					e1.printStackTrace();
				}
				GameObject o2 = null;
				try {
					o2 = constructor.newInstance(this);
				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				
				o2.position.set(object.getX(), object.getY());
				o2.velocity.set(object.getProperties().getVelocityX(), object
						.getProperties().getVelocityY());
				addObject(o2);
				
				if (object.getType().equals("Player")) {
					player = o2;
					renderer.setCamera(player.position);
				}
			}
		}
		return map;
	}

//	public GameMap loadMap(String mapName) {
//
//		BufferedImage mapImg = null;
//		try {
//			mapImg = ImageIO.read(this.getClass().getResource(
//					"/img/" + mapName + ".png"));
//		} catch (IOException e) {
//			System.out.println("haha");
//		}
//
//		System.out.println("lol height" + mapImg.getHeight());
//
//		final byte[] pixels = ((DataBufferByte) mapImg.getRaster()
//				.getDataBuffer()).getData();
//		final int w = mapImg.getWidth();
//		final int h = mapImg.getHeight();
//		final boolean hasAlphaChannel = mapImg.getAlphaRaster() != null;
//
//		Tile[] tiles = new Tile[w * h];
//		if (hasAlphaChannel) {
//			final int pixelLength = 4;
//			for (int pixel = 0, index = 0; pixel < pixels.length; pixel += pixelLength, index++) {
//				int argb = 0;
//				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
//				argb += ((int) pixels[pixel + 1] & 0xff); // blue
//				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
//				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
//
//				Color color = new Color(argb);
//				// System.out.println("R: " + color.getRed() + " G: " +
//				// color.getGreen() + " B: " + color.getBlue());
//
//				if (color.getRed() < 30 && color.getGreen() < 30
//						&& color.getBlue() < 30) {
//					tiles[index] = Tile.Floor;
//				} else if (color.getRed() > 225 && color.getGreen() > 225
//						&& color.getBlue() > 225) {
//					tiles[index] = Tile.Air;
//				} else {
//					tiles[index] = Tile.Door;
//				}
//
//			}
//		} else {
//			final int pixelLength = 3;
//			for (int pixel = 0, index = 0; pixel < pixels.length; pixel += pixelLength, index++) {
//				int argb = 0;
//				argb += -16777216; // 255 alpha
//				argb += ((int) pixels[pixel] & 0xff); // blue
//				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
//				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
//
//				Color color = new Color(argb);
//				// System.out.println("R: " + color.getRed() + " G: " +
//				// color.getGreen() + " B: " + color.getBlue());
//
//				if (color.getRed() < 30 && color.getGreen() < 30
//						&& color.getBlue() < 30) {
//					tiles[index] = Tile.Floor;
//				} else if (color.getRed() > 225 && color.getGreen() > 225
//						&& color.getBlue() > 225) {
//					tiles[index] = Tile.Air;
//				} else {
//					tiles[index] = Tile.Door;
//				}
//			}
//		}
//
//		GameMap map = new GameMap(w, h, tiles);
//		return map;
//
//	}

	public void update() {
		// add all objects to be added
		state.objects.addAll(addObj);
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
					o.collide(o2);
					o2.collide(o);
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
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyStates.put(e.getKeyCode(), KeyState.PRESS);
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
//		mouseLeftState = KeyState.UP;
//		mouseRightState = KeyState.UP;
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
	
	public Vector2 getMouseGamePosition(){
		return new Vector2((double)(mouseX - renderer.getWidth()/2) / Renderer.TILE_SIZE + renderer.getCamera().x,
				(double)(mouseY - renderer.getHeight()/2) / Renderer.TILE_SIZE + renderer.getCamera().y);
	}
	public int getMouseScreenX(){
		return mouseX;
	}
	public int getMouseScreenY(){
		return mouseY;
	}
}
