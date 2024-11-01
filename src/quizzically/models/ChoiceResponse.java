package quizzically.models;

/**
 * Represents a response to a multiple choice question with a chosen answer id and
 * corresponding text.
 * It extends the Response Class and provides methods to retrieve and access this information.
 * The Class is designed to encapsulate the chosen answer's details.
 */
public class ChoiceResponse extends Response {
	private int id;
	private String text;

	/**
	 * Response to a multiple choice question
	 * @param id Answer id that was chosen
	 */
	public ChoiceResponse(int id) {
		Answer ans = Answer.retrieve(id);
		this.id = id;
		this.text = ans.text();
	}

	/**
	 * Get the id of the chosen answer
	 * @return Answer id that was chosen
	 */
	public int id() {
		return id;
	}

	/**
	 * Returns the string value of `text`.
	 *
	 * @returns the value of the `text` variable.
	 */
	public String text() {
		return text;
	}
}
