package quizzically.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Picture-as-question question
 */
public class PictureQuestion extends TextQuestion {
	public PictureQuestion(int id, String text, List<Answer> answers) {
		super(id, text, answers);
		this.type = Question.TYPE_PICTURE;
	}

	// TODO necessary constructor?
	public PictureQuestion(String url, ArrayList<Answer> answers) {
		super("<img src=\"url\" />", answers);
	}
	// TODO no special functionality...?
}
