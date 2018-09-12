package com.book.sns.dao;

import java.util.ArrayList;

import com.book.sns.dto.Book;

public interface BookMapper {
	public String test(String keyword);
	
	public boolean insertBook(Book book);
	public ArrayList<Book> dbBookFind(Book book);
	public ArrayList<Book> selectBookCover(Book book);
	public int updateYNCheck(Book book);
	public int getBooksCount(String userid);
}
