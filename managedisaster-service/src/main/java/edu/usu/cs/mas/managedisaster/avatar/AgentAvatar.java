package edu.usu.cs.mas.managedisaster.avatar;

import java.awt.Color;
import java.awt.Graphics2D;

import edu.usu.cs.mas.managedisaster.common.AgentRole;
import edu.usu.cs.mas.managedisaster.common.AgentStatus;
import edu.usu.cs.mas.managedisaster.common.Chemical;
import edu.usu.cs.mas.managedisaster.entity.BuildingEntity;
import edu.usu.cs.mas.managedisaster.entity.FireEntity;
import edu.usu.cs.mas.managedisaster.entity.IntersectionEntity;
import edu.usu.cs.mas.managedisaster.player.AgentPlayer;
import edu.usu.cs.mas.managedisaster.service.util.Config;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.MutableInt2D;

public class AgentAvatar extends OvalPortrayal2D{

  private static final long serialVersionUID = -590918026353130923L;
  private static final String ENABLE_ID = "edu.usu.cs.mas.managedisaster.agentid.enable";

  private AgentPlayer agentPlayer;
  private Config config;
  
  public AgentAvatar(AgentPlayer agentPlayer, Config config){
    this.agentPlayer = agentPlayer;
    this.config = config;
  }
  
  @Override
  public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
//  	AgentAvatar agentAvatar = (AgentAvatar) object;
//    AgentPlayer agentPlayer = agentAvatar.agentPlayer;
//  paint = agentPlayer.getColor();
    paint = getColor(); 
    super.draw(object, graphics, info);
    
    if(config.getBoolean(ENABLE_ID)) {
	    int strx = (int)info.draw.x;
	    int stry = (int)(info.draw.y);
	    graphics.drawString(getLabel(), strx, stry);
    }
  }
  
  private Color getColor() {
    AgentStatus agentStatus = agentPlayer.getStatus();
    switch(agentStatus) {
      case SEARCHING: return Color.YELLOW;
      case IDENTIFYING_AND_ASSESSING_RISK: return Color.ORANGE;
      case ESTIMATING_RESOURCES: return Color.GRAY;
      case FORMING_COALITIONS: return Color.BLUE;
      case TRAVELING: return Color.BLACK;
      case TRAVELING_TO_FIRE_STATION: return Color.MAGENTA;
      case EXECUTING_TASKS: return Color.GREEN;
      case VALIDATING_THE_EXECUTION: return Color.CYAN;
      case REVIEWING_AND_UPDATING: return Color.LIGHT_GRAY;
      case FILLING_UP_CHEMICAL: return Color.PINK;
      case NECESSITY_OF_CHEMICAL: return Color.RED;
      default: return Color.WHITE; 
    }
  }
  
  private String getLabel() {
    String label = null;
    AgentStatus agentStatus = agentPlayer.getStatus();
    switch(agentStatus) {
      case SEARCHING: label = "S"; break;
      case IDENTIFYING_AND_ASSESSING_RISK: label = "IA"; break;
      case ESTIMATING_RESOURCES: label = "ER"; break;
      case FORMING_COALITIONS: label = "FC"; break;
      case TRAVELING: label = "T"; break;
      case TRAVELING_TO_FIRE_STATION: label = "TFS"; break;
      case EXECUTING_TASKS: label = "ET"; break;
      case VALIDATING_THE_EXECUTION: label = "VE"; break;
      case FILLING_UP_CHEMICAL: label = "FUC"; break;
      case NECESSITY_OF_CHEMICAL: label = "NC"; break;
      default:  label = ""; 
    }
    return label += agentPlayer.getId();
  }

	public AgentPlayer getAgentPlayer() {
		return agentPlayer;
	}

	public void setAgentPlayer(AgentPlayer agentPlayer) {
		this.agentPlayer = agentPlayer;
	}
  
	public Long getId() {
		return agentPlayer.getId();
	}
	
	public String getName() {
		return agentPlayer.getAgentModel().getName();
	}
	
	public AgentStatus getStatus() {
		return agentPlayer.getStatus();
	}
	
	public AgentRole getRole() {
		return agentPlayer.getAgentModel().getRole();
	}
	
	public double getSpeed() {
		return agentPlayer.getSpeed();
	}
	
	public Long getVicinity() {
		return agentPlayer.getVicinity();
	}
	
	public Chemical getChemical() {
		return agentPlayer.getChemical();
	}
	
	public Double getCheAmt() {
		return agentPlayer.getChemicalAmount();
	}
	
	public Long getMinFirePxty() {
		return agentPlayer.getMinimumFireProximity();
	}
	
	public Integer getSqtPrs() {
		return agentPlayer.getSquirtPressure();
	}
	
	public FireEntity getFire() {
		return agentPlayer.getAgentModel().getFire();
	}
	
	public BuildingEntity getBuilding() {
		FireEntity fire = agentPlayer.getAgentModel().getFire();
		return fire == null ? null : fire.getBurningBuilding();
	}
	
	public MutableInt2D getTgtLoc() {
		return agentPlayer.getTargetLocation();
	}
	
	public IntersectionEntity getTgtInt() {
		return agentPlayer.getTargetIntersection();
	}
	
	public IntersectionEntity getSrcInt() {
		return agentPlayer.getSourceIntersection();
	}
}
