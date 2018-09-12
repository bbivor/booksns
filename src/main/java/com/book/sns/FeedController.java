package com.book.sns;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.book.sns.dao.FavoriteMapper;
import com.book.sns.dao.UserUpdateMapper;
import com.book.sns.dao.kshFeedMapper;
import com.book.sns.dto.Favorite;
import com.book.sns.dto.Feed;
import com.book.sns.dto.Photo;
import com.book.sns.dto.Relationship;
import com.book.sns.dto.SnsUser;


@Controller
public class FeedController {
	
	private static final String UPLOADPATH="C:\\sh\\booksns\\src\\main\\webapp\\resources\\images\\FileRepo";

 
	@Autowired
	SqlSession sqlsession;
	
	
	//泥섏쓬 �삤由ъ��궇 �뵾�뱶瑜� �띀�쓣 �븣 而⑦듃濡ㅻ윭
	@RequestMapping(value = "/feedWrite", method = RequestMethod.POST)
	public @ResponseBody String feedWrite(HttpSession hsession,@RequestParam("files")List<MultipartFile> images
	,@RequestParam("file_count")int file_length,@RequestParam("content")String content,	
	@RequestParam("tag")String tag,@RequestParam("targetId")String targetId) {
		kshFeedMapper mapper=sqlsession.getMapper(kshFeedMapper.class);
		int feedNum=0;
		feedNum=mapper.selectNextVal();
		
		Feed feed = new Feed();
		feed.setFeedNum(feedNum);
		feed.setContent(content);
		feed.setTargetId(targetId);
		feed.setTag(tag);
		feed.setUserId((String)hsession.getAttribute("userId"));
		feed.setOriginalFeedNum(feedNum);
		
		if(images.size() > 0) {
			feed.setPhotoYN("Y");
		}else {
			feed.setPhotoYN("N");
		}
		mapper.insertFeed(feed);

		//�룷�넗媛� �엳�떎硫� 異붽��븯怨� �뾾�떎硫� 異붽� X
		if(images.size()>0) {
		for(int i=0;i<images.size();i++) {
			if(images.get(i).getOriginalFilename()!=null){
			Photo photo=new Photo();
			UUID uuid=UUID.randomUUID();
			photo.setFeedNum(feedNum);
			photo.setOriginImg(images.get(i).getOriginalFilename());
			photo.setSaveImg(uuid+"_"+images.get(i).getOriginalFilename());
			mapper.insertPhoto(photo);
			File file=new File(UPLOADPATH,uuid+"_"+images.get(i).getOriginalFilename());
			//�뿬湲곗꽌 �씠�젣 ���옣�릺寃좎�?
			try {
				images.get(i).transferTo(file);	
			} catch (Exception e) {	
				e.printStackTrace();
			}
			}
		}
		}

		return "success!!!!";
	}
	
/*	@RequestMapping(value = "/feedWrite", method = RequestMethod.POST)
	public String feedW1rite(HttpSession hsession,Feed feed,
			MultipartFile[] upload) {
		System.out.println(feed);
		
		for(MultipartFile a : upload) {
			System.out.println(a.getOriginalFilename());
		}

		return "ksh/feed";
	}
	*/
	
	
	/**
	 * get feed
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getFeed", method = RequestMethod.GET)
	public String profileForm(String userId, Model model, HttpSession session) {

		kshFeedMapper mapper = sqlsession.getMapper(kshFeedMapper.class);
		FavoriteMapper mapper2 = sqlsession.getMapper(FavoriteMapper.class);
		String loginId = (String) session.getAttribute("userId");
		
		
		SnsUser user = mapper.selectProfile(userId);
		model.addAttribute("user", user);

		// feed
		ArrayList<Feed> feed = mapper.selectAllFeed(userId);

			
		// targetId
		for (int i = 0; i < feed.size(); i++) {
			if (feed.get(i).getTargetId() != null) {
				String[] targetId = feed.get(i).getTargetId().split("@");
				feed.get(i).setViewTargetId(targetId);
			}
			feed.get(i).getFeedNum();
			Favorite fav = new Favorite();
	         fav.setFeedNum(feed.get(i).getFeedNum());
	         fav.setId(loginId);
	         
	         feed.get(i).setFavoriteCheck(mapper2.selectFavorite(fav));
			
		}
		
		//getProfilePic
		for (int i = 0; i < feed.size(); i++) {
			
			String profilePic = mapper.selectProfile(feed.get(i).getUserId()).getSaveFile();
			
			feed.get(i).setProfilePic(profilePic);
		}
		
		
		//get photo
		ArrayList<Photo> photo = new ArrayList<Photo>();
		for (int i = 0; i < feed.size(); i++) {
			
			if (feed.get(i).getPhotoYN().equals("Y")) {
				photo = mapper.selectPhoto(feed.get(i).getFeedNum());
				String saveFile="";
				
				for(int j=0;j<photo.size();j++) {
					
					
					if(j<photo.size()-1) {
						saveFile+=photo.get(j).getSaveImg()+",";
					}else {
						saveFile+=photo.get(j).getSaveImg();
					}
					
				}
				
				feed.get(i).setSaveFile(saveFile);
			}
		}
		
		model.addAttribute("feed", feed);
		
		//followList
		ArrayList<Relationship> follow = mapper.following(loginId);
		for (int i = 0; i < follow.size(); i++) {
				System.out.println("followList : " + follow.get(i));
			}
		
		model.addAttribute("follow", follow);
		
		return "feed";
	}
	
	
	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	public String follow(String followId, HttpSession session) {
		
		kshFeedMapper mapper = sqlsession.getMapper(kshFeedMapper.class);
		
		String loginId = (String) session.getAttribute("userId");
		Relationship fu = new Relationship();
		fu.setFollowId(followId);
		fu.setLoginId(loginId);
		
		boolean insert = mapper.follow(fu);
		
		return "redirect:/profileForm?userId=" + fu.getFollowId();
	}
	
	@RequestMapping(value = "/unfollow", method = RequestMethod.GET)
	public String unfollow(String followId, HttpSession session) {
		
		String loginId = (String) session.getAttribute("userId");
		Relationship fu = new Relationship();
		fu.setFollowId(followId);
		fu.setLoginId(loginId);
		
		kshFeedMapper mapper = sqlsession.getMapper(kshFeedMapper.class);
		boolean delete = mapper.unfollow(fu);
		
		return "redirect:/profileForm?userId=" + fu.getFollowId();
	}
	
	

	   @RequestMapping(value = "/mentionForm", method = RequestMethod.GET)
	   public @ResponseBody Map<String,Object> mentionWrite(int feedNum) {
	      kshFeedMapper mapper=sqlsession.getMapper(kshFeedMapper.class);
	      UserUpdateMapper userMapper=sqlsession.getMapper(UserUpdateMapper.class);
	      Feed feed=null;
	      SnsUser user=null;
	      Map<String,Object> map=new HashMap<>();
	      feed=mapper.selectOneFeed(feedNum);
	      user=userMapper.selectOneUser(feed.getUserId());
	      map.put("user", user);
	      map.put("feed", feed);
	      return map;
	   }
	   
	   
	   
	   
	   //硫섏뀡 �뵾�뱶瑜� �씤�꽌�듃 �븷 �뻹 �벐�뒗 �븘�씠.
	   @RequestMapping(value = "mentionInsert", method = RequestMethod.POST)
	   public @ResponseBody String mentionInsert(HttpSession hsession,@RequestParam("files")List<MultipartFile> images
	   ,@RequestParam("file_count")int file_length,@RequestParam("content")String content,@RequestParam("feedNum")int feedNum,   
	   @RequestParam("tag")String tag,@RequestParam("targetId")String targetId,
	   @RequestParam("userId")String userId) {
	      kshFeedMapper mapper=sqlsession.getMapper(kshFeedMapper.class);
	      int selectNextVal=0;
	      selectNextVal=mapper.selectNextVal();
	      ///////////////depth異붽��븯湲� �쐞�빐�꽌 �뵾�뱶�꽆�쑝濡� �븷�뱾 遺덈윭�삤怨� 洹멸구濡� �럞�뒪援ы븯怨� �뜑�빐�꽌 �꽔�뼱以섏빞�븿.
	      Feed formerFeed=mapper.selectOneFeed(feedNum);
	      Feed feed = new Feed();
	      int originDepth=0;
	      originDepth=formerFeed.getDepth();
	      feed.setDepth((originDepth+1));
	      feed.setFeedNum(selectNextVal);
	      feed.setContent(content);
	      feed.setTargetId(targetId);
	      feed.setTag(tag);
	      feed.setUserId((String)hsession.getAttribute("userId"));
	      feed.setOriginalFeedNum(formerFeed.getOriginalFeedNum());
	      if(images.size() > 0) {
	         feed.setPhotoYN("Y");
	      }else {
	         feed.setPhotoYN("N");
	      }
	      mapper.insertMention(feed);

	      //�룷�넗媛� �엳�떎硫� 異붽��븯怨� �뾾�떎硫� 異붽� X
	      if(images.size()>0) {
	      for(int i=0;i<images.size();i++) {
	         if(images.get(i).getOriginalFilename()!=null){
	         Photo photo=new Photo();
	         UUID uuid=UUID.randomUUID();
	         photo.setFeedNum(selectNextVal);
	         photo.setOriginImg(images.get(i).getOriginalFilename());
	         photo.setSaveImg(uuid+"_"+images.get(i).getOriginalFilename());
	         mapper.insertPhoto(photo);
	         File file=new File(UPLOADPATH,uuid+"_"+images.get(i).getOriginalFilename());
	         //�뿬湲곗꽌 �씠�젣 ���옣�릺寃좎�?
	         try {
	            images.get(i).transferTo(file);   
	         } catch (Exception e) {   
	            e.printStackTrace();
	         }
	         }
	      }
	      }

	      return "success!!!!";
	   }
	
	   
	   
	   //���옒   硫섏뀡 �뵾�뱶瑜� �씤�꽌�듃 �븷 �뻹 �벐�뒗 �븘�씠.
	      @RequestMapping(value = "mentionInsert2", method = RequestMethod.POST)
	      public @ResponseBody Map<String,Object> mentionInsert2(HttpSession hsession,@RequestParam("files")List<MultipartFile> images
	      ,@RequestParam("file_count")int file_length,@RequestParam("content")String content,@RequestParam("feedNum")int feedNum,   
	      @RequestParam("tag")String tag,@RequestParam("targetId")String targetId,
	      @RequestParam("userId")String userId) {
	         kshFeedMapper mapper=sqlsession.getMapper(kshFeedMapper.class);
	         UserUpdateMapper mapper1=sqlsession.getMapper(UserUpdateMapper.class);
	         int selectNextVal=0;
	         selectNextVal=mapper.selectNextVal();
	         ///////////////depth異붽��븯湲� �쐞�빐�꽌 �뵾�뱶�꽆�쑝濡� �븷�뱾 遺덈윭�삤怨� 洹멸구濡� �럞�뒪援ы븯怨� �뜑�빐�꽌 �꽔�뼱以섏빞�븿.
	        
	         Feed formerFeed=new Feed();
	         formerFeed=mapper.selectOneFeed((feedNum));
	         System.out.println("�뵾�뱶�꽆踰� 沅곴툑"+feedNum);
	         System.out.println("�룷癒명뵾�뱶 "+formerFeed);
	         Feed feed = new Feed();
	         int originDepth=0;
	         //洹몄쟾 �럞�벐瑜� �삤由ъ쭊 �럞�벐�뿉 吏묒뼱�꽔�쓬.
	         targetId += "@"+formerFeed.getUserId();
	         
	         originDepth=formerFeed.getDepth();
	         feed.setTargetFeedNum(formerFeed.getFeedNum());
	         feed.setDepth((originDepth+1));
	         feed.setFeedNum(selectNextVal);
	         feed.setContent(content);
	         feed.setTargetId(targetId);
	         feed.setTag(tag);
	         feed.setUserId((String)hsession.getAttribute("userId"));
	         feed.setOriginalFeedNum(formerFeed.getOriginalFeedNum());
	         System.out.println(feed);
	         if(images.size() > 0) {
	            feed.setPhotoYN("Y");
	         }else {
	            feed.setPhotoYN("N");
	         }
	         mapper.insertMention(feed);

	         //�룷�넗媛� �엳�떎硫� 異붽��븯怨� �뾾�떎硫� 異붽� X
	         if(images.size()>0) {
	         for(int i=0;i<images.size();i++) {
	            if(images.get(i).getOriginalFilename()!=null){
	            Photo photo=new Photo();
	            UUID uuid=UUID.randomUUID();
	            photo.setFeedNum(selectNextVal);
	            photo.setOriginImg(images.get(i).getOriginalFilename());
	            photo.setSaveImg(uuid+"_"+images.get(i).getOriginalFilename());
	            mapper.insertPhoto(photo);
	            File file=new File(UPLOADPATH,uuid+"_"+images.get(i).getOriginalFilename());
	            //�뿬湲곗꽌 �씠�젣 ���옣�릺寃좎�?
	            try {
	               images.get(i).transferTo(file);   
	            } catch (Exception e) {   
	               e.printStackTrace();
	            }
	            }
	         }
	         }
	         //�씠�젣 紐⑤떖 �궡�뿉�꽌 �쓣�슱 移쒓뎄�뱾�쓣 遺덈윭蹂댁옄�쑝
	         Map<String,Object> map=new HashMap<>();
	         Feed tarae_append=null;
	         tarae_append= mapper.selectOneFeed(selectNextVal);
	         List<Photo> photo_append=null;
	         photo_append=mapper.selectPhoto(tarae_append.getFeedNum());
	         SnsUser sUser=null;
	         sUser = mapper1.selectOneUser(tarae_append.getUserId());
	         map.put("feedNum", feedNum);
	         map.put("feed", tarae_append);
	         map.put("photo", photo_append);
	         map.put("user", sUser);
	         return map;
	      }

}


