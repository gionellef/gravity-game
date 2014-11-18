package com.megahard.gravity;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javazoom.jl.decoder.JavaLayerException;
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
			System.out.println(values);
		}
		
		scanner.close();
	}
	
	public void play(String fileName){
	    this.fileName = fileName;
	    System.out.println(fileName);
    }

	@Override
	public void run() {
		playing = true;
		
		while (playing) {
			try {
				
				DataInputStream dis = new DataInputStream(
						Sound.class.getResourceAsStream("/music/" + fileName));
		        pl = new AdvancedPlayer(dis);
		
		        pl.getPlayBackListener();
			  
				pl.play();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("IM HERE");
	}
	
	public void stop() {
		playing = false;
	}
	
}