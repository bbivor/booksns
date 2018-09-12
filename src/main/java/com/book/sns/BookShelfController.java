package com.book.sns;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.book.sns.dao.BookMapper;
import com.book.sns.dao.ProfileMapper;
import com.book.sns.dto.Book;


@Controller
public class BookShelfController {

   
   @Autowired
   SqlSession sqlSession;
   
   //등록된 책 가져오기
   @RequestMapping(value = "/bookShelf", method = RequestMethod.GET)
   public String bookShelf(HttpSession hsession,Model model,Book book) {
      
      //각 유저의 서재가 가져오기 위해서
      ProfileMapper mapper = sqlSession.getMapper(ProfileMapper.class);
      String loginId = (String) hsession.getAttribute("userId");
      
      //서재 정보 가져오기
      BookMapper mapper2 = sqlSession.getMapper(BookMapper.class);
      ArrayList<Book> result = mapper2.selectBookCover(book);
      
      model.addAttribute("bookShelf",result);
      
      return "bookShelf";
   }
   
   //책 클릭시 이동
   @RequestMapping(value = "/goBookContent", method = RequestMethod.GET)
   public String goBookContent(Model model, String searchIsbn) {
      
      String authorization = "KakaoAK 62f3a956ce11a4820b9408378304d68d";
      String result = "";
      Book oneBook = new Book();
      String[] num = searchIsbn.split(" ");
      
      System.out.println("isbn : " + num[0]);
      
      try {
           
           String keyword = URLEncoder.encode(num[0], "UTF-8");
            String apiURL = "https://dapi.kakao.com/v2/search/book?target=isbn&query=" + keyword;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", authorization);
            int responseCode = con.getResponseCode();
            
            System.out.println("response code : " + responseCode);
            
            
            BufferedReader br;
            if(responseCode==200) {                                        // �젙�긽 �샇異�
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {                                                     // �뿉�윭 諛쒖깮
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                result = result + inputLine.trim();
                
                System.out.println(inputLine);
                
            }
            br.close();
            
            JSONParser jp = new JSONParser();
            JSONObject obj = (JSONObject) jp.parse(result);
            JSONArray documents = (JSONArray) obj.get("documents");
            String author = "";
            for (int i = 0; i < documents.size(); i++) {
               JSONObject document = (JSONObject) documents.get(i);
               JSONArray authors = (JSONArray) document.get("authors");
               Iterator<String> iterator = authors.iterator();
               while (iterator.hasNext()) {
                  author = author + " " + iterator.next();
               }
               
               String category = (String) document.get("category").toString();
               String contents = (String) document.get("contents").toString();
               String isbn = (String) document.get("isbn").toString();
               String publisher = (String) document.get("publisher").toString();
               String thumbnail = (String) document.get("thumbnail").toString();
               String title = (String) document.get("title").toString();
               String price = (String) document.get("price").toString();
               
               oneBook.setAuthors(author);
               oneBook.setContents(contents);
               oneBook.setCategories(category);
               oneBook.setIsbn(isbn);
               oneBook.setPublisher(publisher);
               oneBook.setThumbnail(thumbnail);
               oneBook.setTitle(title);
               oneBook.setPrice(price);
               
               
            }
            

            
            //error
        } catch (Exception e) {
            System.out.println(e);
        }
        
      model.addAttribute("book", oneBook);
      
      
      return "/bookShelfContent";
   }

   
   //등록된 책 Y/N 변경
      /*@RequestMapping(value = "/updateYNCheck", method = RequestMethod.GET)
      public String updateYNCheck(Book book,String readYN) {
         
         BookMapper mapper = sqlSession.getMapper(BookMapper.class);
         int result = mapper.updateYNCheck(book);
         
         System.out.println("독서확인"+book.getReadYN());
         
         return "/bookShelfContent";
      }*/
      
  
}