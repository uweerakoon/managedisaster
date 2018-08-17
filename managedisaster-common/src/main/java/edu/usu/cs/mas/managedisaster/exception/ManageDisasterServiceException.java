package edu.usu.cs.mas.managedisaster.exception;

public class ManageDisasterServiceException extends RuntimeException{
  
  private static final long serialVersionUID = 4147600059052173353L;

  public ManageDisasterServiceException(String msg){
    super(msg);
  }
}
