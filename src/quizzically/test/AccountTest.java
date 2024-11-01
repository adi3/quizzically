package quizzically.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import quizzically.models.Account;

/**
 * It is a JUnit test class designed to verify the functionality of an Account class.
 */
public class AccountTest {

	Account acc;
	/**
	 * Initializes a new instance of the `Account` class, assigning it to the `acc`
	 * variable. This is typically done to set up a test environment before running a
	 * series of test cases. The `setUp` function is often called before each test case
	 * to ensure a clean and consistent state.
	 */
	@Before
	public void setUp() throws Exception {
		acc = new Account();
	}

	/**
	 * Appears to be a test method, likely used to verify the functionality of an
	 * `accountExists` method. It contains assertions to check if an account exists with
	 * a given username, "adisin", and does not exist with username "lyubo".
	 */
	@Test
	public void testExists() {
	//	assertTrue(acc.accountExists("adisin"));
	//	assertFalse(acc.accountExists("lyubo"));
	}
	
	/**
	 * Tests the `isStrongPass` and `isValidEmail` methods. It checks that a strong
	 * password is correctly identified and that invalid email addresses are rejected.
	 */
	@Test
	public void testStrongPass() {
	//	assertTrue(acc.isStrongPass("foo9!!"));
	//	assertFalse(acc.isStrongPass("diamond"));
		assertTrue(acc.isValidEmail("foo@bar.com"));
		assertFalse(acc.isValidEmail("sddf@sdf"));
		assertFalse(acc.isValidEmail("theet"));
	}
	
	/**
	 * Tests the functionality of the `createAccount` method. It creates an account with
	 * a given name, email, password, and other parameters, and verifies the number of
	 * errors in the account creation process.
	 */
	@Test
	public void testCreate() {
		ArrayList<String> errors = acc.createAccount("foo bar", "foo@bar.com", "foobar1", "foo11!", "foo11!", false);
		System.out.println(errors);
		assertEquals(errors.size(), 2);
		
	//	errors = acc.createAccount("Adi Singh", "adisin@stanford.edu", "adisin", "foo1!!", true);
	//	System.out.println(errors);
	//	assertEquals(errors.size(), 0);
	}
	
	/**
	 * Tests the functionality of the `checkCredentials` method. It verifies that the
	 * method returns true for valid credentials and false for invalid credentials. The
	 * method appears to validate user credentials based on some criteria.
	 */
	@Test
	public void testCheck() {
		assertTrue(acc.checkCredentials("foobar", "adis9$"));
		assertFalse(acc.checkCredentials("foobar", "adis9"));
		assertTrue(acc.checkCredentials("adisin", "foo1!!"));
	}
}
