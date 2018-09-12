<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>책 SNS </title>


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

.dmContent {

   width: 600px;

}
</style>
   
<script>
   //주소에서 파라미터 값을 가져옴. 내가 get으로 설정해줘서.
   $.urlParam = function(name){
       var results = new RegExp('[\?&amp;]' + name + '=([^&amp;#]*)').exec(window.location.href);
       return results[1] || 0;
   }
   // var mno =  $.urlParam('mno'); //mno에다가 내가 가져오고 싶은 파라미터값 넣어줌.
   $(function(){
      $('.ui.selection.dropdown').dropdown();
   });
   
   /* DM  */
   $(function(){
      $('#send').on('click',function(){
         submitFunction();
      });
   });
   
   //메시지 보내는 submit 기능
   function submitFunction(){
      
      var dmNum = $('.dmNum').val();
      var userId= $('.userId').val();
      var dmContent = $('#dmContent').val();
      
      var sendData = {
            "dmNum" : dmNum,
            "dmUserId" : userId,
            "dmContent" : dmContent            
      }
      
      $.ajax({
         method: "POST",
         url : "dmSubmit",
         data : JSON.stringify(sendData),
         dataType : 'json',
         contentType : 'application/json; charset=UTF-8'
      });
      //성공적으로 값을 보냈든 아니든 메시지 보냈으니 값을 비워줘.
      $('#dmContent').val('');
   }
   
   //dm내용 출력
   function dmContentList(type) {
      var userId = $('.userId').val();
      var followId = "<%=session.getAttribute("followId")%>";
      /* //input hidden 안만들어도 가능 */
      var dmNum = $('.dmNum').val();
      
      $.ajax({
         
         method: 'POST',
         url : 'dmContentList',
         data : {
            "followId" : followId,
            "dmUserId" : userId,
            "dmNum"   : dmNum
         },
          
         success : function(data){
            
             if(data==null){
                return;
             }else{
               $('#dmList').html('');
               
               
               for(i in data){
                  if(data[i].dmNum != null && data[i].dmUserId !=null && data[i].dmContent != null){
                     
                     addDm(data[i].dmNum,data[i].dmUserId,data[i].dmContent);
                  }
               }   
             }
            
         }
         
      });
   }
    
    function addDm(dmNum,userid,dmContent){

      $('#dmList').append(
               '<div style="position: relative;">'+
               '<div style="display: flex;">'+
               '<div style="margin-right: 10px;">'+
               userid+
               '</div>'+
               '<div>'+
               dmContent+
               '<input type="hidden" value="'+dmNum+'">'+
               '</div>'+
               '</div>'+
               '</div>'
      );
      $('#dmList').scrollTop($('#dmList')[0].scrollHeight);
   }  
    
    function getInfiniteDm(){ //몇 초 간격으로 새로운 메시지 가져오는 걸 의미
         
         var dmNum = $('.dmNum').val();
         setInterval(function(){
            dmContentList(dmNum);
         },100); //3초에 1번씩 실행됨
      }
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
   <input type="hidden" class="dmNum" value="${sessionScope.dmNum}">
   <input type="hidden" class="userId" value="${sessionScope.userId}">


   <div class="container" style="margin-top: 80px; border: 1px solid #dddddd">
      <div class="row">
      
         <div class="title">
            <h4>DM</h4>
         </div>
         
         <div id="dm" class="dm">
            <!-- dm내용 출력되는 곳 -->
            <div id="dmList" class="dmList"   style="overflow-y : auto; width:500px; height: 300px; border: 1px solid;">
               
            </div>
            <br/>
            <!-- dm입력 -->
            <div id="dmInput" class="dmInput">
               <div class="row" style="height: 90px; display: flex;">
                     <div>
                        <textarea id="dmContent" class="dmContent" placeholder="메시지를 입력하세요" maxlength="100"></textarea>
                     </div>
                     
                     <div>
                        &nbsp;
                        <button type="button" name="send" id="send">전송</button> 
                     </div>
               </div>
            </div>
         </div>
      </div>
   </div>
   
   <script type="text/javascript">
   $(document).ready(function(){
      dmContentList();
      getInfiniteDm();
   });
   
   </script>
</body>
</html>