package quizzically.exceptions;

/**
 * Extends the built-in Java Exception Class to provide a custom exception for
 * model-related errors.
 */
public class ModelException extends Exception {
	public ModelException(String message) {
		super(message);
	}
}
