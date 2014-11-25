package com.megahard.gravity.util;

import java.lang.Math;

public class Vector2 {

	public double x;
	public double y;

	// Constructor methods ....

	public Vector2() {
		x = y = 0;
	}

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 vector){
		x = vector.x;
		y = vector.y;
	}

	// Magnitude... 7.1 earthquake in guam
	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public Vector2 plus(Vector2 v1) {
		// return plus(v1.x, v1.y);
		return new Vector2(this.x + v1.x, this.y + v1.y);
	}

	public Vector2 plus(double x, double y) {
		return new Vector2(this.x + x, this.y + y);
	}
	
	public Vector2 minus(Vector2 v) {
		return new Vector2(x - v.x, y - v.y);
	}

	public Vector2 times(double scaleFactor) {
		return new Vector2(this.x * scaleFactor, this.y * scaleFactor);
	}

	public double dot(Vector2 v) {
		return this.x * v.x + this.y * v.y;
	}

	public Vector2 normalized() {
		return times(1/length());
	}
	
	public double angle(){
		return Math.atan2(y, x);
	}
	
	public double distance(Vector2 other){
		return displacement(other).length();
	}
	
	public Vector2 displacement(Vector2 other){
		return other.minus(this);
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void set(Vector2 value) {
		// set(value.x, value.y)
		x = value.x;
		y = value.y; 
	}

	public void add(double x, double y) {
		this.x += x;
		this.y += y;
	}

	public void add(Vector2 v) {
		this.x += v.x;
		this.y += v.y;
	}
	
	public void subtract(double x, double y) {
		this.x -= x;
		this.y -= y;
	}

	public void scale(double scaleFactor) {
		this.x *= scaleFactor;
		this.y *= scaleFactor;
	}

	public void normalize() {
		scale(1/length());
	}
	
	@Override
	public String toString() {
		return "Vector2(" + x + "," + y + ")";
	}

}