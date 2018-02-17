package org.torc.mainrobot.robot.commands;

import org.torc.mainrobot.tools.MotorControllers;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * 	ULTRA GRABBER
 *  (A.K.A, the cube grabber)
 */
public class UltraGrabber extends Subsystem {
	
	private enum GrabberStates { homing, angling, spitting }
	
	private GrabberStates grabberState = GrabberStates.homing;
	
	VictorSP leftMotor;
	VictorSP rightMotor;
	
	TalonSRX angleMotor;

	UltraGrabber(int leftVictorPort, int rightVictorPort, int angleTalonPort) {
		leftMotor = new VictorSP(leftVictorPort);
		rightMotor = new VictorSP(rightVictorPort);

		angleMotor = new TalonSRX(angleTalonPort);
		
		MotorControllers.TalonSRXConfig(angleMotor, 10, 0, 0);
		
		
		
	}
	
	@Override
	protected void initDefaultCommand() {
	}
}
