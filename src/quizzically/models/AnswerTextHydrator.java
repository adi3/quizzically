package quizzically.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Hydrates AnswerText objects from database ResultSet and dehydrates them to database
 * PreparedStatement.
 */
public class AnswerTextHydrator extends Hydrator {

	/**
	 * Populates an `AnswerText` object from a database query result set, extracting the
	 * `id`, `answer_id`, and `text` fields from the result set and using them to initialize
	 * the `AnswerText` object.
	 *
	 * @param rs ResultSet object that contains the database query results.
	 *
	 * Get its type as a ResultSet object, which is a table of data representing a database
	 * result set.
	 * Its type has methods for retrieving data from the current row, including getInt,
	 * getString, and others.
	 *
	 * @returns an instance of the `AnswerText` class, containing id, answerId, and text
	 * properties.
	 *
	 * Contain an integer id, an integer answerId, and a string text.
	 */
	@Override
	protected Model hydrate(ResultSet rs) throws SQLException {
		int id, answerId;
		String text;

		id = rs.getInt("id");
		answerId = rs.getInt("answer_id");
		text = rs.getString("text");

		return new AnswerText(id, answerId, text);
	}

	/**
	 * Populates a `PreparedStatement` with data from an `AnswerText` object, setting an
	 * integer value and a string value at specified offsets.
	 *
	 * @param stmt PreparedStatement object used to execute SQL queries.
	 *
	 * @param m model object, which is cast to `AnswerText` and used to retrieve the
	 * answer ID and text for dehydrating into the prepared statement.
	 *
	 * @param offset position in the `PreparedStatement` where the next parameter value
	 * should be set.
	 */
	@Override
	protected void dehydrate(PreparedStatement stmt, Model m, int offset)
			throws SQLException {
		AnswerText txt = (AnswerText) m;
		stmt.setInt(offset++, txt.answerId());
		stmt.setString(offset++, txt.text());
	}

}
