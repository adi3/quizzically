package quizzically.servlets;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.exceptions.InvalidResponseException;
import quizzically.models.Achievement;
import quizzically.models.Message;
import quizzically.models.QuizAttempt;
import quizzically.models.Quiz;
import quizzically.models.Question;
import quizzically.models.QuestionResponse;
import quizzically.models.User;

/**
 * Servlet implementation class TakeQuiz
 */
@WebServlet("/TakeQuiz")
public class TakeQuiz extends BaseServlet implements Servlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TakeQuiz() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		umbli(request);
		int qAId = getInt(request, "attempt_id", -1);
		QuizAttempt qA;
		if(qAId == -1){ // new attempt
			int qId = getInt(request, "id");
			Quiz quiz = Quiz.retrieve(qId);
			qA = QuizAttempt.create(quiz, getUser(request));
		} else { // retrieve existing attempt
			qA = QuizAttempt.retrieve(qAId);
		}
		request.setAttribute("attempt", qA); // for use by doPost()

		if (request.getSession().getAttribute("user") != null) {
			boolean hasUnread = Message.hasUnread((String) request.getSession().getAttribute("user"));
			String msgIcon = hasUnread ? "msg-new.png" : "msg-def.png";
			request.setAttribute("msgIcon", msgIcon);
		}

		RequestDispatcher dispatch = request.getRequestDispatcher("TakeQuiz.jsp");
		dispatch.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		QuizAttempt qA = QuizAttempt.retrieve(getInt(request, "attempt_id"));
		//grade question -> update QuizAttempt score
		// set QuizAttempt position to current Question position
		// if all Questions completed -> setCompletAt(new Date()) on QuizAttempt
		int id;
		Quiz quiz;

		quiz = qA.quiz();

		Enumeration<String> params = request.getParameterNames();
		HashMap<Integer, QuestionResponse> responses = 
			new HashMap<Integer, QuestionResponse>();

		// parse question/response data
		while (params.hasMoreElements()) {
			String paramName = params.nextElement();
			// assume paramName is format "question-QID-answer-AID"
			try {
				if (paramName.startsWith("question-")) {
					String[] tmp = paramName.split("-", 4);
					int questionId = Integer.parseInt(tmp[1]);
					int pos = Integer.parseInt(tmp[3]);
					Question question = null;
					QuestionResponse qr = null;

					if (responses.containsKey(questionId)) {
						qr = responses.get(questionId);
						question = qr.question();
					} else {
						question = Question.retrieve(questionId);
						if (question != null) {
							qr = new QuestionResponse(question, pos);
							responses.put(question.id(), qr);
						}
					}

					if (question != null) {
						int answerId = -1;
						if (question.type() == Question.TYPE_MULTIPLE_CHOICE 
								/* || TODO MULTI_MULTI_CHOICE */) {
							answerId = getInt(request, paramName);
						}
						qr.addResponse(answerId, request.getParameter(paramName));
					}
					// else skip
				}
			} catch (NumberFormatException ignored) { }
		}

		// finally, grade the questions
		Collection<QuestionResponse> qrs = responses.values();
		QuestionResponse[] qrsOrdered = new QuestionResponse[qrs.size()];
		int gradeAccum = qA.score();

		int qAPos = qA.position() + qrs.size();
		if (quiz.immediateCorrection()) {
			qAPos = qA.position() + 1;
		}
		for (QuestionResponse qr : qrs) {
			try {
				qrsOrdered[qr.position()] = qr;
				int points = qr.grade().points();
				gradeAccum += qr.grade().points();
			} catch (InvalidResponseException e) {
				throw new ServletException(e.getMessage());
			}
		}

		// update grade in db
		qA.setScore(gradeAccum);
		qA.setPosition(qAPos);
		// quiz is done

		request.setAttribute("attempt", qA);
		
		if (qAPos == quiz.questions().size()) {
			qA.setCompletedAt(new Date());
		}
		qA.save();
		
		if (qAPos == quiz.questions().size()) {
			List<Achievement> newAchievements = Achievement.newTakerAchievements(
					((User) request.getSession().getAttribute("user")).getId(), quiz.id());
			
		}
		
		

		request.setAttribute("quiz", quiz);
		request.setAttribute("gradedResponses", qrsOrdered);

		request.getRequestDispatcher("GradeQuiz.jsp").forward(request, response);
	}

}
