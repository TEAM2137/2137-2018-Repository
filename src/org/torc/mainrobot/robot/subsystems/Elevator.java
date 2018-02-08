package org.torc.mainrobot.robot.subsystems;

import org.torc.mainrobot.program.ButtonMap;
import org.torc.mainrobot.program.RobotMap;
import org.torc.mainrobot.robot.InheritedPeriodic;
import org.torc.mainrobot.robot.commands.elevator.Elevator_Home;
import org.torc.mainrobot.tools.MathExtra;
import org.torc.mainrobot.tools.MotorControllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;

public class Elevator extends Subsystem implements InheritedPeriodic {
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	
	public enum ElevatorPositions { floor, middle, high }
	
	public ElevatorPositions elevatorPosition = ElevatorPositions.floor;
	
	public DigitalInput endstop, cubeInput;
	
	private TalonSRX elevator;
	
	public static int maxSoftLimit = 155880;
	
	private boolean maxLimitTripped = false;
	private boolean minLimitTripped = false;
	
	int targetPosition = 0;
	
	boolean hasBeenHomed = false;
	
	Elevator_Home elevHomer;
	
	public Elevator() {
		// Add to periodic list
		org.torc.mainrobot.robot.Robot.AddToPeriodic(this);
		
		elevator = new TalonSRX(3);
		// Invert motor phase
		//elevator.setInverted(true);
		MotorControllers.TalonSRXConfig(elevator, RobotMap.Elev_TimeoutMs, RobotMap.Elev_SlotIdx, RobotMap.Elev_PIDLoopIdx);
		
		endstop = new DigitalInput(0);
		cubeInput = new DigitalInput(1);
		
        elevator.configPeakOutputForward(0.5, RobotMap.Elev_TimeoutMs);
        elevator.configPeakOutputReverse(-0.5, RobotMap.Elev_TimeoutMs);
	}
	
	
	/**
	 * Initializes the elevator for use. This will home, and arm the elevator for use.
	 * Do not call this from the same elevator subsystem constructor.
	 */
	/*
	public void initElevator() {
		try {
			Elevator_Init initGroup = new Elevator_Init(this, buttonMap);
			initGroup.start();
		}
		catch (IllegalArgumentException exception) {
			System.out.println("Cannot call initElevator; " + exception.getMessage());
		}
	}
	*/
	
	private static int GetElevatorPositions(ElevatorPositions position) {
		int toReturn = 0;
		switch(position) {
			case floor:
				toReturn = 35695;
				break;
			case middle:
				toReturn = 87736;
				break;
			case high:
				toReturn = 138080;
				break;
		}
		return toReturn;
	}
	
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
		System.out.println("Elevator De-Homed!!");
	}
	
	public boolean getHomed() {
		return hasBeenHomed;
	}
	
	public void jogElevatorPerc(double controllerVal) {
		elevator.set(ControlMode.PercentOutput, MathExtra.clamp(controllerVal, (minLimitTripped ? 0 : -0.3), (maxLimitTripped ? 0 : 0.3)));
	}
	
	public void positionFind(ElevatorPositions position) {
		if (!hasBeenHomed) {
			hasNotHomedAlert();
			return;
		}
		int targPos = GetElevatorPositions(position);
		targetPosition = targPos;
		elevatorPosition = position;
		elevator.set(ControlMode.Position, MathExtra.clamp(targPos, 0, maxSoftLimit));
	}
	
	public void zeroEncoder() {
		MotorControllers.TalonSRXSensorZero(elevator, RobotMap.Elev_TimeoutMs, RobotMap.Elev_PIDLoopIdx);
	}
	
	public void printEncoder() {
		//System.out.println(elevator.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("ElevatorEncoder", elevator.getSelectedSensorPosition(0));
	}
	
	public int getEncoder() {
		return elevator.getSelectedSensorPosition(0);
	}
	
	public void jogElevatorPos(double positionInc) {
		if (!hasBeenHomed) {
			hasNotHomedAlert();
			return;
		}
		targetPosition += positionInc;
		targetPosition = MathExtra.clamp(targetPosition, 0, maxSoftLimit);
		elevator.set(ControlMode.Position, targetPosition);
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
	
	/*
	void checkSoftLimits() {
		// Check for position max
		if (elevator.getSelectedSensorPosition(0) >= 155880) {
			// Reposition to keep below the gear
			positionElevator(155000);
			maxLimitTripped = true;
		}
		else {
			maxLimitTripped = false;
		}
		// Check for position min
		if (hasBeenHomed) {
			if (elevator.getSelectedSensorPosition(0) <= -1000) {
				// Reposition to keep below the gear
				positionElevator(0);
				maxLimitTripped = true;
			}
			else {
				maxLimitTripped = false;
			}
		}
	}
	*/
	
	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
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

	}
	
}
