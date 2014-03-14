package quizzically.models;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;

import java.sql.PreparedStatement;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;
import quizzically.lib.SqlResult;

public class AnswerHydrator extends Hydrator {
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
