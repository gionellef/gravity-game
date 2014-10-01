package com.megahard.gravity;

import java.lang.Math;

public class Vector2 {

   public double x;
   public double y;

   // Constructor methods ....

   public Vector2() {
      x = y = 0;
   }

   public Vector2( double x, double y ) {
      this.x = x;
      this.y = y;
   }

   // Magnitude... 7.1 earthquake in guam
   public double length() {
      return Math.sqrt ( x*x + y*y );
   }

   public Vector2 add( Vector2 v1 ) {
       return new Vector2( this.x + v1.x, this.y + v1.y );
   }

   public Vector2 scale( double scaleFactor ) {
       return new Vector2( this.x*scaleFactor, this.y*scaleFactor );
   }

   public double dot ( Vector2 v1 ) {
        return this.x*v1.x + this.y*v1.y;
   }
   
   public void set(double x, double y) {
	   this.x = x;
	   this.y =y;
	   
   }

}
