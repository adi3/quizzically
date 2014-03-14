package quizzically.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedMap;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.exceptions.ModelException;
import quizzically.models.Question;
import quizzically.models.User;

/**
 * Servlet implementation class Answer
 */
@WebServlet("/Answer")
public class Answer extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user;
		String[] texts = {};
		// LHS maintains order and uniqueness of elemenets
		Set<String> cleanTexts = new LinkedHashSet<String>();
		ArrayList<Boolean> cleanCorrect = new ArrayList<Boolean>();
		Question question;
		quizzically.models.Answer answer;
		int id, questionId, type, ques_type;
		String[] correctStrs;

		umbli(request);
		user = getUser(request);

		id = getInt(request, "id", -1);
		questionId = getInt(request, "question_id");
		question = Question.retrieve(questionId);
		ques_type = question.type();
		
		correctStrs = getArray(request, "correct");
		texts = getArray(request, "texts");
		
		// clean answer inputs
		for (int i = 0; i < texts.length; i++) {
			String text = texts[i];
			if (!text.equals("")) {
				int prevSize = cleanTexts.size();
				cleanTexts.add(text);
				// new and unique
				if (cleanTexts.size() != prevSize && i < correctStrs.length) {
					boolean corr = correctStrs[i].equals("1");
					cleanCorrect.add(new Boolean(corr));
				}
			}
		}

		switch (question.type()) {
			case Question.TYPE_TEXT:
			case Question.TYPE_FILL_IN:
			case Question.TYPE_PICTURE:
				boolean correct = cleanCorrect.get(0);
				// just one answer object
				if (id != -1) {
					answer = quizzically.models.Answer.retrieve(id);
					answer.setCorrect(correct);
					answer.setAnswerTextsStrings(cleanTexts);
					answer.save();
				} else {
					try {
						answer = question.createAnswer(correct, cleanTexts);
					} catch (quizzically.exceptions.ModelException e) {
						throw new ServletException(e.getMessage());
					}
				}
				break;
			case Question.TYPE_MULTIPLE_CHOICE:
				// create many answer objects
				SortedMap<Integer, quizzically.models.Answer> curAnswers = 
					quizzically.models.Answer.retrieveByQuestionID(questionId);
				quizzically.models.Answer[] newAnswers = 
					new quizzically.models.Answer[cleanTexts.size()];
				int i;
				
				if (cleanCorrect.size() != cleanTexts.size()) {
					throw new ServletException("Invalid request: Must include same number of texts and correct");
				}
								
				// delete old answers
				for (quizzically.models.Answer a : curAnswers.values()) {
					a.delete();
				}

				i = 0;
				for (String txt : cleanTexts) {
					Set<String> singleText = new LinkedHashSet<String>();
					singleText.add(txt);
					try {
						newAnswers[i] = question.createAnswer(cleanCorrect.get(i), singleText);
					} catch (ModelException e) {
						e.printStackTrace();
						// well shit... we're not gonna backtrack... just throw it to the user
						throw new ServletException(e.getMessage());
					}
					i++;
				}
				// return the id of the first one just for shits, we don't care though when they edit
				answer = newAnswers[0];
				break;
			default:
				throw new RuntimeException("Unsupported question type");
		}

		String json = "{\"id\": \"" + answer.id() + "\"}";
		response.getWriter().write(json);
	}

}
