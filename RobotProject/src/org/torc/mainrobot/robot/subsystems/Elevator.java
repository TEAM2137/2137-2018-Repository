package org.torc.mainrobot.robot.subsystems;

import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.robot.InheritedPeriodic;
import org.torc.mainrobot.robot.commands.Elevator_Home;
import org.torc.mainrobot.tools.MathExtra;
import org.torc.mainrobot.tools.MotorControllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

public class Elevator extends Subsystem implements InheritedPeriodic {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	
	public enum ElevatorPositions { floor, middle, climb, high }
	
	public ElevatorPositions elevatorPosition = ElevatorPositions.floor;
	
	private DigitalInput endstop;
	
	private TalonSRX elevator;
	
	public final static int maxSoftLimit = RobotMap.RobInfo.isPracticeBot()?28345:26726;//25297;
	
	public final static int posPerInch = RobotMap.RobInfo.isPracticeBot()?651:448;
	
	private boolean maxLimitTripped = false;
	private boolean minLimitTripped = false;
	
	private int targetPosition = 0;
	
	private boolean hasBeenHomed = false;
	
	Elevator_Home elevHomer;
	
	public Elevator(int talonPort, int endstopPort) {
		// Add to periodic list
		org.torc.mainrobot.robot.Robot.AddToPeriodic(this);
		
		elevator = new TalonSRX(talonPort);
		// Invert motor phase
		//elevator.setInverted(false);
		//elevator.setSensorPhase(true);
		
		//MotorControllers.TalonSRXConfig(elevator, 10, 0, 0, 0, 5, 0.01, 0);
		MotorControllers.TalonSRXConfig(elevator, 10, 0, 0, 0.58, 3, 0.01, 60);
		elevator.config_IntegralZone(0, 300, 10);
		
		endstop = new DigitalInput(endstopPort);
		
        elevator.configPeakOutputForward(1, 10);
        elevator.configPeakOutputReverse(-1, 10);
        
        elevator.configMotionCruiseVelocity(10000, 10);
        elevator.configMotionAcceleration(2500, 10);
	}
	
	public static int GetElevatorPositions(ElevatorPositions position) {
		int toReturn = 0;
		switch (position) {
			case floor:
				toReturn = 0;
				break;
			case middle:
				toReturn = RobotMap.RobInfo.isPracticeBot()?17769:20650;
				break;
			case climb:
				toReturn = 15792;
				break;
			case high:
				toReturn = 26840;
				break;
		}
		return toReturn;
	}
	
	/**
	 * Initializes the elevator for use. This will home, and arm the elevator for use.
	 * Do not call this from the same elevator subsystem constructor.
	 */	
	public void homeElevator() {
		if (hasBeenHomed) {
			deHome();
		}
		elevHomer = new Elevator_Home(this);
		elevHomer.start();
	}
	
	
	/**
	 * Sets the elevator's state to "unHomed", requiring 
	 * another homing to work again.
	 */
	public void deHome() {
		hasBeenHomed = false;
		targetPosition = 0;
		System.out.println("Elevator De-Homed!!");
	}
	
	public boolean getHomed() {
		return hasBeenHomed;
	}
	
	public void jogElevatorPerc(double controllerVal) {
		elevator.set(ControlMode.PercentOutput, MathExtra.clamp(controllerVal, (minLimitTripped ? 0 : -0.3), (maxLimitTripped ? 0 : 0.3)));
	}
	
	public void positionFind(ElevatorPositions position) {
		System.out.println("Finding position: " + position.name());
		if (!hasBeenHomed) {
			hasNotHomedAlert();
			return;
		}
		int targPos = GetElevatorPositions(position);
		targetPosition = targPos;
		elevatorPosition = position;
		elevator.set(ControlMode.MotionMagic, MathExtra.clamp(targPos, 0, maxSoftLimit));
	}
	
	public void zeroEncoder() {
		MotorControllers.TalonSRXSensorZero(elevator, 10, 0);
	}
	
	public void printEncoder() {
		//System.out.println(elevator.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("ElevatorEncoder", elevator.getSelectedSensorPosition(0));
	}
	
	public int getEncoder() {
		return elevator.getSelectedSensorPosition(0);
	}
	
	public boolean getEndstop() {
		return endstop.get();
	}
	
	/*
	public void setPosMagic(int pos) {
		elevator.set(ControlMode.MotionMagic, pos);
	}
	*/
	
	public void jogElevatorPos(double positionInc) {
		if (!hasBeenHomed) {
			hasNotHomedAlert();
			return;
		}
		targetPosition += positionInc;
		targetPosition = MathExtra.clamp(targetPosition, 0, maxSoftLimit);
		elevator.set(ControlMode.MotionMagic, targetPosition);
	}
	
	public void jogElevatorPosInc(int increment) {
		if (!hasBeenHomed) {
			hasNotHomedAlert();
			return;
		}
		elevatorPosition = Elevator.ElevatorPositions.values()[(int) MathExtra.clamp(elevatorPosition.ordinal() + increment, 0, Elevator.ElevatorPositions.values().length-1)];
		positionFind(elevatorPosition);
	}
	
	static void hasNotHomedAlert() {
		System.out.println("Cannot move Elevator; has not homed!!");
	}
	
	@Override
	protected void initDefaultCommand() {
	}
	
	@Override
	public void Periodic() {
		if (!hasBeenHomed && elevHomer != null && elevHomer.isFinished()) {
			System.out.println("Elevator Homed!!");
			elevHomer.free();
			elevHomer = null;
			hasBeenHomed = true;
		}
		// Print Encoders
		printEncoder();
		
		SmartDashboard.putNumber("ElevatorError", elevator.getSelectedSensorPosition(0) - targetPosition);
		//SmartDashboard.putNumber("ElevatorEncoder", elevator.getSelectedSensorPosition(0));
		//System.out.println("ElevatorEncoder " + elevator.getSelectedSensorPosition(0));
		SmartDashboard.putBoolean("ElevatorEndstop", endstop.get());
		
		SmartDashboard.putNumber("ElevatorVel", elevator.getSelectedSensorVelocity(0));
		//System.out.println("ElevatorVel " + elevator.getSelectedSensorVelocity(0));
	}
	
}
