package org.torc.mainrobot.program;

import org.torc.mainrobot.program.AutonSelector.AutonPriority;
import org.torc.mainrobot.program.AutonSelector.StartPositions;
import org.torc.mainrobot.tools.CommandList;

import edu.wpi.first.wpilibj.DriverStation;

public class AutonDatabase {
	
	private static CommandList ComList;
	private static StartPositions StartPosition;
	private static AutonPriority AutonPri;
	
	private static char[] GameData;
	
	/**
	 * Gets the proper Auton routine in CLCommands into a givin CommandList.
	 * (Note: CLCommands are ADDED into the givin CommandList).
	 * 
	 * @param comList
	 * @param startPosition
	 * @param autonPri
	 */
	public static void GetAuton(CommandList cList, StartPositions sPosition, AutonPriority autonP) {
		ComList = cList;
		StartPosition = sPosition;
		AutonPri = autonP;
		String gData = DriverStation.getInstance().getGameSpecificMessage();
		GameData = new char[3];
		GameData[0] = gData.charAt(0);
		GameData[1] = gData.charAt(1);
		GameData[2] = gData.charAt(2);
		
		autonGetStart();
	}
	
	
	private static void autonGetStart() {
		System.out.println("GameData: " + GameData[0] + GameData[1] + GameData[2]);
		
		switch(StartPosition) {
			case left:
				if (GameData[0] == 'R') {
					
				}
				break;
			case center:
				break;
			case right:
				break;
		}
	}
	
	
}
