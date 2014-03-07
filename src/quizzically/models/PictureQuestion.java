package quizzically.models;

import java.util.ArrayList;

/**
 * Picture-as-question question
 */
public class PictureQuestion extends TextQuestion {
	public PictureQuestion(int id, String text) {
		super(id, text);
		this.type = Question.TYPE_PICTURE;
	}

	// TODO necessary constructor?
	public PictureQuestion(String url, ArrayList<Answer> answers) {
		super("<img src=\"url\" />", answers);
	}
	// TODO no special functionality...?
}
