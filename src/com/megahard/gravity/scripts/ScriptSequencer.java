package com.megahard.gravity.scripts;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D.Double;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.megahard.gravity.engine.GameContext;
import com.megahard.gravity.engine.base.Script;

public abstract class ScriptSequencer extends Script {

	private static class ScriptStep{
		Runnable runnable;
		String image;
		String message;
		int duration;
		public ScriptStep(String image, String message, int duration){
			this.image = image;
			this.message = message;
			this.duration = duration;
		}
		public ScriptStep(Runnable runnable, int duration){
			this.runnable = runnable;
			this.duration = duration;
		}
		public ScriptStep(int duration){
			this.duration = duration;
		}
	}
	
	private boolean firstRun = true;
	private boolean active = false;
	
	private boolean cine = false;
	private boolean skip = false;

	private Queue<ScriptStep> stepQueue = new LinkedList<>();
	private ScriptStep currentStep = null;
	private int timer = 0;
	private int nextStepTime = 0;

	public ScriptSequencer(GameContext game, Double region,
			Map<String, String> properties) {
		super(game, region, properties);
	}

	public ScriptSequencer(GameContext game, Double region) {
		super(game, region);
	}

	@Override
	public void onUpdate() {
		if(active){
			if(timer >= nextStepTime){
				if(!stepQueue.isEmpty()){
					currentStep = stepQueue.remove();
					nextStepTime += currentStep.duration;
					if(currentStep.runnable != null){
						currentStep.runnable.run();
					}
					if(currentStep.message != null){
						getGame().showMessage(currentStep.image, currentStep.message, currentStep.duration);
					}
				}else{
					endSequence();
					onEnd();
				}
			}
			
			timer++;
			
			if(skip){
				if(getGame().keyIsDown(KeyEvent.VK_ENTER)){
					endSequence();
					onSkip();
				}
			}
		}
	}

	protected void addMessage(String message, int duration){
		addMessage(null, message, duration);
	}
	protected void addMessage(String image, String message, int duration){
		stepQueue.add(new ScriptStep(image, message, duration));
	}
	protected void addRunnable(Runnable runnable, int duration){
		stepQueue.add(new ScriptStep(runnable, duration));
	}
	protected void addDelay(int duration){
		stepQueue.add(new ScriptStep(duration));
	}
	
	protected void beginSequence(boolean runOnce, boolean cinematic, boolean skippable){
		/* if(runOnce -> firstRun) */
		if(!runOnce || firstRun){
			active = true;
			cine = cinematic;
			skip = skippable;
			
			if(cine){
				getGame().setCinematicMode(true);
			}
			
			firstRun = false;
		}
	}
	protected void endSequence(){
		active = false;

		if(currentStep != null && currentStep.message != null){
			getGame().removeMessage();
		}
		
		if(cine){
			getGame().setCinematicMode(false);
		}
	}

	// Overridable
	protected abstract void onSkip();
	protected abstract void onEnd();
}