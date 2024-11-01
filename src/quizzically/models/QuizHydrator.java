package quizzically.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.SortedMap;

/**
 * Handles the serialization and deserialization of Quiz objects to and from a database.
 */
public class QuizHydrator extends Hydrator {
	/**
	 * Populates a `Quiz` object from a database query result set, retrieving its attributes
	 * and associated questions and owner. It then returns the fully populated `Quiz` object.
	 *
	 * @param rs ResultSet object that contains the data to be used to hydrate the Quiz
	 * model, retrieved from a database.
	 *
	 * Fetch its columns as named values.
	 *
	 * @returns a fully initialized `Quiz` object with associated questions and owner.
	 *
	 * It is a Quiz object.
	 */
	@Override
	public Model hydrate(ResultSet rs) 
			throws java.sql.SQLException{
		int id, ownerId;
		String name, description;
		java.util.Date createdAt;
		int pageFormat, order;
		boolean immediateCorrection;
		Quiz quiz;

		id = rs.getInt("id");
		ownerId = rs.getInt("owner_id");
		name = rs.getString("name");
		description = rs.getString("description");
		createdAt = new java.util.Date(rs.getTimestamp("created_at").getTime());
		pageFormat = rs.getInt("page_format");
		order = rs.getInt("order");
		immediateCorrection = rs.getInt("immediate_correction") == 1;

		quiz = new Quiz(id, name, ownerId, description, 
				createdAt, pageFormat, order, immediateCorrection);

		SortedMap<Integer, Question> orderedQuestions = Question.retrieveByQuizID(id);
		quiz.setQuestions(orderedQuestions);
		User owner = User.getUserById(Integer.toString(ownerId));
		quiz.setOwner(owner);
		return quiz;
	}

	/**
	 * Populates a SQL query statement with data from a `Quiz` model object. It sets
	 * various attributes of the quiz, such as its name, owner ID, description, creation
	 * date, page format, order, and correction flag, into the query statement.
	 *
	 * @param stmt PreparedStatement object used to execute SQL queries.
	 *
	 * Set the string value of a parameter at the current offset.
	 * Set the integer value of a parameter at the current offset.
	 * Set the string value of a parameter at the current offset.
	 * Set the timestamp value of a parameter at the current offset.
	 * Set the integer value of a parameter at the current offset.
	 * Set the integer value of a parameter at the current offset.
	 * Set the integer value of a parameter at the current offset based on a boolean condition.
	 *
	 * @param m model being dehydrated, which is cast to a `Quiz` object for further processing.
	 *
	 * Destructure `m` into the `Quiz` class.
	 * The `Quiz` class has properties `name`, `ownerId`, `description`, `createdAt`,
	 * `pageFormat`, `order`, and `immediateCorrection`.
	 *
	 * @param offset current position in the prepared statement where the next parameter
	 * value should be inserted.
	 */
	@Override
	public void dehydrate(PreparedStatement stmt, Model m, int offset) 
			throws java.sql.SQLException {
		Quiz quiz = (Quiz) m;
		stmt.setString(offset++, quiz.name());
		stmt.setInt(offset++, quiz.ownerId());
		stmt.setString(offset++, quiz.description());
		stmt.setTimestamp(offset++, 
				new java.sql.Timestamp(quiz.createdAt().getTime()));
		stmt.setInt(offset++, quiz.pageFormat());
		stmt.setInt(offset++, quiz.order());
		stmt.setInt(offset++, quiz.immediateCorrection() ? 1 : 0);
	}
}
