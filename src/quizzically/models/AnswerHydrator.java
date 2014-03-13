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

		rs.first();
		id = rs.getInt("id");
		questionId = rs.getInt("question_id");
		position = rs.getInt("position");
		correctInt = rs.getInt("correct");

		correct = correctInt == 1;

		Set<String> answerTexts = new HashSet<String>();
		MySql sql = MySql.getInstance();
		SqlResult answerTextsResult = sql.get(MyDBInfo.ANSWER_TEXTS_TABLE, "`answer_id`="+id);
		for(int i=0; i<answerTextsResult.size(); i++){
			answerTexts.add(answerTextsResult.get(i).get("text"));
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
//		AnswerText.deleteOthers(answer.answerTexts());
//		for (AnswerText txt : answer.answerTexts()) {
//			txt.save(true);
//		}
	}
}
