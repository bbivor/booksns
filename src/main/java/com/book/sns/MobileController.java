package com.book.sns;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.book.sns.dao.BookMapper;
import com.book.sns.dao.UserMapper;
import com.book.sns.dto.Book;
import com.book.sns.dto.Result;
import com.book.sns.dto.SnsUser;

@Controller
@RequestMapping(value="/mobile")
public class MobileController {

	@Autowired SqlSession sqlSession;
	
	/**
	 * login
	 * @author MinJeJung
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Result login(@RequestBody SnsUser user) {
		System.out.println("from Mobile: " + user);
		
		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		SnsUser resultUser = mapper.login(user);
		System.out.println("from DB: " + user);
		
		Result result = null;
		if(resultUser != null) {
			result = new Result("success", resultUser.getUserId());
		} else {
			result = new Result("fail", null);
		}
		System.out.println(result);
		
		return result;
	}
	
	/**
	 * insert into book
	 * @author MinJeJung
	 * @param book
	 * @return
	 */
	@RequestMapping(value = "/bookAdd", method = RequestMethod.POST)
	public @ResponseBody Result bookAdd(@RequestBody Book book) {
		System.out.println("from Mobile: " + book);
		
		BookMapper mapper = sqlSession.getMapper(BookMapper.class);
		boolean result = mapper.insertBook(book);
		
		if(result == true) return new Result("success", null);
		else return new Result("false", null);
	}
	
	
}
