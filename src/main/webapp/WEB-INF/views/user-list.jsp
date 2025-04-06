<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dmart.model.User" %>
<!DOCTYPE html>
<html>
<head>
    <title>Users List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">Users List</h2>
    <table class="table table-striped table-bordered">
        <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Age</th>
                <th>Gender</th>
                <th>Role</th>
                <th>Enabled</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${users}">

            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td>${user.phone}</td>
                <td>${user.age}</td>
                <td>${user.gender}</td>
                <td>${user.role}</td>
                <td>${user.enabled}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
