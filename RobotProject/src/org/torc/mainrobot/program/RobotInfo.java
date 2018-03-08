package org.torc.mainrobot.program;

import edu.wpi.first.wpilibj.DigitalInput;

public class RobotInfo {
	
	DigitalInput jumper;
	
	private boolean practiceBot = false;
	
	public RobotInfo(int jumperPort) {
		jumper = new DigitalInput(jumperPort);
		
		practiceBot = !jumper.get();
	}
	
	public boolean isPracticeBot() {
		return practiceBot;
	}
}
