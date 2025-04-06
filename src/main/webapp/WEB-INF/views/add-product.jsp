<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>Add Product</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2>➕ Add New Product</h2>
    <form:form method="post" action="addProduct" modelAttribute="product" class="mt-4">
        <div class="mb-3">
            <label>Name:</label>
            <form:input path="name" class="form-control"/>
        </div>
        <div class="mb-3">
            <label>Category:</label>
            <form:input path="category" class="form-control"/>
        </div>
        <div class="mb-3">
            <label>Price:</label>
            <form:input path="price" class="form-control"/>
        </div>
        <div class="mb-3">
            <label>MRP:</label>
            <form:input path="mrp" class="form-control"/>
        </div>
        <div class="mb-3">
            <label>Unit Options:</label>
            <form:input path="unitOptions" class="form-control"/>
        </div>
        <div class="mb-3">
            <label>Image URL:</label>
            <form:input path="imageUrl" class="form-control"/>
        </div>
        <button type="submit" class="btn btn-success">Add Product</button>
    </form:form>
</div>
</body>
</html>
