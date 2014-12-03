package com.megahard.gravity.scripts;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.GameObject;
import com.megahard.gravity.objects.Player;

public class TheEncounter extends ScriptSequencer {
	
	private Player player;
	
	public TheEncounter(GameContext game, Double region) {
		super(game, region);
	}

	@Override
	public void onStart() {
		addDelay(50);
		addMessage(
			"galileo",
			"Those features, that hair, this overwhelming aura...",
			150
		);
		addMessage(
			"galileo",
			"Are you perhaps a descendant of the royal princess Gravis?",
			150
		);
		addMessage(
			"isaac",
			"Who are you? What is this place? Do you know why I am here?",
			150
		);
		addMessage(
				"galileo",
				"So you are clueless. I am Galileo. I've been an adviser to the Gravitonia royal family for 5000 years.",
				200
			);
		addMessage(
			"galileo",
			"Let me tell you who you really are. You are a descendant of Princess Gravis. You have the royal bloodline of the planet Gravitonia.",
			200
		);
		
		addMessage(
			"isaac",
			"You're crazy. I'm just a normal and handsome gay living a normal and boring life on Earth. How did that happen?",
			200
		);
		
		addRunnable(new Runnable() {
			@Override
			public void run() {
				getGame().fadeScreen(null, Color.black, 20);
			}
		}, 30);
		addMessage(
			"PlanetGravitonia",
			"In the planet of Gravitonia, there exists the royal family who ruled the whole planet. They posses a strange and powerful ability to control gravity.",
			200
		);
		addMessage(
			"PlanetGravitonia",
			"Without them and their power, the planet is at constant risk of being swallowed by rogue black holes getting close to the planet. They are the protectors of Gravitonia.",
			200
		);
		addRunnable(new Runnable() {
			@Override
			public void run() {
				getGame().fadeScreen(Color.black, null, 40);
				getGame().showMessage("galileo", "It happened 2000 years ago.", 100);
			}
		}, 100);
		addMessage(
			"galileo",
			"Princess Gravis, your ancestor, is the oldest child of the king and queen of that generation.",
			200
		);
		addMessage(
			"galileo",
			"The king arranged Gravis' wedding but she already has a lover. One day, Gravis and her lover decided to flee the planet in order to escape their fates in Gravitonia.",
			200
		);
		addMessage(
				"galileo",
				"The king tried to stop them... They managed to escape but Gravis' lover died in the process. Gravis, enraged, decided never to go back to Gravitonia ever again.",
				200
			);
		addMessage(
				"galileo",
				"...",
				20
			);
		addMessage(
				"galileo",
				"Maybe in search for a place to live, Gravis found Earth and another man to spend her whole life with. I just hope she had a happy life on Earth.",
				200
			);
		
		addMessage(
				"isaac",
				"WHAT!? But why am I here? And what is this stupid place?",
				150
			);

		addMessage(
				"galileo",
				"We are in an Ytivarg asteroid outpost base. You were captured by them. They want the power you posses.",
				200
			);
		addMessage(
				"galileo",
				"The whole royal family of Gravitonia was massacred by the Ytivargs in order to unlock the secrets on how they could control gravity by using their bodies in experiments.",
				200
			);
		addMessage(
				"galileo",
				"Our then king told me, in his dying breaths, to burn their bodies in order to protect the secrets. I managed to burn their bodies but I was captured in the process. So I am here now.",
				200
			);

		addMessage(
				"galileo",
				"You need to escape from here. You must go to the control center in the eastern tower this outpost. Send a message to Gravitonia. Our royal guards will come and help.",
				250
			);
	}

	@Override
	public void onEnter(GameObject object) {
		if(object.getClass().equals(Player.class)){
			getGame().setCinematicMode(true);
			player = (Player) object;
			player.velocity.x = 0;
			beginSequence(true, true, true);
		}
	}

	@Override
	public void onExit(GameObject object) {
	}

	@Override
	protected void onSkip() {
		getGame().showMessage(
			"galileo",
			"You must go to the control center in the eastern tower this outpost to send a message to Gravitonia.",
			200
		);
	}

	@Override
	protected void onEnd() {
	}

}
