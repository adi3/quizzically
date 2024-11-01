package quizzically.exceptions;

/**
 * Extends the Exception class to represent a custom exception for handling invalid
 * responses.
 */
public class InvalidResponseException extends Exception {
	public InvalidResponseException(String message) {
		super(message);
	}
}
