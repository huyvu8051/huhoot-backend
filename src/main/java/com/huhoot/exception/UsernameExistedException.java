package com.huhoot.exception;

public class UsernameExistedException extends Exception {
	public UsernameExistedException() {
		super();
	}

	public UsernameExistedException(String msg) {
		super(msg);
	}
}
