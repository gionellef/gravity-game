package com.megahard.gravity.util;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javazoom.jl.player.advanced.AdvancedPlayer;

public class MusicPlayer implements Runnable{

	private AdvancedPlayer pl;
	private String fileName;
	public static ArrayList<String[]> mFiles;
	private boolean playing;
	
	public MusicPlayer(){
		mFiles = new ArrayList<String[]>();
		InputStream in = getClass().getResourceAsStream("/music/music.txt");
		Scanner scanner = new Scanner(in);
		
		while(scanner.hasNextLine()){
			String[] values = scanner.nextLine().split(",");
			//String mapName = values[0];
			//String fileName = values[1];
			mFiles.add(values);
		}
		
		scanner.close();
	}
	
	public void play(String fileName){
	    this.fileName = fileName;
	    new Thread(this).start();
    }

	@Override
	public void run() {
		playing = true;
        try {
        	while (playing) {
        		DataInputStream dis = new DataInputStream(
        				Sound.class.getResourceAsStream("/music/" + fileName));
        		pl = new AdvancedPlayer(dis);
    			pl.getPlayBackListener();
    			pl.play();

        		int num = new Random().nextInt(mFiles.size());
        		fileName = mFiles.get(num)[1];
        	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}
	}
	
	public void stop() {
		playing = false;
		try {
			pl.close();
		} catch (Exception e) {
			
		}
		
	}
	
}