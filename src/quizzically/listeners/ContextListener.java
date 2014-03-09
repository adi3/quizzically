package quizzically.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import quizzically.models.Account;
import quizzically.models.User;
import quizzically.lib.MySql;

/**
 * Application Lifecycle Listener implementation class AccountListener
 *
 */
@WebListener
public class ContextListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	arg0.getServletContext().setAttribute("acc", new Account()); // FIXME this should almost definitely be in SessionContext
    	arg0.getServletContext().setAttribute("mysql", MySql.getInstance());
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	MySql sql = (MySql) arg0.getServletContext().getAttribute("mysql");
    	sql.close();
    }
	
}
