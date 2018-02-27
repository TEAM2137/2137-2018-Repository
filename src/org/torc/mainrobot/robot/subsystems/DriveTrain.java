package org.torc.mainrobot.robot.subsystems;

import org.torc.mainrobot.tools.MathExtra;
import org.torc.mainrobot.tools.MotorControllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem {
	/**
	 * The amount of encoder ticks it takes 
	 * for the driveline to do a full rotation.
	 */
	public final int TicksPerRev = 3600;
	
	public final double WheelDiameterIn = 6;
	
	public final double VelFullHigh = 4800;
	public final double VelFullLow = 1240;
	
	private TalonSRX rightMaster, rightSlave, leftMaster, leftSlave;

	PigeonIMU drivePigeon;

	PigeonIMU.FusionStatus fusionStatus = new PigeonIMU.FusionStatus();
	
	Solenoid rightShifter;
	Solenoid leftShifter;
	
	double controllerDeadband = 0.02;
	
	double driveMaxOutput = 1;
	
	final double quickTurnConstant = 0.3;
	final double quickTurnSensitivity = 0.7;
	final double speedTurnSensitivity = 0.7;
	
	final double voltageRampRate = 0.2;
	
	// True is low gear
	private boolean shifterState = false;
	
	public enum DTSide {left, right}
	
	public DriveTrain(int rightMasterPort, int rightSlavePort, int leftMasterPort, int leftSlavePort, int pigeonPort) {
		rightMaster = new TalonSRX(rightMasterPort);
		rightSlave = new TalonSRX(rightSlavePort);
		leftMaster = new TalonSRX(leftMasterPort);
		leftSlave = new TalonSRX(leftSlavePort);
		// Config master Talons
		MotorControllers.TalonSRXConfig(rightMaster, 10, 0, 0, 0, 0, 0, 0);
		MotorControllers.TalonSRXConfig(leftMaster, 10, 0, 0, 0, 0, 0, 0);
		rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		
		rightMaster.configOpenloopRamp(voltageRampRate, 10);
		rightSlave.configOpenloopRamp(voltageRampRate, 10);
		leftMaster.configOpenloopRamp(voltageRampRate, 10);
		leftSlave.configOpenloopRamp(voltageRampRate, 10);
		
		// Flip left sensor
		leftMaster.setSensorPhase(true);
		
		// Flip right sensor
		//rightMaster.setSensorPhase(true);
		
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
		
		// Remember: shifterState as true means low gear
		double kFVal = shifterState?0.82:0.21;
		rightMaster.config_kF(0, kFVal, 10);
		leftMaster.config_kF(0, kFVal, 10);
		
		double kPVal = shifterState?0.83:0.0802;
		rightMaster.config_kP(0, kPVal, 10);
		leftMaster.config_kP(0, kPVal, 10);
		
		System.out.println("ShifterVal: " + shift);
	}
	
	public void setVelocity(double leftSide, double rightSide) {
		leftSide = MathExtra.clamp(leftSide, -1, 1) * (shifterState?VelFullLow:VelFullHigh);
		leftMaster.set(ControlMode.Velocity, leftSide);
		if (leftSlave.getControlMode() != ControlMode.Follower) {
			leftSlave.set(ControlMode.Follower, leftMaster.getDeviceID());
		}
		
		rightSide = MathExtra.clamp(rightSide, -1, 1) * (shifterState?VelFullLow:VelFullHigh);
		rightMaster.set(ControlMode.Velocity, rightSide);
		if (rightSlave.getControlMode() != ControlMode.Follower) {
			rightSlave.set(ControlMode.Follower, rightMaster.getDeviceID());
		}
	}
	
	public void setPercVBus(double leftSide, double rightSide) {
		leftMaster.set(ControlMode.PercentOutput, leftSide);
		leftSlave.set(ControlMode.PercentOutput, leftSide);
		
		rightMaster.set(ControlMode.PercentOutput, rightSide);
		rightSlave.set(ControlMode.PercentOutput, rightSide);
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
		//setPercVBus(leftMotorOutput, rightMotorOutput);
		setVelocity(leftMotorOutput, rightMotorOutput);
		
		// Read encoders
		SmartDashboard.putNumber("leftEncoder", leftMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("rightEncoder", rightMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("RightEVel", rightMaster.getSelectedSensorVelocity(0));
		
		SmartDashboard.putNumber("currSpeed", rightMaster.getSelectedSensorVelocity(0));
		
	}
	
	public double getGyroHeader() {
		// Negative to keep the "Negative angle turns left" code.
		double header = -drivePigeon.getFusedHeading(fusionStatus);
		
		/*
		// All of this division/checking code is to keep the angle wrapped around in -360 - 360deg increments
		double div = header / 360;
		if (div > 1) {
			header -= (360 * Math.floor(div));
		}
		else if (div < -1) {
			header += (360 * Math.floor(Math.abs(div)));
		}
		*/
		return header;
	}
	
	public void zeroGyro() {
		drivePigeon.setFusedHeading(0.0, 10);
	}
	
	public int getEncoder(DTSide side) {
		int retVal = 0;
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
