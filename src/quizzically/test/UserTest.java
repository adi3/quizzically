package quizzically.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import quizzically.models.User;

public class UserTest {

//	@Test
	public void test() {
		User user1 = new User("adisin");
		User user2 = new User("foobar");
		
		ArrayList<User> friends = user1.getFriends();
		for (User f : friends) {
			System.out.println(f.getName());
		}
		
		System.out.println(friends.contains(user2));
	}
	
//	@Test
	public void test2() {
		String id = "33";
		User user = User.getUserById(id);
		assertEquals(user.getUsername(), "adisin");
	}
	
	@Test
	public void test3() {
		User user1 = new User("adisin");
		User user2 = new User("drb");
		
		assertTrue(user1.isFriend(user2));
		assertTrue(user1.deleteFriend(user2));
	}

}
