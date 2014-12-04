package com.megahard.gravity.util;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class MusicPlayer extends PlaybackListener implements Runnable {

	private String fileName;
	public static ArrayList<String[]> mFiles;
	private boolean playing;
	private Thread thread;

	public MusicPlayer() {
		mFiles = new ArrayList<String[]>();
		InputStream in = getClass().getResourceAsStream("/music/music.txt");
		Scanner scanner = new Scanner(in);

		while (scanner.hasNextLine()) {
			String[] values = scanner.nextLine().split(",");
			// String mapName = values[0];
			// String fileName = values[1];
			mFiles.add(values);
		}

		scanner.close();
	}

	public void play(String fileName) {
		stop();
		try {
			if(thread != null)
				thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.fileName = fileName;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		final AdvancedPlayer pl = createPlayer(fileName);

		new Thread() {
			public void run() {
				try {
					pl.play();
				} catch (JavaLayerException e) {
					e.printStackTrace();
				}
			};
		}.start();

		playing = true;
		while (playing){
			Thread.yield();
		}
		pl.close();
	}

	private AdvancedPlayer createPlayer(String file) {
		DataInputStream dis = new DataInputStream(
			getClass().getResourceAsStream("/music/" + file));
		try {
			return new AdvancedPlayer(dis);
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void stop() {
		playing = false;
	}

	@Override
	public void playbackFinished(PlaybackEvent e) {
		if (playing) {
			int i = new Random().nextInt(mFiles.size());
//			play(mFiles.get(i)[1]);
		}
	}
}