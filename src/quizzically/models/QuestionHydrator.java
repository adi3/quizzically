package quizzically.models;

import java.sql.ResultSet;
import java.util.SortedMap;

import java.sql.PreparedStatement;

public class QuestionHydrator extends Hydrator {
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

	@Override
	public void dehydrate(PreparedStatement stmt, Model m, int offset) 
			throws java.sql.SQLException {
		Question question = (Question) m;
		stmt.setString(offset++, question.text());
		stmt.setInt(offset++, question.type());
	}
}
