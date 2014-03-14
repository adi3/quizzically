package quizzically.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import quizzically.lib.MySql;
import quizzically.lib.QueryBuilder;

public class AnswerText extends Model {
	private static final String TABLE = "answer_texts";
	private static final String[] COLUMNS = new String[]{"answer_id", "text"};

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
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.deleteInstance(TABLE, COLUMNS);
		ArrayList<Integer> keepIds = new ArrayList<Integer>();

		int i = 0;
		for (AnswerText text : texts) {
			int id = text.id();
			// only delete texts already in db
			if (id != -1) {
				keepIds.add(id);
			}
		}

		qb.addConstraint("answer_id", QueryBuilder.Operator.EQUALS, answer.id());
		if (keepIds.size() > 0) {
			qb.addConstraint("id", QueryBuilder.Operator.NOT_IN, keepIds.toArray(new Integer[0]));
		}

		sql.delete(qb);
	}
	
	public static AnswerText[] retrieveByAnswerId(int answerId) {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, COLUMNS);
		Model[] models;
		qb.addConstraint("answer_id", QueryBuilder.Operator.EQUALS, answerId);
		models = sql.getMany(qb, new AnswerTextHydrator());
		return Arrays.copyOf(models, models.length, AnswerText[].class);
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
		return COLUMNS;
	}

}
