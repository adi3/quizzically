package quizzically.models;

public class ChoiceResponse extends Response {
	private int id;

	/**
	 * Response to a multiple choice question
	 * @param id Answer id that was chosen
	 */
	public ChoiceResponse(int id) {
		this.id = id;
	}

	/**
	 * Get the id of the chosen answer
	 * @return Answer id that was chosen
	 */
	public int id() {
		return id;
	}
}
