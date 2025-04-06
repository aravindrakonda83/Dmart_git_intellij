package com.dmart.controller;

import com.dmart.model.*;
import com.dmart.service.OrderService;
import com.dmart.service.ProductService;
import com.dmart.service.UserService;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private ProductService productService;
    @Autowired private OrderService orderService;

    // ================================
    // USER REGISTRATION & LOGIN
    // ================================

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        userService.register(user);
        model.addAttribute("message", "Registration successful! Please log in.");
        return "login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "ADMIN".equalsIgnoreCase(user.getRole()) ? "redirect:/admin/dashboard" : "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ================================
    // PRODUCT LISTING & FILTERING
    // ================================

    @GetMapping("/dashboard")
    public String showProducts(@RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "search", required = false) String search,
                               Model model,
                               HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"USER".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        List<Product> allProducts = (category == null || category.equalsIgnoreCase("All"))
                ? productService.listAll()
                : productService.listAll().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());

        List<Product> filteredProducts = (search != null && !search.trim().isEmpty())
                ? allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(search.trim().toLowerCase()))
                .collect(Collectors.toList())
                : allProducts;

        model.addAttribute("products", filteredProducts);
        model.addAttribute("selectedCategory", category != null ? category : "All");
        model.addAttribute("searchQuery", search);

        return "dashboard";
    }

    // ================================
    // ADMIN: USER LISTING
    // ================================

    @GetMapping("/users")
    public String listUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) return "redirect:/login";

        model.addAttribute("users", userService.findAllUsers());
        return "user-list";
    }

    // ================================
    // CART MANAGEMENT
    // ================================

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") int productId, HttpSession session) {
        Product product = productService.listAll().stream()
                .filter(p -> p.getId() == productId)
                .findFirst().orElse(null);
        if (product == null) return "redirect:/dashboard";

        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new HashMap<>();

        CartItem item = cart.get(productId);
        if (item == null) cart.put(productId, new CartItem(product, 1));
        else item.setQuantity(item.getQuantity() + 1);

        session.setAttribute("cart", cart);
        return "redirect:/dashboard";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        model.addAttribute("cart", cart != null ? cart.values() : List.of());
        return "cart";
    }

    @GetMapping("/remove-from-cart")
    public String removeFromCart(@RequestParam("productId") int productId, HttpSession session) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart != null) cart.remove(productId);
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @PostMapping("/update-cart")
    public String updateCart(@RequestParam("productId") int productId,
                             @RequestParam("quantity") int quantity,
                             HttpSession session) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart != null && cart.containsKey(productId)) {
            if (quantity <= 0) cart.remove(productId);
            else cart.get(productId).setQuantity(quantity);
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    // ================================
    // ORDER PLACEMENT
    // ================================

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        model.addAttribute("cart", cart != null ? cart.values() : List.of());
        return "checkout";
    }

    @PostMapping("/place-order")
    public String placeOrder(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

        if (user == null || cart == null || cart.isEmpty()) return "redirect:/dashboard";

        orderService.placeOrder(user, cart);
        session.removeAttribute("cart");

        return "order-success";
    }

    // ================================
    // ORDER HISTORY & FILTERING
    // ================================

    @GetMapping("/order-history")
    public String viewOrderHistory(@RequestParam(defaultValue = "") String search,
                                   @RequestParam(required = false) String status,
                                   @RequestParam(required = false) String date,
                                   @RequestParam(defaultValue = "1") int page,
                                   HttpSession session,
                                   Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        int pageSize = 5;
        List<Order> orders = orderService.getOrderHistory(user.getId(), search, status, date, page, pageSize);
        boolean hasNext = orderService.hasNextPage(user.getId(), search, status, date, page, pageSize);

        model.addAttribute("orders", orders);
        model.addAttribute("search", search);
        model.addAttribute("status", status);
        model.addAttribute("date", date);
        model.addAttribute("page", page);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("hasPrevious", page > 1);

        return "order-history";
    }

    // ================================
    // INVOICE PDF DOWNLOAD
    // ================================

    @GetMapping("/download-invoice")
    public void downloadInvoice(@RequestParam("orderId") int orderId,
                                HttpServletResponse response) throws IOException {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found.");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice_" + orderId + ".pdf");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.DARK_GRAY);
            Paragraph title = new Paragraph("DMart Order Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Order ID: " + order.getId()));
            document.add(new Paragraph("Order Date: " + order.getOrderDate()));
            document.add(new Paragraph("Status: " + order.getStatus()));
            document.add(new Paragraph("Total Amount: ₹" + order.getTotalAmount()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{3, 2, 2, 2});
            addTableHeader(table, "Product ID", "Quantity", "Price", "Total");

            for (OrderItem item : order.getItems()) {
                double total = item.getPrice() * item.getQuantity();
                table.addCell(String.valueOf(item.getProductId()));
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell("₹" + item.getPrice());
                table.addCell("₹" + total);
            }

            document.add(table);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Thank you for shopping with DMart!"));

            document.close();
        } catch (DocumentException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate invoice PDF.");
            e.printStackTrace();
        }
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headFont));
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("products", productService.getAllProducts()); // Consistent method
        model.addAttribute("customers", userService.findAllUsers());     // Consistent attribute

        return "admin-dashboard";
    }

    @GetMapping("/viewAllProducts")
    public String viewAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "view-products";
    }

    @GetMapping("/addProductForm")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    @GetMapping("/deleteProductPage")
    public String showDeleteProductPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "delete-product";
    }

    @GetMapping("/viewAllUsers")
    public String viewAllUsers(Model model) {
        model.addAttribute("customers", userService.findAllUsers());
        return "view-users";
    }

    @PostMapping("/admin/add-product")
    public String addProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            productService.addProduct(product);
            redirectAttributes.addFlashAttribute("message", "✅ Product added successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "❌ Failed to add product.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/delete-product/{id}")
    public String deleteProduct(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "🗑️ Product deleted successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "⚠️ Failed to delete product.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        }
        return "redirect:/admin/dashboard";
    }



}
