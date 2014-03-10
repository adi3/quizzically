package quizzically.models;

import java.util.List;
import java.util.SortedMap;

/**
 * Fill-in-the-blank question
 */
public class FillInQuestion extends TextQuestion {
	public static String BLANK = "__blank__";

	public FillInQuestion(int id, String text, SortedMap<Integer, Answer> orderedAnswers) {
		super(id, text, orderedAnswers);
		this.type = Question.TYPE_FILL_IN;
	}

	// TODO no special functionality...?
}
