package quizzically.models;

import java.util.Set;

public class AnswerText extends Model {
	private static final String TABLE = "answer_texts";
	private static final String[] ANSWER_TEXTS_COLUMNS = new String[]{"answer_id", "text"};

	private int answerId;
	private String text;

	/**
	 * Delete other answertexts for this answer
	 * @param answer The answer whose answertexts we care about
	 * @param texts The texts to save (not delete)
	 */
	public static void deleteOthers(Answer answer, Set<AnswerText> texts) {
		// FIXME implement me
		// pseudo-query to do
		// DELETE FROM TABLE WHERE `answer_id` = answer.id() AND `id` 
		// 		NOT IN (foreach text: texts, text.id)
	}

	protected AnswerText(int id, int answerId, String text) {
		super(id, TABLE, new AnswerTextHydrator());
		this.answerId = answerId;
		this.text = text;
	}

	public int answerId() {
		return answerId;
	}

	public String text() {
		return text;
	}

	@Override
	public String[] cols() {
		return ANSWER_TEXTS_COLUMNS;
	}

}
