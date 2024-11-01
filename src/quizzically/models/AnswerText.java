package quizzically.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import quizzically.lib.MySql;
import quizzically.lib.QueryBuilder;

/**
 * Represents a text answer in a database, providing methods for CRUD operations and
 * managing related answer texts.
 */
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
	
	/**
	 * Retrieves an array of `AnswerText` objects from a database based on the provided
	 * `answerId`. It uses a query builder to construct a SQL query with a constraint for
	 * the specified `answerId` and then hydrates the results into `AnswerText` objects.
	 *
	 * @param answerId identifier used in the database query to retrieve AnswerText objects
	 * matching a specific answer.
	 *
	 * @returns an array of `AnswerText` objects, matching the specified answer ID.
	 */
	public static AnswerText[] retrieveByAnswerId(int answerId) {
		MySql sql = MySql.getInstance();
		QueryBuilder qb = QueryBuilder.selectInstance(TABLE, COLUMNS);
		Model[] models;
		qb.addConstraint("answer_id", QueryBuilder.Operator.EQUALS, answerId);
		models = sql.getMany(qb, new AnswerTextHydrator());
		return Arrays.copyOf(models, models.length, AnswerText[].class);
	}

	/**
	 * Creates a new `AnswerText` object, initializes it with the provided answer ID and
	 * text, and saves it to the database with an ID of -1. The object is then returned.
	 *
	 * @param answer identifier for the answer being created, used to initialize the
	 * `AnswerText` object's `answer.id()` field.
	 *
	 * @param text text content associated with the answer being created.
	 *
	 * @returns an instance of `AnswerText` with the specified attributes.
	 */
	public static AnswerText create(Answer answer, String text) {
		AnswerText at = new AnswerText(-1, answer.id(), text);
		at.save(true);
		return at;
	}

	protected AnswerText(int id, int answerId, String text) {
		super(id, TABLE, new AnswerTextHydrator());
		this.answerId = answerId;
		this.text = text;
	}

	/**
	 * Returns an integer representing the answer ID.
	 * The value is directly retrieved from the `answerId` variable.
	 * It does not perform any calculations or operations.
	 *
	 * @returns the value of the `answerId` variable.
	 */
	public int answerId() {
		return answerId;
	}

	/**
	 * Returns a string representation of the `text` variable.
	 *
	 * @returns the value of the `text` field.
	 */
	public String text() {
		return text;
	}

	/**
	 * Returns an array of column names, likely used in a database or data storage context.
	 * The `COLUMNS` variable is assumed to be a predefined array of column names. The
	 * function overrides a method, indicating it's part of an interface or abstract class.
	 *
	 * @returns an array of strings representing the column names.
	 */
	@Override
	public String[] cols() {
		return COLUMNS;
	}

}
