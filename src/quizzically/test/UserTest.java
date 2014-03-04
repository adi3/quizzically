package quizzically.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import quizzically.models.User;

public class UserTest {

	@Test
	public void test() {
		User user1 = new User("adisin");
		User user2 = new User("foobar");
		
		ArrayList<User> friends = user1.getFriends();
		for (User f : friends) {
			System.out.println(f.getName());
		}
		
		System.out.println(friends.contains(user2));
	}

}
