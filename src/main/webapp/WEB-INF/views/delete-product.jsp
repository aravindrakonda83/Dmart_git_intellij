<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Delete Product</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2>🗑 Delete Products</h2>
    <form method="post" action="deleteProduct">
        <table class="table table-hover mt-3">
            <thead>
                <tr><th>Select</th><th>ID</th><th>Name</th><th>Category</th></tr>
            </thead>
            <tbody>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td><input type="checkbox" name="productIds" value="${product.id}"/></td>
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.category}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <button type="submit" class="btn btn-danger">Delete Selected</button>
    </form>
</div>
</body>
</html>
