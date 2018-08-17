package edu.usu.cs.mas.managedisaster.common;

public enum AgentStatus {
  SEARCHING(1, "S","Waiting for a task to be appeared"),
  IDENTIFYING_AND_ASSESSING_RISK(2, "IA","collecting historical and "
    + "recent data on existing, potential and perceived threats and hazards"),
  ESTIMATING_RESOURCES(3, "ER","Determine the specific capabilities and "
    + "activities to best address those risks"),
  FORMING_COALITIONS(4, "FC","figuring out the best way to use limited "
    + "resources to build capabilities"),
  TRAVELING(5, "T","Maneuver to the target"),
  TRAVELING_TO_FIRE_STATION(6, "TFS","Traveling to the fire station"),
  EXECUTING_TASKS(7, "ET","Execute the task assigned"),
  VALIDATING_THE_EXECUTION(8, "VE","Validate the execution work as intended"),
  REVIEWING_AND_UPDATING(9, "RU","Update all capabilities, "
    + "resources and plans. Risks and resources evolve"),
  FILLING_UP_CHEMICAL(10, "FUC","Agent move to a resource locator in order to fill its tank "
    + "with the chemicals to fight against the fire"),
  NECESSITY_OF_CHEMICAL(11, "NC","The agent identify its state as no more chemicals available, need "
  		+ "to move to a fire station in order to fill up its tanks");
  
	private final String abbrevation;
  private final String description;
  private final int statusId;

  private AgentStatus(int statusId, String abbrevation, String description) {
    this.statusId = statusId;
  	this.abbrevation = abbrevation;
  	this.description = description;
  }
  
  public int getStatusId() {
  	return statusId;
  }
  
  @Override
  public String toString(){
    return statusId +") "+ abbrevation +" : "+description;
  }
}
