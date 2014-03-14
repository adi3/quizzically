package quizzically.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerTextHydrator extends Hydrator {

	@Override
	protected Model hydrate(ResultSet rs) throws SQLException {
		int id, answerId;
		String text;

		id = rs.getInt("id");
		answerId = rs.getInt("answer_id");
		text = rs.getString("text");

		return new AnswerText(id, answerId, text);
	}

	@Override
	protected void dehydrate(PreparedStatement stmt, Model m, int offset)
			throws SQLException {
		AnswerText txt = (AnswerText) m;
		stmt.setInt(offset++, txt.answerId());
		stmt.setString(offset++, txt.text());
	}

}
