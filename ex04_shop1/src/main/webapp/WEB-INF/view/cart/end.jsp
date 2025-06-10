<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주문완료</title>
</head>
<body><h2>${sale.user.username}님 주문하신 정보입니다.</h2>
<table>
	<tr><td width="30%">주문아이디</td>
			<td width="70%">${sessionScope.loginUser.userid}</td></tr>
	<tr><td width="30%">이름</td>
			<td width="70%">${sessionScope.loginUser.username}</td></tr>
	<tr><td width="30%">우편번호</td>
			<td width="70%">${sessionScope.loginUser.postcode}</td></tr>
	<tr><td width="30%">주소</td>
			<td width="70%">${sessionScope.loginUser.address}</td></tr>
	<tr><td width="30%">전화번호</td>
			<td width="70%">${sessionScope.loginUser.phoneno}</td></tr>
</table>
<h2>주문 상품</h2>
<table>
	<tr><th>상품명</th><th>가격</th><th>수량</th><th>합계</th></tr>
	<c:forEach items="${sale.itemList}" var="saleitem">
		<tr><td>${saleitem.item.name}</td><td>${saleitem.item.price}</td>
				<td>${saleitem.quantity}</td>
				<td><fmt:formatNumber value="${saleitem.item.price * saleitem.quantity}"
				pattern="###,###" /></td></tr>
	</c:forEach>
	<tr><td colspan="4" align="right">
		총 주문 금액 : <fmt:formatNumber value="${sale.total}" pattern="###,###" /></td></tr>
		<tr><td colspan="4"><a href="../item/list">상품 목록</a>
	</td></tr></table>
</body>
</html>