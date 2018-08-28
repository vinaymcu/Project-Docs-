package com.acn.avs.unicast.exception;

/**
 * 
 * @author Anand.Jha
 *
 */
public class UnicastServiceInitializeException extends RuntimeException{
	
	static final long serialVersionUID = -7037807690745766939L;
	
	/**
	 * UnicastServiceInitializeException constructor
	 * @param message
	 */
	public UnicastServiceInitializeException(String message) {
        super(message);
    }
}
