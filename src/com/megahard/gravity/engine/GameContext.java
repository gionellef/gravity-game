package com.megahard.gravity.engine;

import java.awt.Color;
import java.util.List;

import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.objects.Player;
import com.megahard.gravity.util.Sound.Clips;
import com.megahard.gravity.util.Vector2;

public interface GameContext {

	public abstract void addObject(GameObject obj);

	public abstract void removeObject(GameObject obj);

	public abstract void playSoundAtLocation(Clips sound, double x, double y,
			double volume);

	public abstract void playSoundAtLocation(Clips sound, Vector2 position,
			double volume);

	public abstract GameMap getMap();

	public abstract boolean keyIsDown(int keyCode);

	public abstract boolean keyIsJustPressed(int keyCode);

	public abstract boolean keyIsUp(int keyCode);

	public abstract boolean keyIsJustReleased(int keyCode);

	public abstract boolean mouseIsDown(int button);

	public abstract boolean mouseIsJustPressed(int button);

	public abstract boolean mouseIsUp(int button);

	public abstract boolean mouseIsJustReleased(int button);

	public abstract Vector2 getMouseGamePosition();

	public abstract int getMouseScreenX();

	public abstract int getMouseScreenY();

	public abstract <T> T findObject(Class<T> type, double x, double y,
			double w, double h, boolean inclusive);

	public abstract List<GameObject> findObjects(double x, double y, double w,
			double h, boolean inclusive);

	public abstract Player getPlayerObject();

	public abstract boolean isCinematicMode();

	public abstract void setCinematicMode(boolean cinematicMode);

	public abstract void showMessage(String message, int duration);

	public abstract <T> T findObject(Class<T> type);

	public abstract void finish(boolean win, boolean esc);

	void fadeScreen(boolean out, int duration);

	void showMessage(String image, String message, int duration);

	public abstract void fadeScreen(Color colorStart, Color colorEnd, int duration);

	public abstract <T> List<T> findObjects(Class<T> type, int x, int y, int w,
			int h, boolean inclusive);

	public abstract <T> List<T> findObjects(Class<T> type);

}