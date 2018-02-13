package org.torc.mainrobot.robot.subsystems;

import org.torc.mainrobot.tools.MathExtra;
import org.torc.mainrobot.tools.MotorControllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem {

	DifferentialDrive roboDrive;	
	
	TalonSRX rightMaster, rightSlave, leftMaster, leftSlave;

	PigeonIMU drivePigeon;

	PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
	
	double controllerDeadband = 0.02;
	
	double driveMaxOutput = 1;
	
	public enum DTSide {left, right}
	
	public DriveTrain(int rightMasterPort, int rightSlavePort, int leftMasterPort, int leftSlavePort, int pigeonPort) {
		
		rightMaster = new TalonSRX(rightMasterPort);
		rightSlave = new TalonSRX(rightSlavePort);
		leftMaster = new TalonSRX(leftMasterPort);
		leftSlave = new TalonSRX(leftSlavePort);
		// Config master Talons
		MotorControllers.TalonSRXConfig(rightMaster, 10, 0, 0, 0, 0.1, 0, 0);
		MotorControllers.TalonSRXConfig(leftMaster, 10, 0, 0, 0, 0.1, 0, 0);
		rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		
		// Flip left sensor
		leftMaster.setSensorPhase(false);
		
		// Set slaves as followers
		rightSlave.set(ControlMode.Follower, rightMaster.getDeviceID());
		leftSlave.set(ControlMode.Follower, leftMaster.getDeviceID());
		
		drivePigeon = new PigeonIMU(pigeonPort);	
	}
	
	public void setVelocity(double leftSide, double rightSide) {
		leftMaster.set(ControlMode.Velocity, leftSide);
		rightMaster.set(ControlMode.Velocity, rightSide);
	}
	
	public void setPercVBus(double leftSide, double rightSide) {
		leftMaster.set(ControlMode.PercentOutput, leftSide);
		rightMaster.set(ControlMode.PercentOutput, rightSide);
		// Read encoders
		SmartDashboard.putNumber("leftEncoder", leftMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("rightEncoder", rightMaster.getSelectedSensorPosition(0));
	}
	
	  /**
	 * @param xSpeed
	 * @param zRotation
	 * @param squaredInputs
	 * This method is meant to be used to drive the DriveTrain with an arcadeDrive control, just like the one
	 * in the standard DifferentialDrive class.
	 * (The code for this method is adopted from the original method in DifferentialDrive in order to use it for
	 * our TalonSRX drive setup.)
	 */
	public void arcadeDrive(double xSpeed, double zRotation, boolean squaredInputs) {
		xSpeed = MathExtra.clamp(xSpeed, -1, 1);
		xSpeed = MathExtra.applyDeadband(xSpeed, controllerDeadband);

		zRotation = MathExtra.clamp(zRotation, -1, 1);
		zRotation = MathExtra.applyDeadband(zRotation, controllerDeadband);

		// Square the inputs (while preserving the sign) to increase fine control
		// while permitting full power.
		if (squaredInputs) {
			xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
			zRotation = Math.copySign(zRotation * zRotation, zRotation);
		}

		double leftMotorOutput;
		double rightMotorOutput;

		double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

		if (xSpeed >= 0.0) {
			// First quadrant, else second quadrant
			if (zRotation >= 0.0) {
				leftMotorOutput = maxInput;
				rightMotorOutput = xSpeed - zRotation;
			} else {
				leftMotorOutput = xSpeed + zRotation;
				rightMotorOutput = maxInput;
			}
		} 
		else {
			// Third quadrant, else fourth quadrant
			if (zRotation >= 0.0) {
				leftMotorOutput = xSpeed + zRotation;
				rightMotorOutput = maxInput;
			} else {
				leftMotorOutput = maxInput;
				rightMotorOutput = xSpeed - zRotation;
			}
		}
		
		leftMaster.set(ControlMode.PercentOutput, MathExtra.clamp(leftMotorOutput, -1, 1) * driveMaxOutput);
		rightMaster.set(ControlMode.PercentOutput, MathExtra.clamp(rightMotorOutput, -1, 1) * driveMaxOutput);
	}
	
	public double getGyroHeader() {
		double header = drivePigeon.getFusedHeading(fusionStatus);
		
		// All of this division/checking code is to keep the angle wrapped around in -360 - 360deg increments
		double div = header / 360;
		if (div > 1) {
			header -= (360 * Math.floor(div));
		}
		else if (div < -1) {
			header += (360 * Math.floor(Math.abs(div)));
		}
		return header;
	}
	
	public void zeroGyro() {
		drivePigeon.setFusedHeading(0.0, 10);
	}
	
	public double getEncoder(DTSide side) {
		double retVal = 0;
		switch (side) {
			case left:
				retVal = leftMaster.getSelectedSensorPosition(0);
				break;
			case right:
				retVal = rightMaster.getSelectedSensorPosition(0);
				break;
		}
		return retVal;
	}
	
	public void zeroEncoder(DTSide side) {
		switch (side) {
		case left:
			leftMaster.setSelectedSensorPosition(0, 0, 10);
			break;
		case right:
			rightMaster.setSelectedSensorPosition(0, 0, 10);
			break;
	}
	}
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
	}
	
}
