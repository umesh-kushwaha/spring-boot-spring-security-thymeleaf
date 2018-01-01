<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
Hi, 
<sec:authorize access="hasRole('ADMIN')">
    Admin
</sec:authorize>
<sec:authorize access="hasRole('USER')">
    User
</sec:authorize>
</body>
</html>