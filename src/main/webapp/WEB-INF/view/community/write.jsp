<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="<c:url value="/static/js/jquery-3.3.1.min.js"/>"
	type="text/javascript"></script>
<script type="text/javascript">
	$().ready(function() {

		<c:if test="${mode == 'modify' && not empty communityVO.displayFilename}">
		$("#file").closest("div").hide();
		</c:if>

		$("#displayFilename").change(function() {
			
			var isChecked = $(this).prop("checked");
			console.log(isChecked);
			if (isChecked) {
				$("label[for=displayFilename]").css({
					"text-decoration-line" : "line-through",
					"text-decoration-style" : "double",
					"text-decoration-color" : "#FF0000"
				});
				$("#file").closest("div").hide();
			} else {
				$("label[for=displayFilename]").css({
					"text-decoration" : "none"
				})
			}
		});

		$("#writeBtn").click(function() {
			/* if ($("#title").val() ==""){
				$("#errorTitle").slideDown(300);
				$("#title").focus();
				return false;
			}
			else{
				$("#errorTitle").slideUp(300);
			}
			if ( $("#body").val() ==""){
				$("#errorBody").slideDown(300);
				$("#body").focus();
				return false;
			}
			else{
				$("#errorBody").slideUp(300);
			}
			
			if ( $("#writeDate").val() == "" ){
				$("#errorWriteDate").slideDown(300);
				$("#writeDate").focus();
				return false;
			}
			else{
				$("#errorWriteDate").slideUp(300);
			} */

			var mode = "${mode}";
			if (mode == "modify") {
				var url = "<c:url value="/modify/${communityVO.id}"/>";
			} else {
				var url = "<c:url value="/write"/>"
			}

			var writeForm = $("#writeForm");
			writeForm.attr({
				"method" : "post",
				"action" : url
			});
			writeForm.submit();
		});

	});
</script>
</head>
<body>

	<div id="wrapper">
		<jsp:include page="/WEB-INF/view/template/menu.jsp" />
	</div>
	<div>
		<form:form modelAttribute="writeForm" enctype="multipart/form-data">
			<c:if test="${sessionScope.status eq 'fail' }">
				<div id="invalidInsert">
					<div>값을 채워주세요.</div>
				</div>
			</c:if>
			<div>
				<input type="text" id="title" name="title" placeholder="제목"
					value="${communityVO.title }"></input>
			</div>
			<div>
				<form:errors path="title" />
			</div>
			<div id="errorTitle" style="display: none;">제목을 입력하세요</div>
			<div>
				<textarea rows="10" cols="30" id="body" name="body" placeholder="내용">${communityVO.body}</textarea>
			</div>

			<c:if
				test="${mode =='modify' && not empty communityVO.displayFilename}">
				<div>
					<input type="checkbox" id="displayFilename" name="displayFilename"
						value="${communityVO.displayFilename}" />
					<label for="displayFilename">
					${communityVO.displayFilename} </label>
				</div>
			</c:if>

			<div>
				<form:errors path="body" />
			</div>
			<div id="errorBody" style="display: none;">내용을 입력하세요</div>
			<div>
				<input type="hidden" id="nickname" name="nickname" placeholder="작성자"
					value="${sessionScope.__USER__.nickname}"></input>
			</div>
			<div>
				<input type="hidden" id="userId" name="userId" placeholder="사용자아이디"
					value="${sessionScope.__USER__.id}"></input>
			</div>
			<div>
				<input type="date" id="writeDate" name="writeDate"
					placeholder="작성 일자" value="${communityVO.writeDate}"></input>
			</div>
			<div>
				<form:errors path="writeDate" />
			</div>

			<div>
				<input type="file" id="file" name="file" />
			</div>

			<div id="errorWriteDate" style="display: none;">날짜를 입력하세요.</div>
		</form:form>
	</div>
	<input type="button" id="writeBtn" value="등록" />
</body>
</html>