package org.torc.mainrobot.robot.subsystems;

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
