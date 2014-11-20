package com.megahard.gravity;

import java.util.List;

import com.megahard.gravity.Sound.Clips;
import com.megahard.gravity.objects.Player;

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

}