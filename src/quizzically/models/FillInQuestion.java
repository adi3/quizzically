package quizzically.models;

/**
 * Fill-in-the-blank question
 */
public class FillInQuestion extends TextQuestion {
	public static String BLANK = "__blank__";

	public FillInQuestion(int id, String text) {
		super(id, text);
		this.type = Question.TYPE_FILL_IN;
	}

	// TODO no special functionality...?
}