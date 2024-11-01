package quizzically.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import quizzically.models.Message;
import quizzically.models.User;

/**
 * This class contains three test methods, test, putTest, and getTest, which exercise
 * the functionality of the Message class, including message creation, saving, and
 * retrieval from a database.
 */
public class MessageTest {

	/**
	 * Creates two `User` objects and a `Message` object, then asserts that the message's
	 * text, sender, and recipient match the expected values.
	 */
	@Test
	public void test() {
		User from = new User("adisin");
		User to = new User("drb");
		String text = "Whatdup modafucka!";
		
		Message msg = new Message(text, "NOTE", from, to);
		
	//	System.out.println(msg.getDate());
		assertEquals(msg.getMsg(), text);
		assertEquals(msg.getFromUser(), from);
		assertEquals(msg.getToUser(), to);		
	}
	
	/**
	 * Tests the creation and saving of a new message.
	 * It creates two user objects and a message object with a given text and user IDs,
	 * then asserts that the message is saved successfully.
	 */
	@Test
	public void putTest() {
		User from = new User("adisin");
		User to = new User("drb");
		String text = "Whatdup modafucka!";
		
		boolean status = new Message(text, "4", from, to).save();
		assertTrue(status);
	}
	
	/**
	 * Tests the retrieval of messages for a specified user by calling the `getMessages`
	 * method, which returns an ArrayList of Message objects, and then prints the ID,
	 * message, and date of each message in the list.
	 */
	@Test
	public void getTest() {
		User to = new User("drb");
		ArrayList<Message> msgs = Message.getMessages(to);
		
		for (Message msg : msgs) {
			System.out.println(msg.getId());
			System.out.println(msg.getMsg());
			System.out.println(msg.getDate());
			System.out.println();
		}
	}
}
