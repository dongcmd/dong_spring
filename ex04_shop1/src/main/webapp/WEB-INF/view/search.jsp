<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${title}찾기</title>
</head>
<body>
<table>
	<tr><th>${title} : </th><td>${result}</td></tr>
	<tr><td colspan="2">
			<c:if test="${title=='아이디'}">
				<input type="button" value="아이디전송" onclick="sendclose()"></c:if>
			<c:if test="${title!='아이디'}">
				<input type="button" value="닫기" onclick="self.close()"></c:if></td></tr>
</table>
<script>
	function sendclose() {
		opener.document.loginForm.userid.value='${result}';
		self.close();
	}
</script>
</body>
</html>