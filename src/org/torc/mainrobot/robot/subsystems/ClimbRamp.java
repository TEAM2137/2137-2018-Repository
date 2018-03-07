package org.torc.mainrobot.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimbRamp extends Subsystem {

	private Servo rServo;
	
	private final double homePos = 0;
	
	private boolean isOpen = false;
	
	public ClimbRamp(int servoPort) {
		rServo = new Servo(servoPort);
	}
	
	public void openRamp() {
		rServo.set(1);
		isOpen = true;
	}
	
	public void closeRamp() {
		rServo.set(homePos);
		isOpen = false;
	}
	
	public double getRampPos() {
		return rServo.get();
	}
	
	public boolean rampIsOpen() {
		return isOpen;
	}
	
	@Override
	protected void initDefaultCommand() {
	}

}
