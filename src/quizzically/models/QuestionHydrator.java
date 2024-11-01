package quizzically.models;

import java.sql.ResultSet;
import java.util.SortedMap;

import java.sql.PreparedStatement;

/**
 * Extends the Hydrator class, implementing data hydration and dehydration for Question
 * objects.
 */
public class QuestionHydrator extends Hydrator {
	/**
	 * Populates a `Question` object from a database `ResultSet`, retrieving answers
	 * associated with the question based on its ID.
	 *
	 * @param rs ResultSet object from which data is retrieved to populate the `Question`
	 * object.
	 *
	 * Fetches data from a database ResultSet.
	 *
	 * @returns an instance of the `Question` class, populated with data from the `ResultSet`.
	 *
	 * The returned output is an instance of the `Question` class, which has four attributes:
	 * `id`, `text`, `type`, and `orderedAnswers`.
	 */
	@Override
	public Model hydrate(ResultSet rs) 
			throws java.sql.SQLException{
		int id, type;
		String text;
		Question question;

		id = rs.getInt("id");
		type = rs.getInt("type");
		text = rs.getString("text");

		SortedMap<Integer, Answer> orderedAnswers = Answer.retrieveByQuestionID(id);

		return Question.instance(id, text, type, orderedAnswers);
	}

	/**
	 * Binds a question's text and type to a prepared statement's parameters, incrementing
	 * the parameter offset after each binding.
	 *
	 * @param stmt PreparedStatement object used to execute SQL queries.
	 *
	 * @param m Model object, which is cast to a Question object within the function.
	 *
	 * @param offset starting position of the next parameter in the `PreparedStatement`.
	 */
	@Override
	public void dehydrate(PreparedStatement stmt, Model m, int offset) 
			throws java.sql.SQLException {
		Question question = (Question) m;
		stmt.setString(offset++, question.text());
		stmt.setInt(offset++, question.type());
	}
}
