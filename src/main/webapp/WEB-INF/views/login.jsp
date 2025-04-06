<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - DMart</title>
    <!-- Bootstrap CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background: #f8f9fa;
        }
        .login-container {
            max-width: 400px;
            margin: 80px auto;
            padding: 30px;
            background-color: white;
            border-radius: 12px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
        .btn-register {
            background-color: #6c757d;
            color: white;
        }
        .btn-register:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="login-container">
        <h3 class="text-center mb-4">Login to DMart</h3>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form method="post" action="/login">
            <div class="mb-3">
                <label class="form-label">Username</label>
                <input type="text" name="username" class="form-control" required />
            </div>

            <div class="mb-3">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" required />
            </div>

            <div class="d-grid mb-2">
                <button type="submit" class="btn btn-primary">Login</button>
            </div>

            <div class="d-grid">
                <a href="/register" class="btn btn-register">Register</a>
            </div>
        </form>
    </div>
</div>

<!-- Bootstrap JS (Optional for interactivity) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
