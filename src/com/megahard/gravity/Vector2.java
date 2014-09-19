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

   public Vector2 translate( Vector2 v1 ) {
       Vector2 tempVector = new Vector2( this.x + v1.x, this.y + v1.y );
       return tempVector;
   }

   public Vector2 scale( double scaleFactor ) {
       Vector2 tempVector = new Vector2( this.x*scaleFactor, this.y*scaleFactor );
       return tempVector;
   }

   public double dotProduct ( Vector2 v1 ) {
        return this.x*v1.x + this.y*v1.y;
   }

}
