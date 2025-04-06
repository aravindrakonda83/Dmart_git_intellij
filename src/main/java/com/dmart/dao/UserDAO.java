package com.dmart.dao;

import com.dmart.model.User;

import java.util.List;

public interface UserDAO {
    void saveUser(User user);
    User findByUsername(String username);

    List<User> findAllUsers();


}
