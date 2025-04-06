package com.dmart.dao;

import com.dmart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getPhone(),
                user.getAge(),
                user.getGender(),
                user.getAddress(),
                user.getDob(),
                user.getRole(),
                user.isEnabled()
        );
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> mapUser(rs));
    }

    @Override
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";

        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> mapUser(rs));
        System.out.println("Users fetched: " + users.size());
        return users;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setAge(rs.getInt("age"));
        user.setGender(rs.getString("gender"));
        user.setAddress(rs.getString("address"));

        if (rs.getDate("dob") != null) {
            user.setDob(rs.getDate("dob").toLocalDate());
        }

        user.setRole(rs.getString("role"));
        user.setEnabled(rs.getBoolean("enabled"));

        if (rs.getTimestamp("created_at") != null) {
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        if (rs.getTimestamp("updated_at") != null) {
            user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        return user;
    }
}
