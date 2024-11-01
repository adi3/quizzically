package quizzically.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import quizzically.models.User;

/**
 * Contains three test methods that exercise various aspects of the User class,
 * including friend management and retrieval by ID.
 */
public class UserTest {

//	@Test
	/**
	 * Creates two `User` objects, retrieves the friends of the first user, prints their
	 * names, and checks if the second user is in the list of friends.
	 */
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
	/**
	 * Retrieves a `User` object from the database using the `id` "33", and then asserts
	 * that the username associated with this user is "adisin".
	 */
	public void test2() {
		String id = "33";
		User user = User.getUserById(id);
		assertEquals(user.getUsername(), "adisin");
	}
	
	/**
	 * Tests the functionality of a `User` class by creating two instances, `user1` and
	 * `user2`, and asserting that `user1` is initially friends with `user2` and that
	 * deleting `user2` as a friend is successful.
	 */
	@Test
	public void test3() {
		User user1 = new User("adisin");
		User user2 = new User("drb");
		
		assertTrue(user1.isFriend(user2));
		assertTrue(user1.deleteFriend(user2));
	}

}
