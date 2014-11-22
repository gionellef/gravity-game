package com.megahard.gravity.objects;

import com.megahard.gravity.GameContext;
import com.megahard.gravity.GameMap;
import com.megahard.gravity.GameObject;
import com.megahard.gravity.Sound;

public class Button extends GameObject {

	private double totalMass = 0;
	private int pressCounter = 0;
	private boolean pressed = false;
	
	private int outputX = -1;
	private int outputY = -1;
	private boolean outputInverted;

	private static final int[] OUTPUT_ON = {
		7,
		31,
		34,
		132,
	};
	private static final int[] OUTPUT_OFF = {
		7 + 8,
		31 + 8,
		34 + 8,
		132 + 8,
	};
	
	// {onIndex, offIndex, connectivityFlags (UDLR)}
	private static final int[][] WIRES = {
		{7, 	7+8,	0b0100},
		{31,	31+8,	0b0001},
		{34,	34+8,	0b0010},
		{132,	132+8,	0b1000},
		{32,	32+8,	0b1111},
		{33,	33+8,	0b0011},
		{57,	57+8,	0b1100},
		{79,	79+8,	0b0110},
		{80,	80+8,	0b0101},
		{104,	104+8,	0b1001},
		{105,	105+8,	0b1010},
		{81,	81+8,	0b0011},
		{107,	107+8,	0b1100},
		{82,	82+8,	0b1111},
		{83,	83+8,	0b0111},
		{84,	84+8,	0b1110},
		{108,	108+8,	0b1101},
		{109,	109+8,	0b1011},
		{59,	59+8,	0b0100},
		{78,	78+8,	0b0001},
		{110,	110+8,	0b0010},
		{133,	133+8,	0b1000},
		{18,	19, 	0b1100},
		{68,	69, 	0b1100},
		{237,	44, 	0b1100},
		{20,	45, 	0b0011},
		{22,	47, 	0b0011},
		{238,	46, 	0b0011},
	};
	
	public Button(GameContext game) {
		super(game, "button");
		size.set(2, 0.3);
		mass = 30;
		fixed = true;
		
		zIndex = 250;
	}
	
	@Override
	public void init() {
		findSource();
	}

	private void findSource(){
		GameMap map = getGame().getMap();
		
		int maxRadius = 6;
		
		int x = (int) position.x;
		int y = (int) position.y;
		
		out: for(int r=1; r<maxRadius; r++){
			for(int i=r; i>=0; i--){
				for(int sx=-1; sx<=1; sx += 2){
					for(int sy=-1; sy<=1; sy += 2){
						int rsx = (r - i) * sx;
						int rsy = i * sy;
						int tileIndex = map.getTile(x + rsx, y + rsy).getTileIndex();
						for(int index : OUTPUT_ON){
							if(tileIndex == index){
								outputX = x + rsx;
								outputY = y + rsy;
								outputInverted = true;
								break out;
							}
						}
						for(int index : OUTPUT_OFF){
							if(tileIndex == index){
								outputX = x + rsx;
								outputY = y + rsy;
								outputInverted = false;
								break out;
							}
						}
					}
				}
			}
		}
	}
	
	private void setPressed(boolean p){
		pressed = p;
		
		sprite.setAction(pressed ? "pressed" : "default");
		
		if(outputX != -1 && outputY != -1){
			powerWire(outputX, outputY, outputInverted ? !pressed : pressed);
		}
		
		if(pressed)
			getGame().playSoundAtLocation(Sound.button_press, position, 0.8);
		else
			getGame().playSoundAtLocation(Sound.button_release, position, 0.7);
			
	}
	
	private void powerWire(int x, int y, boolean on){
		GameMap map = getGame().getMap();
		
		if(x < 0 || y < 0 || x >= map.getWidth() || y >= map.getHeight())
			return;
		
		int tileIndex = map.getTile(x, y).getTileIndex();
		
		if(!isWire(tileIndex))
			return;
		
		if(wirePower(tileIndex) == on)
			return;

		map.setTile(x, y, wireIndex(tileIndex, on));
		int conn = wireConnectivity(tileIndex);
		if((conn & 0b1000) != 0){
			powerWire(x, y - 1, on);
		}
		if((conn & 0b0100) != 0){
			powerWire(x, y + 1, on);
		}
		if((conn & 0b0010) != 0){
			powerWire(x - 1, y, on);
		}
		if((conn & 0b0001) != 0){
			powerWire(x + 1, y, on);
		}
	}
	
	private boolean isWire(int tileIndex){
		for(int[] row : WIRES){
			if(row[0] == tileIndex || row[1] == tileIndex) return true;
		}
		return false;
	}
	
	private boolean wirePower(int tileIndex){
		for(int[] row : WIRES){
			if(row[0] == tileIndex) return true;
			if(row[1] == tileIndex) return false;
		}
		return false;
	}
	
	private int wireIndex(int tileIndex, boolean on){
		for(int[] row : WIRES){
			if(row[0] == tileIndex || row[1] == tileIndex) return row[on ? 0:1];
		}
		return 0;
	}
	
	private int wireConnectivity(int tileIndex){
		for(int[] row : WIRES){
			if(row[0] == tileIndex || row[1] == tileIndex) return row[2];
		}
		return 0;
	}
	
	@Override
	public void update() {
		super.update();
		
		if(pressCounter > 0){
			pressCounter--;
			if(!pressed)
				setPressed(true);
		}else{
			if(pressed)
				setPressed(false);
		}
		
		totalMass = 0;
	}
	
	@Override
	public void onCollide(GameObject obj) {
		if(!obj.fixed){
			totalMass += obj.mass;
			if(totalMass > 30){
				pressCounter = 1;
			}
		}
	}

}
