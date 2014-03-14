package quizzically.models;

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

	public String text() {
		return text;
	}
}
