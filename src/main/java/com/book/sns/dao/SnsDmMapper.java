package com.book.sns.dao;

import java.util.ArrayList;
import java.util.Map;

import com.book.sns.dto.DmContent;
import com.book.sns.dto.DmContentList;
import com.book.sns.dto.DmList;
import com.book.sns.dto.Relationship;
import com.book.sns.dto.SnsUser;

public interface SnsDmMapper {
   public ArrayList<Relationship> selectFollowingId(SnsUser snsUser); //팔로잉리스트 가져오기
   public DmList dmCheck(Relationship relationship); //방 중복 확인
   public int dmRoomCreate(Relationship relationship); //DM방 생성
   public int dmSubmit(DmContent dmContent);//dm 전송
   public ArrayList<DmContent> selectDm(Map<String, Object> map); //dm 내용불러오기
   public ArrayList<DmContentList> getDmRecentById(DmContentList dmContentList); //dm 최근 내용 (방가져오기)
}