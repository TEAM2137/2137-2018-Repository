package org.torc.mainrobot.robot.subsystems;

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
	
	public final static int maxSoftLimit = 25287;
	
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
		//elevator.setInverted(true);
		MotorControllers.TalonSRXConfig(elevator, RobotMap.Elev_TimeoutMs, RobotMap.Elev_SlotIdx, RobotMap.Elev_PIDLoopIdx, 0, 0.1, 0, 0);
		
		// Invert elev encoder
		elevator.setSensorPhase(false);
		
		endstop = new DigitalInput(endstopPort);
		cubeInput = new DigitalInput(1);
		
        elevator.configPeakOutputForward(1, RobotMap.Elev_TimeoutMs);
        elevator.configPeakOutputReverse(-1, RobotMap.Elev_TimeoutMs);
	}
	
	private static int GetElevatorPositions(ElevatorPositions position) {
		int toReturn = 0;
		switch(position) {
			case floor:
				toReturn = 2436;
				break;
			case middle:
				toReturn = 11745;
				break;
			case high:
				toReturn = 25287;
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
