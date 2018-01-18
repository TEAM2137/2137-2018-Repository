package org.torc.mainrobot.program;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.torc.mainrobot.tools.EncoderExtra;

public class Teleop {
	
	public static void Init() {
		//RobotMap.myRobot.setSafetyEnabled(true);
	}
	
	public static void Periodic() {
		// Testing Encoder Tracking
		
		//EncoderExtra encTest = new EncoderExtra(RobotMap.encoderL);
		
		//encTest.startTracking();
		
		RobotMap.driveTrainSubSys.tankDrive(-ButtonMap.xController0.getY(GenericHID.Hand.kLeft), ButtonMap.xController0.getY(GenericHID.Hand.kRight));
		
		if (ButtonMap.xController0.getBumper(Hand.kLeft)) {
			// Forward is Low Gear
			RobotMap.shifter.set(DoubleSolenoid.Value.kForward);
		}
		
		else if (ButtonMap.xController0.getBumper(Hand.kRight)) {
			// Reverse is High Gear
			RobotMap.shifter.set(DoubleSolenoid.Value.kReverse);	
		}
		
		String soleState = "";
		
		switch (RobotMap.shifter.get()) {
		case kReverse:
			soleState = "High";
			break;
			
		case kForward:
			soleState = "Low";
			break;
			
		default:
			soleState = "Other";
			break;
		
		}
		// Write the gear state to smartDashboard
		SmartDashboard.putString("Gear State", soleState);
		SmartDashboard.putNumber("Left Encoder Rotations", RobotMap.encoderL.getDistance());
		SmartDashboard.putNumber("Right Encoder Rotations", RobotMap.encoderR.getDistance());
		SmartDashboard.putNumber("Left Encoder Disatance", EncoderExtra.EncoderDistance(RobotMap.WheelDiameter, RobotMap.encoderL.getDistance()));
		SmartDashboard.putNumber("Left Encoder RPM?", RobotMap.encoderL.getRate());
		SmartDashboard.putNumber("Right Encoder RPM?", RobotMap.encoderR.getRate());
		
	}
}
