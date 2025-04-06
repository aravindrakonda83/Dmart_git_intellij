package com.dmart.dao;

import com.dmart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveUser(User user) {
        String sql = """
            INSERT INTO users(username, password, email, phone, age, gender, address, dob, role, enabled, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
        """;
        jdbcTemplate.update(sql,
                user.getUsername(), user.getPassword(), user.getEmail(), user.getPhone(),
                user.getAge(), user.getGender(), user.getAddress(), user.getDob(),
                user.getRole(), user.isEnabled()
        );
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, (ResultSet rs, int rowNum) -> {
            User u = new User();
            u.setId(rs.getInt("id"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setEmail(rs.getString("email"));
            u.setPhone(rs.getString("phone"));
            u.setAge(rs.getInt("age"));
            u.setGender(rs.getString("gender"));
            u.setAddress(rs.getString("address"));
            u.setDob(rs.getDate("dob").toLocalDate());
            u.setRole(rs.getString("role"));
            u.setEnabled(rs.getBoolean("enabled"));
            return u;
        });
    }

    @Override
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
            User u = new User();
            u.setId(rs.getInt("id"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setEmail(rs.getString("email"));
            u.setPhone(rs.getString("phone"));
            u.setAge(rs.getInt("age"));
            u.setGender(rs.getString("gender"));
            u.setDob(rs.getDate("dob") != null ? rs.getDate("dob").toLocalDate() : null);
            u.setAddress(rs.getString("address"));
            u.setRole(rs.getString("role"));
            u.setEnabled(rs.getBoolean("enabled"));
            u.setCreatedAt(rs.getTimestamp("createdAt") != null ? rs.getTimestamp("createdAt").toLocalDateTime() : null);
            u.setUpdatedAt(rs.getTimestamp("updatedAt") != null ? rs.getTimestamp("updatedAt").toLocalDateTime() : null);
            return u;
        });
        System.out.println("Users fetched: " + users.size());
        return users;
    }

}
