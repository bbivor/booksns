<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${sessionScope.userId}</title>


	<script src="https://cdn.anychart.com/releases/8.3.0/js/anychart-base.min.js"></script>
	<script src="https://cdn.anychart.com/releases/8.3.0/themes/coffee.min.js"></script>
	<script src="https://cdn.anychart.com/releases/8.3.0/js/anychart-ui.min.js"></script>
	<script src="https://cdn.anychart.com/releases/8.3.0/js/anychart-exports.min.js"></script>
	<script src="https://cdn.anychart.com/releases/8.3.0/js/anychart-radar.min.js"></script>
	<link rel="stylesheet" href="https://cdn.anychart.com/releases/8.3.0/css/anychart-ui.min.css" />
	<link rel="stylesheet" href="https://cdn.anychart.com/releases/8.3.0/fonts/css/anychart-font.min.css" />
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<link rel="stylesheet" type="text/css" href="./resources/semanticUI/semantic.css">
	<script src="./resources/semanticUI/semantic.js"></script>
	<link rel="stylesheet" href="./resources/css/menubar.css">
	<script src="./resources/js/menubar.js"></script>
	<script src="./resources/js/modal.js"></script>
	<script src="./resources/js/chart.js"></script>
	
<style type="text/css">

.feed {

	margin-top: 100px;
	
}


#container {

      width: 800px;
      height: 500px;
      padding: 0;
      z-index: 1;
      
}
    


#tareModal {

	position: relative;
	padding: 30px;
	display: none;

}

.ui.tare.modal {

	padding: 1rem;

}


.img_show_example{
width:50px;
height:30px;
}
.tarae_img{
width:50px;
height:30px;
}

.feed .ui .segment {

	width: 40%;

}


</style>

<script>


$(function(){
	$('.ui.top.left.pointing.dropdown').dropdown({
		action: 'hide',
		onChange: function($selectedItem) {
			alert($selectedItem);
 			if ($selectedItem == 'unfollow') {
				$('#targetUnfollow').submit();
			}
			if ($selectedItem == 'follow') {
				$('#targetFollow').submit();
			}
		}
	});

});


$(function(){
	$('#follow').click(function(){
		$('#followForm').submit();
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

<!-- ========================feed(div)================================================== -->
     
     <c:forEach varStatus="num" var="feed" items="${feed }">
    	<div class="feed" align="left">	
    		<div class="feedInner ui segment">
    			<div class="ui list">

<!-- ================================= userid ===================================================== -->		    
		   
		   <div class="item">
	    	<div class="feedUser">
		    	<c:if test="${feed.profilePic == null}">
					<img class="ui avatar image" src="./resources/images/defaultAvatar.png">
		    	</c:if>
		    	<c:if test="${feed.profilePic != null }">
					<img class="ui avatar image" src="./resources/images/FileRepo/${feed.profilePic }">
		    	</c:if>
		    
		    	<!-- 로그인한 아이디랑 feed userid랑 같을때 -->
		    	<c:if test="${sessionScope.userId == feed.userId }">
			    	<div class="ui top left pointing floating dropdown">
				    	<div class="content">
				    		${feed.userId }
				    	</div>
						<div class="menu">
							<div class="item"><a href="profileForm?userId=${feed.userId}">${feed.userId }</a></div>
						</div>
					</div>
		    	</c:if>
		    	
		    	
		    	<!-- 로그인한 아이디랑 feed userid랑 다를때 -->
		    	<c:if test="${sessionScope.userId != feed.userId }">
			    	<div class="ui top left pointing dropdown">
				    	<div class="content">
				    		${feed.userId }
				    	</div>
						<div class="menu">
							<div class="item"><a href="profileForm?userId=${feed.userId}">${feed.userId }</a></div>
							<%-- <div class="item" data-value="${feed.userId }">
							<c:set var="flag" value="false"/>
								<c:choose>
									<c:when test="${follow == '[]' }">
										<c:if test="${not flag }">
											Follow
											<c:set var="flag" value="true"/>
										</c:if>
									</c:when>
									<c:when test="${follow != '[]' }">
										<c:forEach var="id" items="${follow }">
											<c:if test="${id.followId == feed.userId }">
												<c:if test="${not flag }">
													Unfollow
													<c:set var="flag" value="true"/>
												</c:if>
											</c:if>
											<c:if test="${id.followId != feed.userId }">
												<c:if test="${not flag }">
													Follow
													<c:set var="flag" value="true"/>
												</c:if>
											</c:if>
		 								</c:forEach>
		 							</c:when>
		 							<c:otherwise>
		 								Unfollow
		 							</c:otherwise>
								</c:choose>
							</div> --%>
							<div class="item">DM</div>
						</div>
					</div>
		    	</c:if>
	    	</div>
	    </div>	

<!-- ================================== targetId ======================================================= -->	    	
	    
	    <div class="item">	
	    	<div class="feedTarget">
	    		<c:if test="${feed.viewTargetId != null }">
	    			<c:forEach var="targetId" items="${feed.viewTargetId }" begin="1">
				    	<a href="profileForm?userId=${targetId}"><b>@${targetId}</b></a>
	    			</c:forEach>
	    		</c:if>
	    	</div>	
	    </div>
	    		
<!-- ================================== content+photo+regDate ========================================== -->	    		
	    		
			<div class="item">
	    		<div id="feedContent${num.index}" onclick="ajax(${num.index})">

	    		<input type="hidden" id="feedNum${num.index }" value="${feed.feedNum }">
	    		<input type="hidden" id="targetFeedNum${num.index }" value="${feed.targetFeedNum }">
	    		<input type="hidden" id="originalFeedNum${num.index }" value="${feed.originalFeedNum }">
	    			<br>${feed.content }<br>
		    			<c:if test="${feed.photoYN eq 'Y'}">
		    			<c:forTokens items="${feed.saveFile }" delims="," var="photo">
		    			
								<img class="img_show_example" src="./resources/images/FileRepo/${photo}">
		    				
		    				
		    			</c:forTokens>
	    			</c:if>
	    			<br><font color="#c7c7c7">${feed.regDate }</font><br>
	    		</div>
	    	</div>
	    		
<!-- ========================================= tag ======================================================== -->	    		
	    		
	    	<div class="item">	
	    		<div class="feedTag">
	    			${feed.tag }
	    		</div>
	    	</div>	
	    		
<!-- ========================================= feedMenu =================================================== -->	    		
	    	
	    	<div class="item">
	    		
             <div class="feedMenu">
                <input type="hidden" class="feedNum" value="${feed.feedNum }">
                <i class="large comment outline icon"></i>
                <c:if test="${feed.favoriteCheck == 0 }">
                <i class="large heart outline icon" data-num="${feed.feedNum }"></i>${feed.likeCount }
                </c:if>
                <c:if test="${feed.favoriteCheck == 1 }">
                <i class="large heart icon" data-num="${feed.feedNum }"></i>${feed.likeCount }
                </c:if>
	    			<i class="large paper plane outline icon"></i>${feed.spreadCount }
	    		</div>
	    	<div class="item">	
	    	</div>
	    	</div>
	    	</div>
	    	</div>
	   	</div>
    </c:forEach> 
    
    
<!-- 타래 modal -->

	<div class="ui tare modal"></div>
    


</body>
</html>



