<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/WEB-INF/view/template/menu.jsp" />
	</div>
	<table>
		<tr>
			<th>ID</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
			<th>내용</th>
			<th>추천수</th>
		</tr>
		<c:forEach items="${communityList}" var="community">
			<tr>
				<td>${community.id}</td>
				<td><a href="<c:url value="/read/${community.id}"/>">
						${community.title} </a> <c:if
						test="${not empty community.displayFilename}">
						<img src="<c:url value="static/ing/file.png"/>"
							alt="${community.displayFilename }" style="width: 20px;" />
					</c:if></td>

				<!-- nickname(Email) -->
				<c:choose>
					<c:when test="${not empty community.memberVO}">
						<td>${community.memberVO.nickname}(${community.memberVO.email})</td>
					</c:when>
					
					<c:otherwise>
						<td>탈퇴한 회원입니다. ${community.requestIp}</td> 
					</c:otherwise>
				</c:choose>
					<td>${community.writeDate}</td>
					<td>${community.viewCount}</td>
					<td>${community.body}</td>
					<td>${community.recommendCount}</td>
			</tr>
		</c:forEach>

		<c:if test="${empty communityList}"></c:if>
		<tr>
			<td colspan="5">등록된 게시글이 없습니다.</td>
		</tr>
	</table>
	<div>
		<a href="<c:url value="/write"/>"> 글쓰기 </a>
	</div>
	</div>
</body>
</html>