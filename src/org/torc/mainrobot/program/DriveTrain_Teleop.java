package org.torc.mainrobot.program;

import org.torc.mainrobot.program.ButtonMap.RCAxis;
import org.torc.mainrobot.robot.ControlledStateMachine;

public class DriveTrain_Teleop extends ControlledStateMachine {
	
	// TODO: Make this a real value that is acurate to our gears & encoder ticks.
	double maxSpeed = 0.5;
	
	double throttle;
	double wheel;
	
	double thresh = 0.3;
	
	@Override
	public void execute() {
		/*
		throttle = -RobotMap.mainController.getAxis(RCAxis.leftY);
		wheel = RobotMap.mainController.getAxis(RCAxis.rightX);
		
		double rightVal = 0;
		double leftVal = 0;
		
		
		double wheelAdj = 0.5 * wheel;
		rightVal = throttle - wheelAdj;
		leftVal = throttle + wheelAdj;
		/*
		if(Math.abs(throttle) < thresh) {
			rightVal = -wheelAdj;
			leftVal = wheelAdj;
		}
		else {
			rightVal = throttle - wheelAdj;
			leftVal = throttle + wheelAdj;
		}
		*/
		

		//RobotMap.DriveSubsystem.setPercVBus(leftVal, rightVal);
		RobotMap.DriveSubsystem.setPercVBus(RobotMap.mainController.getAxis(RCAxis.leftY) * maxSpeed, RobotMap.mainController.getAxis(RCAxis.rightY) * maxSpeed);
	}
	
}
