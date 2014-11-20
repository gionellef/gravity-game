package com.megahard.gravity.scripts;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;
import java.util.LinkedList;
import java.util.Queue;

import com.megahard.gravity.Engine;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Script;
import com.megahard.gravity.objects.Player;

public class SGalileo extends Script {
	
	private Player player;
	
	private boolean firstRun = true;
	private boolean active = false;
	private int timer = 0;
	
	private int nextMessageTime = 0;
	
	private class Msg{
		Runnable runnable;
		String image;
		String message;
		int duration;
		public Msg(String image, String message, int duration){
			this.image = image;
			this.message = message;
			this.duration = duration;
		}
		public Msg(Runnable runnable, int duration){
			this.runnable = runnable;
			this.duration = duration;
		}
	}
	private Queue<Msg> msgQueue;

	public SGalileo(Engine game, Double region) {
		super(game, region);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onStart() {
		msgQueue = new LinkedList<>();

		msgQueue.add(new Msg(null, null, 20));
		msgQueue.add(new Msg(
			"galileo",
			"Those features, that hair, this overwhelming aura...",
			150
		));
		msgQueue.add(new Msg(
			"galileo",
			"But no... It can't be...",
			100
		));
		msgQueue.add(new Msg(
			"galileo",
			"Are you perhaps a descendant of the royal princess Gravis?",
			150
		));
		msgQueue.add(new Msg(
			"isaac",
			"Who are you? What is this place? Do you know why I am here?",
			150
		));
		msgQueue.add(new Msg(
				"galileo",
				"So you are clueless. I am Galileo. I've been an adviser to the Gravitonia royal family for 5000 years.",
				200
			));
		msgQueue.add(new Msg(
			"galileo",
			"Let me tell you who you really are. You are a descendant of Princess Gravis. You have the royal bloodline of the planet Gravitonia.",
			200
		));
		
		msgQueue.add(new Msg(
			"isaac",
			"You're crazy. I'm just a normal and handsome gay living a normal and boring life on Earth. How did that happen?",
			150
		));
		
		msgQueue.add(new Msg(new Runnable() {
			@Override
			public void run() {
				getGame().fadeScreen(null, Color.black, 20);
			}
		}, 30));
		msgQueue.add(new Msg(
			"PlanetGravitonia",
			"In the planet of Gravitonia, there exists the royal family who ruled the whole planet. They posses a strange and powerful ability to control gravity.",
			300
		));
		msgQueue.add(new Msg(
			"PlanetGravitonia",
			"Without them and their power, the planet is at constant risk of being swallowed by rogue black holes getting close to the planet. They are the protectors of Gravitonia.",
			300
		));
		msgQueue.add(new Msg(new Runnable() {
			@Override
			public void run() {
				getGame().fadeScreen(Color.black, null, 100);
				getGame().showMessage("galileo", "It happened 2000 years ago.", 100);
			}
		}, 100));
		msgQueue.add(new Msg(
			"galileo",
			"Princess Gravis, your ancestor, is the oldest child of the king and queen of that generation.",
			200
		));
		msgQueue.add(new Msg(
			"galileo",
			"The king arranged Gravis' wedding but she already has a lover. One day, Gravis and her lover decided to flee the planet in order to escape their fates in Gravitonia.",
			200
		));
		msgQueue.add(new Msg(
				"galileo",
				"The king tried to stop them... They managed to escape but Gravis' lover died in the process. Gravis, enraged, decided never to go back to Gravitonia ever again.",
				200
			));
		msgQueue.add(new Msg(
				"galileo",
				"...",
				80
			));
		msgQueue.add(new Msg(
				"galileo",
				"Maybe in search for a place to live, Gravis found Earth and another man to spend her whole life with. I just hope she had a happy life on Earth.",
				200
			));
		
		msgQueue.add(new Msg(
				"isaac",
				"WHAT!? But why am I here? And what is this stupid place?",
				100
			));

		msgQueue.add(new Msg(
				"galileo",
				"We are in an Ytivarg asteroid outpost base. You were captured by them. They want the power you posses.",
				200
			));
		msgQueue.add(new Msg(
				"galileo",
				"The whole royal family of Gravitonia was massacred in order to unlock the secrets on how they could control gravity by using their bodies in experiments.",
				200
			));
		msgQueue.add(new Msg(
				"galileo",
				"Our then king told me, in his dying breaths, to burn their bodies in order to protect the secrets. I managed to burn their bodies but I was captured in the process. So I am here now.",
				200
			));
		
		msgQueue.add(new Msg(
				"isaac",
				"So they will kill me and use me in their experiments!?",
				150
			));
	
		msgQueue.add(new Msg(
				"galileo",
				"They will do worse than that.",
				100
			));
		msgQueue.add(new Msg(
				"galileo",
				"You need to escape from here. You must go to the communications module. Send a message to Gravitonia. Our royal guards will come and help you.",
				200
			));
	
		msgQueue.add(new Msg(
				"isaac",
				"Will the royal guards take me back to Earth?",
				150
			));
	
		msgQueue.add(new Msg(
				"galileo",
				"No, you must fulfill your destiny as the protector of Gravitonia and avenge the royal family.",
				200
			));
	
		msgQueue.add(new Msg(
				"isaac",
				"No. I don't want that. I have a life on Earth. My destiny is on Earth.",
				150
			));
	
		msgQueue.add(new Msg(
				"galileo",
				"You have no other choice. This is who you really are.",
				150
			));
		msgQueue.add(new Msg(
				"galileo",
				"Your power is still weak but it will grow to something you have never imagined. The people of Gravitonia will help you unlock its full potential. Use that power to stop the plans of the Ytivargs.",
				200
			));
		msgQueue.add(new Msg(
				"galileo",
				"You don't belong on Earth. Now leave!",
				150
			));
	
		msgQueue.add(new Msg(
				"isaac",
				"Wait! I will save you!",
				100
			));
	
		msgQueue.add(new Msg(
				"galileo",
				"I am too weak to move. I will die soon. But it is well.",
				150
			));
		msgQueue.add(new Msg(
				"galileo",
				"I've lived for 10,000 years and I am happy to have served the royal family until the end.",
				200
			));
		msgQueue.add(new Msg(
				"galileo",
				"I am so glad that I met you. You really resemble Gravis.",
				150
			));
	
		msgQueue.add(new Msg(
				"isaac",
				"Farewell, old fool.",
				100
			));
	}

	@Override
	public void onUpdate() {
		if(active){
			if(timer == nextMessageTime){
				if(!msgQueue.isEmpty()){
					Msg msg = msgQueue.remove();
					nextMessageTime += msg.duration;
					if(msg.runnable != null){
						msg.runnable.run();
					}
					if(msg.message != null){
						getGame().showMessage(msg.image, msg.message, msg.duration);
					}
				}else{
					getGame().setCinematicMode(false);
				}
			}
			
			timer++;
		}
	}

	@Override
	public void onEnter(GameObject object) {
		if(firstRun && object.getClass().equals(Player.class)){
			player = (Player) object;
			getGame().setCinematicMode(true);
			firstRun = false;
			active = true;
		}
	}

	@Override
	public void onExit(GameObject object) {
		// TODO Auto-generated method stub

	}

}
