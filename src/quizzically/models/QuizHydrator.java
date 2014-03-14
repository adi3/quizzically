package quizzically.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.SortedMap;

public class QuizHydrator extends Hydrator {
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
