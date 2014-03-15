package quizzically.models;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

/**
 * Picture-as-question question
 */
public class PictureQuestion extends TextQuestion {
	public PictureQuestion(int id, String text, SortedMap<Integer, Answer> orderedAnswers) {
		super(id, text, orderedAnswers);
		this.type = Question.TYPE_PICTURE;
	}

	public String text() {
		return "<img src=\"" + super.text() + "\" style=\"width: 500px;\"/>";
	}

	// TODO necessary constructor?
//	public PictureQuestion(String url, ArrayList<Answer> answers) {
//		super("<img src=\"url\" />", answers);
//	}
	// TODO no special functionality...?
}
