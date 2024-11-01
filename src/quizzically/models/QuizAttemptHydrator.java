package quizzically.models;


import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Handles the conversion of QuizAttempt Model objects to and from a database, utilizing
 * the Hydrator interface.
 */
public class QuizAttemptHydrator extends Hydrator {
	private static final int NULL_VALUE = -1;

	/**
	 * Populates a `QuizAttempt` object from a database `ResultSet`. It retrieves various
	 * attributes such as id, quiz, user, creation date, completion date, score, and
	 * position from the result set.
	 *
	 * @param rs ResultSet object that provides data from a database query, allowing the
	 * function to retrieve and hydrate the data into a QuizAttempt object.
	 *
	 * Retrieve its columns as follows: id, quiz_id, user_id, position, created_at,
	 * completed_at, and score.
	 *
	 * @returns a QuizAttempt object, encapsulating quiz attempt metadata.
	 *
	 * The returned object is an instance of `QuizAttempt` with the following attributes:
	 * - `id`: an integer uniquely identifying the quiz attempt
	 * - `quiz`: a `Quiz` object representing the quiz taken
	 * - `user`: a `User` object representing the user who took the quiz
	 * - `createdAt`: a `Date` representing the time when the quiz attempt was created
	 * - `completedAt`: a `Date` representing the time when the quiz attempt was completed,
	 * or null if not completed
	 * - `score`: an integer representing the score achieved in the quiz attempt
	 * - `position`: an integer representing the position in the quiz attempt
	 */
	@Override
	protected Model hydrate(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		int quizId = rs.getInt("quiz_id");
		Quiz quiz;
		User user;
		int userId = rs.getInt("user_id");
		int position = rs.getInt("position");
		Date createdAt = new java.util.Date(rs.getTimestamp("created_at").getTime());
		Date completedAt;
		int score;
		Timestamp completedAtTimestamp = rs.getTimestamp("completed_at");
		if(completedAtTimestamp != null){ // quiz completed
			completedAt = new java.util.Date(completedAtTimestamp.getTime());
			score = rs.getInt("score");
		} else { // quiz not completed
			completedAt = null;
			score = 0;
		}
		quiz = Quiz.retrieve(quizId);
		user = User.retrieve(userId);
		return new QuizAttempt(id, quiz, user, createdAt, completedAt, score, position);
	}

	/**
	 * Serializes a `QuizAttempt` object into a prepared SQL statement. It sets the
	 * object's properties, such as creation and completion timestamps, score, quiz ID,
	 * user ID, and position, as parameters in the statement.
	 *
	 * @param stmt PreparedStatement used to execute SQL queries, which is modified by
	 * the function to store data from the `Model` object.
	 *
	 * Set to a `PreparedStatement` object.
	 *
	 * @param m model object, which is cast to a `QuizAttempt` object to access its properties.
	 *
	 * Decomposed from `Model m`, `m` is a `QuizAttempt` object. It has timestamps for
	 * `createdAt` and `completedAt` if the attempt was completed, and a score.
	 *
	 * @param offset position in the `PreparedStatement` where the next parameter value
	 * is to be set.
	 */
	@Override
	protected void dehydrate(PreparedStatement stmt, Model m, int offset)
			throws SQLException {
		QuizAttempt qA = (QuizAttempt) m;
		stmt.setTimestamp(offset++, new Timestamp(qA.createdAt().getTime()));
		if(qA.completed()){
			stmt.setTimestamp(offset++, new Timestamp(qA.completedAt().getTime()));
			stmt.setInt(offset++, qA.score());
		} else {
			stmt.setNull(offset++, java.sql.Types.TIMESTAMP);
			stmt.setNull(offset++, java.sql.Types.INTEGER);
		}
		stmt.setInt(offset++, qA.quizId());
		stmt.setInt(offset++, qA.userId());
		stmt.setInt(offset++, qA.position());
	}

}
