package quizzically.models;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;

import java.sql.PreparedStatement;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.SqlResult;

/**
 * Hydrates Answer objects from ResultSet data and dehydrates them to PreparedStatement
 * data in a database.
 */
public class AnswerHydrator extends Hydrator {
	/**
	 * Populates an `Answer` object with data from a database `ResultSet`. It retrieves
	 * the answer's ID, question ID, position, and correctness, as well as its associated
	 * `AnswerText` objects.
	 *
	 * @param rs ResultSet object returned from a database query, which provides data to
	 * populate the Answer object.
	 *
	 * Fetch the `ResultSet` object's column values by their names, which are used to
	 * retrieve data from the database.
	 *
	 * @returns an Answer object containing the answer's ID, question ID, position,
	 * correctness, and associated AnswerText objects.
	 *
	 * The returned output is an instance of the `Answer` class, with attributes: id,
	 * questionId, position, correct, and answerTexts.
	 */
	@Override
	public Model hydrate(ResultSet rs) 
			throws java.sql.SQLException{
		int id, questionId, position, correctInt;
		boolean correct;
		Answer answer;
		HashSet<AnswerText> answerTexts = new HashSet<AnswerText>();

		id = rs.getInt("id");
		questionId = rs.getInt("question_id");
		position = rs.getInt("position");
		correctInt = rs.getInt("correct");

		correct = correctInt == 1;

		AnswerText[] texts = AnswerText.retrieveByAnswerId(id);

		for (AnswerText text : texts) {
			answerTexts.add(text);
		}
		
		return new Answer(id, questionId, position, correct, answerTexts);
	}

	/**
	 * Serializes an `Answer` object into a `PreparedStatement`. It sets the question ID,
	 * position, and correctness flag, and saves associated `AnswerText` objects while
	 * deleting others.
	 *
	 * @param stmt PreparedStatement object used to execute SQL queries.
	 *
	 * Set the properties of the `stmt` object:
	 * - PreparedStatement type: a SQL statement with input parameters.
	 * - Properties: offset, int type, and set methods for setting integer values.
	 *
	 * @param m Model object, which is cast to an `Answer` object within the function.
	 *
	 * Cast to `Answer`, `m` has properties: questionId, position, correct, id, and answerTexts.
	 *
	 * @param offset starting position for inserting values into the PreparedStatement.
	 */
	@Override
	public void dehydrate(PreparedStatement stmt, Model m, int offset) 
			throws java.sql.SQLException {
		Answer answer = (Answer) m;
		stmt.setInt(offset++, answer.questionId());
		stmt.setInt(offset++, answer.position());
		stmt.setInt(offset++, answer.correct() ? 1 : 0);

		// FIXME update the answertexts
		if (answer.id() != -1) {
			AnswerText.deleteOthers(answer, answer.answerTexts());
		}
		for (AnswerText txt : answer.answerTexts()) {
			txt.save(true);
		}
	}
}
