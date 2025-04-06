<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Shopping Cart</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"/>
</head>
<body>
<div class="container mt-5">
    <h2>Your Shopping Cart</h2>

    <c:if test="${empty cart}">
        <div class="alert alert-info mt-4">
            Your cart is empty. <a href="dashboard">Start shopping</a> now!
        </div>
    </c:if>

    <c:if test="${not empty cart}">
        <table class="table table-bordered table-striped mt-4">
            <thead>
            <tr>
                <th>Product</th>
                <th>Price (₹)</th>
                <th>Quantity</th>
                <th>Total (₹)</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:set var="grandTotal" value="0"/>
            <c:forEach var="item" items="${cart}">
                <c:set var="itemTotal" value="${item.product.price * item.quantity}"/>
                <tr>
                    <td>${item.product.name}</td>
                    <td>₹ ${item.product.price}</td>
                    <td>
                        <form action="update-cart" method="post" class="d-inline-flex">
                            <input type="hidden" name="productId" value="${item.product.id}"/>
                            <input type="number" name="quantity" value="${item.quantity}" min="1"
                                   class="form-control" style="width: 70px;"/>
                            <button type="submit" class="btn btn-sm btn-secondary ms-2">Update</button>
                        </form>
                    </td>
                    <td>₹ ${itemTotal}</td>
                    <td>
                        <a href="remove-from-cart?productId=${item.product.id}" class="btn btn-sm btn-danger">
                            Remove
                        </a>
                    </td>
                </tr>
                <c:set var="grandTotal" value="${grandTotal + itemTotal}"/>
            </c:forEach>
            </tbody>
        </table>

        <div class="d-flex justify-content-between align-items-center mt-4">
            <h4>Total Amount: ₹ ${grandTotal}</h4>
            <div>
                <a href="dashboard" class="btn btn-primary me-2">Continue Shopping</a>
                <a href="checkout" class="btn btn-success">Proceed to Checkout</a>
            </div>
        </div>
    </c:if>
</div>
</body>
</html>
