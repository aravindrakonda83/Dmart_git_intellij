<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet" />
    <style>
        body {
            background-color: #f8f9fa;
        }
        .sidebar {
            height: 100vh;
            background-color: #343a40;
            padding-top: 1rem;
        }
        .sidebar a {
            color: #ddd;
            padding: 10px;
            display: block;
            text-decoration: none;
        }
        .sidebar a:hover, .sidebar a.active-link {
            background-color: #495057;
            color: white;
        }
        .topbar {
            background-color: #fff;
            border-bottom: 1px solid #dee2e6;
        }
        .card-stat i {
            font-size: 2rem;
        }
        .content-section {
            display: none;
        }
        .content-section.active {
            display: block;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <nav class="col-md-2 sidebar d-none d-md-block">
            <h4 class="text-center text-white mb-4">🛒 DMart Admin</h4>
            <a href="javascript:void(0);" class="sidebar-link active-link" data-target="dashboard"><i class="fas fa-boxes me-2"></i> Dashboard</a>
            <a href="javascript:void(0);" class="sidebar-link" data-target="add-product"><i class="fas fa-plus-circle me-2"></i> Add Product</a>
            <a href="javascript:void(0);" class="sidebar-link" data-target="product-table"><i class="fas fa-eye me-2"></i> Products</a>
            <a href="javascript:void(0);" class="sidebar-link" data-target="user-table"><i class="fas fa-users me-2"></i> Users</a>
            <a href="logout"><i class="fas fa-sign-out-alt me-2"></i> Logout</a>
        </nav>

        <!-- Main Content -->
        <main class="col-md-10 ms-sm-auto px-md-4">
            <!-- Topbar -->
            <div class="topbar py-3 d-flex justify-content-between align-items-center">
                <div class="d-flex align-items-center">
                    <i class="fas fa-user-shield fa-lg me-2 text-primary"></i>
                    <strong>Welcome, Admin</strong>
                </div>
                <div class="d-flex align-items-center gap-3">
                    <form class="d-none d-md-block">
                        <input class="form-control form-control-sm" placeholder="Search..." />
                    </form>
                    <div class="dropdown">
                        <a class="dropdown-toggle text-decoration-none text-dark" href="#" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-bell"></i>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><a class="dropdown-item" href="#">🔔 New order placed</a></li>
                            <li><a class="dropdown-item" href="#">📦 Stock low on Dals</a></li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- All Sections wrapped with class="content-section" -->
            <div class="content-section active" id="dashboard">
                <div class="row my-3">
                    <div class="col-md-6">
                        <div class="card shadow-sm p-3 d-flex flex-row justify-content-between align-items-center">
                            <div>
                                <h6>Total Products</h6>
                                <h4>${fn:length(products)}</h4>
                            </div>
                            <i class="fas fa-box text-primary"></i>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="card shadow-sm p-3 d-flex flex-row justify-content-between align-items-center">
                            <div>
                                <h6>Total Users</h6>
                                <h4>${fn:length(customers)}</h4>
                            </div>
                            <i class="fas fa-users text-success"></i>
                        </div>
                    </div>
                </div>
            </div>

            <div class="card shadow-sm mb-4 content-section active" id="add-product">
                <div class="card-header bg-primary text-white">
                    <i class="fas fa-plus-circle"></i> Add Product
                </div>
                <div class="card-body">
                    <c:if test="${not empty message}">
                        <div class="alert ${alertClass} alert-dismissible fade show" role="alert">
                            ${message}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <form action="<c:url value='/admin/add-product' />" method="post" class="row g-3">
                        <div class="col-md-4"><input name="name" class="form-control" placeholder="Product Name" required /></div>
                        <div class="col-md-4"><input name="category" class="form-control" placeholder="Category" required /></div>
                        <div class="col-md-2"><input name="price" class="form-control" type="number" step="0.01" placeholder="Price ₹" required /></div>
                        <div class="col-md-2"><input name="mrp" class="form-control" type="number" step="0.01" placeholder="MRP ₹" required /></div>
                        <div class="col-md-4"><input name="unitOptions" class="form-control" placeholder="Unit (e.g., 1kg)" required /></div>
                        <div class="col-md-6"><input name="imageUrl" class="form-control" placeholder="Image URL" required /></div>
                        <div class="col-md-2">
                            <button class="btn btn-success w-100 mt-md-0 mt-2">Add</button>
                        </div>
                    </form>
                </div>
            </div>

            <div class="card mb-4 shadow-sm content-section active" id="product-table">
                <div class="card-header bg-info text-white">
                    <i class="fas fa-boxes"></i> All Products
                </div>
                <div class="card-body table-responsive">
                    <table class="table table-bordered table-hover align-middle text-center">
                        <thead class="table-light">
                            <tr><th>ID</th><th>Name</th><th>Category</th><th>Price</th><th>MRP</th><th>Unit</th><th>Action</th></tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${products}">
                                <tr>
                                    <td>${p.id}</td>
                                    <td>${p.name}</td>
                                    <td>${p.category}</td>
                                    <td>₹${p.price}</td>
                                    <td>₹${p.mrp}</td>
                                    <td>${p.unitOptions}</td>
                                    <td>
                                        <a href="<c:url value='/admin/delete-product/${p.id}' />" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this product?');">
                                            <i class="fas fa-trash-alt"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="card shadow-sm content-section active" id="user-table">
                <div class="card-header bg-secondary text-white">
                    <i class="fas fa-users"></i> Registered Users
                </div>
                <div class="card-body table-responsive">
                    <table class="table table-hover align-middle text-center">
                        <thead class="table-light">
                            <tr><th>ID</th><th>Username</th><th>Email</th><th>Role</th></tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${customers}">
                                <tr>
                                    <td>${c.id}</td>
                                    <td>${c.username}</td>
                                    <td>${c.email}</td>
                                    <td>${c.role}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Hide alerts after 4 seconds
    setTimeout(() => {
        const alert = document.querySelector('.alert');
        if (alert) {
            alert.classList.remove('show');
        }
    }, 4000);

    // Section toggle logic
    const sidebarLinks = document.querySelectorAll('.sidebar-link');
    sidebarLinks.forEach(link => {
        link.addEventListener('click', function () {
            const target = this.getAttribute('data-target');

            sidebarLinks.forEach(l => l.classList.remove('active-link'));
            this.classList.add('active-link');

            const sections = document.querySelectorAll('.content-section');
            if (target === 'dashboard') {
                sections.forEach(section => section.classList.add('active'));
            } else {
                sections.forEach(section => section.classList.remove('active'));
                document.getElementById(target).classList.add('active');
            }
        });
    });
</script>
</body>
</html>
