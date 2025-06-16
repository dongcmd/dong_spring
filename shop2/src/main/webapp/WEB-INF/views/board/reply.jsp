<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 답글 쓰기</title>
</head>
<body>
<form:form modelAttribute="board" action="reply" bethod="post" name="f">
	<%-- 원글의 정보 --%>
	<form:hidden path="num" />
	<form:hidden path="boardid" />
	<form:hidden path="grp" />
	<form:hidden path="grplevel" />
	<form:hidden path="grpstep" />
	<h2>${boardName} 답글 등록</h2>
	<table class="w3-table">
	<tr><td>글쓴이</td><td><input name="writer">
		<font color="red"><form:errors path="writer" /></font></td></tr>
	<tr><td>비밀번호</td><td><form:password path="pass" />
		<font color="red"><form:errors path="pass" /></font></td></tr>
	<tr><td>제목</td><td><form:input path="title" value="RE:${baord.title}" />
		<font color="red"><form:errors path="title" /></font></td></tr>
	<tr><td>내용</td><td><textarea name="content" rows="15" cols="80" id="summernote"></textarea>
		<font color="red"><form:errors path="content" /></font></td></tr>
	<tr><td colspan="2">
		<a href="javascript:document.f.submit()">[답변글등록]</a></td></tr>
	</table>
</form:form>
<script>
	$(function() {
		  $('#summernote').summernote({
			  height : 300,
			  callbacks : {
					onImageUpload : function(images) {
						for(let i = 0; i < images.length; i++) {
							sendFile(images[i])
						}
					}
			  }
		  })
	});
	function sendFile(file) {
		let data = new FormData(); // 데이터 컨테이너 생성
		data.append("image", file); // 컨테이너에 이미지 객체 추가
		$.ajax({ // ajax를 이용하여 파일 업로드
			url : "/ajax/uploadImage", // 서버 요청 url
			type : "post",						// post 방식
			data : data,							// 전송 데이터
			processData : false,			// 문자열 전송 아님.
			contentType : false,			// 컨텐트 타입 자동 설정 안함. 파일 업로드시 사용
			success : function(src) { // 서버 응답 정상처리
				$("#summernote").summernote("insertImage", src)
			}, error : function(e) { // 서버 응답 오류
				alert("이미지 업로드 실패 : " + e.status)
			}
		})
	}
</script>
</body>
</html>