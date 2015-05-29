package uss.controller;

import next.jdbc.mysql.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uss.model.join.UserCard;
import uss.response.Response;
import uss.response.Result;

@RestController
@RequestMapping("/api/search")
public class SearchController {
	
	@Autowired
	DAO dao;
	
	@RequestMapping(method = RequestMethod.GET)
	public Response search(String query) {
		String sql = "SELECT * FROM User JOIN Card "
				+ "ON User.userId = Card.userId "
				+ "WHERE User.name LIKE '%query%' OR User.email LIKE '%query%' "
				+ "OR Card.phoneNumber LIKE '%query%' OR Card.company LIKE '%query%'";
		
		sql = sql.replaceAll("query", query);
		UserCard userCard = dao.get(UserCard.class, sql);
		if (userCard == null)
			return Result.getErrorSearchNotFound();
		return Result.getSuccess(userCard);
	}
}
