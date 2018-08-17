package edu.usu.cs.mas.managedisaster.common;

public enum CoalitionStatus {
	INITIATING("Starts the coalition"),
	FORMING("Forming the coalition in accordence with the changes"),
	OPTIMIZING("Optimize the coalition by removing or adding agents"),
	EXECUTING("Executing the task of estingush fire"),
	CANCEL("Cancel the coalition because the best coalition win the task"),
	TERMINATE("After executing the task successfully, the coalition members get dismissed"),
	DISMISS("In the middle of the task execution, the coalition members decide to dismiss in the middle of the execution")
	;
	
	private String description;
	
	private CoalitionStatus(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
}
