package quizzically.models;


import java.sql.*;
import java.util.*;
import java.util.Date;

public class QuizAttemptHydrator extends Hydrator {
	private static final int NULL_VALUE = -1;

	@Override
	protected Model hydrate(ResultSet rs) throws SQLException {
		rs.first();
		int id = rs.getInt("id");
		int quizId = rs.getInt("quiz_id");
		int userId = rs.getInt("user_id");
		Date createdAt = new java.util.Date(rs.getTimestamp("created_at").getTime());
		Date completedAt;
		int score;
		Timestamp completedAtTimestamp = rs.getTimestamp("completed_at");
		if(completedAtTimestamp != null){ // quiz completed
			completedAt = new java.util.Date(completedAtTimestamp.getTime());
			score = rs.getInt("score");
		} else { // quiz not completed
			completedAt = null;
			score = NULL_VALUE;
		}
		return new QuizAttempt(id, quizId, userId, createdAt, completedAt, score);
	}

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
	}

}
