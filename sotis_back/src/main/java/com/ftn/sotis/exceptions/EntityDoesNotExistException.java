package com.ftn.sotis.exceptions;

public class EntityDoesNotExistException extends Exception {

	private static final long serialVersionUID = 1L;

	public EntityDoesNotExistException() {}
	
	public EntityDoesNotExistException(String message){
		super(message);
	}
}
