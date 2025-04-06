package com.dmart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Username is required")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    @Column(length = 10)
    private String phone;

    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must be realistic")
    private int age;

    private String gender;
    private String address;
    private LocalDate dob;

    @NotBlank(message = "Role is required")
    private String role;

    private boolean enabled = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    // (Generate using IDE or Lombok for brevity)
}
