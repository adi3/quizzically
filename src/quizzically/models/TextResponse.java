package quizzically.models;

public class TextResponse extends Response {
	private String text;

	public TextResponse(String text) {
		this.text = text;
	}

	/**
	 * Get the text of the response
	 * @return the text
	 */
	public String text() {
		return text;
	}
}
