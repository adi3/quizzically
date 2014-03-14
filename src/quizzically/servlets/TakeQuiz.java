package quizzically.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizzically.models.QuizAttempt;

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
		if(qAId == -1){ // new attempt
			int qId = getInt(request, "quiz_id");
			QuizAttempt qA = QuizAttempt.create(qId, getUser(request).getId());
			request.setAttribute("attempt_id", qA.id()); // for use by doPost()
		} else { // retrieve existing attempt
			QuizAttempt qA = QuizAttempt.retrieve(qAId);
		}
		
		RequestDispatcher dispatch = request.getRequestDispatcher("ShowQuiz.jsp");
		dispatch.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		QuizAttempt qA = QuizAttempt.retrieve(Integer.parseInt(getString(request, "attempt_id")));
		//grade question -> update QuizAttempt score
		// set QuizAttempt position to current Question position
		// if all Questions completed -> setCompletAt(new Date()) on QuizAttempt
		
	}

}
