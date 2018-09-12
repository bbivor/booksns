package com.book.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.SocketUtils;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.book.sns.dao.SnsDmMapper;
import com.book.sns.dto.DmContent;
import com.book.sns.dto.DmContentList;
import com.book.sns.dto.DmList;
import com.book.sns.dto.Relationship;
import com.book.sns.dto.SnsUser;


@Controller
public class SnsDmController {

   
   @Autowired
   SqlSession sqlSession;
   
   //DM페이지로 보내기
   @RequestMapping(value = "/dmGo", method = RequestMethod.GET)
   public String dmGo(HttpSession hsession,DmContentList dmContentList,Model model) {
      
      System.out.println("DM페이지로 오셨습니다");
      
      SnsDmMapper mapper = sqlSession.getMapper(SnsDmMapper.class);
      
      ArrayList<DmContentList> result = mapper.getDmRecentById(dmContentList);
      
      //출력위해서
      model.addAttribute("dmContentList",result);
      
      
      return "dmList";
   }
      
   
   //DM 팔로워 리스트 페이지
   @RequestMapping(value = "/dmFollowingListGo", method = RequestMethod.GET)
   public String dmFollowingList(HttpSession hsession,Model model) {
      System.out.println("<DM팔로워를 선택하는 페이지로 왔습니다>");
      
      //userId세션 저장
      SnsUser snsUser = new SnsUser();
      snsUser.setUserId((String)hsession.getAttribute("userId"));
      
      //followerList 불러오기
      SnsDmMapper mapper =sqlSession.getMapper(SnsDmMapper.class);
      
      ArrayList<Relationship> result = mapper.selectFollowingId(snsUser); 
      
      model.addAttribute("relationship",result);
      
      System.out.println("userId :: "+snsUser.getUserId());
      
      return "dmFollowingList";
   }

   
   
   //DM 팔로워 선택하고 DM페이지로 이동 
   @RequestMapping(value = "/dmRoomGo", method = RequestMethod.GET)
   public String dmRoomGo(HttpSession hsession,Relationship relationship, String followId,DmContent dmContent) {
   
      System.out.println("<DM페이지로 오셨습니다.>");
      System.out.println("followId::"+followId);
      
      //로그인아이디 세션 저장
      relationship.setLoginId((String)hsession.getAttribute("userId"));
      
      //dmCheck (방 존재하는지 확인)
      SnsDmMapper mapper =sqlSession.getMapper(SnsDmMapper.class);
      
      DmList dmCheckResult = mapper.dmCheck(relationship);
      //hsession.setAttribute("dmNum", dmCheckResult.getDmNum()); //데이터가 있다는 걸 가정해서 쓴 거니까
      
         //방이 존재하지 않는 경우 
         if(dmCheckResult==null) { //데이터가없는것을 가정하는 것은 모순됨 따라서 데트코드.
            System.out.println("방이 없습니다.");
            
            //dmRoom만들기
            int dmRoomCreateResult = mapper.dmRoomCreate(relationship);
   
            if(dmRoomCreateResult==1) {
               System.out.println("새로운 방이 만들어졌습니다.");
               DmList dmCheckResult2 = mapper.dmCheck(relationship);
               System.out.println("DmRoom번호 :: "+dmCheckResult2.getDmNum());
               hsession.setAttribute("dmNum", dmCheckResult2.getDmNum());
               hsession.setAttribute("fromId", dmCheckResult2.getFromId());
               hsession.setAttribute("followId", followId);
            }
            
         }else {
            System.out.println("이미 DM방이 존재합니다.");

            System.out.println("DmRoom번호 :: "+dmCheckResult.getDmNum());
            hsession.setAttribute("dmNum", dmCheckResult.getDmNum()); //else로 넣으면 if의 예외니까 따라서 돌아감.
            hsession.setAttribute("followId", followId);
         }
         

      return "dm";
   }
   
   //DM 전송
   @RequestMapping(value = "/dmSubmit", method = RequestMethod.POST)
   public @ResponseBody int dmSubmit(HttpSession hsession,@RequestBody DmContent dmContent) {

      System.out.println(dmContent);
      
      //dmCheck (방 존재하는지 확인)
      SnsDmMapper mapper =sqlSession.getMapper(SnsDmMapper.class);
      
      int result = mapper.dmSubmit(dmContent);
      
      if(result==1) {
         System.out.println("dm전송 성공");
      }else {
         System.out.println("dm전송 실패");
      }
      return result;
   }

   //DM 내용 가져오기
   @RequestMapping(value="/dmContentList", method = RequestMethod.POST)
   public @ResponseBody ArrayList<DmContent> selectDm(int dmNum,String followId, String dmUserId) {
      
      
      SnsDmMapper mapper = sqlSession.getMapper(SnsDmMapper.class);
      
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("followId", followId);
      map.put("dmUserId", dmUserId);
      map.put("dmNum", dmNum);//map에 dmnum 집어 넣기
      
      ArrayList<DmContent> result = mapper.selectDm(map);

      return result;
   }
   
   
}