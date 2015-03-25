package uss.controllers;

import java.io.UnsupportedEncodingException;

import lib.database.DAO;
import lib.mapping.annotation.Before;
import lib.mapping.annotation.HttpMethod;
import lib.mapping.annotation.Mapping;
import lib.mapping.dispatch.support.Http;
import lib.mapping.view.Json;
import uss.database.objects.User;
import uss.exception.JsonAlert;
import uss.right.Right;
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
	public void update(Http http, DAO dao) throws JsonAlert {
		User loggedUser = http.getSessionAttribute(User.class, "user");
		User user = http.getJsonObject(User.class, "user");
		Right right = new UpdateRight(loggedUser, user);
		if (!right.hasRight())
			throw new JsonAlert("사용자 수정 권한이 없습니다.");
		dao.update(user);
		http.setSessionAttribute("user", user);
		http.setView(new Json(user));
	}

	@Mapping(value = "/api/user", method = "GET", before = "loginCheck")
	public void read(Http http, DAO dao) throws JsonAlert {
		User loggedUser = http.getSessionAttribute(User.class, "user");
		User user = dao.getRecordByClass(User.class, http.getParameter("stringId"));
		Right right = new ReadRight(loggedUser, user);
		if (!right.hasRight())
			throw new JsonAlert("사용자 조회 권한이 없습니다.");
		http.setView(new Json(user));
	}

	@Mapping(value = "/api/user/login", method = "POST")
	public void login(Http http, DAO dao) throws JsonAlert {
		User loggedUser = http.getJsonObject(User.class, "user");
		User user = dao.getRecord(User.class, "SELECT * FROM User WHERE User_stringId=?", loggedUser.getStringId());
		user.login(loggedUser);
		http.setSessionAttribute("user", user);
		http.setView(new Json(user));
	}

	@HttpMethod
	public void loginCheck(Http http) throws JsonAlert {
		User user = http.getSessionAttribute(User.class, "user");
		if (user == null)
			throw new JsonAlert("로그인이 필요합니다.");
	}
	
	@Before
	public void characterSet(Http http) {
		try {
			http.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}