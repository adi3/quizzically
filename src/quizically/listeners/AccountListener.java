package quizically.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import quizzically.models.Account;

/**
 * Application Lifecycle Listener implementation class AccountListener
 *
 */
@WebListener
public class AccountListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public AccountListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	arg0.getServletContext().setAttribute("acc", new Account());
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}
