package quizzically.config;


/*
 * CS108 Student: This file will be replaced when we test your code. So, do not add any of your
 * assignment code to this file. Also, do not modify the public interface of this file.
 * Only change the public MyDBInfo constants so that it works with the database login credentials 
 * that we emailed to you.
 */
/**
 * Contains a collection of static final constants representing database connection
 * information and table names.
 *
 * - MYSQL_USERNAME (String): stores the username for accessing a MySQL database.
 *
 * - MYSQL_PASSWORD (String): represents the password for a MySQL database.
 *
 * - MYSQL_DATABASE_SERVER (String): specifies the hostname of a MySQL database server.
 *
 * - MYSQL_DATABASE_NAME (String): specifies the name of the MySQL database.
 *
 * - QUIZZES_TABLE (String): represents a database table named "quizzes".
 *
 * - QUESTIONS_TABLE (String): represents the name of a database table.
 *
 * - QUIZ_QUESTIONS_TABLE (String): represents a database table.
 *
 * - ANSWERS_TABLE (String): specifies the name of a database table.
 *
 * - ANSWER_TEXTS_TABLE (String): represents a database table.
 *
 * - USERS_TABLE (String): represents the database table name for storing user information.
 *
 * - FRIENDS_TABLE (String): represents the name of a database table.
 *
 * - MESSAGES_TABLE (String): represents a database table.
 *
 * - QUIZ_ATTEMPTS_TABLE (String): represents the name of a database table.
 */
public class MyDBInfo {
	
	public static final String MYSQL_USERNAME = "ccs108adisin";
	public static final String MYSQL_PASSWORD = "raibapae";
	public static final String MYSQL_DATABASE_SERVER = "mysql-user.stanford.edu";
	public static final String MYSQL_DATABASE_NAME = "c_cs108_adisin";
	
	// TABLE NAMES
	public static final String QUIZZES_TABLE = "quizzes";
	public static final String QUESTIONS_TABLE = "questions";
	public static final String QUIZ_QUESTIONS_TABLE = "quiz_questions";
	public static final String ANSWERS_TABLE = "answers";
	public static final String ANSWER_TEXTS_TABLE = "answer_texts";
	public static final String USERS_TABLE = "users";
	public static final String FRIENDS_TABLE = "friends";
	public static final String MESSAGES_TABLE = "messages";
	public static final String QUIZ_ATTEMPTS_TABLE = "quiz_attempts";

}
