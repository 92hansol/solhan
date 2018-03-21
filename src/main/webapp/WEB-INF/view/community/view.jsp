<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<div id="wrapper">
		<jsp:include page="/WEB-INF/view/template/menu.jsp" />
	</div>

		<h1>${community.title }</h1>
		<h3>${community.memberVO.nickname}(${community.memberVO.email})
			${community.requestIp}</h3>
		<p>${community.viewCount} | ${community.recommendCount}
		<p>
		<p>${community.writeDate}</p>
		<p></p>
		<c:if test="${not empty community.displayFilename }">
			<p>
				<a href="<c:url value="/get/${community.id}"/>">
					${community.displayFilename} </a>
			</p>

		</c:if>

		<p>${community.body}</p>
		<div>
			<a href="<c:url value="/"/>">뒤로가기</a> 
			<a href="<c:url value="/recommend/${community.id}"/>">추천하기</a>
			<a href="<c:url value="/modify/${community.id}"	/>">수정하기</a>
			<c:if test="${sessionScope.__USER__.id == community.memberVO.id}"> 
				<a href="<c:url value="/delete/${community.id}"/>">삭제하기</a>
			</c:if>
		</div>
</body>
</html>