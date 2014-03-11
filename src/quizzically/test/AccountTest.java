package quizzically.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import quizzically.models.Account;

public class AccountTest {

	Account acc;
	@Before
	public void setUp() throws Exception {
		acc = new Account();
	}

	@Test
	public void testExists() {
	//	assertTrue(acc.accountExists("adisin"));
	//	assertFalse(acc.accountExists("lyubo"));
	}
	
	@Test
	public void testStrongPass() {
	//	assertTrue(acc.isStrongPass("foo9!!"));
	//	assertFalse(acc.isStrongPass("diamond"));
		assertTrue(acc.isValidEmail("foo@bar.com"));
		assertFalse(acc.isValidEmail("sddf@sdf"));
		assertFalse(acc.isValidEmail("theet"));
	}
	
	@Test
	public void testCreate() {
		ArrayList<String> errors = acc.createAccount("foo bar", "foo@bar.com", "foobar1", "foo11!", "foo11!", false);
		System.out.println(errors);
		assertEquals(errors.size(), 2);
		
	//	errors = acc.createAccount("Adi Singh", "adisin@stanford.edu", "adisin", "foo1!!", true);
	//	System.out.println(errors);
	//	assertEquals(errors.size(), 0);
	}
	
	@Test
	public void testCheck() {
		assertTrue(acc.checkCredentials("foobar", "adis9$"));
		assertFalse(acc.checkCredentials("foobar", "adis9"));
		assertTrue(acc.checkCredentials("adisin", "foo1!!"));
	}
}
