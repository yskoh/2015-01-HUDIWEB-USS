package uss.controller;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpSession;

import next.jdbc.mysql.maker.PackageCreator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uss.model.User;
import uss.response.Result;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class UserControllerTest {

	@Autowired
	UserController controller;

	@Mock
	HttpSession session;

	@Before
	public void setup() throws NoSuchFieldException, SecurityException {
		MockitoAnnotations.initMocks(this);
		PackageCreator.reset();
	}

	@Test
	public void registerTest() {
		User user = new User("id", "pw");
		assertEquals(Result.SUCCESS, controller.register(user));
		assertEquals(Result.ERROR_SQL_EXCUTE, controller.register(user));
	}

	@Test
	public void loginTest() {
		controller.register(new User("id", "pw"));
		assertEquals(Result.Login.ERROR_PASSWORD_NOT_MATCHED, controller.login(new User("id", "pw2"), session));
		assertEquals(Result.Login.ERROR_USER_NULL, controller.login(new User("id3", "pw"), session));
		assertEquals(Result.SUCCESS, controller.login(new User("id", "pw"), session));
	}

	@Test
	public void updateTest() {
		User user = new User("id", "pw");
		user.setUserId(1);
		Mockito.when(session.getAttribute(UserController.USER)).thenReturn(user);
		User updateUser = new User("id2", "pw1");
		assertEquals(Result.ERROR_BAD_REQUEST, controller.update(updateUser, session));
		assertEquals(Result.SUCCESS, controller.update(user, session));
	}

}