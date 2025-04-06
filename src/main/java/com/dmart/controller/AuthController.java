package com.dmart.controller;

import com.dmart.model.CartItem;
import com.dmart.model.Order;
import com.dmart.model.Product;
import com.dmart.model.User;
import com.dmart.service.OrderService;
import com.dmart.service.ProductService;
import com.dmart.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    // =======================
    // USER REGISTRATION & LOGIN
    // =======================
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
            return "redirect:/dashboard";
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

    // =======================
    // PRODUCT LISTING & CATEGORY FILTER
    // =======================
    @GetMapping("/dashboard")
    public String showProducts(@RequestParam(value = "category", required = false) String category, Model model) {
        List<Product> products = productService.listAll();

        if (category != null && !category.equalsIgnoreCase("All")) {
            products = products.stream()
                    .filter(p -> p.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }

        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category != null ? category : "All");
        return "dashboard";
    }

    // =======================
    // USER LISTING (ADMIN)
    // =======================
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "user-list";
    }

    // =======================
    // CART MANAGEMENT
    // =======================
    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") int productId, HttpSession session) {
        Product product = productService.listAll().stream()
                .filter(p -> p.getId() == productId)
                .findFirst()
                .orElse(null);

        if (product == null) return "redirect:/dashboard";

        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        CartItem item = cart.get(productId);
        if (item == null) {
            cart.put(productId, new CartItem(product, 1));
        } else {
            item.setQuantity(item.getQuantity() + 1);
        }

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
        if (cart != null) {
            cart.remove(productId);
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    @PostMapping("/update-cart")
    public String updateCart(@RequestParam("productId") int productId,
                             @RequestParam("quantity") int quantity,
                             HttpSession session) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

        if (cart != null && cart.containsKey(productId)) {
            if (quantity <= 0) {
                cart.remove(productId);
            } else {
                cart.get(productId).setQuantity(quantity);
            }
            session.setAttribute("cart", cart);
        }

        return "redirect:/cart";
    }

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

        if (user == null || cart == null || cart.isEmpty()) {
            return "redirect:/dashboard";
        }

        orderService.placeOrder(user, cart);
        session.removeAttribute("cart");

        return "order-success";
    }

    // =======================
    // ORDER HISTORY
    // =======================
    @GetMapping("/order-history")
    public String viewOrderHistory(@RequestParam(defaultValue = "") String search,
                                   @RequestParam(defaultValue = "1") int page,
                                   HttpSession session,
                                   Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        int pageSize = 5;
        List<Order> orders = orderService.getOrderHistory(user.getId(), search, page, pageSize);
        boolean hasNext = orderService.hasNextPage(user.getId(), search, page, pageSize);
        boolean hasPrevious = page > 1;

        model.addAttribute("orders", orders);
        model.addAttribute("search", search);
        model.addAttribute("page", page);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("hasPrevious", hasPrevious);

        return "order-history";
    }


}
