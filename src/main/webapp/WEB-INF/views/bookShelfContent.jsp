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
   <script src="./resources/js/bookAdd.js"></script>
   <script src="./resources/js/menubar.js"></script>
   <link rel="stylesheet" href="./resources/css/menubar.css">
   <link rel="stylesheet" href="./resources/css/bookAdd.css">

<script>


$(function(){
   $('#selfBtn').click(function(){
      location.href="selfAdd";
   });
   
   $('#bookDeleteBtn').click(function(){
	   $('#bookAdd').submit();
   });
   
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
               <c:if test="${user.saveFile == null }">
                  <img class="profileImg" src="./resources/images/defaultAvatar.png"><br>
                  ${sessionScope.userId }
               </c:if>
               <c:if test="${user.saveFile != null }">
                  <img class="profileImg" src="./resources/images/FileRepo/${user.saveFile }"><br>
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
         </div>
      </div>
</div>


<!-- ==============================================contetnt====================================================== -->

<div class="container" >
   <div class="outer">
      <div class="inner">
         <div class="centered">
         
         
            <c:if test="${book != null}">
                  <div class="searchresult ui segment">
                     <form action="bookAdd" method="post" id="bookAdd">
                        <input type="hidden" name="thumbnail" value="${book.thumbnail}">
                        <input type="hidden" name="title" id="title" value="${book.title}">
                        <input type="hidden" name="authors" value="${book.authors}">
                        <input type="hidden" name="publisher" value="${book.publisher}">
                        <input type="hidden" name="contents" value="${book.contents}">
                        <input type="hidden" name="categories" value="${book.categories}">
                        <input type="hidden" name="isbn" value="${book.isbn}">
                        <input type="hidden" name="memo" id="memoInput" value="">
                        <input type="hidden" name="readYN" id="readYN" value="">
                     </form>
                  
                     <img src="${book.thumbnail}">
                        
                        <div class="ui list">
                           
                           <div class="item">
                              <h3 class="ui header">Title</h3>
                              ${book.title}
                           </div>
                           
                           <div class="item">
                              <h3 class="ui header">Author</h3>
                              ${book.authors}
                           </div>
                           
                           <div class="item">
                              <h3 class="ui header">Publisher</h3>
                              ${book.publisher }
                           </div>
                           
                           <div class="item">
                              <h3 class="ui header">ISBN</h3>
                              ${book.isbn}
                           </div>
                           
                           <div class="item">
                              <h3 class="ui header">Description</h3>
                              ${book.contents}
                           </div>
                           
                           <div class="item">
                              <h3 class="ui header">Category</h3>
                              ${book.categories}
                           </div>
                           
                           <div class="item">
                              <h3 class="ui header">Read</h3>
                              <div class="ui input">
                                 <input type="radio" name="readYN" value="Y">읽음
                                 <input type="radio" name="readYN" value="N" checked="checked">안읽음
                              </div>
                           </div>
                           
                           <div class="item">
                              <h3 class="ui header">Memo</h3>
                              <textarea class="ten thirteen wide column" rows="10" cols="95" name="memo" id="memo"></textarea>
                           </div>
                           
                           <div class="item">
                              <button class="ui basic right floated button" id="bookDeleteBtn">
                                 <i class="trash icon"></i>
                                 삭제하기
                              </button>
                              
                              <button class="ui basic right floated button" id="bookUpdateBtn">
                                 <i class="eraser icon"></i>
                                 수정하기
                              </button>
                           </div>
                        </div>   
                     </div>
               </c:if>
      
            
         </div>
      </div>
   </div>
</div>
</body>
</html>