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
import org.springframework.web.bind.annotation.ResponseBody;

import com.book.sns.dao.BookMapper;
import com.book.sns.dto.Book;


@Controller
public class BookAddController {

	
	@Autowired
	SqlSession sqlSession;
	
	
	
	/**
	 * go to bookAddForm
	 * @return
	 */
	@RequestMapping(value = "/bookAddForm", method = RequestMethod.GET)
	public String bookAddForm() {
		
		return "bookAddForm";
	}
	
	@RequestMapping(value = "/selfAdd", method = RequestMethod.GET)
	public String selfAdd() {
		
		return "selfAdd";
	}
	
	
	/**
	 * search through api
	 * @param searchIsbn
	 * @param model
	 * @return
	 */
/*	@RequestMapping(value = "/bookFind", method = RequestMethod.GET)
	public String bookAdd(String searchIsbn, Model model) {
		
		String authorization = "KakaoAK 62f3a956ce11a4820b9408378304d68d";
		String result = "";
		HashMap<String, String> book = new HashMap<String, String>();
		
		try {
        	
        	String keyword = URLEncoder.encode(searchIsbn, "UTF-8");
            String apiURL = "https://dapi.kakao.com/v2/search/book?target=isbn&query=" + keyword;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", authorization);
            int responseCode = con.getResponseCode();
            
            System.out.println("response code : " + responseCode);
            
            
            BufferedReader br;
            if(responseCode==200) { 													// �젙�긽 �샇異�
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  																	// �뿉�윭 諛쒖깮
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
            	
            	
            	book.put("authors", author);
            	book.put("categories", category);
            	book.put("contents", contents);
            	book.put("isbn", isbn);
            	book.put("publisher", publisher);
            	book.put("thumbnail", thumbnail);
            	book.put("title", title);
            	book.put("price", price);
            	
            	for (Entry<String, String> entry : book.entrySet()) {
            		System.out.println("key : " + entry.getKey());
            		System.out.println("value : " + entry.getValue());
            	}
            }
            
            
            //error
        } catch (Exception e) {
            System.out.println(e);
        }
        
		model.addAttribute("book", book);
		return "/bookAddForm";
	}*/
	
	
	
	
	@RequestMapping(value = "/bookFind", method = RequestMethod.GET)
	public String bookAdd(String searchIsbn, Model model) {
		
		String authorization = "KakaoAK 62f3a956ce11a4820b9408378304d68d";
		String result = "";
		ArrayList<Book> book = new ArrayList<>();
		
		
		try {
        	
        	String keyword = URLEncoder.encode(searchIsbn, "UTF-8");
            String apiURL = "https://dapi.kakao.com/v2/search/book?target=isbn&query=" + keyword;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", authorization);
            int responseCode = con.getResponseCode();
            
            System.out.println("response code : " + responseCode);
            
            
            BufferedReader br;
            if(responseCode==200) { 													// �젙�긽 �샇異�
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  																	// �뿉�윭 諛쒖깮
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
            	Book oneBook = new Book();
            	
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
            	
            	book.add(oneBook);
            	
            }
            
            for (Book b : book) {
            	System.out.println(b);
            }
            
            //error
        } catch (Exception e) {
            System.out.println(e);
        }
        
		model.addAttribute("book", book);
		model.addAttribute("currentPage", "bookFind");
		return "/bookFind";
	}
	
	
	
	/**
	 * insert into book
	 * @param book
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/bookAdd", method = RequestMethod.POST)
	public String bookAdd(Book book, HttpSession session) {
		
		
		String userId = (String) session.getAttribute("userId");
		
		book.setUserId(userId);
		
		System.out.println(book);
		
		BookMapper mapper = sqlSession.getMapper(BookMapper.class);
		
		mapper.insertBook(book);
		
		
		return "redirect:/bookFind";
	}
	
	@RequestMapping(value = "/goBookAdd", method = RequestMethod.GET)
	public String goBookAdd(Model model, String searchIsbn) {
		
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
            if(responseCode==200) { 													// �젙�긽 �샇異�
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  																	// �뿉�윭 諛쒖깮
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
		
		
		return "/bookAddForm";
	}
	
	
	//validation
	@RequestMapping(value = "/dbBookFind", method = RequestMethod.POST)
	public @ResponseBody boolean dbBookFind(Book book, HttpSession session) {
		
		BookMapper mapper = sqlSession.getMapper(BookMapper.class);
		String userId = (String) session.getAttribute("userId");
		book.setUserId(userId);
		
		
		ArrayList<Book> result = mapper.dbBookFind(book);
		
		boolean tf = result.isEmpty();
		
		return tf;
	}
}
