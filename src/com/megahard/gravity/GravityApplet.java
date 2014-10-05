package com.megahard.gravity;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;

public class GravityApplet extends Applet {
	
	private Renderer renderer = new Renderer();
	
	@Override
	public void init() {
		setLayout(new BorderLayout());
		add(renderer, BorderLayout.CENTER);
	}
	
	public void start() {
		renderer.start();
	}
	
	public void stop() {
		renderer.stop();
	}
}
