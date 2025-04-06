<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Checkout</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<div class="container mt-4">
    <h2>Checkout</h2>
    <c:choose>
        <c:when test="${not empty cart}">
            <table class="table table-bordered mt-3">
                <thead>
                <tr>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Total</th>
                </tr>
                </thead>
                <tbody>
                <c:set var="grandTotal" value="0"/>
                <c:forEach var="item" items="${cart}">
                    <tr>
                        <td>${item.product.name}</td>
                        <td>${item.quantity}</td>
                        <td>₹${item.product.price}</td>
                        <td>₹${item.product.price * item.quantity}</td>
                        <c:set var="grandTotal" value="${grandTotal + (item.product.price * item.quantity)}"/>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="3" class="text-end"><strong>Grand Total:</strong></td>
                    <td><strong>₹${grandTotal}</strong></td>
                </tr>
                </tbody>
            </table>

            <form action="place-order" method="post">
                <button type="submit" class="btn btn-success">Place Order</button>
            </form>
        </c:when>
        <c:otherwise>
            <p>Your cart is empty.</p>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
