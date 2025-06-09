<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
   
<%-- isErrorPage="true" exception 전달 가능--%>
<script>
	alert("${exception.message}");
	location.href="${exception.url}";
</script>