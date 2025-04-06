<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>Order History</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(to right, #e9f5e9, #f0fff0);
            font-family: 'Segoe UI', sans-serif;
        }

        .order-history-container {
            background-color: #fff;
            border-radius: 20px;
            padding: 40px 30px;
            box-shadow: 0 5px 30px rgba(0, 126, 58, 0.2);
        }

        .order-history-title {
            font-weight: 700;
            color: #007e3a;
        }

        .order-card {
            border: none;
            border-radius: 15px;
            transition: transform 0.2s;
        }

        .order-card:hover {
            transform: scale(1.02);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
        }

        .card-header {
            background-color: #007e3a;
            color: #fff;
            padding: 15px;
            font-size: 1.1rem;
        }

        .list-group-item {
            background-color: #f9f9f9;
            border: none;
            padding: 10px 15px;
        }

        .badge-status {
            font-size: 0.8rem;
            padding: 5px 10px;
            border-radius: 15px;
        }

        .badge-Delivered {
            background-color: #28a745;
            color: #fff;
        }

        .badge-Cancelled {
            background-color: #dc3545;
            color: #fff;
        }

        .badge-Pending {
            background-color: #ffc107;
            color: #000;
        }

        .search-form input, .search-form select {
            max-width: 250px;
        }

        @media (max-width: 768px) {
            .search-form {
                flex-direction: column;
                gap: 10px;
                align-items: center;
            }

            .search-form input, .search-form select {
                width: 100%;
            }
        }
    </style>
</head>
<body>
<div class="container my-5">
    <div class="order-history-container text-center">
        <h2 class="order-history-title mb-4">
            <i class="bi bi-clock-history"></i> Your Order History
        </h2>

        <!-- 🔍 Filters Form -->
        <form method="get" class="search-form d-flex flex-wrap justify-content-center gap-3 mb-4">
            <input type="text" name="search" placeholder="Order ID or Status"
                   value="${fn:escapeXml(search)}" class="form-control" />

            <input type="date" name="date" value="${fn:escapeXml(date)}" class="form-control" />

            <select name="status" class="form-select">
                <option value="">All Status</option>
                <option value="Delivered" ${status == 'Delivered' ? 'selected' : ''}>Delivered</option>
                <option value="Pending" ${status == 'Pending' ? 'selected' : ''}>Pending</option>
                <option value="Cancelled" ${status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
            </select>

            <button type="submit" class="btn btn-success">
                <i class="bi bi-funnel-fill"></i> Filter
            </button>
        </form>

        <!-- ⚠ No Orders Found -->
        <c:if test="${empty orders}">
            <div class="alert alert-warning text-center">
                <i class="bi bi-exclamation-triangle-fill"></i> No orders found.
            </div>
        </c:if>

        <!-- 📦 Order Cards -->
        <div class="row justify-content-center">
            <c:forEach var="order" items="${orders}">
                <div class="col-md-6 col-lg-4 mb-4">
                    <div class="card order-card h-100">
                        <div class="card-header text-start">
                            <div class="d-flex justify-content-between align-items-start">
                                <div>
                                    <div><i class="bi bi-bag-check-fill"></i> Order #${order.id}</div>
                                    <div class="small mt-1">
                                        <i class="bi bi-calendar-event"></i> ${order.orderDate}
                                    </div>
                                    <div class="small">
                                        <i class="bi bi-currency-rupee"></i> Total: ${order.totalAmount}
                                    </div>
                                </div>
                                <!-- ✅ Status Badge -->
                                <span class="badge badge-status badge-${order.status}">${order.status}</span>
                            </div>
                        </div>

                        <!-- 🧾 Order Items -->
                        <ul class="list-group list-group-flush text-start">
                            <c:forEach var="item" items="${order.items}">
                                <li class="list-group-item">
                                    <div><i class="bi bi-box"></i> <strong>Product ID:</strong> ${item.productId}</div>
                                    <div><i class="bi bi-hash"></i> <strong>Qty:</strong> ${item.quantity}</div>
                                    <div><i class="bi bi-currency-rupee"></i> <strong>Price:</strong> ${item.price}</div>
                                </li>
                            </c:forEach>
                        </ul>

                        <!-- 📄 Download Invoice -->
                        <div class="card-footer bg-light text-center">
                            <a href="download-invoice?orderId=${order.id}" class="btn btn-outline-primary btn-sm">
                                <i class="bi bi-file-earmark-arrow-down-fill"></i> Download Invoice
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- 🔁 Pagination -->
        <div class="pagination-btns d-flex justify-content-center gap-3 mt-4">
            <c:if test="${page > 1}">
                <a href="?search=${fn:escapeXml(search)}&date=${fn:escapeXml(date)}&status=${status}&page=${page - 1}"
                   class="btn btn-outline-success">
                    <i class="bi bi-arrow-left-circle"></i> Previous
                </a>
            </c:if>
            <c:if test="${hasNext}">
                <a href="?search=${fn:escapeXml(search)}&date=${fn:escapeXml(date)}&status=${status}&page=${page + 1}"
                   class="btn btn-outline-success">
                    Next <i class="bi bi-arrow-right-circle"></i>
                </a>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>
