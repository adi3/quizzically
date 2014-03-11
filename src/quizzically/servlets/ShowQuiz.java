package quizzically.servlets;

import java.io.IOException;
import java.util.*;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.Question;
import quizzically.models.QuestionResponse;
import quizzically.models.Quiz;
import quizzically.models.Response;

/**
 * Servlet implementation class ShowQuiz
 */
@WebServlet("/ShowQuiz")
public class ShowQuiz extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowQuiz() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int id;
		Quiz quiz;
		try {
			id = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			throw new ServletException("Invalid parameter id");
		}

		quiz = Quiz.retrieve(id);
		request.setAttribute("quiz", quiz);
		request.getRequestDispatcher("ShowQuiz.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id;
		Quiz quiz;
		try {
			id = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			throw new ServletException("Invalid parameter id");
		}

		quiz = Quiz.retrieve(id);

		Enumeration<String> params = request.getParameterNames();
		HashMap<Integer, QuestionResponse> responses = 
			new HashMap<Integer, QuestionResponse>();

		// parse question/response data
		while (params.hasMoreElements()) {
			String paramName = params.nextElement();
			// assume paramName is format "question-QID-answer-AID"
			try {
				if (paramName.startsWith("question-")) {
					String[] tmp = paramName.split("-", 6);
					int questionId = Integer.parseInt(tmp[1]);
					int answerId = Integer.parseInt(tmp[3]);
					int pos = Integer.parseInt(tmp[5]);
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
						qr.addResponse(answerId, request.getParameter(paramName));
					}
					// else skip
				}
			} catch (NumberFormatException ignored) { }
		}

		// finally, grade the questions
		Collection<QuestionResponse> qrs = responses.values();
		QuestionResponse[] qrsOrdered = new QuestionResponse[qrs.size()];
		for (QuestionResponse qr : qrs) {
			qrsOrdered[qr.position()] = qr;
		}

		request.setAttribute("quiz", quiz);
		request.setAttribute("gradedResponses", qrsOrdered);

		request.getRequestDispatcher("GradeQuiz.jsp").forward(request, response);
		// TODO TODO create quiz history
	}


}
