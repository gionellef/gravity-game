package com.megahard.gravity.scripts;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.objects.Player;

public class Finale extends ScriptSequencer {

	public Finale(GameContext game, Double region) {
		super(game, region);
	}

	@Override
	protected void onSkip() {
	}

	@Override
	protected void onEnd() {
	}

	@Override
	public void onStart() {
		// talk talk talk
		addMessage("isaac", "This must be the control center.", 100);

		// send message
		addMessage("isaac", "I need to send this message to Gravitonia fast.", 80);
		addMessage("isaac", "...", 50);
		addMessage("isaac", "Done, I hope they got my message.", 90);

		// fade to black
		addRunnable(new Runnable() {
			@Override
			public void run() {
				getGame().setCameraTarget(getCenter().plus(0, 7));
				getGame().fadeScreen(null, Color.black, 100);
			}
		}, 100);
		
		addMessage("PlanetGravitonia", "", 100);
		
		// soldier
		addMessage("gravitonian-soldier", "General Maxwell, a message came from x300, y125, h123, t2467 coordinates. It seems to be an Ytivargs' base. The message has Galileo's signiture in it.", 200);
		// Commander Maxwell
		addMessage("maxwell", "Tell me the its contents quickly!", 100);
		// soldier
		addMessage("gravitonian-soldier", "It seems like the last survivor of the royal family has been found and he needs our help ASAP.", 180);
		// Commander Maxwell
		addMessage("maxwell", "We need to protect him. He is our only hope...", 120);
		// Commander Maxwell
		addMessage("maxwell", "Prepare the war ships and the Pendulum War Fortress! We are gonna F*** some Ytivargs' nig*as asses!!!", 180);

		//addMessage("Ytivargland", "", 100);
		
		// Ytivarg soldier
		addMessage("ytivarg", "Commander Swaggernaut, some dipsh*t sent an unauthorized message from the Arkthuri System. In the Zumtalan Gamma cluster, Mk'tharon outpost 316.", 190);
		// Commander Swaggernaut
		addMessage("swaggernaut", "Aight, what does it say mah homie dawg nig*a?", 100);
		// Ytivarg soldier
		addMessage("ytivarg", "The Earthling punk we ubducted sent a message to Gravitonia. He's asking for some help.", 120);
		// Commander Swaggernaut
		addMessage("swaggernaut", "What the f***! You ain't no kidding right mah nig*a? ", 110);
		// Ytivarg soldier
		addMessage("ytivarg", "Dem right boss!", 100);
		// Commander Swaggernaut
		addMessage("swaggernaut", "Stop chillin' mah brotahs. Those Gravitonian bastards got the message by now. Prepare mah nig*as. We are sending the Swag War Fleet. They ain't got no match for us!", 220);
		// Ytivarg soldier
		addMessage("ytivarg", "They gonna get #rekt!", 100);
		
		// Ending text
		addMessage(null, "The real war is just about to begin", 200);
		
		// fade back the game
		addRunnable(new Runnable() {
			@Override
			public void run() {
				getGame().fadeScreen(Color.black, null, 50);
			}
		}, 50);
	}

	@Override
	public void onEnter(GameObject object) {
		if (object.getClass().equals(Player.class)) {
			beginSequence(true, true, false);
		}
	}

	@Override
	public void onExit(GameObject object) {
	}

}
