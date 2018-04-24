package org.torc.mainrobot.robot.commands.auton;

import org.torc.mainrobot.tools.CLCommand;
import org.torc.mainrobot.tools.MathExtra;

import edu.wpi.first.wpilibj.PWMSpeedController;

public class RampMCSpeed extends CLCommand {
	
	private PWMSpeedController sCont;
	
	private double targetSpeed;
	
	private int targetTime;
	
	private int countingTime = 0;
	
	private double oldSpeed;
	
	public RampMCSpeed(PWMSpeedController speedController, double targSpeed, int timeMS) {
		sCont = speedController;
		targetSpeed = targSpeed;
		targetTime = timeMS / 20;
	}
	
	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		oldSpeed = sCont.getSpeed();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		double lerpVal = MathExtra.clamp(((double)countingTime / (double)targetTime), 0, 1);
		double speedVal = MathExtra.lerp(oldSpeed, targetSpeed, lerpVal);
		
		System.out.println("lerpVal: " + lerpVal + " || speedVal: " + speedVal);
		
		sCont.set(speedVal);
		
		countingTime++;
		
		if (lerpVal >= 1) {
			finishedCommand = true;
		}
		
	}


	// Called once after isFinished returns true
	@Override
	protected void end() {
	}
	
}
