package com.ftn.sotis.exceptions;

public class EntityAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public EntityAlreadyExistsException() {}

	public EntityAlreadyExistsException(String message) {
		super(message);
	}
}
