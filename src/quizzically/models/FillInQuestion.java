package quizzically.models;

import java.util.List;

/**
 * Fill-in-the-blank question
 */
public class FillInQuestion extends TextQuestion {
	public static String BLANK = "__blank__";

	public FillInQuestion(int id, String text, List<Answer> answers) {
		super(id, text, answers);
		this.type = Question.TYPE_FILL_IN;
	}

	// TODO no special functionality...?
}
