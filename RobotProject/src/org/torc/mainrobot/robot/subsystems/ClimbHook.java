package org.torc.mainrobot.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimbHook extends Subsystem {

	private Servo cServo;
	
	private final double homePos = 0;
	private final double openPos = 1;
	
	private boolean isOpen = false;
	
	public ClimbHook(int servoPort) {
		cServo = new Servo(servoPort);
	}
	
	public void openHook() {
		cServo.set(openPos);
		isOpen = true;
	}
	
	public void closeHook() {
		cServo.set(homePos);
		isOpen = false;
	}
	
	public double getHookPos() {
		return cServo.get();
	}
	
	public boolean hookIsOpen() {
		return isOpen;
	}
	
	@Override
	protected void initDefaultCommand() {
	}

}
