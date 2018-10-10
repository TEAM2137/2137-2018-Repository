package org.torc.mainrobot.robot.subsystems;

import org.torc.mainrobot.robot.InheritedPeriodic;
import org.torc.mainrobot.robot.Robot;
import org.torc.mainrobot.tools.MathExtra;
import org.torc.mainrobot.tools.MotorControllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem implements InheritedPeriodic {
	/**
	 * The amount of encoder ticks it takes 
	 * for the driveline to do a full rotation.
	 */
	public final int TicksPerRev = 3600;
	
	public final int TicksPerInch = 189;
	
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
	
	final double voltageRampRateHigh = 0.4;
	
	// True is low gear
	private boolean shifterState = false;
	
	// If driving in auton
	private boolean autonDrive = false;
	
	public enum DTSide {left, right}
	
	public DriveTrain(int rightMasterPort, int rightSlavePort, int leftMasterPort, int leftSlavePort, int pigeonPort) {
		// Add to periodic list
		Robot.AddToPeriodic(this);
		
		rightMaster = new TalonSRX(rightMasterPort);
		rightSlave = new TalonSRX(rightSlavePort);
		leftMaster = new TalonSRX(leftMasterPort);
		leftSlave = new TalonSRX(leftSlavePort);
		// Config master Talons
		MotorControllers.TalonSRXConfig(rightMaster, 10, 0, 0, 0, 0, 0, 0);
		MotorControllers.TalonSRXConfig(leftMaster, 10, 0, 0, 0, 0, 0, 0);
		rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		
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
		
		setShifters(true); //false
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
		
		double kPVal = shifterState?0.83:0.1604;//0.83:0.0802;
		rightMaster.config_kP(0, kPVal, 10);
		leftMaster.config_kP(0, kPVal, 10);
		
		/*
		// Old values
		double kFVal = shifterState?0.82:0.21;
		rightMaster.config_kF(0, kFVal, 10);
		leftMaster.config_kF(0, kFVal, 10);
		
		double kPVal = shifterState?0.83:0.0802;
		rightMaster.config_kP(0, kPVal, 10);
		leftMaster.config_kP(0, kPVal, 10);
		*/
		
		configVRamp();
		
		System.out.println("ShifterVal: " + shift + ". kFVal: " + kFVal);
	}
	
	public void setVelocity(double leftSide, double rightSide) {
		
		SmartDashboard.putNumber("LeftVelInput", leftSide);
		SmartDashboard.putNumber("RightVelInput", rightSide);
		
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
	
	public void haloDrive(double throttle, double wheel, boolean squared) {
		
		double driverThrottle = MathExtra.clamp(MathExtra.applyDeadband(throttle, 0.15), -1, 1);
		double driverWheel = MathExtra.clamp(MathExtra.applyDeadband(wheel, 0.15), -1, 1);
		
		if (squared) {
			driverThrottle = (Math.pow(driverThrottle, 2) * (driverThrottle<0?-1:1));
			driverWheel = (Math.pow(driverWheel, 2) * (driverWheel<0?-1:1));
		}
		
		double rightMotorOutput = 0;
		double leftMotorOutput = 0;

		// Halo Driver Control Algorithm
		if (Math.abs(driverThrottle) < quickTurnConstant) {
			if (Math.abs(driverWheel) > 0.2) {
				zeroVRamp();
			}
			rightMotorOutput = driverThrottle - driverWheel * quickTurnSensitivity;
			leftMotorOutput = driverThrottle + driverWheel * quickTurnSensitivity;
		} else {
			configVRamp();
			rightMotorOutput = driverThrottle - Math.abs(driverThrottle) * driverWheel * speedTurnSensitivity;
			leftMotorOutput = driverThrottle + Math.abs(driverThrottle) * driverWheel * speedTurnSensitivity;
		}
		
		// If low gear, drive percVBus
		if (getShifters()) {
			setPercVBus(leftMotorOutput, rightMotorOutput);
		}
		// High gear: drive velocity
		else {
			setVelocity(leftMotorOutput, rightMotorOutput);
		}
		
	}
	
	private void zeroVRamp() {
		rightMaster.configOpenloopRamp(0, 10);
		rightSlave.configOpenloopRamp(0, 10);
		leftMaster.configOpenloopRamp(0, 10);
		leftSlave.configOpenloopRamp(0, 10);
		rightMaster.configClosedloopRamp(0, 10);
		rightSlave.configClosedloopRamp(0, 10);
		leftMaster.configClosedloopRamp(0, 10);
		leftSlave.configClosedloopRamp(0, 10);
	}
	
	private void configVRamp() {
		// If in auton, ramping is expected to be managed by commands, so disable vRamp
		if (autonDrive) {
			rightMaster.configOpenloopRamp(0, 10);
			rightSlave.configOpenloopRamp(0, 10);
			leftMaster.configOpenloopRamp(0, 10);
			leftSlave.configOpenloopRamp(0, 10);
			rightMaster.configClosedloopRamp(0, 10);
			rightSlave.configClosedloopRamp(0, 10);
			leftMaster.configClosedloopRamp(0, 10);
			leftSlave.configClosedloopRamp(0, 10);
		}
		else {
			double kRampVal = shifterState?(voltageRampRateHigh * 4):voltageRampRateHigh;
			rightMaster.configOpenloopRamp(kRampVal, 10);
			rightSlave.configOpenloopRamp(kRampVal, 10);
			leftMaster.configOpenloopRamp(kRampVal, 10);
			leftSlave.configOpenloopRamp(kRampVal, 10);
			rightMaster.configClosedloopRamp(kRampVal, 10);
			rightSlave.configClosedloopRamp(kRampVal, 10);
			leftMaster.configClosedloopRamp(kRampVal, 10);
			leftSlave.configClosedloopRamp(kRampVal, 10);
		}
	}
	
	public double getGyroHeader() {
		// Negative to keep the "Negative angle turns left" code.		
		return -drivePigeon.getFusedHeading(fusionStatus);
	}
	
	public void zeroGyro() {
		drivePigeon.setYaw(0, 10);
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
	
	public void setAutonDriving(boolean val) {
		autonDrive = val;
		configVRamp();
	}
	
	@Override
	public void Periodic() {
		SmartDashboard.putNumber("DriveTrainGyro", getGyroHeader());
		
		double[] driveMotors = {leftMaster.getMotorOutputPercent(), rightMaster.getMotorOutputPercent(), 
								leftSlave.getMotorOutputPercent(), rightSlave.getMotorOutputPercent()};
		
		SmartDashboard.putNumberArray("RobotDrive Motors", driveMotors);
		
		// Read encoders
		SmartDashboard.putNumber("leftEncoder", leftMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("rightEncoder", rightMaster.getSelectedSensorPosition(0));
		
		SmartDashboard.putNumber("rightSpeed", rightMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("leftSpeed", leftMaster.getSelectedSensorVelocity(0));
		
		SmartDashboard.putBoolean("lowGear", shifterState);
	}
	
	@Override
	protected void initDefaultCommand() {
	}

}
