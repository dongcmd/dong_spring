<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>
</head>
<body>
<form:form modelAttribute="board" action="write" enctype="multipart/form-data" name="f">
<input type="hidden" name="boardid" value="${param.boardid}">
<table class="w3-table">
	<tr><th>글쓴이</th><td><form:input path="writer" class="w3-input" />
				<font color="red"><form:errors path="writer" /></font></td></tr>
	<tr><th>비밀번호</th><td><form:password path="pass" class="w3-input" />
				<font color="red"><form:errors path="pass" /></font></td></tr>
	<tr><th>제목</th><td><form:input path="title" class="w3-input" />
				<font color="red"><form:errors path="title" /></font></td></tr>
	<tr><th>내용</th>
			<td><form:textarea path="content" rows="15" cols="80"  id="summernote" />
				<font color="red"><form:errors path="content" /></font>
	</td></tr>
	<tr><th>첨부파일</th><td><input type="file" name="file1" ></td></tr>
	<tr><td colspan="2" class="w3-center">
			<a href="javascript:document.f.submit()">[게시물등록]</a>
			<a href="list?boardid=${param.boardid}">[게시물목록]</a></td></tr>
</table></form:form>
<script>
	$(function() {
		  $('#summernote').summernote({
			  height : 300,
			  callbacks : {
					// onImageUpload : 이미지 업로드하는 이벤트
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
				// <img src="/board/image/파일명" ... />
			}, error : function(e) { // 서버 응답 오류
				alert("이미지 업로드 실패 : " + e.status)
			}
		})
	}
</script></body></html>