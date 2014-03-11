package quizzically.models;

public abstract class Response {

	/**
	 * Create a response object based on the question, answerId, 
	 * answerText
	 */
	public static Response create(Question question, int answerId,
			String answerText) {
		Response rsp = null;
		switch (question.type()) {
			case Question.TYPE_TEXT:
			case Question.TYPE_FILL_IN:
			case Question.TYPE_PICTURE:
				rsp = new TextResponse(answerText);
				break;
			case Question.TYPE_MULTIPLE_CHOICE:
				rsp = new ChoiceResponse(answerId);
				break;
			default:
				throw new RuntimeException("Invalid question type");
		}
		return rsp;
	}

}
