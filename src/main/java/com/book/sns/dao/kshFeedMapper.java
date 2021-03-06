package com.book.sns.dao;

import java.util.ArrayList;

import com.book.sns.dto.Feed;
import com.book.sns.dto.Photo;
import com.book.sns.dto.Relationship;
import com.book.sns.dto.SnsUser;

public interface kshFeedMapper {
	public int selectNextVal();
	public int insertFeed(Feed feed);
	public int insertPhoto(Photo photo);
	
	public SnsUser selectProfile(String userId);
	public ArrayList<Feed> selectAllFeed(String userId);
	public ArrayList<Feed> selectMention(Feed feed);
	public ArrayList<Photo> selectPhoto(int feedNum);
	public ArrayList<Feed> selectDepthOne(int originalFeedNum);
	public ArrayList<Feed> selectNoMentionFeed(int feedNum);
	public ArrayList<Relationship> following(String userId);
	
	public boolean follow(Relationship relationship);
	public boolean unfollow(Relationship relationship);
	public Feed selectOneFeed(int feedNum);
	public void insertMention(Feed feed);
}
