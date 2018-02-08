package org.torc.mainrobot.robot;

public class ControlledStateMachine {
	
	private boolean initCalled = false;
	
	public final void callUpdate() {
		if (!initCalled) {
			initCalled = true;
			init();
		}
		execute();
	}
	
	protected void init() {
		
	}
	
	protected void execute() {
		
	}
}
