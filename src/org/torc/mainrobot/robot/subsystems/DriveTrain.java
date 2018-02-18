package org.torc.mainrobot.robot.subsystems;

import org.torc.mainrobot.tools.MathExtra;
import org.torc.mainrobot.tools.MotorControllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem {

	DifferentialDrive roboDrive;	
	
	TalonSRX rightMaster, rightSlave, leftMaster, leftSlave;

	PigeonIMU drivePigeon;

	PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
	
	Solenoid rightShifter;
	Solenoid leftShifter;
	
	double controllerDeadband = 0.02;
	
	double driveMaxOutput = 1;
	
	final double quickTurnConstant = 0.3;
	final double quickTurnSensitivity = 0.7;
	final double speedTurnSensitivity = 0.7;
	
	/**
	 * Maximum desired velocity for movement control
	 * (What full speed should be).
	 */
	final double maxVelocity = 3500;
	
	final double voltageRampRate = 0.2;
	
	private boolean shifterState = false;
	
	public enum DTSide {left, right}
	
	public DriveTrain(int rightMasterPort, int rightSlavePort, int leftMasterPort, int leftSlavePort, int pigeonPort) {
		rightMaster = new TalonSRX(rightMasterPort);
		rightSlave = new TalonSRX(rightSlavePort);
		leftMaster = new TalonSRX(leftMasterPort);
		leftSlave = new TalonSRX(leftSlavePort);
		// Config master Talons
		MotorControllers.TalonSRXConfig(rightMaster, 10, 0, 0, 0, 0.5, 0, 0);
		MotorControllers.TalonSRXConfig(leftMaster, 10, 0, 0, 0, 0.5, 0, 0);
		rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		
		rightMaster.configOpenloopRamp(voltageRampRate, 10);
		rightSlave.configOpenloopRamp(voltageRampRate, 10);
		leftMaster.configOpenloopRamp(voltageRampRate, 10);
		leftSlave.configOpenloopRamp(voltageRampRate, 10);
		
		/*
		// Flip left sensor
		leftMaster.setSensorPhase(false);
		*/
		// Flip right sensor
		leftMaster.setSensorPhase(true);
		
		// Set slaves as followers
		/*
		rightSlave.set(ControlMode.Follower, rightMaster.getDeviceID());
		leftSlave.set(ControlMode.Follower, leftMaster.getDeviceID());
		*/
		
		drivePigeon = new PigeonIMU(pigeonPort);	
		
		rightShifter = new Solenoid(0);
		leftShifter = new Solenoid(1);
		
		setShifters(false);
	}
	
	public boolean getShifters() {
		return shifterState;
	}
	
	public void setShifters(boolean shift) {
		shifterState = shift;
		rightShifter.set(shift);
		leftShifter.set(shift);
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
	
	public void haloDrive(double throttle, double wheel) {
		double driverThrottle = MathExtra.applyDeadband(throttle, 0.2);
		double driverWheel = MathExtra.applyDeadband(wheel, 0.2);
		
		double rightMotorOutput = 0;
		double leftMotorOutput = 0;

		// Halo Driver Control Algorithm
		if (Math.abs(driverThrottle) < quickTurnConstant) {
			rightMotorOutput = driverThrottle - driverWheel * quickTurnSensitivity;
			leftMotorOutput = driverThrottle + driverWheel * quickTurnSensitivity;
		} else {
			rightMotorOutput = driverThrottle - Math.abs(driverThrottle) * driverWheel * speedTurnSensitivity;
			leftMotorOutput = driverThrottle + Math.abs(driverThrottle) * driverWheel * speedTurnSensitivity;
		}
		rightMotorOutput = MathExtra.clamp(rightMotorOutput, -1, 1);
		leftMotorOutput = MathExtra.clamp(leftMotorOutput, -1, 1);
		
		/*
		rightMaster.set(ControlMode.Velocity, rightMotorOutput * maxVelocity);
		leftMaster.set(ControlMode.Velocity, leftMotorOutput * maxVelocity);
		*/
		SmartDashboard.putNumber("RightVelocity", rightMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("RightOutput", rightMotorOutput);
		
		rightMaster.set(ControlMode.PercentOutput, rightMotorOutput);
		rightSlave.set(ControlMode.PercentOutput, rightMotorOutput);
		
		leftMaster.set(ControlMode.PercentOutput, leftMotorOutput);
		leftSlave.set(ControlMode.PercentOutput, leftMotorOutput);

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
