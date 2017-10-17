
package org.torc.mainrobot.robot;

import org.torc.mainrobot.robot.commands.ExampleCommand;
import org.torc.mainrobot.robot.subsystems.ExampleSubsystem;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	
	XboxController xController0 = new XboxController(0);

	Talon frontLeft, frontRight, rearLeft, rearRight;
	
	DoubleSolenoid shifter;
	
	RobotDrive myRobot;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		
		shifter = new DoubleSolenoid(0, 0, 1);
	    frontLeft = new Talon(0);
	    frontRight = new Talon(1);
	    rearLeft = new Talon (2);
	    rearRight = new Talon (3);
	    myRobot = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
	    System.out.println("CALLED ROBOTDRIVE CONSTRUCTOR");
	    myRobot.setExpiration(0.1);
	    Compressor compress = new Compressor(0);
	    // Set compressor status to automatically 
	    // refill the compressor tank when below 120psi
	    compress.setClosedLoopControl(true);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		
		myRobot.setSafetyEnabled(true);
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
		myRobot.tankDrive(xController0.getY(GenericHID.Hand.kLeft), xController0.getY(GenericHID.Hand.kRight));
		
		if (xController0.getBumper(Hand.kLeft)) {
			// Forward is Low Gear
			shifter.set(DoubleSolenoid.Value.kForward);
		}
		
		else if (xController0.getBumper(Hand.kRight)) {
			// Reverse is High Gear
			shifter.set(DoubleSolenoid.Value.kReverse);	
		}
		
		String soleState = "";
		
		switch (shifter.get()) {
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
		// Write to smartDashboard
		SmartDashboard.putString("Gear State", soleState);
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
