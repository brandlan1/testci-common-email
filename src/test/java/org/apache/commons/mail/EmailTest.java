package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;


public class EmailTest {
	private static final String[] TEST_EMAILS = {"ab@bc.com", "a.b@c.org,", 
			"abcdefghijklmnopqrst@abcdefghijklmnopqrst.com.bd"};
	/*Concrete Email Class for testing*/
	private EmailConcrete email;
	
	@Before
	public void setUpEmailTest() throws Exception {
		email = new EmailConcrete();
	}
	
	/*
	 * Test AddBcc(String email)function
	 */
	@Test
	public void testAddBcc() throws Exception {
		email.addBcc(TEST_EMAILS);
		assertEquals(3, email.getBccAddresses().size());
	}
	
	/*
	 * Test addCc(String email) function
	 */
	@Test
	public void testaddCc() {
		try {
			email.addCc("ab@bc.com");
		} catch (EmailException e) {
			// Auto-generate catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Test getHostName() function when hostName is set
	 */
	@Test
	public void testgetHostName() {
		email = new EmailConcrete();
        email.setHostName("a.b@c.org");

        String result = email.getHostName();

        assertEquals("a.b@c.org", result);
	}
	
	/*
	 * Test getHostName() function when both values are empty
	 */
	@Test
	public void testgetHostName2() {
		email = new EmailConcrete();

	    String result = email.getHostName();

	    assertNull(result);
	}
	
	/*
	 * Test getMailSession() function when session is not empty
	 */
	@Test
	public void testgetMailSession() throws EmailException {
		email = new EmailConcrete();
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.test.com");
		Session aSession = Session.getInstance(props);
		
        email.setMailSession(aSession);

        Session result = email.getMailSession();

        assertEquals(aSession, result);
	}
	
	/*
	 * Test getMailSession() function when hostname is missing
	 */
	@Test
	public void testGetMailSession2() throws EmailException {
		Email email = new EmailConcrete();

	    try {
	        email.getMailSession();
	        fail("Expected EmailException to be thrown");
	    } catch (EmailException e) {
	        assertTrue(e.getMessage().contains("hostname"));
	    }
	}
	
	/*
	 * Test getMailSession() function when session is null and hostname is set
	 */
	@Test
	public void testGetMailSession3() throws EmailException {
		Email email = new EmailConcrete();

	    email.setHostName("smtp.test.com");

	    Session result = email.getMailSession();

	    assertNotNull(result);
	}
	
	/*
	 * Test getSentDate() function with Date set
	 */
	@Test
	public void testgetSentDate() {
		email = new EmailConcrete();

        Date date = new Date();
        email.setSentDate(date);

        Date result = email.getSentDate();

        assertEquals(date, result);
	}
	
	/*
	 * Test getSocketConnectionTimeout() function with Date set
	 */
	@Test
	public void testgetSocketConnectionTimeout() {
		email = new EmailConcrete();
		email.setSocketConnectionTimeout(1);
		
		int result = email.getSocketConnectionTimeout();
		assertEquals(1, result);
	}
	
	/*
	 * Test setFrom() function
	 */
	@Test
	public void testsetFrom() throws EmailException {
		email = new EmailConcrete();
		email.setFrom("ab@bc.com");
		assertEquals("ab@bc.com", null);
	}
	
	/*
	 * Test addHeader() function when name is empty
	 */
	@Test
	public void testaddHeader() {
		email = new EmailConcrete();

	    try {
	        email.addHeader("", "value");
	        fail("Expected IllegalArgumentException for empty name");
	    } catch (IllegalArgumentException e) {
	        assertTrue(e.getMessage().contains("name can not be null or empty"));
	    }
	}
	
	/*
	 * Test addHeader() function when value is empty
	 */
	@Test
	public void testaddHeader2() {
		Email email = new EmailConcrete();

	    try {
	        email.addHeader("name", "");
	        fail("Expected IllegalArgumentException for empty value");
	    } catch (IllegalArgumentException e) {
	        assertTrue(e.getMessage().contains("value cannot be null or empty"));
	    }
	}
	
	/*
	 * Test addHeader() function when name and value exist
	 */
	@Test
	public void testaddHeader3() throws Exception {
		email = new EmailConcrete();
		
		email.addHeader("Subject", "Hello World");

		// access private 'headers' field
	    Field field = Email.class.getDeclaredField("headers");
	    field.setAccessible(true);

	    @SuppressWarnings("unchecked")
	    Map<String, String> headers = (Map<String, String>) field.get(email);

	    assertEquals("Hello World", headers.get("Subject"));
	}
	
	/*
	 * Test addReplyTo() function 
	 */
	@Test
	public void testaddReplyTo() throws EmailException {
		email = new EmailConcrete();
		email.addReplyTo("a.b@c.org", "name");
		assertEquals("a.b@c.org", "name");
	}
	
	/*
	 * Test buildMimeMessage() function when a mime message gets successfully built  
	 */
	@Test
	public void testbuildMimeMessage() throws EmailException {
		email = new EmailConcrete();
		email.setHostName("smtp.test.com"); 
		email.setFrom("test@test.com"); 
		email.addTo("receiver@test.com");
		// build first 
		email.buildMimeMessage(); 
		MimeMessage message = email.getMimeMessage(); 
		assertNotNull(message);
	}
	
	/*
	 * Test buildMimeMessage() function with no "from" address  
	 */
	@Test
	public void testbuildMimeMessage2() {
		email = new EmailConcrete();
		email.setHostName("smtp.test.com");
		try {
	        email.buildMimeMessage();
	        fail("Expected EmailException");
	    } catch (EmailException e) {
	        assertTrue(e.getMessage().contains("From address required"));
	    }		
	}
	
	/*
	 * Test buildMimeMessage() function with no recipients  
	 */
	@Test
	public void testbuildMimeMessage3() throws EmailException {
		email = new EmailConcrete();
		email.setHostName("smtp.test.com");
		email.setFrom("test@test.com");

	    try {
	        email.buildMimeMessage();
	        fail("Expected EmailException");
	    } catch (EmailException e) {
	        assertTrue(e.getMessage().contains("receiver address"));
	    }	
	}	
	
	/*
	 * Test buildMimeMessage() function with a mime message is already built  
	 */
	@Test
	public void testbuildMimeMessage4() throws EmailException {
		email = new EmailConcrete();
		email.setHostName("smtp.test.com");
	    email.setFrom("test@test.com");
	    email.addTo("receiver@test.com");

	    email.buildMimeMessage();

	    try {
	        email.buildMimeMessage();
	        fail("Expected IllegalStateException");
	    } catch (IllegalStateException e) {
	        assertTrue(e.getMessage().contains("already built"));
	    }	
	}
	
	/*
	 * Test buildMimeMessage() function with subject  
	 */
	@Test
	public void testbuildMimeMessage5() throws Exception {
		email = new EmailConcrete();

	    email.setHostName("smtp.test.com");
	    email.setFrom("test@test.com");
	    email.addTo("receiver@test.com");
	    email.setSubject("Test Subject");

	    email.buildMimeMessage();

	    assertEquals("Test Subject", email.getMimeMessage().getSubject());
	}
	
	/*
	 * Test buildMimeMessage() function with headers  
	 */
	@Test
	public void testBuildMimeMessage6() throws Exception {
	    email = new EmailConcrete();

	    email.setHostName("smtp.test.com");
	    email.setFrom("test@test.com");
	    email.addTo("receiver@test.com");
	    email.addHeader("X-Test", "value");

	    email.buildMimeMessage();

	    String[] header = email.getMimeMessage().getHeader("X-Test");

	    assertNotNull(header);
	    assertEquals("value", header[0]);
	}
	
	/*
	 * Test buildMimeMessage() function with sent date  
	 */
	@Test
	public void testBuildMimeMessage7() throws Exception {
	    email = new EmailConcrete();

	    email.setHostName("smtp.test.com");
	    email.setFrom("test@test.com");
	    email.addTo("receiver@test.com");

	    email.buildMimeMessage();

	    assertNotNull(email.getMimeMessage().getSentDate());
	}
	
	/*
	 * Test buildMimeMessage() function when emailBody isn't null  
	 */
	@Test
	public void testBuildMimeMessage8() throws Exception {
	    email = new EmailConcrete();

	    email.setHostName("smtp.test.com");
	    email.setFrom("test@test.com");
	    email.addTo("receiver@test.com");

	    email.setMsg("Test body");

	    email.buildMimeMessage();

	    assertNotNull(email.getMimeMessage().getContent());
	}
	
	/*
	 * Test buildMimeMessage() function with CC  
	 */
	@Test
	public void testBuildMimeMessage9() throws Exception {
	    email = new EmailConcrete();

	    email.setHostName("smtp.test.com");
	    email.setFrom("test@test.com");
	    email.addTo("receiver@test.com");
	    email.addCc("cc@test.com");

	    email.buildMimeMessage();

	    assertNotNull(email.getMimeMessage());
	}
	
	/*
	 * Test buildMimeMessage() function with charset  
	 */
	@Test
	public void testBuildMimeMessage10() throws Exception {
	    email = new EmailConcrete();

	    email.setHostName("smtp.test.com");
	    email.setFrom("test@test.com");
	    email.addTo("receiver@test.com");
	    email.setSubject("Test Subject");
	    email.setCharset("UTF-8");

	    email.buildMimeMessage();

	    assertEquals("Test Subject", email.getMimeMessage().getSubject());
	}
	
	/*
	 * Test buildMimeMessage() function with setContent  
	 */
	@Test
	public void testBuildMimeMessage11() throws Exception {
	    email = new EmailConcrete();

	    email.setHostName("smtp.test.com");
	    email.setFrom("test@test.com");
	    email.addTo("receiver@test.com");

	    email.setContent("<h1>Hello</h1>", "text/html");

	    email.buildMimeMessage();

	    assertNotNull(email.getMimeMessage().getContent());
	}
	
	/*
	 * Test buildMimeMessage() function with replyTo  
	 */
	@Test
	public void testBuildMimeMessage12() throws Exception {
	    email = new EmailConcrete();

	    email.setHostName("smtp.test.com");
	    email.setFrom("test@test.com");
	    email.addTo("receiver@test.com");
	    email.addReplyTo("reply@test.com");

	    email.buildMimeMessage();

	    assertNotNull(email.getMimeMessage().getReplyTo());
	}
}
