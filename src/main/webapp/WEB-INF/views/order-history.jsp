<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>Order History</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"/>
    <style>
        body {
            background-color: #f9f9f9;
        }

        h2 {
            font-weight: bold;
            color: #007e3a;
        }

        .order-card {
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
            background-color: #ffffff;
        }

        .card-header {
            background-color: #007e3a;
            color: white;
            font-weight: bold;
        }

        .list-group-item {
            font-size: 0.95rem;
        }

        .pagination-btns {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }

        .form-control {
            max-width: 300px;
        }

        .search-form {
            display: flex;
            gap: 10px;
            align-items: center;
            flex-wrap: wrap;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4 text-center">📦 Order History</h2>

    <!-- 🔍 Search Form -->
    <form method="get" class="search-form mb-4">
        <input type="text" name="search" placeholder="Search by Order ID or Status" value="${search}" class="form-control"/>
        <button type="submit" class="btn btn-primary">Search</button>
    </form>

    <!-- ⚠️ No Orders Found -->
    <c:if test="${empty orders}">
        <div class="alert alert-warning text-center">No orders found.</div>
    </c:if>

    <!-- 🧾 Order Cards -->
    <c:forEach var="order" items="${orders}">
        <div class="card order-card mb-4">
            <div class="card-header">
                Order #${order.id} &nbsp; | &nbsp; Date: ${order.orderDate} &nbsp; | &nbsp; Total: ₹${order.totalAmount}
            </div>
            <ul class="list-group list-group-flush">
                <c:forEach var="item" items="${order.items}">
                    <li class="list-group-item">
                        <strong>Product ID:</strong> ${item.productId} |
                        <strong>Qty:</strong> ${item.quantity} |
                        <strong>Price:</strong> ₹${item.price}
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:forEach>

    <!-- 🔁 Pagination -->
    <div class="pagination-btns">
        <c:if test="${page > 1}">
            <a href="?search=${search}&page=${page - 1}" class="btn btn-outline-secondary">← Previous</a>
        </c:if>
        <c:if test="${hasNext}">
            <a href="?search=${search}&page=${page + 1}" class="btn btn-outline-secondary ms-auto">Next →</a>
        </c:if>
    </div>
</div>
</body>
</html>
