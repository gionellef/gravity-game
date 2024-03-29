package com.megahard.gravity.util;

// Sound class adapted from Metagun source code
// Ⓒ Markus Persson

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.*;

public class Sound {
	public static class Clips {
		public Clip[] clips;
		private int p;
		private int count;

		public Clips(byte[] buffer, int count) throws LineUnavailableException,
				IOException, UnsupportedAudioFileException {
			if (buffer == null)
				return;

			clips = new Clip[count];
			this.count = count;
			for (int i = 0; i < count; i++) {
				clips[i] = AudioSystem.getClip();
				clips[i].open(AudioSystem
						.getAudioInputStream(new ByteArrayInputStream(buffer)));
			}
		}
		
		public void play(){
			play(1, 0);
		}

		public void play(float volume, float pan) {
			if (clips == null)
				return;

			p++;
			if (p >= count)
				p = 0;
			clips[p].stop();

			try{
				FloatControl gainControl = (FloatControl) clips[p].getControl(FloatControl.Type.MASTER_GAIN);
				float max = gainControl.getMaximum();
				float min = gainControl.getMinimum();
				float g = min + (max - min) * (float) Math.pow(volume, 0.5);
				gainControl.setValue(Math.max(min, Math.min(g, max)));
			}catch(IllegalArgumentException e){
			}
			
			try{
				FloatControl panControl = (FloatControl) clips[p].getControl(FloatControl.Type.PAN);
				float max = panControl.getMaximum();
				float min = panControl.getMinimum();
				panControl.setValue(Math.max(min, Math.min(pan, max)));
			}catch(IllegalArgumentException e){
				try{
					FloatControl balanceControl = (FloatControl) clips[p].getControl(FloatControl.Type.BALANCE);
					float max = balanceControl.getMaximum();
					float min = balanceControl.getMinimum();
					balanceControl.setValue(Math.max(min, Math.min(pan, max)));
				}catch(IllegalArgumentException e2){
					System.out.println("Can't pan sound!");
				}
			}

			clips[p].setFramePosition(0);
			clips[p].start();
		}

		public void stop() {
			clips[p].stop();
		}
		
		public void stopAll(){
			for(Clip c : clips){
				c.stop();
			}
		}
	}
	
	public static void stopAll(){
		for(Clips c : allClipses){
			c.stopAll();
		}
	}
	
	private static List<Clips> allClipses = new LinkedList<>();

	public static Clips airjump = load("airjump.wav", 2);
	public static Clips bomb_beep = load("bomb-beep.wav", 4);
	public static Clips button_press = load("button-press.wav", 2);
	public static Clips button_release = load("button-release.wav", 2);
	public static Clips door_close = load("door-close.wav", 1);
	public static Clips door_open = load("door-open.wav", 1);
	public static Clips explosion = load("explosion.wav", 8);
	public static Clips gravwell = load("gravwell.wav", 3);
	public static Clips gravwell_start = load("gravwell-start.wav", 4);
	public static Clips jump1 = load("jump1.wav", 1);
	public static Clips jump2 = load("jump2.wav", 1);
	public static Clips jump3 = load("jump3.wav", 1);
	public static Clips jump4 = load("jump4.wav", 1);
	public static Clips land1 = load("land1.wav", 2);
	public static Clips land2 = load("land2.wav", 2);
	public static Clips land3 = load("land3.wav", 2);
	public static Clips land4 = load("land4.wav", 2);
	public static Clips negravwell = load("negravwell.wav", 3);
	public static Clips negravwell_start = load("negravwell-start.wav", 4);
	public static Clips plasma = load("plasma.wav", 8);
	public static Clips power = load("power.wav", 2);
	public static Clips spark = load("spark.wav", 16);
	public static Clips step1 = load("step1.wav", 2);
	public static Clips step2 = load("step2.wav", 2);
	public static Clips step3 = load("step3.wav", 2);
	public static Clips step4 = load("step4.wav", 2);
	public static Clips step5 = load("step5.wav", 2);
	public static Clips step6 = load("step6.wav", 2);
	public static Clips step7 = load("step7.wav", 2);
	public static Clips switchSound = load("switch.wav", 2);
	public static Clips switch_sentinel = load("switch-sentinel.wav", 2);

	private static Clips load(String name, int count) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(
					Sound.class.getResourceAsStream("/sound/" + name));
			byte[] buffer = new byte[1024];
			int read = 0;
			while ((read = dis.read(buffer)) >= 0) {
				baos.write(buffer, 0, read);
			}
			dis.close();

			byte[] data = baos.toByteArray();
			Clips clips = new Clips(data, count);
			allClipses.add(clips);
			return clips;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return new Clips(null, 0);
			} catch (Exception ee) {
				ee.printStackTrace();
				return null;
			}
		}
	}

	public static void touch() {
	}
}
