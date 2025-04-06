<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>View Products</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2>🛒 All Products</h2>
    <table class="table table-striped mt-3">
        <thead>
            <tr>
                <th>ID</th><th>Name</th><th>Category</th><th>Price</th><th>MRP</th><th>Unit Options</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>${product.id}</td>
                <td>${product.name}</td>
                <td>${product.category}</td>
                <td>₹${product.price}</td>
                <td>₹${product.mrp}</td>
                <td>${product.unitOptions}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
