<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Book add </title>


   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
   <link rel="stylesheet" type="text/css" href="./resources/semanticUI/semantic.css">
   <script src="./resources/semanticUI/semantic.js"></script>
   <link rel="stylesheet" href="./resources/css/menubar.css">
   <script src="./resources/js/menubar.js"></script>
   
<style type="text/css">
body{
   overflow: hidden;
}

.container {
  width: 100%;
  height: 100vh;
  max-width: 650px;
  max-height: 500px;
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  -webkit-transform: translateX(-50%);
  overflow: hidden;
}
</style>

<script>
$(function(){
   $('.ui.selection.dropdown').dropdown();
});
</script>

</head>
<body>
<!-- ====================================MENU================================================= -->
<div class="menubar">
	<div class="threeMenu">
		<i class="big bookmark outline icon"></i>
		<a href="dmGo"><i class="big envelope outline icon"></i></a>
		<i class="big bell outline icon"></i>
	</div>
	<div class="searchbar">
		<form action="bookFind" method="get" id="bookFind">
			<div class="ui action input">
				<input id="searchIsbn" name="searchIsbn" type="text" placeholder="Search...">
				<button class="ui basic button" id="searchbtn" type="button">
					<i class="search icon"></i>
				</button>
			</div>
			<input type="hidden" id="login" value="${sessionScope.userId}">
		</form>
	</div>

		<div class="ui selection dropdown">
			<input type="hidden" name="menu" value="">
			<div class="default text">Menu</div>
			<div class="menu">
				<div class="item" data-value="profile">
					<c:if test="${sessionScope.saveFile == null }">
						<img class="profileImg" src="./resources/images/defaultAvatar.png"><br>
						${sessionScope.userId }
					</c:if>
					<c:if test="${sessionScope.saveFile != null }">
						<img class="profileImg" src="./resources/images/FileRepo/${sessionScope.saveFile }"><br>
						${sessionScope.userId }
					</c:if>	
				</div>
				<div class="item">Home</div>
				<div class="item">프로필</div>
				<div class="item">서재</div>
				<div class="item">책 등록</div>
				<div class="item">랭킹</div>
				<div class="item">쪽지</div>
				<div class="item">알람</div>
				<div class="item">로그아웃</div>
			</div>
		</div>
</div>


<!-- ==============================================contetnt====================================================== -->

<div class="container" style="margin-top: 80px; border: 1px solid #dddddd">
   <div class="row">
      
         <div class="title">
            <h1>DM</h1> 
            바로 DM을 보내실 거면 여기로 ---><a href="dmFollowingListGo">글쓰기</a>
         </div>
         
         <br/>
         
         <div id="dmList" class="dmList">
            <!-- dm방 출력되는 곳 -->
            <table>
               <c:forEach var="list" items="${dmContentList}">
                  <tr>
                     <td>@<a href="dmRoomGo?followId=${list.toId}">${list.toId}</a></td>
                     <td>${list.dmContent}</td>
                  </tr>
                  
               </c:forEach>
            </table>
         </div>
      </div>
</div>

</body>
</html>