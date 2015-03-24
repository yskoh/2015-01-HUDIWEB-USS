package uss.controllers;

import lib.database.DAO;
import lib.mapping.annotation.HttpMethod;
import lib.mapping.annotation.Mapping;
import lib.mapping.dispatch.support.Http;
import lib.mapping.view.Json;
import uss.database.objects.User;
import uss.exception.HasNoRight;
import uss.exception.LoggonNeeded;
import uss.right.Right;
import uss.right.user.LoginRight;
import uss.right.user.ReadRight;
import uss.right.user.UpdateRight;

public class UserController {

	@Mapping(value = "/api/user", method = "POST")
	public void register(Http http, DAO dao) {
		User user = http.getJsonObject(User.class, "user");
		dao.insert(user);
		user.setId(dao.getLastKey().intValue());
		http.setSessionAttribute("user", user);
		http.setView(new Json(user));
	}

	@Mapping(value = "/api/user", method = "PUT", before = "loginCheck")
	public void update(Http http, DAO dao) throws HasNoRight {
		User loggedUser = http.getSessionAttribute(User.class, "user");
		User user = http.getJsonObject(User.class, "user");
		Right right = new UpdateRight(loggedUser, user);
		if (!right.hasRight())
			throw new HasNoRight();
		dao.update(user);
		http.setSessionAttribute("user", user);
		http.setView(new Json(user));
	}

	@Mapping(value = "/api/user", method = "GET", before = "loginCheck")
	public void read(Http http, DAO dao) throws HasNoRight {
		User loggedUser = http.getSessionAttribute(User.class, "user");
		User user = dao.getRecordByClass(User.class, http.getParameter("stringId"));
		Right right = new ReadRight(loggedUser, user);
		if (!right.hasRight())
			throw new HasNoRight();
		http.setView(new Json(user));
	}

	@Mapping(value = "/api/user/login", method = "POST")
	public void login(Http http, DAO dao) throws HasNoRight {
		User loggedUser = http.getJsonObject(User.class, "user");
		User user = dao.getRecord(User.class, "SELECT * FROM User WHERE User_stringId=?", loggedUser.getStringId());
		Right right = new LoginRight(loggedUser, user);
		if (!right.hasRight())
			throw new HasNoRight();
		http.setSessionAttribute("user", user);
		http.setView(new Json(user));
	}

	@HttpMethod
	public void loginCheck(Http http) throws LoggonNeeded {
		User user = http.getSessionAttribute(User.class, "user");
		if (user == null)
			throw new LoggonNeeded();
	}
}
