package com.ftn.owpproject.exceptions;

public class Exceptions {
	
	public class EmailExistsException extends Exception {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EmailExistsException(String message) {
	        super(message);
	        //neka poruka
	    }
	}
	
}
