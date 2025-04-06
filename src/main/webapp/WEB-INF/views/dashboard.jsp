<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>DMart Ready - Products</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"/>
    <!-- Bootstrap Icons -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css"/>

    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', sans-serif;
        }

        h2 {
            font-weight: bold;
            color: #007e3a;
            text-align: center;
            margin-bottom: 10px;
            font-size: 3rem;
        }

        h4 {
            font-size: 1.5rem;
            font-weight: bold;
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }

        .category-btn {
            background-color: #007e3a;
            color: #fff;
            border: none;
            padding: 10px 18px;
            border-radius: 30px;
            font-weight: 500;
            font-size: 0.95rem;
            transition: all 0.3s ease-in-out;
        }

        .category-btn:hover {
            background-color: #005c2a;
            transform: scale(1.05);
        }

        .navbar-brand {
            font-weight: bold;
            font-size: 1.6rem;
            color: #007e3a !important;
        }

        .nav-link {
            font-weight: 500;
            color: #333 !important;
        }

        .nav-link:hover {
            color: #007e3a !important;
        }

        .product-card {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            border: 1px solid #e0e0e0;
            border-radius: 15px;
            overflow: hidden;
            background-color: #ffffff;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.05);
        }

        .product-card:hover {
            transform: scale(1.02);
            box-shadow: 0 10px 24px rgba(0, 0, 0, 0.1);
        }

        .product-card img {
            height: 200px;
            object-fit: contain;
            padding: 10px;
            background-color: #f1f3f5;
        }

        .product-title {
            font-size: 1rem;
            font-weight: 600;
            color: #212529;
        }

        .product-category {
            font-size: 0.85rem;
            color: #6c757d;
        }

        .price {
            color: #198754;
            font-weight: bold;
            font-size: 1.1rem;
        }

        .mrp {
            text-decoration: line-through;
            color: #adb5bd;
            font-size: 0.95rem;
            margin-left: 8px;
        }

        .unit-label {
            font-size: 0.85rem;
            margin-bottom: 5px;
        }

        .btn-add {
            background-color: #ff5722;
            color: #fff;
            border: none;
            padding: 8px;
            font-size: 0.95rem;
            font-weight: 500;
            border-radius: 8px;
            transition: background-color 0.3s ease;
        }

        .btn-add:hover {
            background-color: #e64a19;
        }

        .form-select {
            font-size: 0.9rem;
        }

        /* Responsive Sidebar Toggle */
        @media (max-width: 768px) {
            #categorySidebar {
                display: none;
            }
            #toggleCategory {
                display: block;
            }
        }

        @media (min-width: 769px) {
            #toggleCategory {
                display: none;
            }
        }
    </style>
</head>
<body>

<!-- Navbar matching DMart Ready look -->
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm py-2 fixed-top">
    <div class="container-fluid px-4">

        <!-- Logo -->
        <a class="navbar-brand d-flex align-items-center" href="#">
            <span class="fw-bold text-success fs-5">DMart</span><span class="text-danger fw-semibold">ready</span>
        </a>

        <!-- Location and Delivery Info -->
        <div class="d-flex align-items-center ms-3 me-auto">
            <i class="bi bi-geo-alt-fill text-success me-1"></i>
            <div>
                <div class="fw-semibold">500074 <span class="text-muted"></span></div>
                <small class="text-muted">Hyderabad</small>
            </div>
            <div class="ms-4 small">
                <span class="text-muted">Earliest</span> <span class="text-success fw-semibold">Home Delivery</span> available<br>
                <i class="bi bi-brightness-alt-low-fill text-warning me-1"></i><span class="text-dark fw-semibold">Today 07:00 PM - 10:00 PM</span>
            </div>
        </div>

        <!-- 🔍 Centered Search Bar -->
        <div class="mx-auto w-50">
            <form class="d-flex" action="dashboard" method="get">
                <input class="form-control me-2" type="search" name="search" placeholder="Search for products"
                       value="${param.search}" aria-label="Search">
                <button class="btn btn-success" type="submit">SEARCH</button>
            </form>
        </div>

        <!-- Icons on the Right -->
		<div class="d-flex align-items-center ms-3">

		    <!-- Green Cart Icon -->
		    <a href="cart" class="text-decoration-none position-relative me-3">
		        <i class="bi bi-cart3 text-success fs-4"></i>
		    </a>

		    <!-- 🧾 Order History Icon -->
		    <a href="order-history" class="text-decoration-none position-relative">
		        <i class="bi bi-receipt-cutoff text-primary fs-4"></i>
		    </a>

		</div>

    </div>
</nav>


<!-- ð Sidebar + Products Layout -->
<div class="container-fluid mt-4">
    <div class="row">

        <!-- Mobile Toggle for Sidebar -->
        <div class="col-12 mb-3 d-md-none text-center">
            <button class="btn btn-outline-success" id="toggleCategory">Show Categories</button>
        </div>

        <div class="container mt-4">
    	<div class="row">
        <!-- Sidebar -->
<div class="col-md-3 mb-4 " id="categorySidebar">
    <div class="list-group shadow-sm position-fixed" style="top: 80px; max-height: calc(100vh - 100px); overflow-y: auto;">
    <h3>CATEGORIES: </h3>
        <c:forEach var="cat" items="${['All', 'Fruits', 'Vegetables', 'Beverages', 'Snacks', 'Household']}">
            <a href="dashboard?category=${cat}"
               class="list-group-item list-group-item-action
               ${selectedCategory == cat ? 'active' : ''}">
                ${cat}
            </a>
        </c:forEach>
    </div>
</div>


        <!-- Products Grid -->
<div class="col-md-9 mt-5 pt-3">
    <h4 class="text-center mb-4">
        <i class="bi bi-box2-heart"></i>
        <c:choose>
            <c:when test="${not empty param.search}">
                Search Results for "<strong>${param.search}</strong>"
            </c:when>
            <c:otherwise>
                <c:out value="${param.category != null ? param.category : 'All'}" /> Products
            </c:otherwise>
        </c:choose>
    </h4>

    <!-- No Products Alert -->
    <c:if test="${empty products}">
        <div class="alert alert-warning text-center w-100" role="alert">
            <i class="bi bi-exclamation-triangle-fill"></i>
            No products found matching "<strong>${param.search}</strong>".
        </div>
    </c:if>

	    <!-- Product Cards -->
	    <div class="row g-4">
	        <c:forEach var="product" items="${products}">
	            <div class="col-12 col-sm-6 col-md-4 col-lg-3">
	                <div class="card product-card h-100 shadow-sm">
	                    <img src="${product.imageUrl}" alt="${product.name}" class="card-img-top">
	                    <div class="card-body d-flex flex-column">
	                        <h5 class="product-title">${product.name}</h5>
	                        <div class="product-category mb-1">${product.category}</div>

	                        <div class="mb-2">
	                            <span class="price">&#8377;${product.price}</span>
	                            <span class="mrp">&#8377;${product.mrp}</span>
	                            <c:set var="discount" value="${((product.mrp - product.price) * 100) / product.mrp}" />
	                            <span class="badge bg-success ms-2">
	                                <fmt:formatNumber value="${discount}" maxFractionDigits="0"/>% Off
	                            </span>
	                        </div>

	                        <form action="add-to-cart" method="post" class="mt-auto">
	                            <input type="hidden" name="productId" value="${product.id}" />
	                            <div class="mb-2">
	                                <label for="unit-${product.id}" class="unit-label">Select Unit:</label>
	                                <select name="unit" class="form-select form-select-sm" id="unit-${product.id}">
	                                    <c:forEach var="unit" items="${fn:split(product.unitOptions, ',')}">
	                                        <option value="${unit}">${unit}</option>
	                                    </c:forEach>
	                                </select>
	                            </div>
	                            <button type="submit" class="btn btn-add w-100">Add to Cart</button>
	                        </form>
	                    </div>
	                </div>
	            </div>
	        </c:forEach>
	    </div>
	</div>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Mobile category toggle
    /* document.getElementById("toggleCategory").addEventListener("click", function () {
        const sidebar = document.getElementById("categorySidebar");
        sidebar.style.display = sidebar.style.display === "none" ? "block" : "none";
        this.textContent = sidebar.style.display === "none" ? "Show Categories" : "Hide Categories";
    }); */
</script>
</body>
</html>