package quizzically.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import quizzically.models.Message;
import quizzically.models.User;

public class MessageTest {

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
	
	@Test
	public void putTest() {
		User from = new User("adisin");
		User to = new User("drb");
		String text = "Whatdup modafucka!";
		
		boolean status = new Message(text, "4", from, to).save();
		assertTrue(status);
	}
	
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
