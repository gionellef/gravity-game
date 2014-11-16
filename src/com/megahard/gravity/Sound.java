package com.megahard.gravity;

// Sound class adapted from Metagun source code
// Ⓒ Markus Persson

import java.io.*;

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

		public void play() {
			if (clips == null)
				return;

			p++;
			if (p >= count)
				p = 0;
			clips[p].stop();
			clips[p].setFramePosition(0);
			clips[p].start();
		}

		public void stop() {
			clips[p].stop();
		}
	}

	public static Clips gravwell_start = load("gravwell-start.wav", 4);
	public static Clips gravwell = load("gravwell.wav", 4);
	public static Clips step_cloth1 = load("step_cloth1.wav", 2);
	public static Clips step_cloth2 = load("step_cloth2.wav", 2);
	public static Clips step_cloth3 = load("step_cloth3.wav", 2);
	public static Clips step_cloth4 = load("step_cloth4.wav", 2);
	public static Clips step_lth1 = load("step_lth1.wav", 2);
	public static Clips step_lth2 = load("step_lth2.wav", 2);
	public static Clips step_lth3 = load("step_lth3.wav", 2);
	public static Clips step_lth4 = load("step_lth4.wav", 2);
	public static Clips step_metal1 = load("step_metal1.wav", 2);
	public static Clips step_metal2 = load("step_metal2.wav", 2);
	public static Clips step_metal3 = load("step_metal3.wav", 2);
	public static Clips step_metal4 = load("step_metal4.wav", 2);

	private static Clips load(String name, int count) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(
					Sound.class.getResourceAsStream("sound/" + name));
			byte[] buffer = new byte[1024];
			int read = 0;
			while ((read = dis.read(buffer)) >= 0) {
				baos.write(buffer, 0, read);
			}
			dis.close();

			byte[] data = baos.toByteArray();
			return new Clips(data, count);
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
